(defproject nz.co.arachnid/scorpion "0.1.0-SNAPSHOT"
  :description "Linear Algebra Playpen with Neanderthal"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure            "1.10.1"]
                 [uncomplicate/neanderthal       "0.25.3"]
                 [org.openjfx/javafx-fxml        "11.0.1"]
                 [org.openjfx/javafx-controls    "11.0.1"]
                 [org.openjfx/javafx-swing       "11.0.1"]
                 [org.openjfx/javafx-base        "11.0.1"]
                 [org.openjfx/javafx-web         "11.0.1"]]


  :resource-paths ["resources/REBL-0.9.172.jar"]

  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.10"]
                                  [org.clojure/java.classpath   "0.3.0"]
                                  [midje                        "1.9.9"]]
                   :plugins      [[lein-ancient "0.6.15"]
                                  [lein-midje    "3.2.1"]]}
             :uberjar {:aot :all}}

  :jvm-opts ^:replace ["-Dclojure.compiler.direct-linking=true"
                       "--add-opens=java.base/jdk.internal.ref=ALL-UNNAMED"]

  :repl-options {:init-ns nz.co.arachnid.scorpion.core
                 :port    10000})