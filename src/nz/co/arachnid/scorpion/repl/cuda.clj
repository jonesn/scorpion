(ns nz.co.arachnid.scorpion.repl.cuda
  (:require [uncomplicate.neanderthal.core :refer :all])
  (:require [uncomplicate.commons.core     :refer :all])
  (:require [uncomplicate.neanderthal.cuda :refer :all]))

(with-default-engine
  (with-release [gpu-x (cuv 1 -2 5)]
                (asum gpu-x)))