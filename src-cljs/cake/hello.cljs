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

;; TODO: Use atoms for these
(def towers [(LaserTower. 200 300) (LaserTower. 100 400)])
(def creeps [(Spawnling. 150 100)])

(def mouse-pos (atom))

(def creep-path '((30 40) (70 90) (70 200) (300 200)))

(defn relative-mouse-pos
  [ev]
  (let [x (- (.-offsetX ev) (.-offsetLeft canvas))
        y (- (.-offsetY ev) (.-offsetTop canvas))]
    [x y]))

(event/listen canvas "click"
              (fn [ev]
                (let [[x y] (relative-mouse-pos ev)]
                  (.log js/console "Mouse pos" x y)
                  (if (line/point-on-thick-path? [x y] creep-path 50)
                    (.log js/console "Not allowed placing on creep path")
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
   (drawing/draw-debug-info @mouse-pos creep-path)
   (let [date (js/Date.)
         time (.getTime date)
         ]
     (doseq [tower towers]
       (tower/draw tower time))
     (doseq [creep creeps]
       (creep/draw creep time))
     )
   )
 40)
