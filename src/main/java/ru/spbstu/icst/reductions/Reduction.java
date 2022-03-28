package ru.spbstu.icst.reductions;

import ru.spbstu.icst.problems.Problem;

public abstract class Reduction {
    Problem problemA;
    Problem problemB;

    protected Reduction (Problem problemA, Problem problemB) {
        this.problemA = problemA;
        this.problemB = problemB;
    }

    // TODO read user data here
    public void start() throws Exception {
        System.out.println("Now you can input data for your " + this.problemA.getShortname() +
                " problem and you will get output for " + this.problemB.getShortname() + " problem.");
        this.problemA.readData();
    }

    public void forward() {}

    public void backward() {}

    public void forwardAndBackward() {}

    public String toString() {
        return this.problemA.getShortname() + " -> " + this.problemB.getShortname();
    }
}
