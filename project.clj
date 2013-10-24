(defproject clj-heroku-api "0.1.0"
  :description "Heroku API"
  :url "https://github.com/mateoconfeugo/clj-heroku-api"
  :license {:name "Eclipse Public License" :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[cheshire "5.2.0"] ; JSON <-> clojure
                 [org.clojure/clojure "1.5.1"]
                 [com.heroku.api/heroku-api "0.15"]
                 [com.heroku.api/heroku-json-jackson "0.15"]
                 [com.heroku.api/heroku-http-apache "0.15"]]
  :min-lein-version "2.0.0"
  :plugins [[lein-set-version "0.3.0"]  ; set version via lein command line
            [lein-marginalia "0.7.1"]   ; literate programming
            [lein-expectations "0.0.7"] ; run expect test
            [lein-autoexpect "0.2.5"]
            [lein-ancient "0.5.1"]]
  :profiles  {:dev {:dependencies [[expectations "1.4.56"]
                                   [com.taoensso/timbre "2.6.2"] ; Logging
                                   [org.clojure/tools.trace "0.7.6"]]}})
