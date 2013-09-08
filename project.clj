(defproject nitrogentd "0.1.0-SNAPSHOT"
  :description "Clojurescript tower defense game"
  :url "https://github.com/stothardj/NitrogenTD"
  :source-paths ["src-clj"]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1843"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.3"]
                 [com.cemerick/clojurescript.test "0.0.4"]
                 [org.clojure/google-closure-library-third-party "0.0-2029"]
                 [domina "1.0.1"]]
  :plugins [[lein-cljsbuild "0.3.2"]
            [lein-ring "0.7.0"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {
              :builds {
                       :dev
                       {:source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/js/main-debug.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       :prod
                       {:source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/js/main.js"
                                   :optimizations :advanced
                                   :pretty-print false}}
                       :test
                       {:source-paths ["src-cljs" "test-cljs"]
                        :compiler {:output-to "resources/private/js/unit-test.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}}
              :test-commands {"unit-tests" ["runners/phantomjs.js"
                                            "resources/private/js/unit-test.js"]}
              }
  :ring {:handler nitrogentd.routes/app}
  )
