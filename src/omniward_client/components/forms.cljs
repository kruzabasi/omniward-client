(ns omniward-client.components.forms
  (:require
   [re-frame.core :refer [subscribe dispatch]]
   [omniward-client.events :as events]
   [omniward-client.subs :as subs]
   [omniward-client.components.validators :as v]))

(defn patient-form-input
  [type placeholder {:keys [value on-change error]}]
  (let [field-id  (last on-change)
        input     (subscribe value)
        change-fn #(dispatch (conj on-change (-> % .-target .-value)))
        error-message (error @input)]
    [:div.input-section
     [:input.text-input
      {:id field-id
       :type type
       :placeholder placeholder
       :value @input
       :required true
       :on-change change-fn}]
     (when (not= "empty" error-message)
       [:span.error-msg error-message])]))

(defn patient-form-select
  [value placeholder change-fn error]
  (let [input (subscribe value)
        field-id (last change-fn)
        error-message (error @input)]
    [:div.input-section
     [:select.text-input
      {:id field-id
       :value     (or @input "")
       :on-change #(dispatch (conj change-fn (-> % .-target .-value)))
       :required true}
      [:option {:value ""} placeholder]
      [:option {:value "Male"} "Male"]
      [:option {:value "Female"} "Female"]
      [:option {:value "Other"} "Other"]]
     (when (not= "empty" error-message)
       [:span.error-msg error-message])]))

(defn form-submit-button
  []
  (let [invalid-name? (v/validate-name @(subscribe [::subs/patient-form-name]))
        invalid-dob?  (v/validate-date-of-birth @(subscribe [::subs/patient-form-date]))
        invalid-add?  (v/validate-address @(subscribe [::subs/patient-form-address]))
        invalid-phone? (v/validate-phone-number @(subscribe [::subs/patient-form-phone]))
        invalid-gender? (v/validate-gender @(subscribe [::subs/patient-form-gender]))
        invalid-form? (or invalid-gender? invalid-phone? invalid-add? invalid-dob? invalid-name?)]
    [:button.add-button
     {:type "submit"
      :className (when invalid-form? "add-button:disabled")
      :disabled invalid-form?}
     "Submit"]))

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
       :on-change [::events/modify-patient-form :name]
       :error     v/validate-name}]
     [patient-form-input
      "date"
      "date of birth"
      {:value     [::subs/patient-form-date]
       :on-change [::events/modify-patient-form :dob]
       :error     v/validate-date-of-birth}]
     [patient-form-select
      [::subs/patient-form-gender]
      "--Patients gender--"
      [::events/modify-patient-form :gender]
      v/validate-gender]
     [patient-form-input
      "text"
      "Patients phone number"
      {:value     [::subs/patient-form-phone]
       :on-change [::events/modify-patient-form :phone]
       :error     v/validate-phone-number}]
     [patient-form-input
      "text"
      "Patients address"
      {:value     [::subs/patient-form-address]
       :on-change [::events/modify-patient-form :address]
       :error     v/validate-address}]
     [:div.card-buttons
      [form-submit-button]]]]))