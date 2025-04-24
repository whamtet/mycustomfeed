(ns customfeed.app.web.views.components
  (:require
    [customfeed.app.util :as util]))

(defn button [label]
  [:button {:type "button"
            :class "bg-clj-blue py-1.5 px-3 rounded-lg text-white"}
   label])

(defn button-warning [label] ;; see also warning below
  [:button {:type "button"
            :class "bg-red-600 py-1.5 px-3 rounded-lg text-white"}
   label])

(defn warning [msg]
  [:span {:class "bg-red-600 p-2 rounded-lg text-white"} msg])

(defmacro cond-class [x & conditions]
  `(cond-> ~x
    ~@(util/forcat [[k v] (partition 2 conditions)]
       [k `(str " " ~v)])))

; (tabs "home" {:value "home" :disp (i18n "Home") ...})
[:div {:class "cursor-pointer border-l border-y bg-clj-blue-light bg-gray-100 border-r px-2"}]
[:div {:class "rounded-tr-xl rounded-rl-xl text-center"}]
(def tab-width #(-> % :disp count (Math/pow 0.35)))
(defn tabs [curr-tab & tabs]
  (let [curr-tab (or curr-tab (-> tabs first :value))
        total-grow (->> tabs (map tab-width) (apply +))]
    [:div.flex.text-xl
     (util/map-first-last
      (fn [first? last? tab]
        [:div (-> tab
                  (dissoc :value :disp)
                  (assoc :style {:flex-grow (-> tab tab-width (/ total-grow))})
                  (assoc :class
                         (cond-class "cursor-pointer border-l border-y px-2 text-center"
                                     (-> tab :value (= curr-tab)) "bg-clj-blue-light"
                                     (-> tab :value (not= curr-tab)) "bg-gray-100"
                                     first? "rounded-tl-xl"
                                     last? "border-r rounded-tr-xl")))
         (:disp tab)])
      tabs)]))
