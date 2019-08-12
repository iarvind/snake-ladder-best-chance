package games;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SnakeAndLadder {
    private int playerCount;
    private int games;
    private boolean newBoardEveryTime;
    private int[] gamesWon;
    private Board currentBoard;
    private Random rand = new Random();

    public SnakeAndLadder(int playerCount, int games, boolean newBoardEveryTime) {
        this.playerCount = playerCount;
        this.games = games;
        this.newBoardEveryTime = newBoardEveryTime;
        this.gamesWon = new int[playerCount];
        for (int i = 0; i < playerCount; i++) {
            gamesWon[i] = 0;
        }
        this.currentBoard = null;
    }

    private int getRandom(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public int[] execute() {
        for (int i = 0; i < games; i++) {
            Board board = getBoard();
            int won = -1;
            ;
            while (won == -1) {
                won = board.run();
            }
            gamesWon[won]++;
        }

        return gamesWon;
    }

    private Board getBoard() {
        if (null == currentBoard || newBoardEveryTime) {
            currentBoard = new Board(playerCount);
        } else {
            currentBoard.reset();
        }
        return currentBoard;
    }

    public class Board {
        private Map<Integer, Integer> snakes = new HashMap<>();
        private Map<Integer, Integer> ladders = new HashMap<>();
        private Map<Integer, Integer> playerLocationMap = new HashMap<>();
        private int snakeCount = 10;
        private int ladderCount = 10;
        private int playerCount;
        private int playerTurn;

        public Board(int playerCount) {
            this.playerCount = playerCount;

            for (int i = 0; i < snakeCount; i++) {
                int[] snake = getSnake();
                if (!snakes.containsKey(snake[0])) {
                    snakes.put(snake[0], snake[1]);
                }
            }

            for (int i = 0; i < ladderCount; i++) {
                int[] ladder = getLadder();

                if (snakes.values().stream().anyMatch(v -> v.equals(ladder[0])) || snakes.containsKey(ladder[1])) {
                    continue;
                }
            }

            reset();
        }

        public int run() {
            int winner = -1;

            if (playerLocationMap.get(playerTurn) != 101) {
                int diceCount = getRandom(1, 6);
                int newLocation = playerLocationMap.get(playerTurn) + diceCount;
                if (newLocation == 101) {
                    winner = playerTurn;
                } else if (newLocation > 101) {
                    newLocation -= diceCount;
                }

                while (snakes.containsKey(newLocation)) {
                    newLocation = snakes.get(newLocation);
                }
                while (ladders.containsKey(newLocation)) {
                    newLocation = ladders.get(newLocation);
                }

//                System.out.println("Player: " + playerTurn +
//                        ", dice: " + diceCount +
//                        ", previousLocation: " + playerLocationMap.get(playerTurn) +
//                        ", newLocation: " + newLocation);
                playerLocationMap.put(playerTurn, newLocation);
            }

            playerTurn = (playerTurn + 1) % playerCount;
            return winner;
        }

        private void reset() {
            for (int i = 0; i < playerCount; i++) {
                playerLocationMap.put(i, 0);
            }
            playerTurn = 0;
        }

        private int[] getSnake() {
            int head = getRandom(11, 100);
            int tail = getRandom(1, (head - (head % 10)));
            int[] result = new int[2];
            result[0] = head;
            result[1] = tail;
            return result;
        }

        private int[] getLadder() {
            int[] snake = getSnake();
            int[] result = new int[2];
            result[0] = snake[1];
            result[1] = snake[0];
            return result;
        }
    }


}
