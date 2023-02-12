import Controller.GameController;
import Model.GameBoard;
import View.GameView;

public class DriverMain {
    public static void main(String[] args) {
        // Create & Initialize Game Board
        GameBoard gameBoard = new GameBoard();

        // Create & Initialize Controller & View
        GameView gameView = new GameView();
        GameController gameController = new GameController(gameBoard, gameView);

        // 1. Prepare the Game
        gameController.prepareGame();

        // 2. Play the Game
        gameController.playGame();

    }
}
