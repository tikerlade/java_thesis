package ru.spbstu.icst.reductions;

import ru.spbstu.icst.problems.Problem;

public abstract class Reduction {
    Problem problemA;
    Problem problemB;

    public void start() {}

    public void forward() {}

    public void backward() {}

    public void forwardAndBackward() {}

    public String toString() {
        return this.problemA.getShortname() + " -> " + this.problemB.getShortname();
    }
}
