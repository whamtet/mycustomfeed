(ns extension-cljs.parse
  (:refer-clojure :exclude [val])
  (:require
   [cljs.core.match :refer-macros [match]]
   [hickory.core :as hickory]
   [promesa.core :as p]
   [extension-cljs.util :as util]
   [extension-cljs.xpath :as xpath]))

(def xpath xpath/xpath)
(defn xpath1
  ([path] (first (xpath path)))
  ([node path] (first (xpath node path))))
(defn xpath1s
  ([paths]
   (some xpath1 paths)))

(defn xpath-case [& cases]
  (->> cases
       (partition 2)
       (some
        (fn [[k v]]
          (when (xpath1 k) v)))))

(defn query-selector [s]
  (-> s js/document.querySelector hickory/as-hiccup))
(defn query-selector-all [s]
  (->> s
       js/document.querySelectorAll
       js/Array.from
       (map hickory/as-hiccup)))

(defn query-selector-all-native
  ([s] (query-selector-all-native js/document s))
  ([node s]
   (js/Array.from
    (.querySelectorAll node s))))

(defn parent [node steps]
  (if (and node (pos? steps))
    (recur (.-parentNode node) (dec steps))
    node))

(defn href-filter
  ([f] (href-filter js/document f))
  ([node f]
   (->> (query-selector-all-native node "a")
        (filter #(-> % (.getAttribute "href") f)))))

(defn aget-last [s n]
  (aget s
        (if (= :last n)
          (dec (.-length s))
          n)))

(defn navigate
  "Simple DSL for navigating through the dom"
  [starting-node & steps]
  (util/reduce-when
   (fn [node step]
     (prn 'step step)
     (match step
       [:child n] (-> node .-children (aget n))
       [:query selector] (query-selector-all-native node selector)
       [:query selector n] (-> node (.querySelectorAll selector) (aget-last n))
       [:parent n] (parent node n)
       [:nth x] (nth node x)
       [:href f] (href-filter node f)
       :else
       (case step
         :first (first node)
         :- (.-previousSibling node)
         :+ (.-nextSibling node))))
   starting-node
   steps))

#_:clj-kondo/ignore
(defn wait-href-change
  "Promise resolves on href change.  No timeout."
  [msg]
  (println msg)
  (js/Promise.
   (fn [resolve]
     (let [initial-href js/location.href
           on-change (fn [_ observer]
                       (p/do
                         (p/delay 100)
                         (when (not= initial-href js/location.href)
                           (.disconnect observer)
                           (resolve))))]
       (doto (js/MutationObserver. on-change)
         (.observe js/document #js {:subtree true :childList true}))))))

(defn poll [msg f]
  (println msg)
  (p/loop [result (f)]
    (or
     result
     (p/do
       (util/delay 200)
       (p/recur (f))))))

(defn val
  ([selector]
   (some-> selector js/document.querySelector .-value))
  ([selector value]
   (when (val selector)
     (set! (.-value (js/document.querySelector selector)) value))))

(defn click [selector]
  (some-> selector js/document.querySelector .click))
