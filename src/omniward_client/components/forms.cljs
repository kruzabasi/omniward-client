(ns omniward-client.components.forms
  (:require
   [re-frame.core :refer [subscribe dispatch]]
   [omniward-client.events :as events]
   [omniward-client.subs :as subs]))

(defn patient-form-input
  [type placeholder {:keys [value on-change]}]
  (let [input     (subscribe value)
        change-fn #(dispatch (conj on-change (-> % .-target .-value)))]
    [:input.text-input
     {:type type
      :placeholder placeholder
      :value @input
      :on-change change-fn}]))

(defn patient-form
  ([title on-submit]
   (patient-form title on-submit nil))
  ([title on-submit data]
   (dispatch [::events/modify-patient-form nil data])
   [:form {:on-submit (fn [e]
                        (.preventDefault e)
                        (on-submit))}
    [:h2 title]
    [:div.card {:style {:display "flex" :flex-direction "column"}}
     [patient-form-input
      "text"
      "Patient's full name"
      {:value     [::subs/patient-form-name]
       :on-change [::events/modify-patient-form :name]}]
     [patient-form-input
      "date"
      "date of birth"
      {:value     [::subs/patient-form-date]
       :on-change [::events/modify-patient-form :dob]}]
     [patient-form-input
      "text"
      "Patients gender"
      {:value     [::subs/patient-form-gender]
       :on-change [::events/modify-patient-form :gender]}]
     [patient-form-input
      "text"
      "Patients phone number"
      {:value     [::subs/patient-form-phone]
       :on-change [::events/modify-patient-form :phone]}]
     [patient-form-input
      "text"
      "Patients address"
      {:value     [::subs/patient-form-address]
       :on-change [::events/modify-patient-form :address]}]
     [:div.card-buttons
      [:button.add-button {:type "submit" :disabled true} "Submit"]]]]))