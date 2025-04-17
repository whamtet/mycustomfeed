(ns customfeed.app.web.controllers.common)

(defn get-detail [query-fn k params]
  (-> (query-fn k params)
      (update :detail #(some-> % read-string))))

(defn update-detail [query-fn k session detail]
  (query-fn k (assoc session :detail (pr-str detail))))
