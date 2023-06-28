(ns app.core
  (:require [reagent.core :as r]
            [app.main :refer [root]]))

(defn ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (r/render [root] (.getElementById js/document "app")))

(defn ^:export main
  "Run application startup logic."
  []
  (render))