(ns customfeed.app.env
  (:require
    [clojure.tools.logging :as log]
    [customfeed.app.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[app starting using the development or test profile]=-"))
   :start      (fn []
                 (log/info "\n-=[app started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[app has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev}})
