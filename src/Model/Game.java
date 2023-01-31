package Model;

import Exceptions.ColumnFullException;
import Exceptions.InvalidColumnException;

public class Game {
    Disc[][] gameBoard;
    Player playerOne;
    Player playerTwo;
    final int numberOfRows = 6;
    final int numberOfColumns = 7;
    boolean turn;

    // Class Constructor
    public Game(Player playerOne, Player playerTwo) {

        // Create PlayerOne and PlayerTwo
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        // Randomize "Who Goes First?"
        randomizePlayers();

        // Create 6 x 7 Array of Discs
        gameBoard = new Disc[numberOfRows][numberOfColumns];
    }

    // End the Game if All Columns are Full
    public boolean isEndGame() {

        // Loop through all Columns
        for (int i = 0; i < numberOfColumns; i++) {

            // Check for Discs on the Top Row
            if (gameBoard[0][i] == null) {
                return false;
            }
        }
        return true; // Game Ends - All Columns are Full
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

    // End of Turn for the Player
    public void endTurn() {
        turn = !turn;
    }

    // Get Turn Player
    public Player getTurnPlayer() {
        if (turn) {
            return playerTwo;
        } else {
            return playerOne;
        }
    }

    // Get the Game Board
    public Disc[][] getBoard() {
        return gameBoard;
    }

    // Set the Game Board
    public void setBoard(Disc[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    // Insert Disc & Throw Exceptions if Necessary
    public boolean insertDisc(int columnNumber) throws ColumnFullException, InvalidColumnException {
        // Create New Disc Object Based on Turn Player
        Disc disc;     
        if (getTurnPlayer().getPlayerType() == PlayerType.PLAYER_ONE) {
            disc = new PlayerOneDisc(getTurnPlayer());
        } else {
            disc = new PlayerTwoDisc(getTurnPlayer());
        }

        // Throw an Exception if Column Input is Invalid
        if (columnNumber >= numberOfColumns || columnNumber < 0) {
            throw new InvalidColumnException();
        }

        // Throw an Exception if Column Input if Full
        if (gameBoard[0][columnNumber] != null) {
            throw new ColumnFullException();
        }

        // Insert Disc and Check for a Win
        for (int i = 0; i < numberOfRows - 1; i++) {

            // Insert Disc if a Disc Exists Below
            if (gameBoard[i + 1][columnNumber] != null) {
                gameBoard[i][columnNumber] = disc;

                // Check If Newly Inserted Disc Prompted a Win for the Player
                return checkWin(i, columnNumber, getTurnPlayer());
            }
        }

        // Insert Disc - an Entire Column is Empty, No Disc Exists Yet
        gameBoard[numberOfRows - 1][columnNumber] = disc;
        return checkWin(numberOfRows - 1, columnNumber, getTurnPlayer());

    }

    // Check if 4 Discs are Consecutively Aligned from 7 Directions
    public boolean checkWin(int rowNumber, int columnNumber, Player turnPlayer) {

        boolean win = false;

        // 7 Directions a Player Can Win
        if (checkWinDown(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinLeft(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinRight(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinDownRight(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinDownLeft(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinUpRight(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        } else if (checkWinUpLeft(rowNumber, columnNumber, turnPlayer)) {
            win = true;
        }

        return win;
    }

    // Check if 4 Discs are Consecutively Aligned from Top to Bottom Direction
    public boolean checkWinDown(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Top to Bottom: Increment Row #, Same Column #
        for (int i = rowNumber; i < numberOfRows; i++) {

            // Increment Count if Disc Belongs to the Same Player
            if (gameBoard[i][columnNumber].getPlayer() == turnPlayer) {
                count++;

                if (count == 4) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }

    // Check if 4 Discs are Consecutively Aligned from Right to Left Direction
    public boolean checkWinLeft(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Right to Left: Same Row #, Decrement Column #
        for (int i = columnNumber; i >= 0; i--) {
            if (gameBoard[rowNumber][i] == null) {
                break;
            }

            // Increment Count if Disc Belongs to the Same Player
            if (gameBoard[rowNumber][i].getPlayer() == turnPlayer) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }

    // Check if 4 Discs are Consecutively Aligned from Left to Right Direction
    public boolean checkWinRight(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Left to Right: Same Row #, Increment Column # < Max Columns
        for (int i = columnNumber; i < numberOfColumns; i++) {
            // Break if there is a Space Between the Discs
            if (gameBoard[rowNumber][i] == null) {
                break;
            }

            // Increment Count if Disc Belongs to the Same Player
            if (gameBoard[rowNumber][i].getPlayer() == turnPlayer) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }

    // Check if 4 Discs are Consecutively Aligned from Top to Bottom Right Direction
    public boolean checkWinDownRight(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Top to Bottom Right: Increment Row #, Increment Column #
        for (int i = rowNumber, j = columnNumber; i < numberOfRows && j < numberOfColumns; i++, j++) {
            if (gameBoard[i][j] == null) {
                break;
            }

            // Increment Count if Disc Belongs to the Same Player
            if (gameBoard[i][j].getPlayer() == turnPlayer) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }

    // Check if 4 Discs are Consecutively Aligned from Top to Bottom Left Direction
    public boolean checkWinDownLeft(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Top to Bottom Left: Increment Row #, Decrement Column #
        for (int i = rowNumber, j = columnNumber; i < numberOfRows && j >= 0; i++, j--) {
            if (gameBoard[i][j] == null) {
                break;
            }

            // Increment Count if Disc Belongs to the Same Player
            if (gameBoard[i][j].getPlayer() == turnPlayer) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }

    // Check if 4 Discs are Consecutively Aligned from Bottom to Top Right Direction
    public boolean checkWinUpRight(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Bottom to Top Right: Decrement Row #, Increment Column #
        for (int i = rowNumber, j = columnNumber; i >= 0 && j < numberOfColumns; i--, j++) {
            if (gameBoard[i][j] == null) {
                break;
            }

            // Increment Count if Disc Belongs to the Same Player
            if (gameBoard[i][j].getPlayer() == turnPlayer) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }

    // Check if 4 Discs are Consecutively Aligned from Bottom to Top Left Direction
    public boolean checkWinUpLeft(int rowNumber, int columnNumber, Player turnPlayer) {
        // Player Wins if Count = 4
        int count = 0;

        // Bottom to Top Right: Decrement Row #, Decrement Column #
        for (int i = rowNumber, j = columnNumber; i >= 0 && j >= 0; i--, j--) {
            if (gameBoard[i][j] == null) {
                break;
            }

            // Increment Count if Disc Belongs to the Same Player
            if (gameBoard[i][j].getPlayer() == turnPlayer) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }
}