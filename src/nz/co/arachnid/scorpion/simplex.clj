(ns nz.co.arachnid.scorpion.simplex
    (:require [clojure.spec.alpha :as s]))

;; Referencing Video
;; https://www.youtube.com/watch?v=M8POtpPtQZc&list=PLhL0OLgFT2BSx6XvhpWmlzOO2kOR82dQK&index=2&t=0s

;; Maximize z = 12x1 + 16x2
;; ========================
;;
;; Subject to:
;; ===========
;; - 10x1 + 20x2 <= 120
;; -  8x1 +  8x2 <= 80
;; - x1 and x2 >= 0
;;
;; Slack Variable Form
;; ===================
;; - Max z: 12x1 + 16x2 + 0s1 + 0s2 (Objective Function)
;; - 10x1 + 20x2 + s1 = 120         (Constraint 1)
;; -  8x1 +  8x2 + s2 = 80          (Constraint 2)

;; Max z: 12x1 + 16x2 + 0s1 + 0s2 so: 12, 16, 0, 0
;; All co-effecients from the slack variable form of the objective or constraint functions.
(s/def ::coefficient nat-int?)
;; Max z: 12x1 + 16x2 + 0s1 + 0s2 so: x1, x2, s1, s2
;; The variables from the slack variable form of the objective or constraint function.
(s/def ::variables   symbol?)

;; Zj Row
;; ======

