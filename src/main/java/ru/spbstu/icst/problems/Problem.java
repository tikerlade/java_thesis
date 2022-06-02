package ru.spbstu.icst.problems;

import ru.spbstu.icst.exceptions.InputException;
import ru.spbstu.icst.exceptions.SolutionNotFoundException;

public abstract class Problem {
    public String shortname;
    public String fullname;
    public String description;

    public boolean solutionFound;


    public abstract String getShortname();

    public abstract void readInput() throws Exception;

    public abstract void solve() throws InputException, SolutionNotFoundException;

    public abstract void printSolution();

    public abstract void readSolution();

    public String getStringSolution() {
        return "";
    };
}
