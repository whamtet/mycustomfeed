(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'extension-cljs.core
   :output-to "../extension/dist/extension_cljs.js"
   :target :webworker
   :optimizations :whitespace})
