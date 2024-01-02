(defproject my-sketch "0.1.0-SNAPSHOT"
  :description "Adventures in Quil"
  :url "http://alphajuliet.com/ns/my-sketch/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [spec-dict "0.2.1"]
                 [quil "4.0.0-SNAPSHOT"]
                 [org.clojure/algo.monads "0.1.6"]]
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}})
