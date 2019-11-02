(ns nz.co.arachnid.scorpion.simplex-test
  (:require [midje.sweet :refer :all])
  (:require [nz.co.arachnid.scorpion.simplex :refer :all]))

;; ======================
;; Max Problem Iterations
;; ======================

(def iteration-0-pre
  {:problem-type              :max
   :iteration                 0
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
   :constraint-limits         [120 80]})

(facts "Calculate ZJ Row Cases"
       (fact "Given Iteration 0 we will correctly calculate a zero Zj row"
             (:Zj-row (calculate-zj-row iteration-0-pre)) => [0 0 0 0]))

(facts "Calculate Cj - Zj"
       (fact "Given iteration 0 we will correctly calculate the Cj-Zj row"
             (let [undertest (comp calculate-cj-zj-row calculate-zj-row)]
              (:Cj-Zj (undertest iteration-0-pre)) => [12 16 0 0])))

(facts "Check Optimality"
       (fact "Given iteration 0 we will deduce that the max optimum has not been meet"
             (let [undertest (comp optimal-solution? calculate-cj-zj-row calculate-zj-row)]
               (undertest iteration-0-pre) => false)))

;; ======================
;; Min Problem Iterations
;; ======================

;; ======================
;;     Zombie Cases
;; ======================
