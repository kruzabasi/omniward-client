(ns omniward-client.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [omniward-client.events :as events]
   [omniward-client.views :refer [main-panel]]))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [main-panel] root-el)))

(defn ^:export main
  []
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch [::events/fetch-patients])
  (mount-root))