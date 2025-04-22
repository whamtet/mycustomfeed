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
  [:div#container.m-2
   [:div#refresh-extension.hidden {:hx-post "login" :hx-target "#container"}]
   [:div#show-warning.hidden {:hx-post "login:warning" :hx-target "#container"}]
   (cond
     (= "do" command)
     (let [session (login/login session)
           req (assoc :session session)]
       {:session session
        :body (extension-panel/extension-panel req)})
     (= "warning" command)
     [:div#return-to-linkedin
       (i18n "Return to LinkedIn or close this panel")]
     user_id
     (extension-panel/extension-panel req)
     :else
     [:div.mt-6.flex.justify-center
      [:div {:hx-post "login:do" :hx-target "#container"}
       (components/button (i18n "Log In"))]])])

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
