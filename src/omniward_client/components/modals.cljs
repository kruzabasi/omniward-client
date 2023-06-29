(ns omniward-client.components.modals
  (:require
   [re-frame.core :refer [dispatch]]
   [omniward-client.events :as events]))

(defmulti multi-modal (fn [x] (:type @x)))

(defn modal-frame
  [open? body]
  [:div.modal
   {:style {:display (if open? "flex" "none")}}
   [:div.modal-content
    body
    [:button.close-button
     {:on-click #(dispatch [::events/toggle-modal {:open false}])}
     "Close"]]])

(defmethod multi-modal :edit
  [args]
  (modal-frame
   (:open @args)
   [:div "Edit"]))

(defmethod multi-modal :delete
  [args]
  (modal-frame
   (:open @args)
   [:h2 (:data @args)]))

(defmethod multi-modal :add
  [args]
  (modal-frame
   (:open @args)
   [:div [:h1 "New Patient"]]))