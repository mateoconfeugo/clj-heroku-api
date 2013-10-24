(ns clj-heroku-api.core
  ^{:author "Matthew Burns"
    :doc "Clojure wrapper around the heroku java library https://github.com/heroku/heroku.jar"}
  (:require [cheshire.core :as json :refer [generate-string]]
            [clojure.core]
            [clojure.edn]
            [clojure.string :refer [split]])
  (:import  [com.heroku.api HerokuAPI]
            [com.heroku.api App ]
            [com.heroku.api.connection Connection]
            [com.heroku.api.connection ConnectionFactory]
            [com.heroku.api.request.key KeyAdd KeyRemove KeyList]
            [com.heroku.api Heroku$Stack]
            [com.heroku.api.request.app AppDestroy AppExists]
            [com.heroku.api.request.config ConfigAdd ConfigList ConfigRemove]))

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

(defn add-key
  "add a key so that github repo can be pulled in"
  [ssh-pub-key api-token]
  (let [conn (ConnectionFactory/get)]
    (.execute conn (KeyAdd. ssh-pub-key) api-token)))

(defn remove-key
  "remove ssh key"
  [ssh-pub-key api-token]
  (let [conn (ConnectionFactory/get)
        actual-key-seq (split ssh-pub-key #"\s") ]
    (if (> (count actual-key-seq) 1)
      (.execute conn (KeyRemove. (nth  (split  ssh-pub-key #"\s") 1)) api-token)
      (.execute conn (KeyRemove.  ssh-pub-key) api-token))))

(defn obtain-key
  "Get the accounts api token"
  [username password]
  (HerokuAPI/obtainApiKey  username password))

(defn list-keys
  "List the public keys associated with the account accessed via the api-token"
  [api-token]
  (let [conn (ConnectionFactory/get)]
    (.execute conn (KeyList. ) api-token)))

;;TODO:  write the test first matt!
(comment
  (defn add-config
    "Add a map of configs to the named application"
    [app-name configs api-token]
    (.execute (ConnectionFactory/get) (ConfigAdd. app-name (json/generate-string configs)) api-token)))
