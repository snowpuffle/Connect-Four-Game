package Controller;

import Exceptions.ColumnFullException;
import Exceptions.InvalidColumnException;
import Model.Game;
import View.GameView;

public class GameLauncher {
    GameView gameView;
    Game game;

    public GameLauncher() {
        gameView = new GameView();
        game = gameView.makeGame();

        boolean isEndGame = false;

        while (true) {
            if (getGame().isEndGame()) {
                isEndGame = true;
                break;
            }

            gameView.printGameBoard(getGame());

            int columnToInsert = -1;

            // Try to Convert Column to Integer
            try {
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
