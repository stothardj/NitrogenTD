(ns nitrogentd.game.creep.test-creep
  (:require-macros [cemerick.cljs.test :refer (is deftest run-tests)])
  (:use [nitrogentd.game.simple :only [simple]])
  (:require [cemerick.cljs.test :as t]
            [nitrogentd.game.selfmerge :as selfmerge]
            [nitrogentd.game.creep.creep :as c]))

(deftest damage-result
  (let [merged
        (c/map->DamageResult
         {:creeps ['a 'b 'k 'f]
          :animations ['c 'd]
          :reward 12})
        
        a
        (c/map->DamageResult
         {:creeps ['a]
          :animations ['c]
          :reward 3})
        
        b         
        (c/map->DamageResult
         {:creeps ['b]
          :animations ['d]
          :reward 4})
        
        c
        (c/map->DamageResult
         {:creeps ['k 'f]
          :animations []
          :reward 5})]
    (is (= merged
           (c/merge-damage-result a b c)))
    (is (= merged
           (selfmerge/self-merge c/DamageResult [a b c]))))
  (is (=
       (c/map->DamageResult
        {:creeps []
         :animations []
         :reward 0})
       (c/merge-damage-result)))
  (is (=
       (c/map->DamageResult
        {:creeps []
         :animations []
         :reward 0})
       (simple c/DamageResult)
       (selfmerge/self-merge c/DamageResult []))))

