(ns omniward-client.subs
  (:require
   [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::patients
 (fn [db]
   (:patients db)))

(reg-sub
 ::search
 (fn [db]
   (:search db)))

(reg-sub
 ::patients-filtered
 :<- [::patients]
 :<- [::search]
 (fn [[all search] _]
   (if (empty? (:query search))
     all
     (:res search))))

(reg-sub
 ::patient-form
 (fn [db]
   (:patient-form db)))

(reg-sub
 ::patient-form-name
 :<- [::patient-form]
 (fn [form]
   (:name form)))

(reg-sub
 ::patient-form-date
 :<- [::patient-form]
 (fn [form]
   (:dob form)))
(reg-sub
 ::patient-form-phone
 :<- [::patient-form]
 (fn [form]
   (:phone form)))

(reg-sub
 ::patient-form-address
 :<- [::patient-form]
 (fn [form]
   (:address form)))

(reg-sub
 ::patient-form-gender
 :<- [::patient-form]
 (fn [form]
   (:gender form)))
(reg-sub
 ::modal
 (fn [db]
   (:modal db)))