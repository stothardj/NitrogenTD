(ns nitrogentd.game.selfmerge)

;; self-merge is a multimethod which dispatches on the type (the first item)
;; the type must be specified because should handle empty list
(defmulti self-merge (fn [t items] t))
