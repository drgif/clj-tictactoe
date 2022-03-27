(defproject clj-tictactoe "0.1.0-SNAPSHOT"
  :description "A simple Tic Tac Toe implementation"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/test.check "1.1.1"]
                 [ring/ring-core "1.8.2"]
                 [ring/ring-jetty-adapter "1.8.2"]]
  :main ^:skip-aot clj-tictactoe.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
