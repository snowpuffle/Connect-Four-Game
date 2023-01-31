package Controller;

import Exceptions.ColumnFullException;
import Exceptions.InvalidColumnException;
import Model.Game;
import View.GameView;

public class GameLauncher {
    GameView gameView;
    Game game;

    // Class Constructor
    public GameLauncher() {
        gameView = new GameView();
        game = gameView.makeGame();

        boolean isEndGame = false;

        // Loop until Game is Over or Encounters an Error
        while (true) {
            // Is the Game Over?
            if (getGame().isEndGame()) {
                isEndGame = true;
                break;
            }

            // Print Game Board
            gameView.printGameBoard(getGame());

            // Try to Convert Column to Integer
            int columnToInsert = -1;
            try {
                //
                columnToInsert = gameView.playTurn(game.getTurnPlayer().getPlayerName());
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid Number Format!");
            }

            // Try to Insert Disc if Column Format is Valid
            try {
                if (getGame().insertDisc(columnToInsert)) {
                    break;
                }
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
            System.out.println(getGame().getTurnPlayer().getPlayerName() + " Won!");
            System.out.println("************************************************************************");
        }
    }

    public static void main(String[] args) {
        new GameLauncher();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}
