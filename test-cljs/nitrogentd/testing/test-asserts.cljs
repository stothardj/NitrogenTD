(ns nitrogentd.testing.test-asserts
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.testing.asserts :only [eq-any-order?]])
  (:require [cemerick.cljs.test :as t]))

(deftest eq-any-order
  (is (eq-any-order? [] []))
  (is (not (eq-any-order? [] [1])))
  (is (not (eq-any-order? [1] [])))
  (is (eq-any-order? [1 2 3] [1 2 3]))
  (is (eq-any-order? [1 2 3] [3 1 2]))
  (is (not (eq-any-order? [1 2 4] [3 1 2])))
  (is (not (eq-any-order? [1 1 2 3] [3 2 1 2]))))
