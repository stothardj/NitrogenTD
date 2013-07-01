(ns cake.tower
  (:require [cake.creep :as creep]
            [cake.point :as point]
            [cake.line :as line]))

(defprotocol Tower
  "A tower defense tower"
  (draw [this] "Draws the tower")
  (attack [this creeps] "Possibly attack some of the creeps.
                         Returns a map
                          :animations Optional. Any new animations
                          :creeps Updated creeps. Killed creeps removed.
                          :tower The updated tower to handle charging, cooldown, etc."))

(defn attack-all
  "Call attack from each tower on all creeps. Creeps are updated between each attack"
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

(defn in-range?
  "Return true if creep with within attack-range of tower"
  [attack-range tower creep]
  (let [creep-p (point/get-point creep)
        tower-p (point/get-point tower)
        r2 (* attack-range attack-range)]
    (< (line/sq-point-to-point-dist creep-p tower-p) r2)))

(defn choose-targets
  "Split creeps on whether they should be attacked. Returns [attack safe].
   valid? is a fn which takes a tower and creep and returns true if it is valid for
     this tower to attack this creep.
   prioritize is a fn which takes all valid creeps and sorts them to put most important
     targets first.
   max-targets is the max number which will be targeted in the end."
  [tower valid? prioritize max-targets creeps]
  (let [valid-for-tower? (partial valid? tower)
        valid-creep (filter valid-for-tower? creeps)
        invalid-creep (remove valid-for-tower? creeps)
        [attacked safe] (split-at max-targets (prioritize valid-creep))]
    [attacked (concat safe invalid-creep)]))
