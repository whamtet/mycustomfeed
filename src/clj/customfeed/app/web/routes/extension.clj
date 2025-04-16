(ns customfeed.app.web.routes.extension
  (:require
   [customfeed.app.web.middleware.cors :as cors]
   [customfeed.app.web.middleware.exception :as exception]
   [customfeed.app.web.middleware.formats :as formats]
   [customfeed.app.web.views.home :as home]
   [integrant.core :as ig]
   [reitit.coercion.malli :as malli]
   [reitit.ring.coercion :as coercion]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]))

(defn route-data [opts]
  (merge
   opts
   {:coercion   malli/coercion
    :muuntaja   formats/instance
    :middleware
    [;; query-params & form-params
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
      exception/wrap-exception]}))

(derive :reitit.routes/extension :reitit/routes)

(defmethod ig/init-key :reitit.routes/extension
  [_ opts]
  ["/extension" (route-data opts) (home/extension opts)])
