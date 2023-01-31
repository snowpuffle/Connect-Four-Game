package View;

import java.util.Scanner;
import java.util.StringJoiner;

import Model.Disc;
import Model.Game;
import Model.Player;
import Model.PlayerType;

public class GameView {

    Scanner scanner;

    // Class Constructor
    public GameView() {
        scanner = new Scanner(System.in);
    }

    // Create New Game
    public Game makeGame() {
        System.out.println("Welcome to Connect Four!");
        System.out.println("************************************************************************");

        System.out.print("Name of Player One: ");
        String playerOneName = scanner.nextLine();

        System.out.print("Name of Player Two: ");
        String playerTwoName = scanner.nextLine();

        Player playerOne = new Player(playerOneName.toUpperCase(), PlayerType.PLAYER_ONE);
        Player playerTwo = new Player(playerTwoName.toUpperCase(), PlayerType.PLAYER_TWO);

        Game game = new Game(playerOne, playerTwo);

        return game;
    }

    // Print & Format the Game Board
    public void printGameBoard(Game game) {
        System.out.println("************************************************************");
        for (Disc[] row : game.getBoard()) {
            StringJoiner sj = new StringJoiner(" | ");
            for (Disc col : row) {
                if (col == null) {
                    sj.add(" ");
                } else {
                    sj.add(col.toString());
                }
            }
            System.out.println(sj.toString());
        }
    }

    // Get & Validate Input from the Turn Player
    public int playTurn(String playerName) throws NumberFormatException {

        // Get Input from the Turn Player
        System.out.print("It is " + playerName + "'s Turn. Write Column Number: ");
        String input = scanner.nextLine();

        // Validate Input from the Turn Player
        int columnToInsert;
        try {
            columnToInsert = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid Input.");
        }
        return columnToInsert;
    }

}
