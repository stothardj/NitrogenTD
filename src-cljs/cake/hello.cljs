(ns cake.hello
  (:use [cake.lasertower :only [LaserTower]]
        [cake.spawnling :only [Spawnling]]
        [cake.drawing :only [canvas]]
        )
  (:require [clojure.browser.event :as event]
            [cake.drawing :as drawing]
            [cake.creep :as creep]
            [cake.tower :as tower]
            [cake.util :as util]
            [cake.line :as line]
            )
  )

(def creep-path '((30 40) (70 90) (70 200) (300 200) (500 400) (600 500) (700 50)))

;; TODO: Use atoms for these
(def towers [(LaserTower. 200 300) (LaserTower. 100 400)])
(def creeps (atom [(Spawnling. 150 100 creep-path)
                   (Spawnling. 100 200 creep-path)
                   ]))

(def mouse-pos (atom nil))

(defn relative-mouse-pos
  [ev]
  (let [rect (.getBoundingClientRect canvas)
        x (- (.-clientX ev) (.-left rect))
        y (- (.-clientY ev) (.-top rect))]
    [x y]))

(event/listen canvas "click"
              (fn [ev]
                (let [[x y] (relative-mouse-pos ev)]
                  (when-not (line/point-on-thick-path? [x y] creep-path 50)
                    (set! towers (cons (LaserTower. x y) towers)))
                  )))

(event/listen canvas "mousemove"
              (fn [ev]
                (let [p (relative-mouse-pos  ev)]
                  (reset! mouse-pos p))))

(util/crashingInterval
 (fn []
   (drawing/clear-canvas)
   (drawing/draw-creep-path creep-path)
   (let [date (js/Date.)
         time (.getTime date)
         ]
     (doseq [tower towers]
       (tower/draw tower time))
     (reset! creeps
             (doall (for [creep @creeps]
                      (do
                        (creep/draw creep time)
                        (creep/move creep)))))
     )
   )
 40)

