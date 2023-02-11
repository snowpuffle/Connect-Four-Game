package Model;

public class GameBoard {

    // Define Class Variables
    GameCell[][] gameBoard;
    boolean turn;

    private static final int numberOfRows = 6;
    private static final int numberOfColumns = 7;

    // Class Constructor
    public GameBoard() {

        // Create 6 x 7 Array of Game Cells
        this.gameBoard = new GameCell[numberOfRows][numberOfColumns];

        // Initialize an Empty Game Board with Game Cells
        for (int i = 0; i < numberOfRows; ++i) {
            for (int j = 0; j < numberOfColumns; ++j) {
                gameBoard[i][j] = new GameCell();
            }
        }
    }

    // Get the Game Board
    public GameCell[][] getGameBoard() {
        return gameBoard;
    }

    // Get the Number of Rows
    public int getNumberOfRows() {
        return numberOfRows;
    }

    // Get the Number of Columns
    public int getNumberOfColumns() {
        return numberOfColumns;
    }
}