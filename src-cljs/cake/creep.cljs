(ns cake.creep)

(defprotocol Creep
  "A creep to be killed by the towers"
  (draw [this time] "Draws the creep"))
