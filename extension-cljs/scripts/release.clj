(require '[cljs.build.api :as b])

(println "Building ...")

(let [start (System/nanoTime)]
  (b/build "src"
    {:main 'extension-cljs.core
     :output-to "../extension/dist/extension_cljs.js"
     :target :webworker
     :optimizations :whitespace})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
