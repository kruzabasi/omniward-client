(ns omniward-client.events
  (:require
   [clojure.string :as string]
   [re-frame.core :refer [reg-event-fx reg-event-db]]
   [omniward-client.db :as db]
   [superstructor.re-frame.fetch-fx]))

(def base-url "http://localhost:8081/api/patients")
;;Include live url once API is deployed.


(defn api-req
  ([method path on-success on-failure]
   (api-req method path on-success on-failure nil))
  ([method path on-success on-failure data]
   {:method method
    :url (str base-url path)
    :params       data
    :mode         :cors
    :timeout      5000
    :on-success   on-success
    :on-failure   on-failure}))

(reg-event-db
 ::load-patients
 (fn [db [_ res]]
   (let [res (->> res :body (.parse js/JSON))
         patients (:data (js->clj res :keywordize-keys true))]
     (assoc db :patients patients))))

(reg-event-db
 ::api-req-failed
 (fn [db [_ _res]]
   (let [dummy-patients (repeat
                         15
                         {:name "King Xavier II"
                          :dob "02-09-2001"
                          :address "new ark address"
                          :gender "Female"
                          :phone "09-453-098"})]
     (assoc db :patients dummy-patients))))
;;Fix req-failed This once API is deployed.

(defn filter-patients
  [query patients]
  (let [query (string/trim query)]
    (filter #(re-find (re-pattern (str "(?i)" query)) (:name %)) patients)))

(reg-event-db
 ::search-patients
 (fn [db [_ input]]
   (let [all-patients (:patients db)
         res (filter-patients input all-patients)]
     (assoc db :search {:query input :res res}))))

(reg-event-db
 ::toggle-modal
 (fn [db [_ opts]]
   (assoc db :modal opts)))

(reg-event-fx
 ::fetch-patients
 (fn [_ _]
   {:fetch
    (api-req :get nil [::load-patients] [::api-req-failed])}))

(reg-event-fx
 ::delete-patient
 (fn [_ [_ patient-id]]
   {:fetch
    (api-req
     :delete
     (str "/" patient-id)
     [::delete-patient-success patient-id]
     [::delete-patient-failure])}))

(defn remove-patient
  [patients id]
  (remove
   (fn [x] (= id (:patient_id x)))
   patients))

(reg-event-fx
 ::delete-patient-success
 (fn [{:keys [db]} [_ id]]
   (let [res (remove-patient (:patients db) id)]
     {:db (assoc-in db [:patients] res)
      :fx [[:dispatch [::toggle-modal {:open false}]]]})))

(reg-event-fx
 ::delete-patient-failure
 (fn [_ _]
   {:fx [[:dispatch [::toggle-modal {:open false}]]]}))
;;TODO: Show message on failure.

(reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))