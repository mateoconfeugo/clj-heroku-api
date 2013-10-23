(ns clj-heroku-api.core
  ^{:author "Matthew Burns"
    :doc "Clojure wrapper around the heroku java library https://github.com/heroku/heroku.jar"}
  (:require [clojure.core]
            [clojure.edn])
  (:import  [com.heroku.api HerokuAPI]
            [com.heroku.api App ]
            [com.heroku.api.request.app AppDestroy AppExists]
            [com.heroku.api Heroku$Stack]
            [com.heroku.api.connection Connection]
            [com.heroku.api.connection ConnectionFactory]))

(defn remove-app
  "Remove the named application from the heroku hosting service"
  [name api-token]
  (let [conn (ConnectionFactory/get)]
    (.execute conn (AppDestroy. name) api-token)))

(defn create-app
  "new heroku application and install it on heroku hosting service"
  [name api-token]
  (let [conn (ConnectionFactory/get)
        api (HerokuAPI. api-token)
        app (-> (App. )
                (.named name)
                (.on com.heroku.api.Heroku$Stack/Cedar))]
    (.createApp api app)))

(defn app-exists?
  "Check if an app with a given name exists in heroku"
  [name api-token]
  (let [conn (ConnectionFactory/get)]
        (.execute conn (AppExists. name) api-token)))

(defn list-apps
  "List the apps associated with the account accessed via the token"
  [api-token]
  (.listApps (HerokuAPI. api-token)))
