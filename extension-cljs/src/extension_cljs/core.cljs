(ns extension-cljs.core
  (:require
    [extension-cljs.client :refer [GET]]
    [extension-cljs.scrape.linkedin :as linkedin]
    promesa.core)
  (:require-macros
    [promesa.core :as p]))

(enable-console-print!)

(def port (atom nil))

(declare make-port)
(defn- on-disconnect [_] (reset! port (make-port)))
(defn- on-message [message]
  (case (.-type message)
        "to_tab"
        (let [msg (.-msg message)]
          (case msg
                "heartbeat" (to-sidePanel "heartbeat")
                nil))))

(defn make-port []
  (doto (js/chrome.runtime.connect)
        (-> .-onDisconnect (.addListener on-disconnect))
        (-> .-onMessage (.addListener on-message))))

(on-disconnect nil)

(defn to-sidePanel [msg]
  (.postMessage @port #js {:type "to_sidePanel"
                          :msg msg}))

(defn click-side-panel-button []
  (.postMessage @port #js {:type "open_side_panel"}))

(defn add-side-panel-button []
  (p/let [response (GET "/api/button")
          html (.text response)
          div (js/document.createElement "div")]
    (set! (.-innerHTML div) html)
    (js/document.body.append div)
    (.addEventListener div "click" click-side-panel-button)))

(add-side-panel-button)

(defn on-new-page
  ([f] (on-new-page f nil))
  ([f old-url]
   (let [new-url js/location.href]
     (when (not= old-url new-url) (f))
     (js/setTimeout #(on-new-page f new-url) 1000))))

(on-new-page
 (fn []
   (p/let [initial-info (linkedin/initial-scrape)]
     (some-> initial-info clj->js to-sidePanel))))
