(ns omniward-client.views
  (:require
   [re-frame.core :refer [subscribe dispatch]]
   [omniward-client.subs :as subs]
   [omniward-client.components.search :refer [search-bar]]
   [omniward-client.events :as events]
   [omniward-client.components.patient-card :refer [patient-cards]]))

(defn main-panel []
  [:div.container
   [search-bar]
   [:button.add-button
    {:on-click #(dispatch
                 [::events/toggle-modal
                  {:open true
                   :type :add}])}
    "Add Patient"]
   (let [patients (subscribe [::subs/patients-filtered])]
     (patient-cards @patients))])