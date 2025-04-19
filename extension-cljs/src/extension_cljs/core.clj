(ns extension-cljs.core)

(def dev? (boolean (System/getenv "WATCH")))
(defmacro base-url []
  (if dev? "http://localhost:3002" "https://app.mycustomfeed.com"))
