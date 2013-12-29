(ns nitrogentd.game.tower.chargetower
  (:use [nitrogentd.game.tower.tower :only [Tower]]
        [nitrogentd.game.drawing :only [ctx]]
        [nitrogentd.game.point :only [Point]]
        [nitrogentd.game.gamestate :only [time time-passed?]]
        [nitrogentd.game.simple :only [simple]]
        [nitrogentd.game.animation.chargeanimation :only [ChargeAnimation]]
        [nitrogentd.game.towerstats :only [map->TowerStats]]
        [nitrogentd.game.selfmerge :only [self-merge]])
  (:require [nitrogentd.game.drawing :as drawing]
            [nitrogentd.game.point :as point]
            [nitrogentd.game.line :as line]
            [nitrogentd.game.creep.creep :as creep]
            [nitrogentd.game.tower.tower :as tower]
            [nitrogentd.game.util :as util]))

(def stats (map->TowerStats
            {:cost 100
             :attack-range 100
             :attack-cooldown 2500
             :force {:min 600 :max 1400}
             :max-targets 1
             :description "Fires one powerful shot. Long range."}))

(def ^:private map-merge (partial merge-with concat))
(def ^:private in-attack-range? (partial tower/in-range? (:attack-range stats)))

(defn attack-creep
  "Attack a single creep. Returns new creep and animations"
  [tower creep]
  (let [force-range (:force stats)
        force (util/rand-between (:min force-range) (:max force-range))]
    (map-merge
     (creep/damage creep force)
     {:animations [(ChargeAnimation. time tower creep)]})))

(defn preview [x y]
  (.beginPath ctx)
  (.arc ctx x y (:attack-range stats) 0 (* 2 Math/PI) false)
  (.closePath ctx)
  (set! (.-strokeStyle ctx) "rgb(0,200,255)")
  (set! (.-lineWidth ctx) 2)
  (.stroke ctx)
  (set! (.-fillStyle ctx) "rgba(0,200,255,0.3)")
  (.fill ctx))

(defrecord ChargeTower [x y cooldown-start]
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
    {:post [(instance? tower/AttackResult %)]}
    (if-not (time-passed? cooldown-start (:attack-cooldown stats))
      (tower/map->AttackResult
       {:damage-result (assoc (simple creep/DamageResult) :creeps creeps)
        :tower this})
      (let [[attacked safe] (tower/choose-targets this in-attack-range?
                                                  shuffle (:max-targets stats) creeps)
            attacked-map (->> attacked
                              (map (partial attack-creep this))
                              (self-merge creep/DamageResult))]
        (tower/map->AttackResult
         {:damage-result (map-merge attacked-map
                                    {:creeps safe})
          :tower (assoc this :cooldown-start time)}))))
  Point
  (get-point [this] [x y]))

(defn construct
  "Constructs and returns a charge tower"
  [x y]
  (let [cooldown-start 0]
    (ChargeTower. x y cooldown-start)))

