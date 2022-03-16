package ru.spbstu.icst.problems;

import java.util.ArrayList;

public class Cnf3Formula extends CnfFormula{
    public static String shortname = "3SAT";

    public Cnf3Formula() {
        super();
    }

    public Cnf3Formula(String formulaString) throws Exception {
        // Parse formula from given string
        super(formulaString);

        // Check that each clause is no longer then 3 literals
        for (ArrayList<Literal> clause : this.clauses) {
            if (clause.size() > 3) {
                throw new Exception("Formula in 3-CNF must consist of clauses" +
                        " which length is between 1 and 3 literals.");
            }
        }
    }
}
