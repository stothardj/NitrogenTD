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
            [nitrogentd.game.line :as line]
            )
  )

(def stats (map->CreepStats {:health 1200 :speed 3}))

(def max-damage 700)

(defn- scuttle-now? [time]
  (< (mod time 2000) 1000))

(deftype Spideree [x y health path spawn-time]
  Creep
  (draw [this]
    (set! (.-fillStyle ctx) "rgba(255, 0, 0, 0.3)")
    (set! (.-lineWidth ctx) 2)
    (set! (.-strokeStyle ctx) "rgba(255, 0, 0, 0.3)")
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
            [newx newy :as newp] (if (scuttle-now? (- time spawn-time))
                                   (line/move-towards [x y] goal (:speed stats))
                                   [x y])
            sq-dist (line/sq-point-to-point-dist newp goal)
            new-path (if (< sq-dist 100)
                       (rest path)
                       path)]
        (Spideree. newx newy health new-path spawn-time))))
  (damage [this force]
    (let [hit (min force max-damage)
          new-health (- health hit)]
      (when (pos? new-health)
        {:creeps [(Spideree. x y new-health path spawn-time)]
         :animations [(NumberAnimation. time hit x y)]})))
  Point
  (get-point [this] [x y]))

(defn spawn-spideree
  "Create and return a spideree with given params"
  [x y path]
  (let [health (:health stats)
        fudged-time (+ time (rand-int 1000))] ;; So not completely synchronized
        (Spideree. x y health path fudged-time)))
