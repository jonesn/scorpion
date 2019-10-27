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

(defn- mult-coeffecients-by-scalar
  [s coeffecients]
  (map (fn [elem] (* s elem)) coeffecients))

(defn calculate-zj-row
  "Zj Row
   ======
   Zj = sum of i = 1..n (cbi[i] * constraint-coeffecients[i,j]
   n is the number of (constraint coeffecients + solution)"
  [tableaux]
  (let [t-rows           (:tableaux-rows tableaux)
        cbi*constraints  (map
                           (fn [row] (mult-coeffecients-by-scalar (:cbi row) (:constraint-coeffecients row)))
                           t-rows)
        zj               (apply #(mapv + %1 %2) cbi*constraints)]
   (merge tableaux {:Zj-row zj})))


(comment (def it0 {:iteration                 0
                   :basic-variables           [:x1 :x2 :s1 :s2]
                   :objective-coeffecients    [12  16  0  0] ;; cj from video
                   :tableaux-rows             [{:cbi 0
                                                :constraint-coeffecients [10 20 1 0]
                                                :solution 120
                                                :ratio    0}
                                               {:cbi 0
                                                :constraint-coeffecients [8 8 0 1]
                                                :solution 80
                                                :ratio    0}]
                   :constraint-limits         [120 80]
                   :Zj-row                    [0 0 0 0]
                   :Cj-Zj                     [12 16 0 0]})
         (def zj-row (calculate-zj-row it0))
         (reduce #(map + %1 %2)  zj-row))