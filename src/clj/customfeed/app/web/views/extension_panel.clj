(ns customfeed.app.web.views.extension-panel
  (:require
    [customfeed.app.web.i18n :refer [i18n]]
    [customfeed.app.web.controllers.list :as list]
    [customfeed.app.web.htmx :refer [defcomponent]]
    [customfeed.app.web.views.components :as components]))

(defcomponent ^:endpoint header [req ^:prompt list-name]
  (if top-level?
    list-name
    [:div.flex.items-center
     [:h5.mr-2 (i18n "My Lists")]
     [:div {:hx-post "header"
            :hx-prompt (i18n "New list name?")}
      (components/button "+")]
     ]))

(defcomponent extension-panel [req]
  [:div.m-2 {:hx-target "this"}
   (header req)
   (pr-str (list/get-list req))])
