package Model;

/* ~ Game Cell Class: A Unit Cell on GameBoard ~ */
public class GameCell {
    private Disc disc;

    // Class Constructor
    public GameCell() {
        this.disc = null;
    }

    /* ~ Getter & Setter Methods ~ */
    public Disc getDisc() {
        return disc;
    }

    public void setDisc(Disc disc) {
        this.disc = disc;
    }
}
