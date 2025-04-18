(ns customfeed.app.web.views.home
  (:require
    [customfeed.app.env :refer [prod?]]
    [customfeed.app.web.controllers.login :as login]
    [customfeed.app.web.i18n :refer [i18n]]
    [customfeed.app.web.htmx :refer [page-htmx defcomponent]]
    [customfeed.app.web.views.components :as components]
    [customfeed.app.web.views.extension-panel :as extension-panel]
    [simpleui.core :as simpleui]))

(defcomponent ^:endpoint login [req command]
  (cond
    (= "do" command)
    (let [session (login/login session)]
      {:session session
       :body (-> req
                 (assoc :session session)
                 extension-panel/extension-panel)})
    user_id
    (extension-panel/extension-panel req)
    :else
    [:div {:id "container" :hx-target "this"}
     [:div.mt-6.flex.justify-center
      [:div {:hx-post "login:do"}
       (components/button (i18n "Log In"))]]]))

(defn extension [{:keys [query-fn]}]
  (simpleui/make-routes-simple
   (if prod?
     "https://app.mycustomfeed.com/extension/"
     "http://localhost:3002/extension/")
   [query-fn]
   login))

(defn home [opts]
  (simpleui/make-routes
   ""
   (fn [req]
     (page-htmx
      {:css ["/output.css"]}
      "fuck"))))
