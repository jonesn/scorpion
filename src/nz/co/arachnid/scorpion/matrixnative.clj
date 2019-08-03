(ns nz.co.arachnid.scorpion.matrixnative
  (:use [uncomplicate.neanderthal core native linalg])
  (:use [uncomplicate.commons.core])
  (:require [clojure.core.protocols :as p])
  (:import (uncomplicate.neanderthal.internal.host.buffer_block RealGEMatrix)))

;; ===================================
;; Patch in additional datafy support
;; ===================================

(extend-protocol p/Datafiable
  RealGEMatrix
  (datafy [x] (with-meta
                (seq x)
                (info x))))

;; =================
;; Intel MKL Version
;; =================

(defn large-square-matrix-mult-native
  "Demo function to do large square Matrix multiplications and time them."
  [n]
  (let [cnt        n
        matrix-a   (fge cnt cnt (repeat 1))
        matrix-b   (copy matrix-a)]
    (time
      (let [result (mm 1.0 matrix-a matrix-b)]
          ;; Return the Matrix and the sum of its elements
          {:sum-of-elements   (rationalize (asum result))
           :matrix            result}))))

(comment
  (large-square-matrix-mult-native 8)
  (large-square-matrix-mult-native 16)
  (large-square-matrix-mult-native 32)
  (large-square-matrix-mult-native 64)
  (large-square-matrix-mult-native 128)
  (large-square-matrix-mult-native 256)
  (large-square-matrix-mult-native 512)
  (large-square-matrix-mult-native 1024)
  (large-square-matrix-mult-native 2048)
  ;; 4096^3 is  68,719,476,736 operations
  ;; 68 billion
  (large-square-matrix-mult-native 4096)
  ;; 8192^3 is 549,755,813,888 1/2 teraflop
  ;; 549 billion
  (large-square-matrix-mult-native 8192))

;; ===========================================
;;   Solving a Linear System, Circuit Example
;; ===========================================

(def coeffecient-matrix (dge 3 3 [1 -1 1
                                  4  1 0
                                  0  1 4] {:layout :row}))

(def resulting-matrix (dge 3 1 [0
                                8
                                16] {:layout :row}))

(defn solve-linear-system
  [coeffecient-matrix result-matrix]
  (sv coeffecient-matrix result-matrix))

(solve-linear-system coeffecient-matrix resulting-matrix)

(comment
  (user/rebl-send
    (:matrix (large-square-matrix-mult-native 16)))
  (user/rebl-send coeffecient-matrix)
  (user/rebl-send resulting-matrix)
  (user/rebl-send (solve-linear-system coeffecient-matrix resulting-matrix)))




