(ns customfeed.app.web.controllers.list
  (:require
    [customfeed.app.web.controllers.common :as common]))

(defn get-lists [{:keys [query-fn session]}]
  (->
   (common/get-detail query-fn :get-lists session)
   :detail
   (or {})))

(defn- update-lists [{:keys [query-fn session]} detail]
  (common/update-detail query-fn :update-lists session detail))

(defn- apply-lists [req f & args]
  (as-> req $
        (get-lists $)
        (apply f $ args)
        (update-lists req $)))

(defn add-list [req list-name]
  (apply-lists req assoc list-name {}))
(defn delete-list [req list-name]
  (apply-lists req dissoc list-name))
(defn add-user [req list-name urn m]
  (apply-lists req assoc-in [list-name urn] m))
