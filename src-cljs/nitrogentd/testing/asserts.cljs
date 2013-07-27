(ns nitrogentd.testing.asserts)

(defn eq-any-order?
  "Returns true if sequences a and b contain equal elements. Order does not matter."
  [a b]
  (cond (every? empty? [a b]) true
        (empty? a) false
        (empty? b) false
        :else
        (let [[h & r] a
              [front back] (split-with #(not= h %) b)]
           (when back
             (eq-any-order? r (concat front (rest back)))))))
