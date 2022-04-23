package ru.spbstu.icst.problems;

public class Cnf3Formula extends CnfFormula{
    private final static String shortname = "3SAT";
    private final static String fullname = "3-Satifiability problem";
    private final static String description = "Given formula in Conjunctive Normal Form, where each clause has at most 3 literals," +
            " required to find such values" +
            " for literals in the formula which will make it satisfied.";

    public Cnf3Formula() {
        super();
    }

    public Cnf3Formula(String formulaString) throws Exception {
        // Parse formula from given string
        super(formulaString);

        // Check that each clause is no longer then 3 literals
        for (Clause clause : this.clauses) {
            if (clause.size() > 3) {
                throw new Exception("Formula in 3-CNF must consist of clauses" +
                        " which length is between 1 and 3 literals.");
            }
        }
    }

    public String getShortname() {
        return Cnf3Formula.shortname;
    }
}
