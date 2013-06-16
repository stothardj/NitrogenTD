(ns cake.tower
  (:require [cake.creep :as creep]))

(defprotocol Tower
  "A tower defense tower"
  (draw [this] "Draws the tower")
  (attack [this creeps] "Possibly attack some of the creeps.
                         Returns a map
                          :animations Optional. Any new animations
                          :creeps Updated creeps. Killed creeps removed.
                          :tower The updated tower to handle charging, cooldown, etc."))

(defn attack-all
  [towers creeps]
  (loop [unprocessed towers
         processed []
         c creeps
         animations []
         ]
    (if-let [tseq (seq unprocessed)]
      (let [[tf & tr] tseq
            {nc :creeps
             nt :tower
             na :animations} (attack tf c)]
        (recur tr (conj processed nt) nc (concat animations na)))
      {:creeps c :towers processed :animations animations})))
