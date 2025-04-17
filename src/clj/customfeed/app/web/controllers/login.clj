(ns customfeed.app.web.controllers.login)

(defn login [session]
  (assoc session :user_id 1))
