(ns customfeed.app.web.controllers.list
  (:require
    [customfeed.app.web.controllers.common :as common]))

(defn get-lists [{:keys [query-fn session]}]
  (->
   (common/get-detail query-fn :get-lists session)
   :detail
   (or {})))

(defn update-lists [{:keys [query-fn session]} detail]
  (common/update-detail query-fn :update-lists session detail))

(defn- apply-lists [req f]
  (->> req
       get-lists
       f
       (update-lists req)))

(defn delete-list [req list-name]
  (apply-lists req #(dissoc % list-name)))
