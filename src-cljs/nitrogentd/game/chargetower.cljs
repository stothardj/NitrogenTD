(ns nitrogentd.game.chargetower
  (:use [nitrogentd.game.tower :only [Tower]]
        [nitrogentd.game.drawing :only [ctx]]
        [nitrogentd.game.point :only [Point]]
        [nitrogentd.game.gamestate :only [time time-passed?]]
        [nitrogentd.game.chargeanimation :only [ChargeAnimation]])
  (:require [nitrogentd.game.drawing :as drawing]
            [nitrogentd.game.point :as point]
            [nitrogentd.game.line :as line]
            [nitrogentd.game.creep :as creep]
            [nitrogentd.game.tower :as tower]
            [nitrogentd.game.util :as util]))

(def attack-range 100)
(def attack-cooldown 2500)
(def max-targets 1)
(def min-force 600)
(def max-force 1400)

(def ^:private map-merge (partial merge-with concat))
(def ^:private in-attack-range? (partial tower/in-range? attack-range))

(defn attack-creep
  "Attack a single creep. Returns new creep and animations"
  [tower creep]
  (let [[x y] (point/get-point creep)
        force (util/rand-between min-force max-force)]
    (map-merge
     (creep/damage creep force)
     {:animations [(ChargeAnimation. time tower creep)]})))

(defn preview [x y]
  (.beginPath ctx)
  (.arc ctx x y attack-range 0 (* 2 Math/PI) false)
  (.closePath ctx)
  (set! (.-strokeStyle ctx) "rgb(0,200,255)")
  (set! (.-lineWidth ctx) 2)
  (.stroke ctx)
  (set! (.-fillStyle ctx) "rgba(0,200,255,0.3)")
  (.fill ctx))

(deftype ChargeTower [x y cooldown-start]
  Tower
  (draw [this]
    (set! (.-fillStyle ctx) "rgba(0, 200, 255, 0.3)")
    (drawing/draw-at (fn []
                       (.beginPath ctx)
                       (.moveTo ctx -6 6)
                       (.lineTo ctx -3 -6)
                       (.lineTo ctx 3 -6)
                       (.lineTo ctx 6 6)
                       (.closePath ctx)
                       (.fill ctx)
                       (set! (.-fillStyle ctx) "rgb(0, 200, 255)")
                       (.beginPath ctx)
                       (.arc ctx 0 -6 (/ (- time cooldown-start) 400) 0 (* 2 Math/PI) false)
                       (.closePath ctx)
                       (.fill ctx)
                       )
                     x y)
    )
  (attack [this creeps]
    (if-not (time-passed? cooldown-start attack-cooldown)
      {:creeps creeps :tower this}
      (let [[attacked safe] (tower/choose-targets this in-attack-range?
                                                  shuffle max-targets creeps)
            attacked-map (->> attacked
                              (map (partial attack-creep this))
                              (apply map-merge))]
        (assoc (map-merge attacked-map
                          {:creeps safe})
          :tower (ChargeTower. x y time)))))
  Point
  (get-point [this] [x y]))

(defn construct
  "Constructs and returns a charge tower"
  [x y]
  (let [cooldown-start 0]
    (ChargeTower. x y cooldown-start)))

