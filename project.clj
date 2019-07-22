(defproject nz.co.arachnid/scorpion "0.1.0-SNAPSHOT"
  :description "Linear Algebra Playpen with Neanderthal"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure            "1.10.1"]
                 [uncomplicate/neanderthal       "0.25.3"]]

  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.10"]
                                  [org.clojure/java.classpath   "0.3.0"]
                                  [walmartlabs/datascope        "0.1.1"]]
                   :plugins      [[lein-ancient "0.6.15"]]}
             :uberjar {:aot :all}}

  ; Required for post 8 jdks on Linux
  ;:jvm-opts ^:replace ["-Dclojure.compiler.direct-linking=true"
  ;                     "--add-opens=java.base/jdk.internal.ref=ALL-UNNAMED"]

  :repl-options {:init-ns nz.co.arachnid.scorpion.core
                 :port    10000})



