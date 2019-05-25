(ns nz.co.arachnid.scorpion.matrixopencl
  (:require [uncomplicate.clojurecl.core :refer :all]
            [uncomplicate.neanderthal.opencl :refer :all]
            [uncomplicate.neanderthal.core :refer :all]))

;; ==================================================================================
;; Conveniently in interactive REPL sessions (but don't do this in production code):
;; ==================================================================================

(set-default-1!)
(set-engine!)

;; =================================
;;   Some Vectors Backed By The GPU
;; =================================
(def x (clv 1 2 3))
(def y (clv 4 5 6))

;; =================================
;;       Vector Operations
;; =================================

(dot x y)

;; 10 * 10 Matrix
(def m (clge 10 10))

(release-context!)


