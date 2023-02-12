package Model;

/* ~ Game Board Class ~ */
public class GameBoard {

    // Game Board is an Array of Game Cells
    GameCell[][] gameBoard;
    boolean turn;

    // Size of the GameBoard
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

    /* ~ Getter Methods ~ */
    public GameCell[][] getGameBoard() {
        return gameBoard;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }
}