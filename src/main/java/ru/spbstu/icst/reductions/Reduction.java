package ru.spbstu.icst.reductions;

import ru.spbstu.icst.problems.Problem;

/**
 * Class which contains all logics required to work with reductions.
 * Reduction is completed from problem A into problem B.
 *
 * Reduction can be completed in 4 different options:
 * - Forward reduction only;
 * - Forward reduction + solve reduced problem;
 * - Forward reduction + solve reduced problem + backward reduction;
 * - Backward reduction only;
 *
 * Forward reduction - convert input of problem A into input of problem B.
 * Backward reduction - convert solution of problem B into solution of problem A.
 */
public abstract class Reduction {
    /**
     * Problem which we want to reduce into problem B.
     */
    Problem problemA;

    /**
     * Problem into which we want to reduce initial problem A.
     */
    Problem problemB;

    /**
     * Constructor with given two exact problems (without data at this point).
     * @param problemA empty problem A.
     * @param problemB empty problem B.
     */
    protected Reduction (Problem problemA, Problem problemB) {
        this.problemA = problemA;
        this.problemB = problemB;
    }

    /**
     * Entry point into actual algorithm.
     * From here we decide which parts of reduction we want to complete.
     * @param programMode mode in which to run program.
     * @throws Exception if there are problems with parsing input data.
     */
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


    /**
     * Wrapper function for FORWARD_ONLY mode to read user data and after that
     * execute clean forward reduction.
     * @throws Exception if there are problems with parsing input data.
     */
    public void forwardOnly() throws Exception {
        System.out.println("Now you can input data for your " + this.problemA.getShortname() +
                " problem and you will get output for " + this.problemB.getShortname() + " problem.");
        this.problemA.readInput();
        this.forward();
    }

    /**
     * Wrapper function for FORWARD_SOLVE mode to read user data and after that
     * execute clean forward reduction + solve reduced problem.
     * @throws Exception if there are problems with parsing input data.
     */
    public void forwardSolve() throws Exception {
        this.forwardOnly();
        this.problemB.solve();
    }

    /**
     * Wrapper function for FORWARD_SOLVE_BACKWARD mode to read user data and after that
     * execute clean forward reduction + solve reduced problem + backward reduction.
     * @throws Exception if there are problems with parsing input data.
     */
    public void forwardSolveBackward() throws Exception {
        this.forwardSolve();
        this.backward();
    }

    /**
     * Wrapper function for BACKWARD_ONLY mode to read user data and after that
     * execute clean backward reduction.
     * @throws Exception if there are problems with parsing input data.
     */
    public void backwardOnly() throws Exception {
        System.out.println("Input data for your " + this.problemB.getShortname() + " problem:");
        this.problemB.readInput();

        System.out.println("Input solution for your " + this.problemB.getShortname() + " problem:");
        this.problemB.readSolution();

        this.backward();
    }

    /**
     * In forward method we know only input data for problemA.
     * And we want to convert this data into input data for problemB.
     */
    public void forward() {}

    /**
     * In backward method we have solution for problemB,
     * and we want to convert it into solution for problemA.
     */
    public void backward() {}

    /**
     * Here we will firstly convert input data of problemA into input data for problemB.
     * After this we'll solve problemB.
     * When solution for problemB will be found we will convert this solution into solution for problemA.
     * Case: We have algorithm for solving problemB, but we have input data only for problemA.
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
