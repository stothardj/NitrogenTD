(ns nitrogentd.game.splitter
  (:use [nitrogentd.game.creep :only [Creep]]
        [nitrogentd.game.drawing :only [ctx]]
        [nitrogentd.game.point :only [Point]]
        [nitrogentd.game.gamestate :only [time]]
        [nitrogentd.game.numberanimation :only [NumberAnimation]]
        [nitrogentd.game.creepstats :only [map->CreepStats]])
  (:require [nitrogentd.game.util :as util]
            [nitrogentd.game.drawing :as drawing]
            [nitrogentd.game.creep :as creep]
            [nitrogentd.game.statuseffect :as statuseffect]))

(def stats (map->CreepStats {:health 200 :speed 1 :reward 10}))

(def max-incarnation 3)

(defrecord Splitter [x y health path status-effects incarnation spawn-time]
  Creep
  (draw [this]
    (set! (.-fillStyle ctx) "rgb(200,200,0)")
    (.save ctx)
    (.translate ctx x y)
    (.scale ctx (/ (- 5 incarnation) 30) (/ (- 5 incarnation) 30))
    (.translate ctx -50 -50)
    (.beginPath ctx)
    ;; Shamelessly copied from a basic shape
    (.moveTo ctx 50 0)
    (.lineTo ctx 65 35)
    (.lineTo ctx 100 37)
    (.lineTo ctx 73 61)
    (.lineTo ctx 82 97)
    (.lineTo ctx 50 78)
    (.lineTo ctx 18 97)
    (.lineTo ctx 28 61)
    (.lineTo ctx 0 37)
    (.lineTo ctx 37 35)
    (.closePath ctx)
    (.fill ctx)
    (.restore ctx))
  (move [this]
    (when-not (empty? path)
      (let [current-stats (statuseffect/apply-effects status-effects stats)
            move-speed (:speed current-stats)
            {:keys [x y path]} (creep/move-along-path [x y] move-speed 100 path)
            t (util/to-radians (mod (/ (- time spawn-time) 3) 360))
            new-x (+ x (Math/sin t))
            new-y (+ y (Math/cos t))]
        (Splitter. new-x new-y health path status-effects incarnation spawn-time))))
  (damage [this force]
    {:post [(instance? creep/DamageResult %)]}
    (let [new-health (- health force)]
      (if (pos? new-health)
        (creep/map->DamageResult
         {:creeps [(Splitter. x y new-health path status-effects incarnation spawn-time)]
          :animations [(NumberAnimation. time force x y)]
          :reward 0})
        (let [new-creeps (if (< incarnation max-incarnation)
                           [(Splitter. (+ x 10) (+ y 10) (:health stats) path []
                                       (inc incarnation) time)
                            (Splitter. (- x 10) (- y 10) (:health stats) path []
                                       (inc incarnation) time)]
                           [])]
          (creep/map->DamageResult
           {:creeps new-creeps
            :animations [(NumberAnimation. time health x y)]
            :rewards [(:reward stats)]})))))
  (add-effect [this effect]
    (let [new-effects (statuseffect/add-effect effect status-effects)]
      {:creeps [(Splitter. x y health path new-effects incarnation spawn-time)]}))
  Point
  (get-point [this] [x y]))

(defn spawn
  "Create and return Splitter with given params."
  [x y path]
  (let [health (:health stats)
        incarnation 0]
    (Splitter. x y health path [] incarnation time)))
