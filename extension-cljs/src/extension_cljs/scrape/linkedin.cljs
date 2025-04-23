(ns extension-cljs.scrape.linkedin
  (:refer-clojure :exclude [delay])
  (:require
    [promesa.core :as p]
    [extension-cljs.parse :as parse]
    [extension-cljs.util :as util]))

(def contacts-loaded-class ".pv-profile-section__section-info.section-info")

(defn delay []
  (util/delay 1000 3000))

(def company-regex #"Current company: (.+). Click to")
(def education-regex #"Education: (.+). Click to")

(defn- aria-regex [r]
  (->> (parse/xpath "//button[@aria-label]")
       (some #(->> % .-ariaLabel (re-find r) second))))

(defn- span-substring [substring]
  (->> (parse/xpath "//span[@aria-hidden]")
       (some #(let [text (.-innerText %)]
               (when (.includes text substring)
                     (.trim text))))))

(defn initial-scrape []
  (when-let [linkedin (-> js/location.pathname (.split "/") (nth 2))]
    (p/let [_ (p/delay 3000)
            bio-href (str js/location.pathname "overlay/about-this-profile/")
            names (some->
                   (parse/xpath1 ["//a" ["@href" bio-href]])
                   .-innerText
                   .trim)]
      (when names
            {:linkedin linkedin
             :linkedinName names
             :img (some->
                   (parse/xpath1 ["//img" ["@title" names]])
                   .-src)}))))
