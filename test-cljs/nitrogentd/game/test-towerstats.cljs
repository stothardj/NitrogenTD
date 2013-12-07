(ns nitrogentd.game.test-towerstats
  (:require-macros [cemerick.cljs.test :refer (are is deftest run-tests)])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.towerstats :as towerstats]))

(deftest format-time
  (are [x y] (= x (towerstats/format-time y))
       "0s" 0
       "1s" 1000
       "5s" 5000
       "2.5s" 2500))

(deftest format-range
  (are [s min max] (= s (towerstats/format-range {:min min :max max}))
       "0-1" 0 1
       "12-15" 12 15)
  (is (not (towerstats/format-range nil))))
