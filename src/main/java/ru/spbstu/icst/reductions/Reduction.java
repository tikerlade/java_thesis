package ru.spbstu.icst.reductions;

import ru.spbstu.icst.problems.Problem;

public abstract class Reduction {
    Problem problemA;
    Problem problemB;

    protected Reduction (Problem problemA, Problem problemB) {
        this.problemA = problemA;
        this.problemB = problemB;
    }

    public void start(ProgramMode programMode) throws Exception {
        switch (programMode) {
            case FORWARD_ONLY -> {
                this.forwardOnly();
                System.out.println(this.problemB);
            }

            case FORWARD_SOLVE -> {
                this.forwardSolve();
                this.problemB.printSolution();
            }

            case FORWARD_SOLVE_BACKWARD -> {
                this.forwardSolveBackward();
                this.problemA.printSolution();
            }

            case BACKWARD_ONLY -> {
                this.backwardOnly();
                this.problemA.printSolution();
            }
        }
    }


    // Wrapper function for forward reduction
    public void forwardOnly() throws Exception {
        System.out.println("Now you can input data for your " + this.problemA.getShortname() +
                " problem and you will get output for " + this.problemB.getShortname() + " problem.");
        this.problemA.readInput();
        this.forward();
    }

    public void forwardSolve() throws Exception {
        this.forwardOnly();
        this.problemB.solve();
    }

    public void forwardSolveBackward() throws Exception {
        this.forwardSolve();
        this.backward();
    }

    public void backwardOnly() throws Exception {
        System.out.println("Input data for your " + this.problemB.getShortname() + " problem:");
        this.problemB.readInput();

        System.out.println("Input solution for your " + this.problemB.getShortname() + " problem:");
        this.problemB.readSolution();

        this.backward();
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
    public void forwardAndBackward() throws Exception {}

    /**
     * Creates title of reduction using shortnames of problems which are reduced.
     * @return name of reduction
     */
    public String toString() {
        return this.problemA.getShortname() + " -> " + this.problemB.getShortname();
    }
}
