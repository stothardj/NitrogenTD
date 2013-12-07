(ns nitrogentd.game.towerstats
  (:require [crate.core  :as crate])
  (:use-macros [crate.def-macros :only [defpartial]]))

(defrecord TowerStats [cost attack-range attack-cooldown force])

(defn format-time [ms]
  (str (/ ms 1000) "s"))

(defn format-range [range]
  (when range (str (:min range) "-" (:max range))))

(defpartial tower-stat-table
  [stats]
  [:div.stat-surround
   [:table.stats-table
    (for [[s k] [["Cost" :cost]
                 ["Force" (comp format-range :force)]
                 ["Range" :attack-range]
                 ["Max Targets" :max-targets]
                 ["Cooldown" (comp format-time :attack-cooldown)]]
          :let [val (k stats)]
          :when val]
      [:tr [:th s] [:td.stats-data val]])
    [:tr [:td (:description stats)]]]])

(defn show
  "Returns the dom to set in the UI to show tower stats"
  [stats]
  (tower-stat-table stats))
