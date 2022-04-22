package ru.spbstu.icst.reductions;

import ru.spbstu.icst.controllers.Controller;
import ru.spbstu.icst.controllers.IsToCliqueScreenController;
import ru.spbstu.icst.problems.Clique;
import ru.spbstu.icst.problems.IndependentSet;

public class IsToClique extends Reduction {

    public IsToClique() {
        super(new IndependentSet(), new Clique());
        this.screenFilename = "IsToClique_screen.fxml";
    }

    @Override
    public Controller getScreenController() {
        return new IsToCliqueScreenController();
    }
}
