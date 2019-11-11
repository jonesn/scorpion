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

;; Helper Functions
;; ================

(defn- positions
  [pred coll]
  (keep-indexed (fn [idx x]
                  (when (pred x)
                    idx))
                coll))

(defn- mult-coeffecients-by-scalar
  [n vec-coeffecients]
  (map (fn [elem] (* n elem)) vec-coeffecients))

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
   (merge tableaux {:Zj-row zj})))


(defn calculate-cj-zj-row
  "Cj - Zj Row
   ===========
   - Once the Zj row is calculated simply subtracts the Cj row from it.
     The supplied tableaux is updated with the result
   ## New Keys
   - :Cj-Zj"
  [tableaux]
  (let [zj-row   (:Zj-row tableaux)
        cj-row   (:objective-coeffecients tableaux)
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
  [tableaux row-key when-min-fn-a when-max-fn-b]
  (let [vec          (row-key tableaux)
        problem-type (:problem-type tableaux)]
    (cond
      (= problem-type :min) (apply when-min-fn-a vec)
      (= problem-type :max) (apply when-max-fn-b vec)
      :else (throw (ex-info "Unknown Problem Type" {:problem-type problem-type})))))


(defn- find-key-column-value
  [tableaux]
  (find-key-value tableaux :Cj-Zj min max))


(defn- find-key-ratio-value
  [tableaux]
  (find-key-value tableaux :ratio-column max min))


(defn calculate-key-column
  "- Fox max problems the key column is the cj-zj-row column containing the highest value.
   - For min problems the key column is the cj-zj-row column containing the lowest value.
   ## New Keys:
   - :key-column-index
   - :key-column-value"
  [tableaux]
  (let [cj-zj-row     (:Cj-Zj tableaux)
        key-value     (find-key-column-value tableaux)
        column-index  (first (positions #{key-value} cj-zj-row))
        key-column    (->> (:tableaux-rows tableaux)
                           (map :constraint-coefficients)
                           (mapv (fn [v] (nth v column-index))))]
    (-> tableaux
        (merge {:key-column-value key-column})
        (merge {:key-column-index column-index}))))


(defn calculate-solution-to-key-val-ratio
  "- Take the solution (s) and key (k) columns and produce a new column of the ratios: s / k
   ## New Keys
   - :ratio-column"
  [tableaux]
  (let [solution-column (:solution-column-value tableaux)
        key-column      (:key-column-value      tableaux)
        ratio-column    (mapv
                          (fn [x y] (/ x y))
                          solution-column
                          key-column)]
    (merge {:ratio-column ratio-column} tableaux)))


(defn calculate-key-row-and-value
  "- Selects the intersection of the key column and row indexes and sets that as the key value.
     The selection of the key row is based on the ratio index.
   ## New Keys:
   - :key-row-index
   - :key-row-value
   - :key-value"
  [tableaux]
  (let [ratio-column     (:ratio-column tableaux)
        key-ratio-value  (find-key-ratio-value tableaux)
        key-ratio-index  (first (positions #{key-ratio-value} ratio-column))
        key-row          (:constraint-coefficients (nth (:tableaux-rows tableaux) key-ratio-index))
        key-column-index (:key-column-index tableaux)
        key-value        (nth key-row key-column-index)]
    (merge {:key-row-value key-row
            :key-row-index key-ratio-index
            :key-value     key-value} tableaux)))

;; ======================================
;;        Comment Helper Functions
;; ======================================

(comment (def it0 {:problem-type           :max
                   :iteration              0
                   :basic-variables        [:x1 :x2 :s1 :s2]
                   :objective-coeffecients [12 16 0 0] ;; cj from video
                   :tableaux-rows          [{:cbi                     0
                                             :constraint-coefficients [10 20 1 0]
                                             :solution                120
                                             :ratio                   0}
                                            {:cbi                     0
                                             :constraint-coefficients [8 8 0 1]
                                             :solution                80
                                             :ratio                   0}]
                   :solution-column-value  [120 80]
                   :Zj-row                 [0 0 0 0]
                   :Cj-Zj                  [12 16 0 0]})
         (def tab1 (calculate-zj-row it0))
         (def tab2 (calculate-cj-zj-row it0))
         (find-key-column-value it0)
         (def calc-fn1 (comp calculate-key-column calculate-cj-zj-row calculate-zj-row))
         (def calc-fn2 (comp calculate-key-row-and-value
                             calculate-solution-to-key-val-ratio
                             calculate-key-column
                             calculate-cj-zj-row
                             calculate-zj-row))
         (calc-fn1 it0)
         (calc-fn2 it0)
         (clojure.pprint/pprint tab2)
         (reduce #(map + %1 %2)  zj-row))