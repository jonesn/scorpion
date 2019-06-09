(ns nz.co.arachnid.scorpion.repl.cuda
  (:require [uncomplicate.clojurecuda.core :refer :all])
  (:require [uncomplicate.commons.core     :refer :all])
  (:require [uncomplicate.clojurecuda.info :refer :all]))

(init)

(map info
     (map device
          (range (device-count))))

(def my-nvidia-gpu (device 0))

(def ctx (context my-nvidia-gpu))

;; set the current context
(current-context! ctx)

;; allocate memory on the GPU
(def gpu-array (mem-alloc 1024))

;; allocate memory on the host
(def main-array (float-array (range 256)))

;; copy host memory to the device
(memcpy-host! main-array gpu-array)

(def kernel-source
  "extern \"C\"
     __global__ void increment (int n, float *a) {
       int i = blockIdx.x * blockDim.x + threadIdx.x;
       if (i < n) {
         a[i] = a[i] * 2.0f;
    }
   };")

(def hello-program (compile! (program kernel-source)))

(def hello-module (module hello-program))

(def increment (function hello-module "increment"))

(launch! increment (grid-1d 256) (parameters 256 gpu-array))

(def result (memcpy-host! gpu-array (float-array 256)))

(take 12 result)