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

    // Create New Players and Game
    public Game makeGame() {
        
        // Introduction
        System.out.println("Welcome to Connect Four!");
        System.out.println("************************************************************************");

        // Get the Player's Names
        System.out.print("Name of Player One: ");
        String playerOneName = scanner.nextLine();
        System.out.print("Name of Player Two: ");
        String playerTwoName = scanner.nextLine();

        // Create the Players
        Player playerOne = new Player(playerOneName, PlayerType.PLAYER_ONE);
        Player playerTwo = new Player(playerTwoName, PlayerType.PLAYER_TWO);

        // Create the Game
        Game game = new Game(playerOne, playerTwo);

        return game;
    }

    // Print & Format the Game Board
    public void printGameBoard(Game game) {
        System.out.println("************************************************************************");
        for (int i = 0; i < game.getNumberOfRows(); i++) {
            if (i == 0) {
                System.out.print("    ");
            } else {
                System.out.print(i + "   ");
            }

        }
        System.out.println();
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
        System.out.println("************************************************************************");
        System.out.print("It is " + playerName + "'s Turn. Write Column Number: ");
        String input = scanner.nextLine();

        // Validate Input from the Turn Player
        int columnToInsert;
        try {
            columnToInsert = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid Input.");
        }

        // Validate Input is 1 >= X >= 5
        if (columnToInsert >= 1 && columnToInsert <= 5) {
            return columnToInsert;
        } else {
            return -1;
        }
    }

}
