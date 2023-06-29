(ns omniward-client.components.search
  (:require
   [re-frame.core :as re-frame]
   [omniward-client.subs :as subs]
   [omniward-client.events :as events]))

(defn search-bar
  []
  (let [search (re-frame/subscribe [::subs/search])]
    (fn []
      [:div.search-bar
       [:input.search-input
        {:type "text"
         :placeholder "Search for a patients record"
         :value (:query @search)
         :on-change #(re-frame/dispatch
                      [::events/search-patients
                       (-> % .-target .-value)])}]])))