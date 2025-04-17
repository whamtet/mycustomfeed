(ns customfeed.app.web.views.extension-panel
  (:require
    [customfeed.app.web.i18n :refer [i18n]]
    [customfeed.app.web.controllers.list :as list]
    [customfeed.app.web.htmx :refer [defcomponent]]
    [customfeed.app.web.views.components :as components]))

(def header
  [:div.flex.items-center
   [:h5.mr-2 (i18n "My Lists")]
   [:div {:hx-post "extension-panel"
          :hx-prompt (i18n "New list name?")}
    (components/button "+")]])

(defcomponent ^:endpoint extension-panel [req ^:prompt list-name]
  (let [lists (list/get-lists req)
        clash? (and top-level? (lists list-name))
        new-list? (and top-level? (not clash?))
        lists (if new-list? (assoc lists list-name []) lists)]
    (when new-list?
      (list/update-lists req lists))
    [:div.m-2 {:hx-target "this"}
     header
     (when clash? (components/warning (i18n "List already created")))
     (pr-str lists)]))
