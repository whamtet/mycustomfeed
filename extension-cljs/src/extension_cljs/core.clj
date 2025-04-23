(ns extension-cljs.core)

(def dev? (boolean (System/getenv "WATCH")))
(defmacro base-url []
  (if dev? "http://localhost:3002" "https://app.mycustomfeed.com"))

(defmacro defport [port on-message]
  `(do
    (def ~port (atom (js/chrome.runtime.connect)))
    (-> ~port deref .-onDisconnect (.addListener #(reset! ~port (js/chrome.runtime.connect))))
    (-> ~port deref .-onMessage (.addListener ~on-message))))
