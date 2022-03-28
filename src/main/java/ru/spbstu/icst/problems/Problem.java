package ru.spbstu.icst.problems;

public abstract class Problem {
    public String shortname;
    public String fullname;
    public String description;

    public abstract String getShortname();

    public abstract void readData() throws Exception;
}
