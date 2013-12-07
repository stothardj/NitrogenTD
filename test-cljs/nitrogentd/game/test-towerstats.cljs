(ns nitrogentd.game.test-towerstats
  (:require-macros [cemerick.cljs.test :refer (are deftest run-tests)])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.towerstats :as towerstats]))

(deftest format-time
  (are [x y] (= x (towerstats/format-time y))
       "0s" 0
       "1s" 1000
       "5s" 5000
       "2.5s" 2500))
