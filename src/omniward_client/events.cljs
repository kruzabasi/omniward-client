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
    :url          (str base-url path)
    :params       (:query data)
    :body         (:body data)
    :mode         :cors
    :timeout      5000
    :on-success   on-success
    :on-failure   on-failure
    :request-content-type :json
    :response-content-types {#"application/.*json" :json}}))

(reg-event-db
 ::load-patients
 (fn [db [_ res]]
   (let [patients (-> res :body :data)]
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

(defn search-by-name
  [query patients]
  (let [query (string/trim query)]
    (filter #(re-find (re-pattern (str "(?i)" query)) (:name %)) patients)))

(defn filter-by-attr
  [val attr patients]
  (filter #(re-matches (re-pattern (str "(?i)" val)) (attr %)) patients))

(reg-event-db
 ::search-patients
 (fn [db [_ input]]
   (let [all-patients (:patients db)
         res (search-by-name input all-patients)]
     (assoc db :search {:query input :res res}))))

(reg-event-db
 ::filter-patients
 (fn [db [_ attr val]]
   (let [all-patients (:patients db)
         res          (filter-by-attr val attr all-patients)
         filter-attr  (attr (:filter-panel db))]
     (assoc db :filter-res {:attr filter-attr :res res}))))

(reg-event-db
 ::show-filter-panel
 (fn [db [_ {:keys [open]}]]
   (let [prev-val  (:open (:filter-panel db))
         new-val   (if (= :toggle open)
                      (not prev-val)
                      open)]
     (assoc-in db [:filter-panel :open] new-val))))

(reg-event-fx
 ::toggle-filter-option
 (fn [{:keys [db]} [_ group selection]]
   (let [current-state  ((:filter-panel db) group)
         prev-selected? (= selection current-state)
         new-state      (if prev-selected? nil selection)]
     {:db (update-in db [:filter-panel] merge {group new-state})
      :fx [[:dispatch [::filter-patients group selection]]]})))

(reg-event-db
 ::modify-patient-form
 (fn [db [_ field input]]
   (if field
     (assoc-in db [:patient-form field] input)
     (assoc-in db [:patient-form] input))))

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
 (fn [_ [_ res]]
   (js/alert (str (:problem-message res) "!"))
   {:fx [[:dispatch [::toggle-modal {:open false}]]]}))

(reg-event-fx
 ::submit:modify-patient
 (fn [{:keys [db]} _]
   (let [data (:patient-form db)
         patient-id (:patient_id data)
         params (-> data
                    (assoc :p-name (:name data))
                    (dissoc :name :patient_id))]
     {:fetch (api-req
              :put
              (str "/" patient-id)
              [::modify-patient-success patient-id]
              [::modify-patient-failure]
              {:query params})})))

(reg-event-fx
 ::modify-patient-success
 (fn [{:keys [db]} [_ id res]]
   (let [patient-res (-> res :body :data)
         patient     (assoc patient-res :patient_id id)
         patients    (:patients db)
         updated     (-> (remove-patient patients id)
                         (conj patient))]
     {:db (assoc-in db [:patients] updated)
      :fx [[:dispatch [::toggle-modal {:open false}]]]})))

(reg-event-fx
 ::modify-patient-failure
 (fn [_ [_ res]]
   (js/alert (str (:problem-message res) "!"))))

(reg-event-fx
 ::submit:create-patient
 (fn [{:keys [db]} _]
   (let [data   (:patient-form db)
         params (-> data
                    (assoc :p-name (:name data))
                    (dissoc :name))]
     {:fetch  (api-req
               :post
               nil
               [::create-patient-success]
               [::create-patient-failure]
               {:body params})})))

(reg-event-fx
 ::create-patient-success
 (fn [{:keys [db]} [_ res]]
   (let [new-patient (-> res :body :data)
         patients (:patients db)
         updated  (conj patients new-patient)]
     {:db (assoc-in db [:patients] updated)
      :fx [[:dispatch [::toggle-modal {:open false}]]]})))

(reg-event-fx
 ::create-patient-failure
 (fn [_ [_ res]]
   (js/alert (str (:body res) "!"))))

(reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))