(ns customfeed.app.web.middleware.core
  (:require
    [customfeed.app.env :as env]
    [customfeed.app.web.middleware.defaults :as defaults]
    [ring.middleware.session.cookie :as cookie]))

(defn wrap-base
  [{:keys [metrics site-defaults-config cookie-secret] :as opts}]
  (let [cookie-store (cookie/cookie-store {:key (.getBytes ^String cookie-secret)})]
    (fn [handler]
      (-> ((:middleware env/defaults) handler opts)
          (defaults/wrap-defaults
           (assoc-in site-defaults-config [:session :store] cookie-store))))))
