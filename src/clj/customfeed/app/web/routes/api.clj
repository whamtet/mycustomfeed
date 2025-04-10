(ns customfeed.app.web.routes.api
  (:require
    [clojure.java.io :as io]
    [customfeed.app.util :refer [defm-dev]]
    [customfeed.app.web.controllers.health :as health]
    [customfeed.app.web.middleware.cors :as cors]
    [customfeed.app.web.middleware.exception :as exception]
    [customfeed.app.web.middleware.formats :as formats]
    [integrant.core :as ig]
    [reitit.coercion.malli :as malli]
    [reitit.ring.coercion :as coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.parameters :as parameters]
    [reitit.swagger :as swagger]))

(def route-data
  {:coercion   malli/coercion
   :muuntaja   formats/instance
   :swagger    {:id ::api}
   :middleware [;; query-params & form-params
                parameters/parameters-middleware
                  ;; content-negotiation
                muuntaja/format-negotiate-middleware
                  ;; encoding response body
                muuntaja/format-response-middleware
                  ;; exception handling
                coercion/coerce-exceptions-middleware
                  ;; decoding request body
                muuntaja/format-request-middleware
                  ;; coercing response bodys
                coercion/coerce-response-middleware
                  ;; coercing request parameters
                coercion/coerce-request-middleware
                cors/wrap-cors
                  ;; exception handling
                exception/wrap-exception]})

(defm-dev button []
  (-> "public-api/button.html" io/resource slurp))

;; Routes
(defn api-routes [_opts]
  [["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info {:title "customfeed.app API"}}
           :handler (swagger/create-swagger-handler)}}]
   ["/button"
    (fn [req]
      {:status 200
       :headers {}
       :body (button)})]
   ["/health"
    {:get health/healthcheck!}]])

(derive :reitit.routes/api :reitit/routes)

(defmethod ig/init-key :reitit.routes/api
  [_ opts]
  (fn [] ["/api" route-data (api-routes opts)]))
