package Models.Cases;

/**
 * The type Case mur.
 */
public class CaseMur extends Case{

    private int durabilite = 100;

    /**
     * Instantiates a new Case mur.
     *
     * @param X the x
     * @param Y the y
     */
    public CaseMur(int X, int Y) {
        super(X, Y);
    }

    /**
     * Instantiates a new Case mur.
     *
     * @param original the original
     */
    public CaseMur(CaseMur original){
        super(original);
        this.durabilite = original.durabilite;
    }

    @Override
    public void free() {}

    @Override
    public boolean isFree() {
        return false;
    }

    /**
     * Damage int.
     *
     * @param damages the damages
     * @return the int
     */
    public int damage(int damages){
        durabilite -= damages;
        return Math.max(durabilite, 0);
    }

    public CaseMur copy(){
        return new CaseMur(this);
    }

    @Override
    public String toString() {
        return "M";
    }
}
