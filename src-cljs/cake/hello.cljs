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
            )
  )

;; TODO: Use atoms for these
(def towers [(LaserTower. 200 300) (LaserTower. 100 400)])
(def creeps [(Spawnling. 150 100)])

(event/listen canvas "click"
              (fn [ev]
                (.log js/console "Clicked!")
                (.log js/console ev)
                (let [x (- (.-offsetX ev) (.-offsetLeft canvas))
                      y (- (.-offsetY ev) (.-offsetTop canvas))]
                  (.log js/console x)
                  (.log js/console y)
                  (set! towers (cons (LaserTower. x y) towers))
                  )))

(util/crashingInterval
 (fn []
   (drawing/clearCanvas)
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
