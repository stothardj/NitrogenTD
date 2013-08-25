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

(defn apply-status-effects [stats status-effects]
  "Applies all status effects sequentially, so they do stack."
  (reduce (twirl-args apply-effect) stats status-effects))

(defn continuing-status-effects [status-effects]
  "Returns status effects which still apply."
  (filter continues? status-effects))
