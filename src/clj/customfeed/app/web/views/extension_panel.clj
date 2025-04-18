(ns customfeed.app.web.views.extension-panel
  (:require
    [customfeed.app.web.i18n :refer [i18n]]
    [customfeed.app.web.controllers.list :as list]
    [customfeed.app.web.htmx :refer [defcomponent]]
    [customfeed.app.web.views.components :as components]
    [customfeed.app.web.views.icons :as icons]))

(def header
  [:div.flex.items-center.mb-2
   [:h5.mr-2 (i18n "My Lists")]
   [:div {:hx-post "extension-panel"
          :hx-prompt (i18n "New list name?")}
    (components/button "+")]])

[:div.h-5.w-5]
(defcomponent ^:endpoint list-selector [req list]
  (when delete?
    (list/delete-list req list))
  (let [lists (list/get-lists req)
        list-name (or list (-> lists keys first))
        list (lists list-name)]
    (if (empty? lists)
      (i18n "Click + to create your first list")
      [:div {:hx-target "this"}
       [:div.flex.items-center
        [:select {:class "mr-1"
                  :hx-get "list-selector"
                  :name "list"}
         (for [[ln] lists]
           [:option {:value ln
                     :selected (= ln list-name)} ln])]
        [:div {:class "cursor-pointer"
               :hx-delete "list-selector"
               :hx-vals {:list list-name}
               :hx-confirm (format (i18n "Delete list %s?") list-name)}
         (icons/trash 5)]]])))

(defcomponent ^:endpoint extension-panel [req ^:prompt list-name]
  (let [lists (list/get-lists req)
        clash? (and top-level? (lists list-name))]
    (when (and top-level? (not clash?))
      (list/update-lists req (assoc lists list-name [])))
    [:div.m-2 {:hx-target "this"}
     header
     (when clash? (components/warning (i18n "List already created")))
     (list-selector req)]))
