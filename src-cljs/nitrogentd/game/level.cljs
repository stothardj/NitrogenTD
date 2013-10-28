(ns nitrogentd.game.level
  (:use [nitrogentd.game.wave :only [load-wave]]))

(defrecord Level [name path waves next])
