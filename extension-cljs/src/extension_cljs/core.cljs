(ns extension-cljs.core
  (:require
    [extension-cljs.client :refer [GET]]
    promesa.core)
  (:require-macros
    [promesa.core :as p]
    [extension-cljs.core :refer [defport]]))

(enable-console-print!)

(declare to-sidePanel)
(defport port
  (fn [message]
    (case (.-type message)
          "to_tab"
          (let [msg (.-msg message)]
            (js/console.log "msg" msg)
            (to-sidePanel #js {:greeting "hi back"})))))

(defn to-sidePanel [msg]
  (try
    (.postMessage port #js {:type "to_sidePanel"
                            :msg msg})
    (catch js/Error e)))

(defn click-side-panel-button []
  (.postMessage port #js {:type "open_side_panel"}))

(defn add-side-panel-button []
  (p/let [response (GET "/api/button")
          html (.text response)
          div (js/document.createElement "div")]
    (set! (.-innerHTML div) html)
    (js/document.body.append div)
    (.addEventListener div "click" click-side-panel-button)))

(add-side-panel-button)

(defn notify-load []
  (.postMessage port #js {:type "notify_load"}))
(notify-load)

(defn heartbeat []
  (to-sidePanel "heartbeat"))
(js/setInterval heartbeat 4000)
