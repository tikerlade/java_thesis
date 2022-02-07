/**
 * Literal is one part of formula in CNF: either variable or it's negation.
 */
public class Literal {
    String name;
    Integer index;
    Boolean negation;

    /**
     * Literal is one part of formula in CNF: either variable or it's negation.
     * @param name characters used in formula to represent variable
     * @param index place in formula where literal is located
     */
    public Literal(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public Literal() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setNegation(Boolean negation) {
        this.negation = negation;
    }

    public String getName() {
        return name;
    }

    public Integer getIndex() {
        return index;
    }

    public Boolean getNegation() {
        return negation;
    }

    @Override
    public String toString() {
        String representation = name;

        if (negation) {
            representation = "-" + representation;
        }

        return representation;
    }
}
