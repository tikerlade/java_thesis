package ru.spbstu.icst.reductions;

public enum ProgramMode {
    BACKWARD_ONLY ("Backward reduction only"),
    FORWARD_ONLY ("Forward reduction only"),
    FORWARD_SOLVE ("Forward reduction + solve"),
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
