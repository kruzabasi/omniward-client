(ns omniward-client.events
  (:require
   [re-frame.core :refer [reg-event-fx reg-event-db subscribe]]
   [omniward-client.db :as db]))

(reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))