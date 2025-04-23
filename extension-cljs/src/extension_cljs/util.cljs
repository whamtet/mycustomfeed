(ns extension-cljs.util
  (:refer-clojure :exclude [rand-int delay])
  (:require
   [promesa.core :as p]))

(defn map-vals [f m]
  (->> m vals (map f) (zipmap (keys m))))

(defn rand-int [a b]
  (+ a (cljs.core/rand-int (- b a))))

(defn delay
  ([a] (p/delay a))
  ([a b]
   (p/delay (rand-int a b))))

(defn map-slow
  "like cljs.core/map except each promise is resolved before applying f to the next element"
  [f s]
  (p/loop [s s
           done []]
    (let [[x & rest] s]
      (if s
        (p/let [y (f x)]
          (p/recur rest (conj done y)))
        done))))

(defn remove-hashtag [s]
  (let [s (.trim s)]
    (not-empty
     (if (.startsWith s "#")
       (.substring s 1)
       s))))

(defn string-count [s substring]
  (-> s (.split substring) count dec))

(defn format [fmt-str & args]
  (reduce #(.replace %1 "%s" %2) fmt-str args))

(defn reduce-when [f x s]
  (reduce
   (fn [x y]
     (when x
       (f x y)))
   x
   s))

(defn some-element
  "returns the first element for which (f element) is truthy"
  [f s]
  (some #(when (f %) %) s))

(defn before [a b]
  (first (.split a b)))
