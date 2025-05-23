(require '[cljs.build.api :as b])
(require '[clojure.string :as string])

(defn map-lines [f s]
  (->> (.split s "\n")
       (map f)
       (string/join "\n")))

(defn wrapper [src]
  (map-lines
   (fn [^String x]
     (if (.startsWith x "goog.createTrustedTypesPolicy=")
       "goog.createTrustedTypesPolicy=() => null;"
       x))
   src))


(println "Building ...")

(let [start (System/nanoTime)]
  (b/build "src"
    {:main 'extension-cljs.core
     :output-to "../extension/dist/extension_cljs.js"
     :target :webworker
     :optimizations :simple
     :output-wrapper wrapper})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
