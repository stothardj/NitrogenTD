(ns nitrogentd.game.test-slow
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.game.slow :only [Slow]]
        [nitrogentd.game.gamestate :only [time]])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.statuseffect :as statuseffect]
            [nitrogentd.game.slow :as slow]))

(deftest apply-effect
  (let [effect (Slow. time)
        old-stats {:speed 2}
        new-stats (statuseffect/apply-effect effect old-stats)]
    (is (= 1 (:speed new-stats)))))

(deftest add-effect
  (let [effect (Slow. time)
        slowed (statuseffect/add-effect effect [])]
    (is (= 1 (count slowed)))
    (is (= effect (first slowed))))
  (let [effect (Slow. time)
        effects (repeat (+ 3 slow/max-stacking) effect)
        slowed (reduce (fn [effects effect]
                         (statuseffect/add-effect effect effects)) [] effects)]
    (is (= slow/max-stacking (count slowed)))))

(deftest continues?
  (is (statuseffect/continues? (Slow. time)))
  (is (statuseffect/continues? (Slow. (inc (- time slow/effect-duration)))))
  (is (not (statuseffect/continues? (Slow. (- time slow/effect-duration 1))))))
