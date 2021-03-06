(ns nitrogentd.game.tower.lasertower
  (:use [nitrogentd.game.tower.tower :only [Tower]]
        [nitrogentd.game.drawing :only [ctx]]
        [nitrogentd.game.point :only [Point]]
        [nitrogentd.game.simple :only [simple]]
        [nitrogentd.game.animation.laseranimation :only [LaserAnimation]]
        [nitrogentd.game.gamestate :only [time time-passed?]]
        [nitrogentd.game.towerstats :only [map->TowerStats]]
        [nitrogentd.game.selfmerge :only [self-merge]])
  (:require [nitrogentd.game.util :as util]
            [nitrogentd.game.drawing :as drawing]
            [nitrogentd.game.point :as point]
            [nitrogentd.game.line :as line]
            [nitrogentd.game.creep.creep :as creep]
            [nitrogentd.game.tower.tower :as tower]))

(def stats (map->TowerStats
            {:cost 100
             :attack-range 40
             :attack-cooldown 1000
             :force {:min 200 :max 400}
             :max-targets 3
             :description "Fires 3 weak lasers at a time. Short range."}))

(def ^:private map-merge (partial merge-with concat))
(def in-attack-range? (partial tower/in-range? (:attack-range stats)))

(defn preview [x y]
  (.beginPath ctx)
  (.arc ctx x y (:attack-range stats) 0 (* 2 Math/PI) false)
  (.closePath ctx)
  (set! (.-strokeStyle ctx) "rgb(255,255,255)")
  (set! (.-lineWidth ctx) 2)
  (.stroke ctx)
  (set! (.-fillStyle ctx) "rgba(255,255,255,0.3)")
  (.fill ctx))

(defn choose-targets [tower creeps]
  "Split creeps on whether they should be attacked. Returns [attacked safe]."
  (tower/choose-targets tower in-attack-range? shuffle (:max-targets stats) creeps))

(defn attack-creep
  "Attack a single creep. Returns new creep and animations"
  [tower creep]
  (let [force-range (:force stats)
        force (util/rand-between (:min force-range) (:max force-range))]
    (map-merge
     (creep/damage creep force)
     {:animations [(LaserAnimation. time tower creep)]})))

(defrecord LaserTower [x y cooldown-start]
  Tower
  (draw [this]
    (let [angle (util/to-radians (mod (/ time 5) 360))]
      (set! (.-fillStyle ctx) "rgba(255, 255, 255, 0.2)")
      (drawing/draw-at #(.fillRect ctx -8 -8 16 16) x y angle)
      (set! (.-fillStyle ctx) "rgba(255, 255, 255, 0.5)")
      (drawing/draw-at #(.fillRect ctx -5 -5 10 10) x y (- angle))))
  (attack [this creeps]
    {:post [(instance? tower/AttackResult %)]}
    (if-not (time-passed? cooldown-start (:attack-cooldown stats))
      (tower/map->AttackResult
       {:damage-result (assoc (simple creep/DamageResult) :creeps creeps)
        :tower this})
      (let [[attacked safe] (choose-targets this creeps)
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
  "Constructs and returns a laser tower"
  [x y]
  (let [cooldown-start 0]
    (LaserTower. x y cooldown-start)))
