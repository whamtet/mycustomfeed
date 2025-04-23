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

(defn scrape-linkedin [trigger]
  (p/let [bio-href (str js/location.pathname "overlay/about-this-profile/")
          contact-href (str js/location.pathname "overlay/contact-info/")
          linkedin (-> js/location.pathname (.split "/") (nth 2))
          names (some->
                 (parse/xpath1 ["//a" ["@href" bio-href]])
                 .-innerText
                 .trim)
          bio (some->
               (parse/xpath1 ["//a" ["@href" bio-href] "/../../following-sibling::div"])
               .-innerText
               .trim)
          summary-location (some->
                            (parse/xpath1 ["//a" ["@href" contact-href] "/../preceding-sibling::span"])
                            .-innerText
                            .trim)
          contact-link (parse/xpath1 ["//a" ["@href" contact-href]])
          submit-from-backend? (or (= :local trigger) (number? trigger))]
    (.click contact-link)
    (parse/poll "contacts appear" #(js/document.querySelector contacts-loaded-class))
    (->> {:names names
          :bio bio
          :contacts (parse/query-selector contacts-loaded-class)
          :linkedin linkedin
          :summary-location summary-location
          :current-company (aria-regex company-regex)
          :education (aria-regex education-regex)
          :mutuals (span-substring "mutual connection")
          :title js/document.title}
         pr-str
         (str (when submit-from-backend? "post"))
         js/chrome.runtime.sendMessage)
    (when (= :warmer trigger)
      (delay))
    (when (number? trigger)
      (js/chrome.runtime.sendMessage (str "close" trigger)))
    (js/history.back)))
