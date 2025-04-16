(ns customfeed.app.web.views.home
    (:require
      [customfeed.app.env :refer [prod?]]
      [customfeed.app.web.htmx :refer [page-htmx defcomponent]]
      [simpleui.core :as simpleui]))

(defcomponent ^:endpoint hello [req my-name]
  [:div#hello "Hello " my-name])

(defn home [opts]
  (simpleui/make-routes
   ""
   (fn [req]
     (page-htmx
      {:css ["/output.css"]}
      [:label {:style "margin-right: 10px"}
       "What is your name?"]
      [:input {:type "text"
               :name "my-name"
               :hx-patch "hello"
               :hx-target "#hello"
               :hx-swap "outerHTML"}]
      (hello req "")))))

(defcomponent ^:endpoint login [req ^:long count]
  [:div {:hx-get "login"
         :hx-vals {:count (inc count)}}
   "Count " count])

(defmacro extension []
  `(simpleui/make-routes-simple
    ~(if prod? "https://mycustomfeed.simpleui.io/extension/" "http://localhost:3002/extension/")
    [~'query-fn]
    login))
