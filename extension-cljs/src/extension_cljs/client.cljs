(ns extension-cljs.client
  (:require
    promesa.core)
  (:require-macros
    [extension-cljs.core :refer [base-url]]
    [promesa.core :as p]))

(defn GET [url]
  (js/fetch (str (base-url) url)))

(defn POST [url]
  (p/let [response (js/fetch
                    (str (base-url) url)
                    #js {:method "POST"
                         :headers #js {:kookie (js/localStorage.getItem "kookie")}})
          kookie (-> response .-headers (.get "kookie"))]
    (when (not-empty kookie)
          (js/localStorage.setItem "kookie" kookie))
    response))
