(defproject nz.co.arachnid/scorpion "0.1.0-SNAPSHOT"
  :description "Linear Algebra Toolkit to use in conjunction with Gorilla REPL"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure            "1.10.0"]
                 [uncomplicate/neanderthal       "0.22.1"]]

  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.10"]
                                  [org.clojure/java.classpath   "0.3.0"]
                                  [walmartlabs/datascope        "0.1.1"]]
                   :plugins      [[lein-ancient "0.6.15"]]}
             :uberjar {:aot :all}}

  :plugins [[lein-with-env-vars "0.1.0"]]

  :env-vars {
             :LD_LIBRARY_PATH
             "/opt/intel/compilers_and_libraries_2019.2.187/linux/mkl/lib/intel64_lin"}
  :repl-options {:init-ns nz.co.arachnid.scorpion.core})



