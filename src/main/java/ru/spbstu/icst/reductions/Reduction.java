package ru.spbstu.icst.reductions;

import ru.spbstu.icst.problems.Problem;

public abstract class Reduction {
    Problem problemA;
    Problem problemB;

    protected Reduction (Problem problemA, Problem problemB) {
        this.problemA = problemA;
        this.problemB = problemB;
    }

    public void start() throws Exception {
        System.out.println("Now you can input data for your " + this.problemA.getShortname() +
                " problem and you will get output for " + this.problemB.getShortname() + " problem.");
        this.problemA.readData();
    }

    /**
     * In forward method we know only data for problemA.
     * And we want to convert this data into data for problemB.
     */
    public void forward() {}

    /**
     * In backward method we have solution for problemA,
     * and we want to convert it into solution for problemA.
     */
    public void backward() {}

    /**
     * We have algorithm for solving problemB, but we have data for problemA.
     * Here we will firstly convert data of problemA into data for problemB.
     * After this we'll solve problemB using known algorithms.
     * When solution for problemB will be found we will convert this solution into solution for problemA.
     */
    public void forwardAndBackward() {
        this.forward();
        this.backward();
    }

    /**
     * Creates title of reduction using shortnames of problems which are reduced.
     * @return name of reduction
     */
    public String toString() {
        return this.problemA.getShortname() + " -> " + this.problemB.getShortname();
    }
}
