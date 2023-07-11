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
 ::filters
 (fn [db]
   (:filter-panel db)))

(reg-sub
 ::filter-res
 (fn [db]
   (:filter-res db)))

(defn common [vec1 vec2] (into [] (for [x vec1 :when (contains? (set vec2) x)] x)))

(reg-sub
 ::patients-filtered
 :<- [::patients]
 :<- [::search]
 :<- [::filter-res]
 (fn [[all search filter] _]
   (let [searched (if (empty? (:query search))
                    all
                    (:res search))
         filtered (if (:attr filter)
                    (:res filter)
                    all)]
     (common searched filtered))))

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