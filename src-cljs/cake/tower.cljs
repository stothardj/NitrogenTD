(ns cake.tower
  (:require [cake.creep :as creep]))

(defprotocol Tower
  "A tower defense tower"
  (draw [this] "Draws the tower")
  (attack [this creeps] "Possibly attack some of the creeps.
                         Returns a map
                          :creeps Updated creeps. Killed creeps removed.
                          :tower The updated tower to handle charging, cooldown, etc."))

(defn attack-all
  [towers creeps]
  (loop [unprocessed towers
         processed []
         c creeps
         ]
    (if-let [tseq (seq unprocessed)]
      (let [tf (first tseq)
            tr (rest tseq)
            m (attack tf c)
            nc (:creeps m)
            nt (:tower m)]
        (recur tr (conj processed nt) nc))
      {:creeps c :towers processed})))
