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

{:iteration                 0
 :basic-variables           [:x1 :x2 :s1 :s2]
 :objective-coeffecients    [12  16  0  0] ;; cj from video
 :tableaux-row              [{:s1 {:cbi 0
                                   :constraint-coeffecients [10 12 1 0]
                                   :solution 0
                                   :ratio    0}}
                             {:s2 {:cbi 0
                                   :constraint-coeffecients [8 8 0 1]
                                   :solution 0
                                   :ratio    0}}]
 :constraint-limits         [120 80]}