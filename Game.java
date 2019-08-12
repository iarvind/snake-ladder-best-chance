package games;

public class Game {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            SnakeAndLadder snakeAndLadder = new SnakeAndLadder(2, 10000, false);
            int[] result = snakeAndLadder.execute();
            System.out.println(result[0] > result[1]);
        }
    }

}
