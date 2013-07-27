(ns nitrogentd.game.spawnling
  (:use [nitrogentd.game.creep :only [Creep]]
        [nitrogentd.game.drawing :only [ctx]]
        [nitrogentd.game.point :only [Point]]
        [nitrogentd.game.gamestate :only [time]]
        [nitrogentd.game.numberanimation :only [NumberAnimation]]
        )
  (:require [nitrogentd.game.util :as util]
            [nitrogentd.game.drawing :as drawing]
            [nitrogentd.game.line :as line]
            )
  )

(defrecord Spawnling [x y health path]
  Creep
  (draw [this]
    (set! (.-fillStyle ctx) "rgba(255, 0, 255, 0.3)")
    (let [t (util/to-radians (mod (/ time 3) 360))
          u (* 4 (Math/sin t))
          w (* 2 u) ;; pun
          ]
      (drawing/draw-at #(.fillRect ctx (- u) -4 w 8) x y)))
  (move [this]
    (when-not (empty? path)
      (let [goal (first path)
            [newx newy :as newp] (line/move-towards [x y] goal 1)
            sq-dist (line/sq-point-to-point-dist newp goal)
            new-path (if (< sq-dist 100)
                       (rest path)
                       path)]
        (Spawnling. newx newy health new-path))))
  (damage [this force]
    (let [new-health (- health force)]
      (when (pos? new-health)
        {:creeps [(Spawnling. x y new-health path)]
         :animations [(NumberAnimation. time force x y)]})))
  Point
  (get-point [this] [x y])
  )

(defn spawn-spawnling
  "Create and return Spawnling with given params."
  [x y path]
  (let [health 1000]
    (Spawnling. x y health path)))
