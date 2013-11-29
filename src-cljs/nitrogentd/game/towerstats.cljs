(ns nitrogentd.game.towerstats
  (:require [crate.core  :as crate])
  (:use-macros [crate.def-macros :only [defpartial]]))

(defrecord TowerStats [cost attack-range attack-cooldown])

(defpartial tower-stat-table
  [stats]
  [:div.stat-surround
   [:table.stats-table
    [:tr [:th "Cost"] [:td.stats-data (:cost stats)]]
    [:tr [:th "Range"] [:td.stats-data (:attack-range stats)]]
    [:tr [:th "Cooldown"] [:td.stats-data (:attack-cooldown stats)]]
    [:tr [:td (:description stats)]]]])

(defn show
  "Returns the dom to set in the UI to show tower stats"
  [stats]
  (tower-stat-table stats))
