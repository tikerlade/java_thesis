package ru.spbstu.icst.problems;

import ru.spbstu.icst.exceptions.InputException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * problems.Literal is one part of formula in CNF: either variable or it's negation.
 */
public class Literal implements Cloneable{
    static int literalCounter = 0;

    String name;
    Integer index;
    Boolean negation = false;
    Boolean value = null;
    Boolean isDummy = false;

    String variableRegex = "[a-zA-Z]+[a-zA-Z0-9]*";

    public Literal (String literalString) throws Exception {
        // Remove leading and ending spaces
        literalString = literalString.strip();
        index = literalCounter++;

        // Empty strings are not valid
        if (literalString.length() < 1) {
            throw new InputException("problems.Literal must consist at least of one character.");
        }

        // Check negation usage
        String firstCharacter = String.valueOf(literalString.charAt(0));
        if (BooleanOperation.NEGATION.equalsName(firstCharacter)) {
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

    public Boolean getValue() {
        // TODO think about exception: Value has not been set
        if (this.value != null) {
            return this.value;
        }

        return false;
    }

    public boolean getLiteralValue() {
        return this.value;
    }

    public boolean getVariableValue() {
        if (this.negation) {
            return !this.value;
        }

        return this.value;
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

    /**
     * Compares two literals by their string representation.
     * @param other literal to which compare current literal
     * @return do two literals have same string representation
     */
    public Boolean isStringReprEquals(Literal other) {
        return Objects.equals(this.name, other.name) && this.negation == other.negation;
    }

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

    public Literal getOppositeLiteral() throws Exception {
        String oppositeLiteralName = Literal.getOppositeLiteralName(this.toString());
        return new Literal(oppositeLiteralName);
    }

    public static String getOppositeLiteralName(String literalName) {
        if (literalName.startsWith("-")) {
            return literalName.substring(1);
        }
        return '-' + literalName;
    }
}
