package Controller;

import Exceptions.ColumnFullException;
import Exceptions.InvalidColumnException;
import Model.Disc;
import Model.GameBoard;
import Model.GameCell;
import Model.Player;
import Model.PlayerOneDisc;
import Model.PlayerTwoDisc;
import Model.PlayerType;
import View.GameView;

/* Game Controller Class: Main Controller of Game */
public class GameController {

    GameView gameView;
    GameBoard gameBoard;

    Player playerOne;
    Player playerTwo;
    Player winPlayer;

    boolean turn;
    int numberOfRows;
    int numberOfColumns;

    // Class Constructor
    public GameController(GameBoard gameBoard, GameView gameView) {
        this.winPlayer = null;
        this.gameBoard = gameBoard;
        this.gameView = gameView;
        this.numberOfRows = gameBoard.getNumberOfRows();
        this.numberOfColumns = gameBoard.getNumberOfColumns();
    }

    // public GameController(GameBoard gameBoard, GameView gameView) {

    // // Create Game View & Make New Game
    // this.gameView = new GameView();
    // this.gameBoard = gameView.makeGame();

    // boolean isEndGame = false;

    // // Loop until the Game is Over or Encounters an Error
    // while (true) {
    // // Is the Game Over?
    // if (getGame().isEndGame()) {
    // isEndGame = true;
    // break;
    // }

    // // Print the Game Board
    // gameView.printGameBoard(getGame());

    // // Try to Convert Column Input to an Integer
    // int columnToInsert = -1;
    // try {
    // columnToInsert = gameView.playTurn(game.getTurnPlayer().getPlayerName());
    // } catch (NumberFormatException e) {
    // System.out.println("ERROR: Invalid Number Format!");
    // }

    // // Try to Insert the Disc
    // try {
    // if (getGame().insertDisc(columnToInsert)) {
    // break;
    // }
    // // End the Player's Turn if Disc Insertion is Successful
    // getGame().endTurn();
    // } catch (ColumnFullException e1) {
    // System.out.println("ERROR: Column is Full!");
    // } catch (InvalidColumnException e2) {
    // System.out.println("ERROR: Invalid Column!");
    // }
    // }

    // // End of Game
    // if (isEndGame) {
    // System.out.println("Game Ended with No Winner!");
    // } else {
    // gameView.printGameBoard(getGame());
    // System.out.println("************************************************************************");
    // System.out.println(getGame().getTurnPlayer().getPlayerName() + " Has Won!");
    // System.out.println("************************************************************************");
    // }
    // }

    /* ~ Main Game Play Methods ~ */
    // 1. Prepare the Game
    public void prepareGame() {

        // Display Game Introduction
        gameView.displayGameIntro();

        // Get Names of the Players
        String[] namesOfPlayers = gameView.displayGetPlayers();

        // Set the Players
        this.playerOne = new Player(namesOfPlayers[0], PlayerType.PLAYER_ONE);
        this.playerTwo = new Player(namesOfPlayers[1], PlayerType.PLAYER_TWO);

        // Randomize the Player's Turn
        randomizePlayers();
    }

    // 2. Play the Game
    public void playGame() {

        // Set Flag
        // boolean isEndGame = false;

        // Loop until Game is Over
        while (true) {
            // Is the Game Over?
            if (isEndGame()) {
                // isEndGame = true;
                break;
            }

            // Print the Game Board
            gameView.printGameBoard(gameBoard.getGameBoard());

            // Get Turn Player
            Player turnPlayer = getTurnPlayer();

            // Turn Player plays Turn
            boolean isCheckerPlaced = playTurn(turnPlayer);

            // Increment number of Checkers and end Turn if there is No Winner
            // Break and end Game if there is a Winner
            // if (isCheckerPlaced && winPlayer == null) {
            // // numberOfCheckersPlaced++;
            // endTurn();
            // } else if (isCheckerPlaced && winPlayer != null) {
            // break;
            // }

            // // Display End of Game
            // if (isEndGame) {
            // // gameView.displayGameResult("It's a Tie!");
            // } else {
            // // gameView.printGameBoard(gameBoard.getGameBoard());
            // // gameView.displayGameResult(winPlayer.getPlayerName() + " Has Won!");
            // }
        }
    }

