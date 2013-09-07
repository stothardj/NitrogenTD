(ns nitrogentd.game.concussivetower
  (:use [nitrogentd.game.tower :only [Tower]]
        [nitrogentd.game.point :only [Point]]
        [nitrogentd.game.slow :only [Slow]]
        [nitrogentd.game.gamestate :only [time time-passed?]]
        [nitrogentd.game.drawing :only [ctx]])
  (:require [nitrogentd.game.drawing :as drawing]
            [nitrogentd.game.util :as util]
            [nitrogentd.game.creep :as creep]
            [nitrogentd.game.tower :as t]))

(def attack-range 65)
(def attack-cooldown 700)

(def ^:private map-merge (partial merge-with concat))
(def in-attack-range? (partial t/in-range? attack-range))

(defn preview [x y]
  (.beginPath ctx)
  (.arc ctx x y attack-range 0 (* 2 Math/PI) false)
  (.closePath ctx)
  (set! (.-strokeStyle ctx) "rgb(255,200,0)")
  (set! (.-lineWidth ctx) 2)
  (.stroke ctx)
  (set! (.-fillStyle ctx) "rgba(255,200,0,0.3)")
  (.fill ctx))

(defn choose-targets [tower creeps]
  "Split creeps on whether they should be attacked. Returns [attacked safe]."
  (let [grouped (group-by (partial in-attack-range? tower) creeps)
        attacked (get grouped true)
        safe (get grouped false)]
    [attacked safe]))

(defn attack-creep
  "Attack a single creep. Returns new creep and animations"
  [tower creep]
  (let [force (util/rand-between min-force max-force)
        slow (Slow. time)]
    (creep/apply-effect creep slow)))

(deftype ConcussiveTower [x y cooldown-start]
  Tower
  (draw [this]
    (set! (.-fillStyle ctx) "rgb(255,200,0)")
    (drawing/draw-at (fn []
                       (.beginPath ctx)
                       (.arc ctx 0 0 8 Math/PI 0 false)
                       (.closePath ctx)
                       (.fill ctx)) x y))
  (attack [this creeps]
    (if-not (time-passed? cooldown-start attack-cooldown)
      {:creeps creeps :tower this}
      (let [[attacked safe] (choose-targets this creeps)
            attacked-map (->> attacked
                              (map (partial attack-creep this))
                              (apply map-merge))]
        (assoc (map-merge attacked-map
                          {:creeps safe})
          :tower (ConcussiveTower. x y time)))))
  Point
  (get-point [this] [x y]))

(defn construct
  "Constructs and returns a laser tower"
  [x y]
  (let [cooldown-start 0]
    (ConcussiveTower. x y cooldown-start)))
