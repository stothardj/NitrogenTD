(ns nitrogentd.game.creep.spawnling
  (:use [nitrogentd.game.creep.creep :only [Creep]]
        [nitrogentd.game.drawing :only [ctx]]
        [nitrogentd.game.point :only [Point]]
        [nitrogentd.game.gamestate :only [time]]
        [nitrogentd.game.animation.numberanimation :only [NumberAnimation]]
        [nitrogentd.game.creepstats :only [map->CreepStats]])
  (:require [nitrogentd.game.util :as util]
            [nitrogentd.game.drawing :as drawing]
            [nitrogentd.game.creep.creep :as creep]
            [nitrogentd.game.statuseffect.statuseffect :as statuseffect]))

(def stats (map->CreepStats {:health 1000 :speed 1 :reward 30}))

(defrecord Spawnling [x y health path status-effects]
  Creep
  (draw [this]
    (set! (.-fillStyle ctx) "rgba(255, 0, 255, 0.7)")
    (let [t (util/to-radians (mod (/ time 3) 360))
          u (* 4 (Math/sin t))
          w (* 2 u) ;; pun
          ]
      (drawing/draw-at #(.fillRect ctx (- u) -4 w 8) x y)))
  (move [this]
    (when-not (empty? path)
      (let [current-stats (statuseffect/apply-effects status-effects stats)
            move-speed (:speed current-stats)
            {:keys [x y path]} (creep/move-along-path [x y] move-speed 100 path)]
        (assoc this :x x :y y :path path))))
  (damage [this force]
    {:post [(instance? creep/DamageResult %)]}
    (let [new-health (- health force)
          new-creeps (if (pos? new-health)
                       [(assoc this :health new-health)]
                       [])
          new-reward (if (pos? new-health) 0 (:reward stats))]
      (creep/map->DamageResult
       {:creeps new-creeps
        :animations [(NumberAnimation. time force x y)]
        :reward new-reward})))
  (add-effect [this effect]
    (let [new-effects (statuseffect/add-effect effect status-effects)]
      {:creeps [(assoc this :status-effects new-effects)]}))
  Point
  (get-point [this] [x y]))

(defn spawn
  "Create and return Spawnling with given params."
  [x y path]
  (let [health (:health stats)]
    (Spawnling. x y health path [])))
