package Model;

/* ~ Player Class ~ */
public class Player {
    private PlayerType playerType;
    private String playerName;

    // Class Constructor
    public Player(String playerName, PlayerType playerType) {
        this.playerName = playerName;
        this.playerType = playerType;
    }

    /* ~ Getter & Setter Methods ~ */
    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
