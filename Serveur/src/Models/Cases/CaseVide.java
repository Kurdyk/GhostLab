package Models.Cases;

/**
 * The type Case vide.
 */
public class CaseVide extends Case{
    /**
     * Instantiates a new Case vide.
     *
     * @param X the x
     * @param Y the y
     */
    public CaseVide(int X, int Y) {
        super(X, Y);
    }

    /**
     * Instantiates a new Case vide.
     *
     * @param original the original
     */
    public CaseVide(CaseVide original){
        super(original);
    }

    /**
     *
     * @return this' copy
     */
    public CaseVide copy(){
        return new CaseVide(this);
    }

    @Override
    public boolean isFree() {
        return true;
    }

    /**
     * significate case's type
     * @return " "
     */
    @Override
    public String toString() {
        return " ";
    }
}
