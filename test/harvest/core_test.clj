(ns harvest.core-test
  (:use midje.sweet 
        harvest.core))

(fact "Encode String converts to base64"
      (encode-str "testString") => 
      "dGVzdFN0cmluZw==")

(fact "Get Basic Auth Header returns map with user/pass encoded in header"
      (get-basic-auth-header "sean" "password") =>
      {:Authorization "Basic encoded"}
      (provided (encode-str "sean:password") => "encoded"))

(fact "Get Json Headers returns a map with accept/content-type as json"
      (get-json-headers) =>
      {:Accept "application/json" :Content-Type "application/json"})

(fact "Get App Headers returns a map with json headers and result of auth function headers"
      (get-app-headers #(hash-map :Authorization "stuff")) =>
      {:Accept "application/json" :Content-Type "application/json" :Authorization "stuff"}
      (provided (get-json-headers) =>
        {:Accept "application/json" :Content-Type "application/json"}))

(fact "Get Basic Headers returns a map with authorization header set with user/pass encoded"
      (get-basic-headers "sean" "password") =>
      (merge (get-json-headers) {:Authorization (str "Basic " (encode-str "sean:password"))}))
