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

(ct/defspec err-on-invalid-turn
  100
  (prop/for-all
   [invalid-pos (gen/such-that
                 (fn [pos] (not-every? #(<= 0 % 2) pos))
                 (gen/tuple gen/small-integer gen/small-integer))]
   (contains? (-> (sut/initial-game) (sut/turn invalid-pos)) :error)))

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

(t/deftest turn
  (t/testing "detect draw"
    (t/is (= :draw
             (first (:state (reduce sut/turn
                                    (sut/initial-game)
                                    [[0 0] [0 1] [0 2] [1 0]
                                     [1 2] [1 1] [2 1] [2 2]
                                     [2 0]]))))))

  (t/testing "detect win"
    (t/is (= :win
             (first (:state (reduce sut/turn
                                    (sut/initial-game)
                                    [[2 0] [2 1] [1 1] [1 2]
                                     [0 2]]))))))
           
  (t/testing "remove :error key after valid move"
    (t/is (not (contains? (-> (sut/initial-game)
                              (sut/turn [-1 -1])
                              (sut/turn [0 0]))
                          :error)))))



(comment
  (gen/sample gen/nat)
  (gen/sample (gen/tuple gen/small-integer gen/small-integer)))