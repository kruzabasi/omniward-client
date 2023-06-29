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
   (let [search       (subscribe [::subs/search])
         all-patients (subscribe [::subs/patients])
         patients     (if (empty? (:query @search))
                        @all-patients
                        (:res @search))]
     (patient-cards patients))])