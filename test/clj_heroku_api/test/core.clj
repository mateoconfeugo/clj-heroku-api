(ns clj-heroku-api.test.core
  ^{:author "Matthew Burns"
    :doc "Unit/Component tests for the functionality offered by the Heroku API via this clojure wrapper library"}
  (:use [clj-heroku-api.core] ;; Library being tested
        [clojure.core])
  (:require  [expectations :refer [expect]])
  (:import [java.lang Exception]))

;;========================================================================
;; SETUP: Obtain secret key from environment and a bogus name
;;========================================================================
(def heroku-api-token (if-let [token  (System/getenv "HEROKU_API_TOKEN")]
                        token
                        (throw (Exception. "No api token environment variable"))))

;; TODO make this some random name or adjust just to use the random name heroku assigns
(def test-app-name "nobody-would-ever-use-this")

;;========================================================================
;; UNIT TEST: New app added and removed from heroku app hosting cloud
;;========================================================================
(def test-heroku-app  (create-app test-app-name heroku-api-token))
(expect true (= test-app-name (.getName test-heroku-app)))
(Thread/sleep 10000) ;; Remove these and use a promise
(expect true (app-exists? test-app-name heroku-api-token))
(def removal-result (remove-app test-app-name heroku-api-token))
(Thread/sleep 10000)
(expect false (app-exists? test-app-name heroku-api-token))
