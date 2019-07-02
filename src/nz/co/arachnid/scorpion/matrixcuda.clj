(ns nz.co.arachnid.scorpion.matrixcuda
  (:require [uncomplicate.clojurecuda.core :refer :all]
            [uncomplicate.commons.core :refer :all])
  (:use [uncomplicate.neanderthal core cuda]))

(init)

;; =======================
;; Large Matrix Operations
;; =======================

(defn large-square-matrix-mult-cuda
  "Demo function to do large square Matrix multiplications and time them."
  [n]
  (with-default
    (with-default-engine
        (with-release [cnt        n
                       matrix-a   (cuge cnt cnt (repeat 1))
                       matrix-b   (copy matrix-a)]
          (time
            (let [result (mm 1.0 matrix-a matrix-b)]
              ;; Return the Matrix and the sum of its elements
              {:sum-of-elements       (rationalize (asum result))
               :matrix                result}))))))

(comment
  (large-square-matrix-mult-cuda 8)
  (large-square-matrix-mult-cuda 16)
  (large-square-matrix-mult-cuda 32)
  (large-square-matrix-mult-cuda 64)
  (large-square-matrix-mult-cuda 128)
  (large-square-matrix-mult-cuda 256)
  (large-square-matrix-mult-cuda 512)
  (large-square-matrix-mult-cuda 1024)
  (large-square-matrix-mult-cuda 2048)
  ;; 4096^3 is  68,719,476,736 operations
  ;; 68 billion
  (large-square-matrix-mult-cuda 4096)
  ;; 8192^3 is 549,755,813,888 1/2 teraflop
  ;; 549 billion
  (large-square-matrix-mult-cuda 8192))

