(ns nitrogentd.test.line
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.line :as line]))

(def inf js/Infinity)

(deftest slope
  (is (= 1 (line/slope [4 5] [5 6])))
  (is (line/infinity? (line/slope [4 5] [4 6]))))

(deftest perp-slope
  (is (= (/ 4 5)
         (line/perp-slope (- (/ 5 4)))))
  (is (= 0 (line/perp-slope inf)))
  (is (line/infinity? (line/perp-slope 0))))

(deftest intersection
  (is (= [0 1]
         (line/intersection [1 2 3]
                            [2 -1 -1])))
  (is (= [2 3]
         (line/intersection [inf 2 8]
                            [1 1 2])
         (line/intersection [1 1 2]
                            [inf 2 8]))))

(deftest sq-dist
  (is (= 25 (line/sq-point-to-point-dist [1 2] [4 -2])))
  (is (= 100 (line/sq-point-to-point-dist [5 7] [15 7])))
  (is (= 100 (line/sq-point-to-point-dist [5 7] [5 -3]))))
