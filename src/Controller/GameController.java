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
    int numberOfDiscsPlaced;

    // Class Constructor
    public GameController(GameBoard gameBoard, GameView gameView) {
        this.winPlayer = null;
        this.gameBoard = gameBoard;
        this.gameView = gameView;
        this.numberOfDiscsPlaced = 0;
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
        boolean isEndGame = false;

        // Loop until Game is Over
        while (true) {
            // Is the Game Over?
            if (isEndGame()) {
                isEndGame = true;
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
            if (isCheckerPlaced && winPlayer == null) {
                numberOfDiscsPlaced++;
                endTurn();
            } else if (isCheckerPlaced && winPlayer != null) {
                break;
            }
        }

        // Display End of Game
        if (isEndGame) {
            gameView.displayGameResult("It's a Tie!");
        } else {
            gameView.printGameBoard(gameBoard.getGameBoard());
            gameView.displayGameResult(winPlayer.getPlayerName() + " Has Won!");
        }

    }

    /* ~ Game Play Helper Methods ~ */
    // Play Turn
    public boolean playTurn(Player turnPlayer) {

        // Display Play Turn & Get Position
        int columnToInsert = gameView.displayGetPlayTurn(turnPlayer);

        // Check if the Position is Valid
        boolean isValidColumn = checkValidColumn(columnToInsert);

        // Set Return Flag
        boolean isDiscPlaced = false;

        // Continue if the Position is Valid
        if (!isValidColumn) {
            // Send Error Response to Game View
            gameView.displayPlayTurnResponse("Input Position is Invalid.");
            isDiscPlaced = false;
        } else {
            // Create Checker based on Player Type
            Disc checker = createDisc(turnPlayer);

            // Place the Checker onto the GameBoard
            int[] discPosition = insertDisc(checker, columnToInsert);

            // Continue only if Checker is Placed
            if (discPosition[0] == -1 || discPosition[1] == -1) {
                gameView.displayPlayTurnResponse("Disc Cannot be Placed. Try Again!");
                isDiscPlaced = false;
            } else {
                // Set Disc is Placed
                isDiscPlaced = true;

                // Check if the Placed Checker is the Winning Move
                boolean isWinningMove = checkForWin(discPosition[0],
                        discPosition[1], turnPlayer);

                System.out.print(isWinningMove);

                // Set Winning Player
                if (isWinningMove) {
                    winPlayer = turnPlayer;

                } else {
                    winPlayer = null;
                }
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

        // Calculate the Maximum number of Checkers possible for the Game Board
        int maxNumOfDiscs = gameBoard.getNumberOfRows() * gameBoard.getNumberOfColumns();

        // End the Game if Game Board is Full
        if (numberOfDiscsPlaced >= maxNumOfDiscs) {
            return true;
        } else {
            return false;
        }
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

    // Insert Disc and Return its Position
    public int[] insertDisc(Disc disc, int columnNumber) {

        // Initialize Position of Disc
        int[] discPosition = new int[] { -1, -1 };

        // Continue only if Column to Insert is NOT Full
        if (gameBoard.getGameBoard()[0][columnNumber].getDisc() != null) {
            discPosition[0] = -1;
            discPosition[1] = -1;
        } else {
            // Loop through every Row in the Column
            for (int i = 0; i < numberOfRows - 1; i++) {

                // Get Game Cell based on the Position
                GameCell gameCell = gameBoard.getGameBoard()[i][columnNumber];
                GameCell gameCellBelow = gameBoard.getGameBoard()[i + 1][columnNumber];

                // Check a Disc Exists Below
                if (gameCellBelow.getDisc() != null && gameCell.getDisc() == null) {

                    // Insert the Disc
                    gameCell.setDisc(disc);

                    // Save Position of the Disc
                    discPosition[0] = i;
                    discPosition[1] = columnNumber;
                }
            }

            // The Entire Column is Empty - No Discs Exist Yet
            GameCell gameCell = gameBoard.getGameBoard()[numberOfRows - 1][columnNumber];
            if (gameCell.getDisc() == null) {

                // Insert the Disc
                gameCell.setDisc(disc);

                // Save Position of the Disc
                discPosition[0] = numberOfRows - 1;
                discPosition[1] = columnNumber;
            }
        }
        return discPosition;
    }

    /* Check for Win Methods */
    // Check for the Winning Move
    public boolean checkForWin(int rowNumber, int columnNumber, Player turnPlayer) {
        // True if a Win is Detected
        boolean win = false;

        // Check for a Win in 7 Different Directions
        if (checkWinTopDown(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinRightLeft(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinLeftRight(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinTopLeftBottomRight(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinBottomLeftTopRight(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinTopRightBottomLeft(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinBottomRightTopLeft(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        }

        return win;
    }

    // Check for Win from Top to Bottom Direction
    public boolean checkWinTopDown(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 1;

        // Top to Bottom: Increment Row #, Same Column #
        for (int i = 0; i < numberOfRows; ++i) {

            GameCell gameCell = gameBoard.getGameBoard()[i][columnNumber];

            // Continue only if Disc is NOT Null
            if (gameCell.getDisc() != null) {

                // Increment Count if the Disc Belongs to the Same Player
                if (gameCell.getDisc().getPlayer() == turnPlayer) {

                    // Check Player's Disc Count
                    if (count == 4) {
                        return true;
                    }
                    count++;
                } else {
                    break;
                }
            }
        }

        return false;
    }

    // Check for Win from Right to Left Direction
    public boolean checkWinRightLeft(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Right to Left: Same Row #, Decrement Column #
        for (int i = numberOfColumns - 1; i >= 0; i--) {
            GameCell gameCell = gameBoard.getGameBoard()[rowNumber][i];

            // Check if Game Cell is NULL at the Last Column
            if (i == numberOfColumns - 1 && gameCell.getDisc() == null) {
                // System.out.println(" LAST COLUMN, COUNT = 0");
                count = 0;
            } else {
                // Break if there is a Space Between the Discs
                // Continue only if Disc is NOT Null
                if (gameCell.getDisc() == null) {
                    break;
                } else {
                    // Increment Count if the Disc Belongs to the Same Player
                    if (gameCell.getDisc().getPlayer() == turnPlayer) {
                        count++;
                        if (count == 4) {
                            return true;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return false;
    }

    // Check for Win from Left to Right Direction
    public boolean checkWinLeftRight(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Left to Right: Same Row #, Increment Column #
        for (int i = 0; i < numberOfColumns; i++) {
            GameCell gameCell = gameBoard.getGameBoard()[rowNumber][i];

            // Check if Game Cell is NULL at the First Column
            if (i == 0 && gameCell.getDisc() == null) {
                // System.out.println(" FIRST COLUMN, BREAK");
                count = 0;
            } else {
                // Break if there is a Space Between the Discs
                // Continue only if Disc is NOT Null
                if (gameCell.getDisc() == null) {
                    break;
                } else {
                    // Increment Count if the Disc Belongs to the Same Player
                    if (gameCell.getDisc().getPlayer() == turnPlayer) {
                        count++;
                        if (count == 4) {
                            return true;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return false;
    }

    // Check for Win from Top-Left to Bottom-Right Direction
    public boolean checkWinTopLeftBottomRight(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Top-Left to Bottom-Right: Increment Row #, Increment Column #
        for (int i = rowNumber, j = columnNumber; i >= 0 && i < numberOfRows && j >= 0
                && j < numberOfColumns; i++, j++) {

            GameCell gameCell = gameBoard.getGameBoard()[i][j];

            // Break if there is a Space Between the Discs
            // Continue only if Disc is NOT Null
            if (gameCell.getDisc() == null) {
                break;
            } else {
                // Increment Count if the Checker Belongs to the Same Player
                if (gameCell.getDisc().getPlayer() == turnPlayer) {
                    count++;
                    if (count == 4) {
                        return true;
                    }
                } else {
                    break;
                }
            }
        }
        return false;
    }

    // Check for Win Top-Right to Bottom-Left Direction
    public boolean checkWinTopRightBottomLeft(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Top-Right to Bottom-Left: Increment Row #, Decrement Column #
        for (int i = rowNumber, j = columnNumber; i >= 0 && i < numberOfRows && j >= 0
                && j < numberOfColumns; i++, j--) {

            GameCell gameCell = gameBoard.getGameBoard()[i][j];

            // Break if there is a Space Between the Discs
            // Continue only if Disc is NOT Null
            if (gameCell.getDisc() == null) {
                break;
            } else {
                // Increment Count if the Checker Belongs to the Same Player
                if (gameCell.getDisc().getPlayer() == turnPlayer) {
                    count++;
                    if (count == 4) {
                        return true;
                    }
                } else {
                    break;
                }
            }
        }
        return false;
    }

    // Check for Win Bottom-Left to Top-Right Direction
    public boolean checkWinBottomLeftTopRight(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Bottom-Left to Top-Right: Decrement Row #, Increment Column #
        for (int i = rowNumber, j = columnNumber; i >= 0 && i < numberOfRows && j >= 0
                && j < numberOfColumns; i--, j++) {

            GameCell gameCell = gameBoard.getGameBoard()[i][j];

            // Break if there is a Space Between the Discs
            // Continue only if Disc is NOT Null
            if (gameCell.getDisc() == null) {
                break;
            } else {
                // Increment Count if the Checker Belongs to the Same Player
                if (gameCell.getDisc().getPlayer() == turnPlayer) {
                    count++;
                    if (count == 4) {
                        return true;
                    }
                } else {
                    break;
                }
            }
        }
        return false;
    }

    // Check for Win Bottom Right to Top Left Direction
    public boolean checkWinBottomRightTopLeft(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Bottom Right to Top Left: Decrement Row #, Decrement Column #
        for (int i = rowNumber, j = columnNumber; i >= 0 && i < numberOfRows && j >= 0
                && j < numberOfColumns; i--, j--) {

            GameCell gameCell = gameBoard.getGameBoard()[i][j];

            // Break if there is a Space Between the Discs
            // Continue only if Disc is NOT Null
            if (gameCell.getDisc() == null) {
                break;
            } else {
                // Increment Count if the Checker Belongs to the Same Player
                if (gameCell.getDisc().getPlayer() == turnPlayer) {
                    count++;
                    if (count == 4) {
                        return true;
                    }
                } else {
                    break;
                }
            }
        }
        return false;
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
