(ns nitrogentd.game.waves
  (:use [nitrogentd.game.wave :only [map->Wave]])
  (:require [nitrogentd.game.spawnlingpool :as spawnlingpool]
            [nitrogentd.game.spidereenest :as spidereenest]
            [nitrogentd.game.paths :as paths]))

(def wave-1-2 (map->Wave
               {:name "The next one"
                :pools [(spidereenest/construct 4 paths/path-1)]
                :next nil}))

(def wave-1-1 (map->Wave
               {:name "The beginning"
                :pools [(spawnlingpool/construct 8 paths/path-1)]
                :next wave-1-2}))
