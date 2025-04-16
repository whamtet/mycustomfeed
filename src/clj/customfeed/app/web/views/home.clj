(ns customfeed.app.web.views.home
  (:require
    [customfeed.app.env :refer [prod?]]
    [customfeed.app.web.controllers.login :as login]
    [customfeed.app.web.i18n :refer [i18n]]
    [customfeed.app.web.htmx :refer [page-htmx defcomponent]]
    [customfeed.app.web.views.components :as components]
    [simpleui.core :as simpleui]))

(defn login-session [req]
  (if (simpleui/post? req)
    (update req :session login/login)
    req))

(defcomponent ^{:endpoint true :req login-session} login [req]
  (cond
    (simpleui/post? req)
    {:session session
     :body "logged in"}
    id "logged in"
    :else
    [:div {:hx-target "this"}
     [:div.mt-6.flex.justify-center
      [:div {:hx-post "login"}
       (components/button (i18n "Log In"))]]]))

(defn extension [{:keys [query-fn]}]
  (simpleui/make-routes-simple
   (if prod?
     "https://mycustomfeed.simpleui.io/extension/"
     "http://localhost:3002/extension/" #_"https://1089-202-241-169-123.ngrok-free.app/extension/")
   [query-fn]
   login))

(defn home [opts]
  (simpleui/make-routes
   ""
   (fn [req]
     (page-htmx
      {:css ["/output.css"]}
      (login req)))))
