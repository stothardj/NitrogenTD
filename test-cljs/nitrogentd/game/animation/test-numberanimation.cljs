(ns nitrogentd.game.animation.test-numberanimation
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.game.gamestate :only [time]])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.animation.animation :as a]
            [nitrogentd.game.animation.numberanimation :as na]))

(deftest continues?
  (let [start-time 20000, number 7, x 10, y 12
        anim (na/NumberAnimation. start-time number x y)]
    (binding [time start-time]
      (is (a/continues? anim)))
    (binding [time (+ start-time na/animation-length 1)]
      (is (not (a/continues? anim))))))
