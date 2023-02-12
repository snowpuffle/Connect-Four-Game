package Model;

/* ~ Disc Class ~ */
public class Disc {
    private Player player;

    // Class Constructor
    public Disc(Player player) {
        this.player = player;
    }

    /* ~ Getter & Setter Methods ~ */
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
