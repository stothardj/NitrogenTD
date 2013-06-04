(defproject cake "0.1.0-SNAPSHOT"
  :description "Clojurescript tower defense game"
  :url "https://github.com/stothardj/OpenTD"
  :source-paths ["src-clj"]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.3"]
                 [com.cemerick/clojurescript.test "0.0.4"]]
  :plugins [[lein-cljsbuild "0.3.0"]
            [lein-ring "0.7.0"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {
              :builds [{:source-paths ["src-cljs" "test-cljs"]
                        :compiler {:output-to "resources/public/js/main.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]
              :test-commands {"unit-tests" ["runners/phantomjs.js" "resources/public/js/main.js"]}
              }
  :ring {:handler cake.routes/app}
  )
