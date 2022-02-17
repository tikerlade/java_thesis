package problems;

import exceptions.InputException;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * problems.Literal is one part of formula in CNF: either variable or it's negation.
 */
public class Literal implements Cloneable{
    String name;
    Integer index;
    Boolean negation = false;
    Boolean value = null;
    Boolean isDummy = false;

    // TODO regex - variable name cannot start with 0-9
    String variableRegex = "[a-zA-Z0-9]+";

    HashMap<BooleanOperation, Character> allowedOperations = new HashMap<>() {{
        put(BooleanOperation.NEGATION, '-');
        put(BooleanOperation.AND, '&');
        put(BooleanOperation.OR, '|');
    }};

    public Literal (String literalString) throws Exception {
        // Remove leading and ending spaces
        literalString = literalString.strip();

        // Empty strings are not valid
        if (literalString.length() < 1) {
            // TODO exception texts to store in one place.
            throw new InputException("problems.Literal must consist at least of one character.");
        }

        // Check negation usage
        if (literalString.charAt(0) == allowedOperations.get(BooleanOperation.NEGATION)) {
            negation = true;
            literalString = literalString.substring(1);
        }

        // From now and until the end - only allowed chars can be used
        if (!Pattern.matches(variableRegex, literalString)) {
            for (Character ch : literalString.toCharArray()) {
                if (!Pattern.matches(variableRegex, ch.toString())) {
                    throw new InputException(String.format("Not allowed character: '%s' was used.", ch));
                }
            }
        }

        name = literalString;
    }

    public Literal (String literalString, Integer index) throws Exception {
        this(literalString);
        this.index = index;
    }

    public Boolean getValue() {
        // TODO think about exception: Value has not been set
        if (this.value != null) {
            return this.value;
        }

        return false;
    }

    public void setValue(Boolean value) {
        this.value = value;
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

    public void setIsDummy(Boolean isDummy) { this.isDummy = isDummy;}

    public String getName() {
        return name;
    }

    public Integer getIndex() {
        return index;
    }

    public Boolean getNegation() {
        return negation;
    }

    public Boolean getIsDummy() {return this.isDummy;}


    @Override
    public String toString() {
        String representation = name;

        if (negation) {
            representation = "-" + representation;
        }

        return representation;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static String getOppositeLiteralName(String literalName) {
        if (literalName.startsWith("-")) {
            return literalName.substring(1);
        }
        return '-' + literalName;
    }
}
