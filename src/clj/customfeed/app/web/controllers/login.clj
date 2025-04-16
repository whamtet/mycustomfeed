(ns customfeed.app.web.controllers.login)

(defn login [session]
  (assoc session :id 1))
