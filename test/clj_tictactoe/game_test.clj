(ns clj-tictactoe.game-test
  (:require [clojure.test :as t]
            [clojure.test.check.clojure-test :as ct]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clj-tictactoe.game :as sut]))

(ct/defspec always-map
  100
  (prop/for-all
   [pos (gen/tuple gen/small-integer gen/small-integer)]
   (let [g (sut/turn (sut/initial-game) pos)]
     (map? g))))

(ct/defspec no-overwrite
  100
  (prop/for-all
   [pos (gen/tuple gen/nat gen/nat)
    tries (gen/such-that pos? gen/nat)]
   (->> (iterate #(sut/turn % pos) (sut/initial-game))
        (drop 1)
        (take tries)
        (map :board)
        (apply =))))



(comment
  (gen/sample gen/nat)
  (gen/sample (gen/tuple gen/small-integer gen/small-integer)))