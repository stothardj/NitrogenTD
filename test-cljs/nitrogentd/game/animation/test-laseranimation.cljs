(ns nitrogentd.game.animation.test-laseranimation
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.game.gamestate :only [time]])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.animation.animation :as a]
            [nitrogentd.game.animation.laseranimation :as la]))

(deftest continues?
  (let [start-time 20000, tower nil, creep nil
        anim (la/LaserAnimation. start-time tower creep)]
    (binding [time start-time]
      (is (a/continues? anim)))
    (binding [time (+ start-time la/animation-length 1)]
      (is (not (a/continues? anim))))))
