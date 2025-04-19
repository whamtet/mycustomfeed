(ns extension-cljs.core
  (:require
    [extension-cljs.client :refer [GET]]
    promesa.core)
  (:require-macros
    [promesa.core :as p]))

(enable-console-print!)

(defn click-side-panel-button []
  (js/chrome.runtime.sendMessage #js {:type "open_side_panel"}))

(defn add-side-panel-button []
  (p/let [response (GET "/api/button")
          html (.text response)
          div (js/document.createElement "div")]
    (set! (.-innerHTML div) html)
    (js/document.body.append div)
    (.addEventListener div "click" click-side-panel-button)))

(add-side-panel-button)
