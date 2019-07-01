(ns nz.co.arachnid.scorpion.matrixnative
  (:use [uncomplicate.neanderthal core native]))

;; Intel MKL Version

(defn large-square-matrix-mult
  "Demo function to do large square Matrix multiplications and time them."
  [n]
  (let [cnt        n
        matrix-a   (fge cnt cnt (repeat 1))
        matrix-b   (copy matrix-a)
        matrix-c   (copy matrix-a)]
    (time
      (do (mm! 3 matrix-a matrix-b 2 matrix-c)
          ;; Return the Matrix and the sum of its elements
          {:sum-of-elements (rationalize (sum matrix-c))
           :matrix          matrix-c}))))

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