;; Zj = sum of i = 1..n (cbi[i] * constraint-coeffecients[i,j]
;; n is the number of (constraint coeffecients + solution).
;; Zj = [0*10 + 0*8, 0*20+0*8, 0*1+0*0, 0*0+0*1, 0*120+0*80]

;; Maximization Problem
;; ====================
;; all Cj - Zj <= 0

;; Minimization Problem
;; ====================
;; all Cj - Zj >= 0

;; Helper Functions Private
;; ========================

(defn- positions
  [pred coll]
  (keep-indexed (fn [idx x]
                  (when (pred x)
                    idx))
                coll))

(defn- mult-coeffecients-by-scalar
  [n vec-coeffecients]
  (map (fn [elem] (* n elem)) vec-coeffecients))

;; Helper Functions Public
;; =======================

(defn calculate-entering-row
  [old-row key-element]
  (let [constraint-coefficients (:constraint-coefficients old-row)
        new-coeffecients        (mapv (fn [x] (/ x key-element)) constraint-coefficients)
        new-solution            (/ (:solution old-row) key-element)]
    (merge old-row {:constraint-coefficients new-coeffecients
                    :solution                new-solution
                    :ratio                   1})))

(defn calculate-non-entering-value
  [old-val corresponding-key-col-val corresponding-key-row-val key-element]
  (- old-val
     (/ (* corresponding-key-col-val corresponding-key-row-val)
        key-element)))

(defn calculate-non-entering-row
  [tableaux-row
   previous-iteration-key-row
   key-row-index
   key-column-index
   current-row-index
   key-element]
  (if (= current-row-index key-row-index)
    ;; then
    tableaux-row
    ;; else
    (let [constraint-coefficients (:constraint-coefficients tableaux-row)
          updated-coefficients    (vec
                                   (map-indexed
                                     (fn [index element]
                                       (calculate-non-entering-value
                                         element
                                         (nth constraint-coefficients key-column-index)
                                         (nth previous-iteration-key-row index)
                                         key-element))
                                     constraint-coefficients))]
      (merge tableaux-row {:constraint-coefficients updated-coefficients}))))


(defn calculate-non-entering-rows
  [tableaux-rows previous-iteration-key-row key-row-index key-column-index key-element]
  (map-indexed
    (fn [current-row-index row]
      (calculate-non-entering-row
        row
        previous-iteration-key-row
        key-row-index
        key-column-index
        current-row-index
        key-element))
    tableaux-rows))


;;  Main Functions
;; ================

(defn calculate-zj-row
  "Zj Row
   ======
   - Zj = sum of i = 1..n (cbi[i] * constraint-coeffecients[i,j]
     n is the number of (constraint coeffecients + solution)
   - The supplied tableaux is updated with the result
   ## New Keys:
   - :Zj-row"
  [tableaux]
  (let [t-rows           (:tableaux-rows tableaux)
        cbi*constraints  (map
                           (fn [row] (mult-coeffecients-by-scalar (:cbi row) (:constraint-coefficients row)))
                           t-rows)
        zj               (apply #(mapv + %1 %2) cbi*constraints)]
   (assoc tableaux :Zj-row zj)))


(defn calculate-cj-zj-row
  "Cj - Zj Row
   ===========
   - Once the Zj row is calculated simply subtracts the Cj row from it.
     The supplied tableaux is updated with the result
   ## New Keys
   - :Cj-Zj"
  [tableaux]
  (let [zj-row   (:Zj-row tableaux)
        cj-row   (:objective-coeffecient-row tableaux)
        calc-row (mapv - cj-row zj-row)]
    (merge tableaux {:Cj-Zj calc-row})))


(defn optimal-solution?
  "- For max problems all Cj-Zj <= 0
   - For min problems all Cj-Zj >= 0"
  [tableaux]
  (let [cj-zj-row     (:Cj-Zj tableaux)
        problem-type  (:problem-type tableaux)]
    (or
      (and (= problem-type :min) (every? (fn [x] (>= x 0)) cj-zj-row))
      (and (= problem-type :max) (every? (fn [x] (<= x 0)) cj-zj-row)))))


(defn- find-key-value
  [tableaux vec when-min-fn-a when-max-fn-b]
  (let [problem-type (:problem-type tableaux)]
    (cond
      (= problem-type :min) (apply when-min-fn-a vec)
      (= problem-type :max) (apply when-max-fn-b vec)
      :else (throw (ex-info "Unknown Problem Type" {:problem-type problem-type})))))


(defn- find-key-column-value
  [tableaux]
  (find-key-value tableaux (:Cj-Zj tableaux) min max))


(defn calculate-key-column
  "- Fox max problems the key column is the cj-zj-row column containing the highest value.
   - For min problems the key column is the cj-zj-row column containing the lowest value.
   ## New Keys:
   - :key-column-index"
  [tableaux]
  (let [cj-zj-row     (:Cj-Zj tableaux)
        key-value     (find-key-column-value tableaux)
        column-index  (first (positions #{key-value} cj-zj-row))]
    (assoc tableaux :key-column-index column-index)))


(defn calculate-solution-to-key-val-ratio
  "- Take the solution (s) and key (k) columns and produce a new column of the ratios: s / k
   - The ratios in the Tableaux rows will be updated
   ## New Keys:
   - key-ratio-index"
  [tableaux]
  (let [tableaux-rows    (:tableaux-rows tableaux)
        solution-column  (map :solution tableaux-rows)
        key-column-index (:key-column-index tableaux)
        key-column       (->> (:tableaux-rows tableaux)
                              (map :constraint-coefficients)
                              (mapv (fn [v] (nth v key-column-index))))
        ratios           (mapv
                           (fn [x y] (/ x y))
                           solution-column
                           key-column)
        updated-tableaux-rows (mapv (fn [map val] (assoc map :ratio val)) tableaux-rows ratios)
        key-ratio-value (find-key-value tableaux ratios max min)
        key-ratio-index (first (positions #{key-ratio-value} ratios))]
    (merge
      tableaux
      {:tableaux-rows   updated-tableaux-rows
       :key-ratio-index key-ratio-index})))


(defn calculate-key-row-and-value
  "- Selects the intersection of the key column and row indexes and sets that as the key value.
     The selection of the key row is based on the ratio index.
   ## New Keys:
   - :key-row-index
   - :key-value"
  [tableaux]
  (let [key-ratio-index  (:key-ratio-index tableaux)
        key-row          (:constraint-coefficients (nth (:tableaux-rows tableaux) key-ratio-index))
        key-column-index (:key-column-index tableaux)
        key-value        (nth key-row key-column-index)]
    (merge
      tableaux
      {:key-row-index key-ratio-index
       :key-element   key-value})))


(defn calculate-entering-and-exiting-variables
  "- Here we clearly extract the entering and exiting variables for the next iteration.
     The entering variable is variable in vector :basic-variables that indexed by :key-column-index.
     The exiting variable is the :active-variable indexed by :key-row-index
     ## New Keys:
     - :entering-variable
     - :exiting-variable"
  [tableaux]
  (let [key-column-index      (:key-column-index tableaux)
        basic-variable-row    (:basic-variable-row tableaux)
        entering-variable     (nth basic-variable-row key-column-index)
        key-row-index         (:key-row-index tableaux)
        tableaux-rows         (:tableaux-rows tableaux)
        exiting-variable      (:active-variable (nth tableaux-rows key-row-index))]
    (merge tableaux {:entering-variable entering-variable
                     :exiting-variable  exiting-variable})))

(defn setup-next-iteration
  [tableaux]
  (let [key-element             (:key-element tableaux)
        key-column-index        (:key-column-index tableaux)
        key-row-index           (:key-row-index tableaux)
        tableaux-rows           (:tableaux-rows tableaux)
        entering-variable       (:entering-variable tableaux)
        coeffecient-row         (:objective-coeffecient-row tableaux)
        entering-coeffecient    (nth coeffecient-row key-column-index)
        entering-row-to-update  (nth tableaux-rows key-row-index)
        updated-entering-row-s1 (merge entering-row-to-update
                                       {:active-variable entering-variable
                                        :cbi             entering-coeffecient})
        updated-entering-row-s2 (calculate-entering-row updated-entering-row-s1 key-element)
        updated-tableaux-rows   (assoc tableaux-rows key-row-index updated-entering-row-s2)]))


;coeffecient-row       (:objective-coeffecient-row tableaux)
;entering-coeffecient  (nth coeffecient-row key-column-index)
;tableaux-rows         (:tableaux-rows tableaux)
;row-to-update         (nth tableaux-rows key-row-index)
;updated-row           (merge row-to-update
;                             {:active-variable entering-variable
;                              :cbi             entering-coeffecient})
;updated-tableaux-rows (assoc tableaux-rows key-row-index updated-row)

;; ======================================
;;        Comment Helper Functions
;; ======================================

(comment (def it0 {:problem-type              :max
                   :iteration                 0
                   :basic-variable-row        [:x1 :x2 :s1 :s2]
                   :objective-coeffecient-row [12 16 0 0] ;; cj from video
                   :tableaux-rows             [{:cbi                     0
                                                :active-variable         :s1
                                                :constraint-coefficients [10 20 1 0]
                                                :solution                120
                                                :ratio                   0}
                                               {:cbi                     0
                                                :active-variable         :s2
                                                :constraint-coefficients [8 8 0 1]
                                                :solution                80
                                                :ratio                   0}]})
         (def tab1 (calculate-zj-row it0))
         (def tab2 (calculate-cj-zj-row it0))
         (find-key-column-value it0)
         (def calc-fn1 (comp calculate-key-column calculate-cj-zj-row calculate-zj-row))
         (def calc-fn2 (comp calculate-entering-and-exiting-variables
                             calculate-key-row-and-value
                             calculate-solution-to-key-val-ratio
                             calculate-key-column
                             calculate-cj-zj-row
                             calculate-zj-row))
         (calc-fn1 it0)
         (calc-fn2 it0)
         (clojure.pprint/pprint tab2)
         (reduce apply #(mapv (fn [x] (* 2 x)) %1) [[1 2] [3 4]])
         (def rows-to-reduce [{:cbi 16, :active-variable :x1, :constraint-coefficients [1/2 1 1/20 0], :solution 6, :ratio 1}
                              {:cbi 0, :active-variable :s2, :constraint-coefficients [8 8 0 1], :solution 80, :ratio 10}])
         (calculate-non-entering-row
           {:cbi 0, :active-variable :s2, :constraint-coefficients [8 8 0 1], :solution 80, :ratio 10}
           [10 20 1 0]
           0
           1
           1
           20)
         (calculate-non-entering-rows [8 8 0 1] [10 20 1 0] 0 1 20))