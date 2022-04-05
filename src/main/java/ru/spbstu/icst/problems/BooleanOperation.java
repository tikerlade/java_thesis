package ru.spbstu.icst.problems;

/**
 * Boolean operations which are recognizable by CNF formulas.
 */
public enum BooleanOperation {
    /**
     * Negation of some variable, can be used only before variable name.
     * Example: -x.
     */
    NEGATION ("-"),

    /**
     * Boolean or operation, or disjunction. Requires variables from both sides of operation.
     * Example: x | y.
     */
    OR ("|"),

    /**
     * Boolean and operation, or conjunction. Requires variables from both sides of operation.
     * Example: x & y.
     */
    AND ("&");


    /**
     * name is string representation for each of operations.
     */
    private final String name;

    /**
     * numModes is number of distinct operations supported by this enum.
     */
    public static final int numModes = BooleanOperation.values().length;

    /**
     * Private constructor to set string representations for each of the operation.
     * @param s string representation of operation.
     */
    BooleanOperation(String s) {
        name = s;
    }

    /**
     * Comparing only string representations of enums.
     * @param otherName string representation of other enum (which we want to compare with).
     * @return are string representations the same.
     */
    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    /**
     * Converts enum value into its string representation.
     * @return string representation of enum.
     */
    public String toString() {
        return this.name;
    }
}
