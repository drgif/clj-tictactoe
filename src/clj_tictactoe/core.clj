(ns clj-tictactoe.core
  (:gen-class))

(def players ["X" "O"])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn- generate-board
  "Generates a game board pre-filled with nil"
  []
  (nth (iterate #(into [] (take 3 (repeat %))) nil)
       2))

(defn- initialize
  "Returns the initial game"
  []
  {:board (generate-board)
   :state [:next-player 0]})

(defn- next-player
  "Returns the index of the next player"
  [current-player]
  (if (contains? players (inc current-player))
    (inc current-player)
    0))

(defn- turn
  "Receives a player's turn as a positional vector, checks for errors and winning"
  [game pos]
  (let [current-player (second (:state game))
        symbol (players current-player)]
    (-> game
        (update :board #(assoc-in % pos symbol))
        (update :state #(assoc % 1 (next-player current-player))))))


(comment
  (initialize)
  (-> (initialize)
      (turn [0 0])
      (turn [1 1]))
  (clojure.pprint/pprint 
   (reductions turn
               (initialize)
               [[0 0] [1 1] [1 0] [2 0]
                [0 2] [0 1] [2 1] [1 2] [2 2]])))