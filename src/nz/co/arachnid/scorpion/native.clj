(ns nz.co.arachnid.scorpion.native
  (:use [uncomplicate.neanderthal core native]))

(def x (dv 1 2 3))
(def y (dv 10 20 30))
(dot x y)
