(ns nitrogentd.game.statuseffect)

(defprotocol StatusEffect
  "A status effect on a creep"
  (apply-effect [this stats] "Applies the effect to stats, returning a new stats object")
  (continues? [this] "Returns true if the status effect should remain"))

(defn- twirl-args [f]
  "Given a function reverse the first two parameters it takes."
  (fn [x y & args]
    (let [new-args (concat [y x] args)]
      (apply f new-args))))

(defn apply-effects [effects stats]
  "Applies all status effects sequentially, so they do stack."
  (reduce (twirl-args apply-effect) stats (filter continues? effects)))

(defn add-effect [effects effect]
  "Adds status effect. Also removes ones that no longer apply since no longer need to track."
  (conj (filter continues? effects) effect))
