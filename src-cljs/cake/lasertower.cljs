(ns cake.lasertower
  (:use [cake.tower :only [Tower]]
        [cake.drawing :only [ctx]]
        [cake.point :only [Point]]
        [cake.laseranimation :only [LaserAnimation]]
        [cake.gamestate :only [time time-passed?]]
        )
  (:require [cake.util :as util]
            [cake.drawing :as drawing]
            [cake.point :as point]
            [cake.line :as line]
            [cake.creep :as creep]
            [cake.tower :as tower]
            )
  )

(def attack-range 40)
(def attack-cooldown 1000)
(def max-targeted 3)

(defn attack-creep
  "Attack a single creep. Returns new creep and animations"
  [tower creep]
  (let [[x y] (point/get-point creep)
        force (+ 200 (rand-int 200))
        {new-creep :creep anims :animation} (creep/damage creep force)]
    {:creep new-creep
     :animation (conj anims (LaserAnimation. time tower creep))}))

(defn merge-attacks [attack-results]
  {:animations (->> attack-results
                    (map :animation)
                    (filter (complement nil?))
                    (apply concat))
  :creeps (->> attack-results
                     (map :creep)
                     (filter (complement nil?)))})

(defn choose-targets
  "Split creep on whether they should be attacked based on range and max-targeted"
  [tower attack-range max-targeted creeps]
  (let [in-range-of-tower? (partial tower/in-range? attack-range tower)
        in-range-creep (filter in-range-of-tower? creeps)
        out-of-range-creep (remove in-range-of-tower? creeps)
        [attacked safe] (split-at max-targeted (shuffle in-range-creep))]
    [attacked (concat safe out-of-range-creep)]))


(deftype LaserTower [x y cooldown-start]
  Tower
  (draw [this]
    (let [angle (util/to-radians (mod (/ time 5) 360))]
      (set! (.-fillStyle ctx) "rgba(255, 255, 255, 0.2)")
      (drawing/draw-at #(.fillRect ctx -8 -8 16 16) x y angle)
      (set! (.-fillStyle ctx) "rgba(255, 255, 255, 0.5)")
      (drawing/draw-at #(.fillRect ctx -5 -5 10 10) x y (- angle))))
  (attack [this creeps]
    (if-not (time-passed? cooldown-start attack-cooldown)
      {:creeps creeps :tower this}
      (let [[attacked safe] (choose-targets this attack-range max-targeted creeps)
            {:keys [animations creeps]}
            (->> attacked
                 (map (partial attack-creep this))
                 (merge-attacks))]
        
        {:animations animations
         :creeps (concat creeps safe)
         :tower (LaserTower. x y time)}
        )))
  Point
  (get-point [this] [x y])
  )

(defn construct-lasertower
  "Constructs and returns a laser tower"
  [x y]
  (let [cooldown-start 0]
    (LaserTower. x y cooldown-start)))
