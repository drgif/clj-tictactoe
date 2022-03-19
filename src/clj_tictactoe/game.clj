(ns clj-tictactoe.game)

(def players ["X" "O"])

(defn- next-player
  "Returns the index of the next player"
  [current-player]
  (if (contains? players (inc current-player))
    (inc current-player)
    0))

(defn- update-state
  "Returns a tuple containing win/winner or draw state, otherwise nil"
  [{:keys [board state] :as game}]
  (let [rows board
        columns (apply mapv vector board)
        up-down-diagonal (map #(get-in board [% %]) (range 3))
        down-up-diagonal (map #(get-in board [(- 2 %) %]) (range 3))
        lines-to-test (conj (concat rows columns) up-down-diagonal down-up-diagonal)
        winning-rows (filter #(and (apply = %) (some? (first %))) lines-to-test)]
    (assoc game :state (cond
                         (seq winning-rows) [:win (first (first winning-rows))]
                         (every? some? (flatten board)) [:draw]
                         :else [:next-player (next-player (second state))]))))

(defn- generate-board
  "Generates a game board pre-filled with nil"
  []
  (nth (iterate #(into [] (take 3 (repeat %))) nil)
       2))

(defn initial-game
  "Returns the initial game"
  []
  {:board (generate-board)
   :state [:next-player 0]})

(defn turn
  "Receives a player's turn as a positional vector, checks for errors and winning"
  [{:keys [state board] :as game} pos]
  (cond
    (not (every? #(<= 0 % 2) pos)) (assoc game
                                          :error
                                          (str pos " is not a valid position"))
    (some? (get-in board pos)) (assoc game
                                      :error
                                      (str pos " is already taken"))
    :else (if (= :next-player (first state))
            (let [current-player (second state)]
              (-> game
                  (update :board #(assoc-in % pos current-player))
                  (update-state)))
            game)))


(comment
  (initial-game)
  (-> (initial-game)
      (turn [0 0])
      (turn [1 1]))
  (clojure.pprint/pprint
   (reductions turn
               (initial-game)
               [[0 0] [1 1] [1 0] [2 0]
                [0 2] [0 1] [2 1] [1 2] [2 2]]))
  (clojure.pprint/pprint
   (reductions turn
               (initial-game)
               [[1 1] [0 0] [1 2] [0 1] [1 0]
                [2 2] [2 1]]))
  (-> (initial-game)
      (turn [1 1])
      (turn [1 1]))
  (-> (initial-game)
      (turn [4 4])))