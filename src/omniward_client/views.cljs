(ns omniward-client.views
  (:require
   [omniward-client.components.patient-card :refer [patient-card]]))

(defn main-panel []
  [:div.container
   [:button.add-button
    {:on-click #()}
    "Add Patient"]
   (let [patient {:name "King Xavier II"
                  :date-of-birth "02-09-2001"
                  :address "new ark address"
                  :gender "Female"
                  :phone-number "09-453-098"}
         patients (repeat 12 patient)]
     (for [patient patients]
       [patient-card patient]))])