(ns nitrogentd.game.tower.tower
  (:require [nitrogentd.game.creep.creep :as creep]
            [nitrogentd.game.point :as point]
            [nitrogentd.game.line :as line]
            [nitrogentd.game.util :as util]))

(defprotocol Tower
  "A tower defense tower"
  (draw [this] "Draws the tower")
  (attack [this creeps] "Possibly attack some of the creeps.
                         Returns AttackResult"))

(defrecord AttackResult [damage-result tower])

(defn attack-all
  "Call attack from each tower on all creeps. Creeps are updated between each attack"
  [towers creeps]
  (loop [unprocessed towers
         processed []
         c creeps
         animations []
         reward 0]
    (if-let [tseq (seq unprocessed)]
      (let [[tf & tr] tseq
            {nt :tower
             {nc :creeps
             na :animations
             nr :reward} :damage-result} (attack tf c)
             new-reward (+ reward nr)]
        (recur tr (conj processed nt) nc (concat animations na) new-reward))
      {:creeps c :towers processed :animations animations :reward reward})))

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

(defn pay-for-tower
  "Attempts to pay for the tower.
   Returns
   {:player new-player
    :tower (optional) new tower if sucessfully paid for}"
  [player cost construct]
  (if (< (:gold player) cost)
    {:player player}
    {:player (assoc player :gold (- (:gold player) cost))
     :tower (construct)}))
