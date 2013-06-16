(ns cake.lasertower
  (:use [cake.tower :only [Tower]]
        [cake.drawing :only [ctx]]
        [cake.point :only [Point]]
        [cake.laseranimation :only [LaserAnimation]]
        [cake.gamestate :only [time]]
        )
  (:require [cake.util :as util]
            [cake.drawing :as drawing]
            [cake.point :as point]
            [cake.line :as line]
            [cake.creep :as creep]
            )
  )

(def attack-range 40)

;; Make range param and move to tower?
(defn in-range?
  [tower creep]
  (let [creep-p (point/get-point creep)
        tower-p (point/get-point tower)
        r2 (* attack-range attack-range)]
    (< (line/sq-point-to-point-dist creep-p tower-p) r2)))

(defn attack-creep
  "Attack a single creep if in range. Returns new creep and animations"
  [tower creep]
  (if (in-range? tower creep)
    {:creep (creep/damage creep 300)
     :animation (LaserAnimation. time tower creep)}
    {:creep creep}))   

(defn merge-attacks [attack-results]
  (letfn [(extract-present [m keyword]
            (->> m
                 (map keyword)
                 (filter (complement nil?))))]
    {:animations (extract-present attack-results :animation)
     :creeps     (extract-present attack-results :creep)}))

(deftype LaserTower [x y]
  Tower
  (draw [this]
    (let [angle (util/to-radians (mod (/ time 5) 360))]
      (set! (.-fillStyle ctx) "rgba(255, 255, 255, 0.2)")
      (drawing/draw-at #(.fillRect ctx -8 -8 16 16) x y angle)
      (set! (.-fillStyle ctx) "rgba(255, 255, 255, 0.5)")
      (drawing/draw-at #(.fillRect ctx -5 -5 10 10) x y (- angle))))
  (attack [this creeps]
    (assoc
        (->> creeps
             (map (partial attack-creep this))
             (merge-attacks)
             )
      :tower this))
  Point
  (get-point [this] [x y])
  )
