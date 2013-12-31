(ns nitrogentd.game.animation.test-chargeanimation
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.game.gamestate :only [time]])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.animation.animation :as a]
            [nitrogentd.game.animation.chargeanimation :as ca]))

(deftest continues?
  (let [start-time 20000, tower nil, creep nil
        anim (ca/ChargeAnimation. start-time tower creep)]
    (binding [time start-time]
      (is (a/continues? anim)))
    (binding [time (+ start-time ca/animation-length 1)]
      (is (not (a/continues? anim))))))
