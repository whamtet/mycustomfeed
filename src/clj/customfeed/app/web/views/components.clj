(ns customfeed.app.web.views.components)

(defn button [label]
  [:button {:type "button"
            :class "bg-clj-blue py-1.5 px-3 rounded-lg text-white"}
   label])

(defn button-warning [label] ;; see also warning below
  [:button {:type "button"
            :class "bg-red-600 py-1.5 px-3 rounded-lg text-white"}
   label])
