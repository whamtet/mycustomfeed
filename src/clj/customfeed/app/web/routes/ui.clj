(ns customfeed.app.web.routes.ui
  (:require
   [customfeed.app.web.middleware.cors :as cors]
   [customfeed.app.web.middleware.exception :as exception]
   [customfeed.app.web.middleware.formats :as formats]
   [customfeed.app.web.views.home :as home]
   [integrant.core :as ig]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]))

(defn route-data [opts]
  (merge
   opts
   {:muuntaja   formats/instance
    :middleware
    [;; Default middleware for ui
    ;; query-params & form-params
      parameters/parameters-middleware
      ;; encoding response body
      muuntaja/format-response-middleware
      cors/wrap-cors
      ;; exception handling
      exception/wrap-exception]}))

(derive :reitit.routes/ui :reitit/routes)

(defmethod ig/init-key :reitit.routes/ui
  [_ opts]
  ["" (route-data opts) (home/home opts)])
