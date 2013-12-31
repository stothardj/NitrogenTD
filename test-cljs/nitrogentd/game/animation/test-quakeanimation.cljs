(ns nitrogentd.game.animation.test-quakeanimation
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.game.gamestate :only [time]])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.animation.animation :as a]
            [nitrogentd.game.animation.quakeanimation :as qa]))

(deftest continues?
  (let [start-time 20000, x 7, y 12, max-range 32
        anim (qa/QuakeAnimation. start-time x y max-range)]
    (binding [time start-time]
      (is (a/continues? anim)))
    (binding [time (+ start-time qa/animation-length 1)]
      (is (not (a/continues? anim))))))
