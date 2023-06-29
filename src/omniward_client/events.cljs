(ns omniward-client.events
  (:require
   [clojure.string :as string]
   [re-frame.core :refer [reg-event-db]]
   [omniward-client.db :as db]
   [superstructor.re-frame.fetch-fx]))

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

(reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))