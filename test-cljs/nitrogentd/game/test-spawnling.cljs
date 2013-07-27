(ns nitrogentd.game.test-spawnling
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.spawnling :as s]))

(deftest value-type
  (is (= (s/spawn-spawnling 1 2 nil) (s/spawn-spawnling 1 2 nil)))
  (is (not= (s/spawn-spawnling 1 2 nil) (s/spawn-spawnling 1 3 nil))))
