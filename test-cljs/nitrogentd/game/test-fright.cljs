(ns nitogentd.game.test-fright
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.game.fright :only [Fright]]
        [nitrogentd.game.gamestate :only [time]])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.statuseffect :as statuseffect]
            [nitrogentd.game.fright :as fright]))

(deftest apply-effect
  (let [effect (Fright. time)
        old-stats {:speed 2}
        new-stats (statuseffect/apply-effect effect old-stats)]
    (is (> (:speed new-stats) (:speed old-stats)))))

(deftest add-effect
  (let [effect (Fright. time)
        frighted (statuseffect/add-effect effect [])]
    (is (= 1 (count frighted)))
    (is (= effect (first frighted))))
  (let [effect (Fright. time)
        effects (repeat (+ 3 fright/max-stacking) effect)
        frighted (reduce (fn [effects effect]
                         (statuseffect/add-effect effect effects)) [] effects)]
    (is (= fright/max-stacking (count frighted)))))

(deftest continues?
  (is (statuseffect/continues? (Fright. time)))
  (is (statuseffect/continues? (Fright. (inc (- time fright/effect-duration)))))
  (is (not (statuseffect/continues? (Fright. (- time fright/effect-duration 1))))))
