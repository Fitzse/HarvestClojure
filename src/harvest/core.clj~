(ns harvest.core
  (:require [http.async.client :as http] [clojure.data.codec.base64 :as b64] [clojure.data.json :as json]))

(defn get-auth-string [user pass]
  (format "Basic %s"
    (String. (b64/encode (.getBytes (format "%s:%s" user pass))))))

(defn get-json[url user pass]
(with-open [client (http/create-client)]
  (let [response (http/GET client url :headers {:Accept "application/json" :Content-Type "application/json" :Authorization (get-auth-string user pass)})]
    (-> response
      http/await
      http/string))))

(defn get-projects [user pass]
  (map #(:project %)
    (json/read-str
      (get-json "https://devfacto.harvestapp.com/projects" user pass)
      :key-fn keyword)))

(defn list-projects [user pass]
  (map #(:name %) (get-projects user pass)))
