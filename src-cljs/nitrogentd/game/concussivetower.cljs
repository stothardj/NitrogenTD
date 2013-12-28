(ns nitrogentd.game.concussivetower
  (:use [nitrogentd.game.tower :only [Tower]]
        [nitrogentd.game.point :only [Point]]
        [nitrogentd.game.slow :only [Slow]]
        [nitrogentd.game.simple :only [simple]]
        [nitrogentd.game.quakeanimation :only [QuakeAnimation]]
        [nitrogentd.game.gamestate :only [time time-passed?]]
        [nitrogentd.game.drawing :only [ctx]]
        [nitrogentd.game.towerstats :only [map->TowerStats]]
        [nitrogentd.game.selfmerge :only [self-merge]])
  (:require [nitrogentd.game.drawing :as drawing]
            [nitrogentd.game.util :as util]
            [nitrogentd.game.creep :as creep]
            [nitrogentd.game.tower :as tower]))

(def stats (map->TowerStats
            {:cost 100
             :attack-range 65
             :attack-cooldown 2000
             :description "Slows creep. Area of effect."}))

(def ^:private map-merge (partial merge-with concat))
(def in-attack-range? (partial tower/in-range? (:attack-range stats)))

(defn preview [x y]
  (.beginPath ctx)
  (.arc ctx x y (:attack-range stats) 0 (* 2 Math/PI) false)
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
  (let [slow (Slow. time)]
    (creep/add-effect creep slow)))

(defrecord ConcussiveTower [x y cooldown-start]
  Tower
  (draw [this]
    (set! (.-fillStyle ctx) "rgb(255,200,0)")
    (drawing/draw-at (fn []
                       (.beginPath ctx)
                       (.arc ctx 0 0 8 Math/PI 0 false)
                       (.closePath ctx)
                       (.fill ctx)) x y))
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
         {:damage-result
          (map-merge attacked-map
                     {:creeps safe
                      :animations [(QuakeAnimation. time x y (:attack-range stats))]})
          :tower (assoc this :cooldown-start time)}))))
  Point
  (get-point [this] [x y]))

(defn construct
  "Constructs and returns a laser tower"
  [x y]
  (let [cooldown-start 0]
    (ConcussiveTower. x y cooldown-start)))
