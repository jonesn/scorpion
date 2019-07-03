(ns nz.co.arachnid.scorpion.matrixopencl
  (:require [uncomplicate.clojurecl.core :refer :all]
            [uncomplicate.neanderthal.opencl :refer :all]
            [uncomplicate.neanderthal.core :refer :all]))

;; ==========================================
;; Linear Algebra Ops Don't Run on OpenCL 1.2
;; ==========================================

(defn large-square-matrix-mult
  [n]
  (with-default
    (with-default-engine
      (let [cnt        n
            matrix-a   (clge cnt cnt (range (* cnt cnt)))
            matrix-b   (copy matrix-a)
            matrix-c   (copy matrix-a)]
        (time (do (mm! 3 matrix-a matrix-b 2 matrix-c)
                  matrix-c))))))

(comment
  (large-square-matrix-mult 8)
  (large-square-matrix-mult 16)
  (large-square-matrix-mult 32)
  (large-square-matrix-mult 64)
  (large-square-matrix-mult 128)
  (large-square-matrix-mult 256)
  (large-square-matrix-mult 1024)
  (large-square-matrix-mult 2048)
  ;; 4096^2.3 is 203,436,033 operations
  (large-square-matrix-mult 4096)
  (large-square-matrix-mult 8192))

