(ns customfeed.app.web.controllers.list
  (:require
    [customfeed.app.web.controllers.common :as common]))

(defn get-list [{:keys [query-fn session]}]
  (:detail
    (common/get-detail query-fn :get-list session)))
