(ns nitrogentd.game.waves
  (:use [nitrogentd.game.wave :only [map->Wave]])
  (:require [nitrogentd.game.spawnlingpool :as spawnlingpool]
            [nitrogentd.game.spidereenest :as spidereenest]
            [nitrogentd.game.roachernest :as roachernest]
            [nitrogentd.game.paths :as paths]))

(def wave-1-1 (map->Wave
               {:name "The beginning"
                :pools [(spawnlingpool/construct 8 paths/path-1a)]}))

(def wave-1-2 (map->Wave
               {:name "The next one"
                :pools [(spidereenest/construct 4 paths/path-1a)]}))

(def wave-1-3 (map->Wave
               {:name "And another"
                :pools [(roachernest/construct 2 paths/path-1a)
                        (spawnlingpool/construct 6 paths/path-1b)]}))
