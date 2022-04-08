package ru.spbstu.icst.reductions;

import ru.spbstu.icst.problems.Clique;
import ru.spbstu.icst.problems.IndependentSet;

public class IsToClique extends Reduction {

    public IsToClique() {
        super(new IndependentSet(), new Clique());
        this.screenLocation = "IsToClique_screen.fxml";
    }
}
