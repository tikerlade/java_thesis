package ru.spbstu.icst.reductions;

/**
 * Different modes in which reduction can be performed.
 */
public enum ProgramMode {
    /**
     * User has Input for problem A and Solution for problem B.
     * And he wants to get Solution for problem A.
     */
    BACKWARD_ONLY ("Backward reduction only"),

    /**
     * User has only Input for problem A.
     * And he wants to get Input for problem B.
     */
    FORWARD_ONLY ("Forward reduction only"),

    /**
     * User has only Input for problem A.
     * And he wants to get Input for problem B, plus Solution for problem B.
     */
    FORWARD_SOLVE ("Forward reduction + solve"),

    /**
     * User has only Input for problem A.
     * And he wants to get Input for problem B, plus Solution for problem B, plus Solution for problem A.
     */
    FORWARD_SOLVE_BACKWARD ("Forward reduction + solve + Backward reduction");

    private final String name;
    public static final int numModes = ProgramMode.values().length;

    private ProgramMode(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
