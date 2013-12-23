(ns nitrogentd.game.spideree
  (:use [nitrogentd.game.creep :only [Creep]]
        [nitrogentd.game.drawing :only [ctx]]
        [nitrogentd.game.point :only [Point]]
        [nitrogentd.game.gamestate :only [time]]
        [nitrogentd.game.numberanimation :only [NumberAnimation]]
        [nitrogentd.game.creepstats :only [map->CreepStats]]
        )
  (:require [nitrogentd.game.util :as util]
            [nitrogentd.game.drawing :as drawing]
            [nitrogentd.game.creep :as creep]
            [nitrogentd.game.statuseffect :as statuseffect]
            )
  )

(def stats (map->CreepStats {:health 1200 :speed 3 :reward 45}))

(def max-damage 700)

(defn- scuttle-now? [time]
  (< (mod time 2000) 1000))

(defrecord Spideree [x y health path spawn-time status-effects]
  Creep
  (draw [this]
    (set! (.-fillStyle ctx) "rgba(255, 100, 0, 0.7)")
    (set! (.-lineWidth ctx) 2)
    (set! (.-strokeStyle ctx) "rgba(255, 100, 0, 0.7)")
    (drawing/draw-at (fn []
                       (let [body (- (Math/sin (/ time 70)) 4)
                             llegx (* 2 (inc (Math/sin (/ time 65))))
                             llegy (inc (Math/sin (/ time 70)))
                             rlegx (* 2 (inc (Math/cos (/ time 65))))
                             rlegy (inc (Math/cos (/ time 70)))
                             ]
                       (.fillRect ctx -4 body 8 4)
                       (.beginPath ctx)
                       (.moveTo ctx -4 -4)
                       (.lineTo ctx (- -6 llegx) llegy)
                       (.moveTo ctx 4 -4)
                       (.lineTo ctx (+ 6 rlegx) rlegy)
                       (.stroke ctx)))
                     x y))
  (move [this]
    (when-not (empty? path)
      (let [goal (first path)
            current-stats (statuseffect/apply-effects status-effects stats)
            move-speed (if (scuttle-now? (- time spawn-time)) (:speed current-stats) 0)
            {:keys [x y path]} (creep/move-along-path [x y] move-speed 100 path)]
        (Spideree. x y health path spawn-time status-effects))))
  (damage [this force]
    (let [hit (min force max-damage)
          new-health (- health hit)
          new-creeps (if (pos? new-health)
                       [(Spideree. x y new-health path spawn-time status-effects)]
                       [])]
      {:creeps new-creeps
       :animations [(NumberAnimation. time hit x y)]}))
  (add-effect [this effect]
    (let [new-effects (statuseffect/add-effect effect status-effects)]
      {:creeps [(Spideree. x y health path spawn-time new-effects)]}))
  Point
  (get-point [this] [x y]))

(defn spawn-spideree
  "Create and return a spideree with given params"
  [x y path]
  (let [health (:health stats)
        fudged-time (+ time (rand-int 1000))] ;; So not completely synchronized
        (Spideree. x y health path fudged-time [])))
