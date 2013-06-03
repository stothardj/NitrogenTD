(ns cake.animation)

(defprotocol Animation
  "An animation to be played. Only to be pretty, does not affect anything"
  (draw [this time] "Draws the animation")
  (continues? [this time] "Returns true if there's more to see in this animation"))
