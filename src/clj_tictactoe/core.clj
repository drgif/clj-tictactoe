(ns clj-tictactoe.core
  (:require [clj-tictactoe.game :as g]
            [clojure.string :as str])
  (:gen-class))

(defn- row-string
  "Formats the string representation of a single row"
  [row]
  (str " " (str/join " | " (map #(if (nil? %)
                                   " "
                                   (g/players %))
                                row))))

(defn- board-string
  "Formats the string representing the entire board"
  [board]
  (str/join "\n-----------\n" (map row-string board)))

(defn- parse
  "Parses user input into a vector"
  [s]
  (let [error-tuple [:parse-err (str s " is not a valid input")]]
    (try
      (let [parsed (map #(Integer/parseInt %) (str/split s #" "))]
        (if (and (= 2 (count parsed))
                 (every? int? parsed)
                 (every? #(<= 0 % 2) parsed))
          parsed
          error-tuple))
      (catch Exception e
        error-tuple))))

(defn -main
  "Play a game on the commandline"
  []
  (println "Let's play a game!")
  (loop [game (g/initial-game)]
    (println)
    (println (board-string (:board game)))
    (println)
    (case (first (:state game))
      :next-player (do (println (str "Player "
                                     (g/players (second (:state game)))
                                     ", make your move!"))
                       (let [user-input (parse (read-line))]
                         (if (= :parse-err (first user-input))
                           (do (println "Error:" (second user-input))
                               (recur game))
                           (recur (g/turn game user-input)))))
      :draw (println "It's a draw!")
      :win (println "Player " (g/players (second (:state game))) " wins!"))))

(comment
  (-main)
  (row-string [0 1 nil])
  (println (board-string [[0 1 nil] [nil 1 0] [0 nil 1]])))