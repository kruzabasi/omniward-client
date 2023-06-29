(ns omniward-client.views
  (:require
   [omniward-client.components.search :refer [search-bar]]
   [omniward-client.components.patient-card :refer [patient-card]]))

(defn main-panel []
  [:div.container
   [search-bar]
   [:button.add-button
    {:on-click #()}
    "Add Patient"]
   (let [patient {:name "King Xavier II"
                  :dob "02-09-2001"
                  :address "new ark address"
                  :gender "Female"
                  :phone "09-453-098"}
         patients (repeat 12 patient)]
     (for [patient patients]
       [patient-card patient]))])