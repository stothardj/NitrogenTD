(ns cake.creep)

(defprotocol Creep
  "A creep to be killed by the towers"
  (draw [this] "Draws the creep")
  (move [this] "Move, follow path, returns a new creep")
  )
