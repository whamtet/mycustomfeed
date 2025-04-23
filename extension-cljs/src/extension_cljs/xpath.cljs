(ns extension-cljs.xpath
  (:require
   [clojure.string :as string]
   [extension-cljs.util :refer [format]]))

(def fragment-fmt "[translate(%s, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')=\"%s\"]")
(defn- xpath-fragment [[a b :as s]]
  (if (string? s)
    s
    (format fragment-fmt a (.toLowerCase b))))

(defn- xpath-fragments [fragments]
  (if (string? fragments)
    fragments
    (->> fragments
         (map xpath-fragment)
         string/join)))

(defn xpath
  ([path] (xpath js/document path))
  ([node path]
   (let [path (xpath-fragments path)
         result (js/document.evaluate path node nil js/XPathResult.ORDERED_NODE_SNAPSHOT_TYPE)]
     (->> result
          .-snapshotLength
          range
          (map #(.snapshotItem result %))))))
