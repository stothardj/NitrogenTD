(ns nitrogentd.game.concussivetower
  (:use [nitrogentd.game.tower :only [Tower]]
        [nitrogentd.game.point :only [Point]]
        [nitrogentd.game.drawing :only [ctx]])
  (:require [nitrogentd.game.drawing :as drawing]))

(def attack-range 65)

(defn preview [x y]
  (.beginPath ctx)
  (.arc ctx x y attack-range 0 (* 2 Math/PI) false)
  (.closePath ctx)
  (set! (.-strokeStyle ctx) "rgb(255,200,0)")
  (set! (.-lineWidth ctx) 2)
  (.stroke ctx)
  (set! (.-fillStyle ctx) "rgba(255,200,0,0.3)")
  (.fill ctx))

(deftype ConcussiveTower [x y cooldown-start]
  Tower
  (draw [this]
    (set! (.-fillStyle ctx) "rgb(255,200,0)")
    (drawing/draw-at (fn []
                       (.beginPath ctx)
                       (.arc ctx 0 0 8 Math/PI 0 false)
                       (.closePath ctx)
                       (.fill ctx)) x y))
  (attack [this creeps] {:creeps creeps :tower this})
  Point
  (get-point [this] [x y]))

(defn construct
  "Constructs and returns a laser tower"
  [x y]
  (let [cooldown-start 0]
    (ConcussiveTower. x y cooldown-start)))
