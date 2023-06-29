(ns omniward-client.components.patient-card
  (:require
   [re-frame.core :refer [subscribe dispatch]]
   [omniward-client.subs :as subs]
   [omniward-client.events :as events]
   [omniward-client.components.modals :refer [multi-modal]]))

(defn patient-card
  [patient]
  (fn []
    [:div.card
     [:h3 (:name patient)]
     [:p "Date of Birth: " (.toLocaleDateString (js/Date. (:dob patient)))]
     [:p "Address: " (:address patient)]
     [:p "Gender: " (:gender patient)]
     [:p "Phone Number: " (:phone patient)]
     [:div.card-buttons
      [:button.edit-button
       {:on-click #(dispatch [::events/toggle-modal 
                              {:open true
                               :type :edit
                               :data patient}])}
       "Edit"]
      [:button.delete-button
       {:on-click #(dispatch [::events/toggle-modal 
                              {:open true 
                               :type :delete 
                               :data (:patient_id patient)}])}
       "Delete"]]]))

(defn patient-cards
  [patients]
  (let [modal-opts (subscribe [::subs/modal])]
    [:div
     (when (:open @modal-opts)
       (multi-modal modal-opts))
     (for [patient patients]
       ^{:key (:patient_id patient)}
       [patient-card patient])]))