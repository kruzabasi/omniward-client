(ns omniward-client.components.patient-card
  (:require [reagent.core :as r]))

(defmulti multi-modal (fn [x] (:type @x)))

(defmethod multi-modal :edit
 [args]
 [:div.modal
  {:style {:display (if (:open @args) "flex" "none")}}
  [:div.modal-content
   [:div "Edit"]
   [:button.close-button
    {:on-click #(reset! args {:open false :type nil})}
    "Close"]]])

(defmethod multi-modal :delete
  [args]
  [:div.modal
   {:style {:display (if (:open @args) "flex" "none")}}
   [:div.modal-content
    [:div "Delete"]
    [:button.close-button
     {:on-click #(reset! args {:open false :type nil})}
     "Close"]]])

(defn patient-card
  [patient]
  (let [modal-open (r/atom {:open false :type nil})]
    (fn []
      (if (:open @modal-open)
        (multi-modal modal-open)
        [:div.card
         [:h3 (:name patient)]
         [:p "Date of Birth: " (.toLocaleDateString (js/Date. (:dob patient)))]
         [:p "Address: " (:address patient)]
         [:p "Gender: " (:gender patient)]
         [:p "Phone Number: " (:phone patient)]
         [:div.card-buttons
          [:button.edit-button
           {:on-click #(reset! modal-open {:open true :type :edit})}
           "Edit"]
          [:button.delete-button
           {:on-click #(reset! modal-open {:open true :type :delete})}
           "Delete"]]]))))