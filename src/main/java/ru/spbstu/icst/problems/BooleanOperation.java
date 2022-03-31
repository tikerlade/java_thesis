package ru.spbstu.icst.problems;

enum BooleanOperation {
    NEGATION ("-"),
    OR ("|"),
    AND ("&");


    private final String name;
    public static final int numModes = BooleanOperation.values().length;

    private BooleanOperation(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
