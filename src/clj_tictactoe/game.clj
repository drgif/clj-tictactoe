(ns clj-tictactoe.game)

(def players ["X" "O"])

(defn- current-player
  "Returns the currently active player"
  [{:keys [state]}]
  (second state))

(defn- next-player
  "Returns the index of the next player"
  [game]
  (let [current-player (current-player game)]
    (if (contains? players (inc current-player))
      (inc current-player)
      0)))

(defn- winner
  "Returns the winning player, otherwise nil"
  [board]
  (let [rows board
        columns (apply mapv vector board)
        up-down-diagonal (map #(get-in board [% %]) (range 3))
        down-up-diagonal (map #(get-in board [(- 2 %) %]) (range 3))
        lines-to-test (conj (concat rows columns) up-down-diagonal down-up-diagonal)
        winning-rows (filter #(and (apply = %) (some? (first %))) lines-to-test)]
    (first (first winning-rows))))

(defn- board-full?
  "Checks whether the board is full and no more moves are possible"
  [board]
  (every? some? (flatten board)))

(defn- update-state
  "Updates the game's :state key"
  [{:keys [board] :as game}]
  (let [winner (winner board)]
    (assoc game :state (cond
                         winner [:win winner]
                         (board-full? board) [:draw]
                         :else [:next-player (next-player game)]))))

(defn- turn-error
  "Returns an error string if applicable, otherwise nil"
  [{:keys [state board]} pos]
  (cond
    (not (= :next-player (first state))) "Game is already finished"
    (not (every? #(<= 0 % 2) pos)) (str pos " is not a valid position")
    (some? (get-in board pos)) (str pos " is already taken")))

(defn- generate-board
  "Generates a game board pre-filled with nil"
  []
  (nth (iterate #(into [] (take 3 (repeat %))) nil) 2))

(defn initial-game
  "Returns the initial game"
  []
  {:board (generate-board)
   :state [:next-player 0]})

(defn turn
  "Executes the turn on game state if there are no errors"
  [game pos]
  (if-let [error (turn-error game pos)]
    (assoc game :error error)
    (-> game
        (dissoc :error)
        (update :board #(assoc-in % pos (current-player game)))
        (update-state))))
                  


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
  (clojure.pprint/pprint
   (reductions turn
               (initial-game)
               [[1 1] [-1 -1] [0 2] [0 0] [0 1] [0 0]
                [2 2] [2 1]]))
  (-> (initial-game)
      (turn [1 1])
      (turn [1 1]))
  (-> (initial-game)
      (turn [4 4]))
  (winner [[0 1 1] [nil 0 nil] [nil nil 0]])
  (board-full? [[0 nil nil] [nil nil nil] [nil nil nil]])
  (board-full? [[0 1 0] [0 1 1] [1 0 0]]))