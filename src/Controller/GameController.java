package Controller;

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

    /* ~ Main Game Play Methods ~ */
    // 1. Prepare the Game: Introduction & Set Up  Players
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

    // 2. Play the Game: Loop Until Game Ends
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

            // Break and end Game if there is a Winner
            if (isCheckerPlaced && winPlayer == null) {

                // Increment number of Discs and end Turn if there is No Winner
                numberOfDiscsPlaced++;
                endTurn();

            } else if (isCheckerPlaced && winPlayer != null) {

                // Break if there is a Winner
                break;
            }
        }

        // Display End of Game
        if (isEndGame) {

            // Game ends with a No Winners
            gameView.displayGameResult("It's a Tie!");

        } else {

            // Game ends with a Winner
            gameView.printGameBoard(gameBoard.getGameBoard());
            gameView.displayGameResult(winPlayer.getPlayerName() + " Has Won!");
        }

    }

    /* ~ Game Play Helper Methods ~ */
    // Play Turn: Insert Disc & Check for a Win
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

            // Create Disc based on Player Type
            Disc disc = createDisc(turnPlayer);

            // Place the Disc onto the GameBoard
            int[] discPosition = insertDisc(disc, columnToInsert);

            // Continue only if Disc is Placed
            if (discPosition[0] == -1 || discPosition[1] == -1) {
                
                // Send Error Response to Game View
                gameView.displayPlayTurnResponse("Disc Cannot be Placed. Try Again!");
                isDiscPlaced = false;

            } else {
                // Set Disc is Placed
                isDiscPlaced = true;

                // Check if the Placed Disc is the Winning Move
                if (checkForWin(discPosition[0],
                        discPosition[1], turnPlayer)) {

                    // Set Winning PlayerF
                    winPlayer = turnPlayer;

                } else {
                    winPlayer = null;
                }
            }
        }
        return isDiscPlaced;
    }

    // Get Turn Player: Return "Who's Turn is it?"
    public Player getTurnPlayer() {
        if (turn) {
            return playerTwo;
        } else {
            return playerOne;
        }
    }

    // End Turn for the Player
    public void endTurn() {
        turn = !turn;
    }

    // End the Game: Return "Is the Game Board Full?""
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

    /* ~ Disc Management Methods ~ */
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

    /* ~ Check for Win Methods ~ */
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
        } else if (checkWinTopLeftBottomRightA(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinTopLeftBottomRightB(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinBottomLeftTopRightA(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinBottomLeftTopRightB(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        }

        return win;
    }

    // Check for Win: Top to Bottom Direction
    public boolean checkWinTopDown(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Loop through every Row
        for (int i = 0; i < numberOfRows; i++) {

            // Get the Game Cells in every Row in the Column
            GameCell gameCell = gameBoard.getGameBoard()[i][columnNumber];

            // Continue only if Disc is NOT Null
            if (gameCell.getDisc() != null) {

                // Increment Count if the Disc Belongs to the Same Player
                if (gameCell.getDisc().getPlayer() == turnPlayer) {

                    // Check Player's Disc Count
                    if (count == 4) {
                        System.out.println("Win Vertical!");
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

    // Check for Win: Right to Left Direction
    public boolean checkWinRightLeft(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Loop through every Column from Right to Left, starting from the Last Column
        for (int i = numberOfColumns - 1; i >= 0; i--) {

            // Get the Game Cells in every Column in the Insert Row
            GameCell gameCell = gameBoard.getGameBoard()[rowNumber][i];

            // Check if Game Cell is NULL at the Last Column
            if (i == numberOfColumns - 1 && gameCell.getDisc() == null) {
                // Reset Count
                count = 0;
            } else {
                // Continue only if Disc is NOT Null
                if (gameCell.getDisc() == null) {
                    // Break if there is a Space Between the Discs
                    break;
                } else {
                    // Increment Count if the Disc Belongs to the Same Player
                    if (gameCell.getDisc().getPlayer() == turnPlayer) {
                        count++;
                        if (count == 4) {
                            System.out.println("Win Horizontal RL!");
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

    // Check for Win: Left to Right Direction
    public boolean checkWinLeftRight(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Loop through every Column from Left to Right, starting from the First Column
        for (int i = 0; i < numberOfColumns; i++) {

            // Get the Game Cells in every Column in the Insert Row
            GameCell gameCell = gameBoard.getGameBoard()[rowNumber][i];

            // Check if Game Cell is NULL at the First Column
            if (i == 0 && gameCell.getDisc() == null) {
                // Reset Count
                count = 0;
            } else {
                // Continue only if Disc is NOT Null
                if (gameCell.getDisc() == null) {
                    // Break if there is a Space Between the Discs
                    break;
                } else {
                    // Increment Count if the Disc Belongs to the Same Player
                    if (gameCell.getDisc().getPlayer() == turnPlayer) {
                        count++;
                        if (count == 4) {
                            System.out.println("Win Horizontal LR!");
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

    // Check for Win: Top-Left to Bottom-Right (including Middle)
    public boolean checkWinTopLeftBottomRightA(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Loop through the Rows
        for (int row = 0; row <= numberOfRows - 4; row++) {

            // Set Row Position
            int rowPosition = row;

            // Loop through the Columns
            for (int column = 0; column < numberOfColumns && rowPosition < numberOfRows; column++) {

                // Get the Game Cell in the Diagonal line
                GameCell gameCell = gameBoard.getGameBoard()[rowPosition][column];

                // Reset Count if Disc is NULL
                if (gameCell.getDisc() == null) {
                    count = 0;
                } else {
                    // Increment Count if the Checker Belongs to the Same Player
                    if (gameCell.getDisc().getPlayer() == turnPlayer) {
                        count++;
                    } else {
                        count = 0;
                    }
                }

                // Check if Count reaches Win Number
                if (count == 4) {
                    System.out.println("Win LR Diagonal Include Middle!");
                    return true;
                }
                rowPosition++;
            }

        }
        return false;
    }

    // Check for Win: Top Left to Bottom Right (After Middle)
    public boolean checkWinTopLeftBottomRightB(int rowNumber, int columnNumber, Player turnPlayer) {

        // Player Wins if Count = 4
        int count = 0;

        // Loop through the Columns
        for (int column = 1; column <= numberOfColumns - 4; column++) {

            // Set Column Position
            int columnPosition = column;

            // Loop through the Rows
            for (int row = 0; row < numberOfRows && columnPosition < numberOfColumns; row++) {

                // Get the Game Cell in the Diagonal line
                GameCell gameCell = gameBoard.getGameBoard()[row][columnPosition];

                // Reset Count if Disc is NULL
                if (gameCell.getDisc() == null) {
                    count = 0;
                } else {
                    // Increment Count if the Disc Belongs to the Same Player
                    if (gameCell.getDisc().getPlayer() == turnPlayer) {
                        count++;
                    } else {
                        count = 0;
                    }
                }

                // Check if Count reaches Win Number
                if (count == 4) {
                    System.out.println("Win LR Diagonal After Middle!");
                    return true;
                }
                columnPosition++;
            }
        }
        return false;
    }

    // Check for Win: Bottom-Left to Top-Right (including Middle)
    public boolean checkWinBottomLeftTopRightA(int rowNumber, int columnNumber, Player turnPlayer) {

        // Player Wins if Count = 4
        int count = 0;

        // Loop through the Rows
        for (int row = numberOfRows - 1; row >= numberOfRows - 4; row--) {

            // Set Row Position
            int rowPosition = row;

            // Loop through the Columns
            for (int column = 0; column < numberOfColumns && rowPosition < numberOfRows && rowPosition >= 0; column++) {

                // Get the Game Cell in the Diagonal line
                GameCell gameCell = gameBoard.getGameBoard()[rowPosition][column];

                // Reset Count if Disc is NULL
                if (gameCell.getDisc() == null) {
                    count = 0;
                } else {
                    // Increment Count if the Disc Belongs to the Same Player
                    if (gameCell.getDisc().getPlayer() == turnPlayer) {
                        count++;
                    } else {
                        count = 0;
                    }
                }

                // Check if Count reaches Win Number
                if (count == 4) {
                    return true;
                }
                rowPosition--;
            }
        }
        return false;
    }

    // Check for Win: Bottom-Left to Top-Right (After Middle)
    public boolean checkWinBottomLeftTopRightB(int rowNumber, int columnNumber, Player turnPlayer) {

        // Player Wins if Count = 4
        int count = 0;

        // Loop through the Rows
        for (int column = 1; column < numberOfColumns; column++) {

            // Set Row Position
            int columnPosition = column;

            // Loop through the Columns
            for (int row = numberOfRows - 1; row < numberOfRows && columnPosition < numberOfColumns
                    && columnPosition >= 1; row--) {

                // Get the Game Cell in the Diagonal line
                GameCell gameCell = gameBoard.getGameBoard()[row][columnPosition];

                // Reset Count if Disc is NULL
                if (gameCell.getDisc() == null) {
                    count = 0;
                } else {
                    // Increment Count if the Disc Belongs to the Same Player
                    if (gameCell.getDisc().getPlayer() == turnPlayer) {
                        count++;
                    } else {
                        count = 0;
                    }
                }

                // Check if Count reaches Win Number
                if (count == 4) {
                    return true;
                }
                columnPosition++;
            }
        }
        return false;
    }

    /* ~ Utility Methods ~ */
    // Validate the Input of Column to Insert
    public boolean checkValidColumn(int columnToInsert) {
        // Validate Column to Insert to Valid
        if (columnToInsert >= 0 && columnToInsert < numberOfColumns) {
            return true;
        } else {
            return false;
        }
    }

    // Randomize Player: "Who Goes First?"
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
