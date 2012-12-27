(ns harvest.core
  (:require [http.async.client :as http] [clojure.data.codec.base64 :as b64] [clojure.data.json :as json]))

(defn encode-str [unencoded]
  (String. (b64/encode (.getBytes unencoded))))

(defn get-basic-auth-header [user pass] 
  {:Authorization (str "Basic " (encode-str (str user ":" pass)))})

(defn get-json-headers []
  {:Accept "application/json" :Content-Type "application/json"})

(defn get-app-headers [auth-fn]
  (merge (get-json-headers) (auth-fn)))

(defn get-basic-headers [user pass]
  (get-app-headers #(get-basic-auth-header user pass)))

(defn get-json[url headers]
(with-open [client (http/create-client)]
  (let [response (http/GET client url :headers headers)] 
    (-> response
      http/string))))

(defn get-projects [user pass]
  (map #(:project %)
    (json/read-str
      (get-json "https://devfacto.harvestapp.com/projects" (get-basic-headers user pass))
      :key-fn keyword)))

(defn get-entry [day user pass]
  (:day_entries 
     (json/read-str
       (get-json (format "https://devfacto.harvestapp.com/daily/%s/2012" day) (get-basic-headers user pass))
       :key-fn keyword)))

(defn get-entries [days user pass]
  (flatten (map #(get-entry % user pass) days)))

(defn list-projects [user pass]
  (map #(:name %) (get-projects user pass)))
