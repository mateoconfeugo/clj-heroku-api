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
;; TODO:  Remove the sleep and use a promise a delay a future something!
;;========================================================================
(def test-heroku-app  (create-app test-app-name heroku-api-token))
(expect true (= test-app-name (.getName test-heroku-app)))
(Thread/sleep 10000)
(expect true (app-exists? test-app-name heroku-api-token))
(def removal-result (remove-app test-app-name heroku-api-token))
(Thread/sleep 10000)
(expect false (app-exists? test-app-name heroku-api-token))

;;========================================================================
;; UNIT TEST: New ssh public key add and remove
;;========================================================================
(def test-ssh-pub-key "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCz29znMi/UJX/nvkRSO5FFugKhU9DkkI53E0vXUnP8zeLFxMgyUqmXryPVjWtGzz2LRWqjm14SbqHAmM44pGHVfBIp6wCKBWSUYGv/FxOulwYgtWzz4moxWLZrFyWWgJAnehcVUifHNgzKwT2ovWm2ns52681Z8yFK3K8/uLStDjLIaPePEOaxaTvgIxZNsfyEoXoHcyTPwdR1GtQuDTuDYqYmjmPCoKybYnXrTQ1QFuQxDneBkswQYSl0H2aLf3uBK4F01hr+azXQuSe39eSV4I/TqzmNJlanpILT9Jz3/J1i4r6brpF3AxLnFnb9ufIbzQAIa/VZIulfrZkcBsUl user@company.com")
(def plain-test-ssh-pub-key "AAAAB3NzaC1yc2EAAAADAQABAAABAQCz29znMi/UJX/nvkRSO5FFugKhU9DkkI53E0vXUnP8zeLFxMgyUqmXryPVjWtGzz2LRWqjm14SbqHAmM44pGHVfBIp6wCKBWSUYGv/FxOulwYgtWzz4moxWLZrFyWWgJAnehcVUifHNgzKwT2ovWm2ns52681Z8yFK3K8/uLStDjLIaPePEOaxaTvgIxZNsfyEoXoHcyTPwdR1GtQuDTuDYqYmjmPCoKybYnXrTQ1QFuQxDneBkswQYSl0H2aLf3uBK4F01hr+azXQuSe39eSV4I/TqzmNJlanpILT9Jz3/J1i4r6brpF3AxLnFnb9ufIbzQAIa/VZIulfrZkcBsUl")
(def add-ssh-pub-key-result (add-key test-ssh-pub-key heroku-api-token))
(expect true (>  (count (list-keys heroku-api-token)) 1))
(def delete-ssh-pub-key-result (remove-key plain-test-ssh-pub-key heroku-api-token))
(expect true (=  (count (list-keys heroku-api-token)) 1))
