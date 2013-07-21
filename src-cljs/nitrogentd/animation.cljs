(ns nitrogentd.animation)

(defprotocol Animation
  "An animation to be played. Only to be pretty, does not affect anything"
  (draw [this] "Draws the animation")
  (continues? [this] "Returns true if there's more to see in this animation"))
