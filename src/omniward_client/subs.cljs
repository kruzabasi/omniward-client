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
 ::modal
 (fn [db]
   (:modal db)))