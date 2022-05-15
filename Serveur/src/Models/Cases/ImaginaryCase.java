package Models.Cases;

/**
 * The type Imaginary case.
 */
public class ImaginaryCase extends Case{
    /**
     * Instantiates a new Imaginary case.
     */
    public ImaginaryCase() {
        super(-1, -1);
    }

    @Override
    public Case copy() {
        return new ImaginaryCase();
    }

    @Override
    public boolean isFree() {
        return false;
    }
}
