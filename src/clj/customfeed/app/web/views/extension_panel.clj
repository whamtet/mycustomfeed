(ns customfeed.app.web.views.extension-panel
  (:require
    [customfeed.app.util :as util]
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
       [:input {:id "#curr-list"
                :type "hidden"
                :name "list"
                :value list-name}]
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

(defcomponent ^:endpoint list-adder [req list linkedin linkedinName img urn]
  (when user_id
    (list/add-user req list urn (util/zipm linkedin linkedinName img urn)))
  nil)

(defcomponent ^:endpoint profile [req ^:json info]
  list-adder
  (let [{:keys [linkedinName
                img]} info
        curr-tab (:curr-tab session "home")]
    [:div#profile
       [:form.hidden {:hx-post "profile"
                      :hx-target "#profile"}
        [:input#profile-info {:name "info" :value (util/write-str info)}]
        [:input#profile-refresh {:type "submit"}]]
     (when (and info (= curr-tab "home"))
       [:div.flex.flex-col.items-center.mt-2
        [:img {:class "rounded-full w-1/2 mt-4"
               :src img}]
        [:h4.mt-2 linkedinName]
        [:div.mt-2 {:hx-post "list-adder"
                    :hx-vals info}
         (components/button (i18n "Add to List"))]])]))

(defn- if-new-tab [new-tab session body]
  (if new-tab
    {:body body :session (assoc session :curr-tab new-tab)}
    body))

(defcomponent ^:endpoint extension-panel [req ^:prompt list-name new-tab]
  (let [lists (list/get-lists req)
        clash? (lists list-name)
        req (if new-tab (assoc-in req [:session :curr-tab] new-tab) req)
        session (:session req)
        curr-tab (:curr-tab session)]
    (when (and user_id list-name (not clash?))
      (list/add-list req list-name))
    (if-new-tab
     new-tab
     session
     [:div {:hx-target "this"}
      [:div.mb-2
       header
       (when clash?
         [:div.my-3
          (components/warning (i18n "List already created"))])
       (list-selector req)
       [:div.mt-2
        (components/tabs
         curr-tab
         {:value "home" :disp (i18n "Add Users")
          :hx-post "extension-panel" :hx-vals {:new-tab "home"}
          :hx-include "#profile-info"}
         {:value "view" :disp (i18n "Display")
          :hx-post "extension-panel" :hx-vals {:new-tab "view"}
          :hx-include "#profile-info"})]]
      (profile req)
      (case curr-tab
        "view" [:div#view "view"]
        nil)])))
