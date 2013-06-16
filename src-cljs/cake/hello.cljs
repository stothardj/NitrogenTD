(ns cake.hello
  (:use [cake.lasertower :only [LaserTower]]
        [cake.spawnling :only [Spawnling]]
        [cake.spawnlingpool :only [SpawnlingPool]]
        [cake.drawing :only [canvas]]
        )
  (:require [clojure.browser.event :as event]
            [cake.drawing :as drawing]
            [cake.creep :as creep]
            [cake.tower :as tower]
            [cake.pool :as pool]
            [cake.util :as util]
            [cake.line :as line]
            [cake.point :as point]
            [cake.animation :as animation]
            [cake.gamestate :as gamestate]
            )
  )

(def creep-path '((30 40) (70 90) (70 200) (300 200) (500 400) (600 500) (700 250) (600 100) (500 200)  ))

(def towers (atom [(LaserTower. 200 300) (LaserTower. 100 400)]))
(def creeps (atom [(Spawnling. 150 100 1000 creep-path)
                   (Spawnling. 100 200 1000 creep-path)
                   ]))
(def animations (atom []))

;; TODO: Make sequence
(def pool (atom (SpawnlingPool. 51 100 8 2 creep-path)))

(def mouse-pos (atom nil))

(defn relative-mouse-pos
  [ev]
  (let [rect (.getBoundingClientRect canvas)
        x (- (.-clientX ev) (.-left rect))
        y (- (.-clientY ev) (.-top rect))]
    [x y]))

(when canvas

  (event/listen canvas "click"
                (fn [ev]
                  (let [[x y] (relative-mouse-pos ev)]
                    (when-not (line/point-on-thick-path? [x y] creep-path 50)
                      (swap! towers (partial cons (LaserTower. x y)))))))

  (event/listen canvas "mousemove"
                (fn [ev]
                  (let [p (relative-mouse-pos ev)]
                    (reset! mouse-pos p))))

  (util/crashingInterval
   (fn []
     (drawing/clear-canvas)
     (drawing/draw-creep-path creep-path)
     (set! gamestate/time (.getTime (js/Date.)))
     (doseq [tower @towers]
       (tower/draw tower))
     (doseq [creep @creeps]
       (creep/draw creep))
     (doseq [anim @animations]
       (animation/draw anim))
     
     (swap! creeps #(for [creep %
                          :let [new-creep (creep/move creep)]
                          :when new-creep] new-creep))

     (let [m (tower/attack-all @towers @creeps)
           nt (:towers m)
           nc (:creeps m)
           na (:animations m)]
       (reset! towers nt)
       (reset! creeps nc)
       (swap! animations (partial concat na)))

     (swap! animations (partial filter animation/continues?))

     (when @pool
       (let [r (pool/spawn-creep @pool)
             new-creep (:creep r)
             new-pool (:pool r)
             ]
         (swap! creeps (partial concat new-creep))
         (reset! pool new-pool)))
     )
   40)
  )