    /* ~ Game Play Helper Methods ~ */
    // Play Turn
    public boolean playTurn(Player turnPlayer) {

        // Display Play Turn & Get Position
        int columnToInsert = gameView.displayGetPlayTurn(turnPlayer);

        // Check if the Position is Valid
        boolean isValidColumn = checkValidColumn(columnToInsert);

        // Set Flag
        boolean isDiscPlaced = false;

        // Continue if the Position is Valid
        if (!isValidColumn) {
            // Send Error Response to Game View
            gameView.displayPlayTurnResponse("Input Position is Invalid.");

        } else {
            // Create Checker based on Player Type
            Disc checker = createDisc(turnPlayer);

            // Place the Checker onto the GameBoard
            isDiscPlaced = insertDisc(checker, columnToInsert);

            // Continue only if Checker is Placed
            if (!isDiscPlaced) {
                gameView.displayPlayTurnResponse("Checker Cannot be Placed. Try Again!");
            } else {
                // Check if the Placed Checker is the Winning Move
                // boolean isWinningMove = checkForWin(positionOfChecker[0],
                // positionOfChecker[1], turnPlayer);

                // // Set Winning Player
                // if (isWinningChecker) {
                // winPlayer = turnPlayer;
                // } else {
                // winPlayer = null;
                // }
            }
        }
        return isDiscPlaced;
    }

    // Get Turn Player
    public Player getTurnPlayer() {
        if (turn) {
            return playerTwo;
        } else {
            return playerOne;
        }
    }

    // End of Turn for the Player
    public void endTurn() {
        turn = !turn;
    }

    // End the Game if All Columns are Full
    public boolean isEndGame() {

        // Loop through all Columns
        for (int i = 0; i < numberOfColumns; i++) {
            // Check for Discs on the Top Row
            if (gameBoard.getGameBoard()[0][i].getDisc() == null) {
                return false;
            }
        }
        // Game Ends when all Columns are Full
        return true;
    }

    /* ~ Disc Management Methods~ */
    // Create and Return Disc based on the Turn Player
    public Disc createDisc(Player turnPlayer) {

        // Create and Return Checker based on the Turn Player
        if (turnPlayer.getPlayerType() == PlayerType.PLAYER_ONE) {
            PlayerOneDisc playerOneChecker = new PlayerOneDisc(turnPlayer);
            return playerOneChecker;
        } else {
            PlayerTwoDisc playerTwoChecker = new PlayerTwoDisc(turnPlayer);
            return playerTwoChecker;
        }
    }

    // Insert Disc & Throw Exceptions if Necessary
    public boolean insertDisc(Disc disc, int columnNumber) {

        // Set Flag
        boolean isDiscPlaced = false;

        // Continue only if Column to Insert is NOT Full
        if (gameBoard.getGameBoard()[0][columnNumber].getDisc() != null) {
            isDiscPlaced = false;
        } else {
            // Loop through every Row in the Column
            for (int i = 0; i < numberOfRows - 1; i++) {

                // Get Game Cell based on the Position
                GameCell gameCell = gameBoard.getGameBoard()[i][columnNumber];
                GameCell gameCellBelow = gameBoard.getGameBoard()[i + 1][columnNumber];

                // Insert the Disc if a Disc Exists Below
                if (gameCellBelow.getDisc() != null && gameCell.getDisc() == null) {
                    gameCell.setDisc(disc);
                    // Check if Newly Inserted Disc Prompted a Win for the Player
                    // return checkWin(i, columnNumber, getTurnPlayer());
                    isDiscPlaced = true;
                }
            }

            // The Entire Column is Empty - No Discs Exist Yet
            // Insert the Disc & Check if Newly Inserted Disc Prompted a Win for the Player
            GameCell gameCell = gameBoard.getGameBoard()[numberOfRows - 1][columnNumber];
            if (gameCell.getDisc() == null) {
                gameCell.setDisc(disc);
                isDiscPlaced = true;
            }
        }

        // return checkWin(numberOfRows - 1, columnNumber, getTurnPlayer());
        return isDiscPlaced;
    }

    /* ~ Utility Methods */
    // Validate the Input of Column to Insert
    public boolean checkValidColumn(int columnToInsert) {
        // Validate Column to Insert to Valid
        if (columnToInsert >= 0 && columnToInsert < numberOfColumns) {
            return true;
        } else {
            return false;
        }
    }

    // Randomize "Who Goes First?"
    public void randomizePlayers() {
        // Use Math Random
        double randomNumber = Math.random();
        Player tempPlayer;

        // Assign Who Goes First Based on Random Number
        if (randomNumber >= 0 && randomNumber < 0.5) {
            tempPlayer = playerOne;
            playerOne = playerTwo;
            playerTwo = tempPlayer;
        }
    }

}
