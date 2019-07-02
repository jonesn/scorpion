(ns nz.co.arachnid.scorpion.matrixnative
  (:use [uncomplicate.neanderthal core native]))

;; Intel MKL Version

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
