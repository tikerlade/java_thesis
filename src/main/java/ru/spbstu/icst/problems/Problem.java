package ru.spbstu.icst.problems;

public abstract class Problem {
    public String shortname;
    public String fullname;
    public String description;

    public boolean solutionFound;


    public abstract String getShortname();

    public abstract void readInput() throws Exception;

    public abstract void solve();

    public abstract void printSolution();

    public abstract void readSolution();
}
