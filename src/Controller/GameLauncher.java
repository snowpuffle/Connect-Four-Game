package Controller;

import Exceptions.ColumnFullException;
import Exceptions.InvalidColumnException;
import Model.Game;
import View.GameView;

public class GameLauncher {

    // Define Class Variables
    GameView gameView;
    Game game;

    // Class Constructor
    public GameLauncher() {

        // Create Game View & Make New Game
        gameView = new GameView();
        game = gameView.makeGame();

        boolean isEndGame = false;

        // Loop until the Game is Over or Encounters an Error
        while (true) {
            // Is the Game Over?
            if (getGame().isEndGame()) {
                isEndGame = true;
                break;
            }

            // Print the Game Board
            gameView.printGameBoard(getGame());

            // Try to Convert Column Input to an Integer
            int columnToInsert = -1;
            try {
                columnToInsert = gameView.playTurn(game.getTurnPlayer().getPlayerName());
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid Number Format!");
            }

            // Try to Insert the Disc
            try {
                if (getGame().insertDisc(columnToInsert)) {
                    break;
                }
                // End the Player's Turn if Disc Insertion is Successful
                getGame().endTurn();
            } catch (ColumnFullException e1) {
                System.out.println("ERROR: Column is Full!");
            } catch (InvalidColumnException e2) {
                System.out.println("ERROR: Invalid Column!");
            }
        }

        // End of Game
        if (isEndGame) {
            System.out.println("Game Ended with No Winner!");
        } else {
            gameView.printGameBoard(getGame());
            System.out.println("************************************************************************");
            System.out.println(getGame().getTurnPlayer().getPlayerName() + " Has Won!");
            System.out.println("************************************************************************");
        }
    }

    // Getter Method
    public Game getGame() {
        return game;
    }

    // Setter Method
    public void setGame(Game game) {
        this.game = game;
    }

    public static void main(String[] args) {
        new GameLauncher();
    }
}
