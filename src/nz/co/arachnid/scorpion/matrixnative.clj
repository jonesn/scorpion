(ns nz.co.arachnid.scorpion.matrixnative
  (:use [uncomplicate.neanderthal core native]))

;; Intel MKL Version

(defn large-square-matrix-mult
  [n]
  (let [cnt        n
        matrix-a   (dge cnt cnt (range (* cnt cnt)))
        matrix-b   (copy matrix-a)
        matrix-c   (copy matrix-a)]
    (time (do (mm! 3 matrix-a matrix-b 2 matrix-c)
              matrix-c))))

(comment
  (large-square-matrix-mult 8)
  (large-square-matrix-mult 16)
  (large-square-matrix-mult 32)
  (large-square-matrix-mult 64)
  (large-square-matrix-mult 128)
  (large-square-matrix-mult 256)
  (large-square-matrix-mult 512)
  (large-square-matrix-mult 1024)
  (large-square-matrix-mult 2048)
  ;; 4096^3 is  68,719,476,736 operations
  ;; 68 billion
  (large-square-matrix-mult 4096)
  ;; 8192^3 is 549,755,813,888 1/2 teraflop
  ;; 549 billion
  (large-square-matrix-mult 8192))


