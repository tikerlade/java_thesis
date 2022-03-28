package ru.spbstu.icst.reductions;

import ru.spbstu.icst.problems.Clique;
import ru.spbstu.icst.problems.IndependentSet;

public class IsToClique extends Reduction {

    public IsToClique() {
        this.problemA = new IndependentSet();
        this.problemB = new Clique();
    }

}
