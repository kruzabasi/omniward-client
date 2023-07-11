(ns omniward-client.components.filter
  (:require
   [re-frame.core :refer [subscribe dispatch]]
   [omniward-client.subs :as subs]
   [omniward-client.events :as events]))

(defn filter-button
  []
  [:button.filter-button
   {:on-click
    #(dispatch
      [::events/show-filter-panel
       {:open :toggle}])
    :on-mouse-over
    #(dispatch
      [::events/show-filter-panel
       {:open true}])
    :on-mouse-leave
    #(dispatch
      [::events/show-filter-panel
       {:open false}])}
   "Filters ▾"])

(defn filter-option
  [filter-opts group option]
  [:label
   [:input
    {:type "checkbox"
     :value option
     :checked (= option (group @filter-opts))
     :on-change #(dispatch
                  [::events/toggle-filter-option
                   group
                   option])}]
   (str option " ")])

(defn filter-panel []
  (let [filter-opts (subscribe [::subs/filters])]
    (when (:open @filter-opts)
      [:div.filter-panel
       {:on-mouse-over
        #(dispatch
          [::events/show-filter-panel
           {:open true}])
        :on-mouse-leave
        #(dispatch
          [::events/show-filter-panel
           {:open false}])}
       [:div.filter-section
        [:div.filter-header "Gender"]
        [filter-option filter-opts :gender "male"]
        [filter-option filter-opts :gender "female"]
        [filter-option filter-opts :gender "other"]]])))