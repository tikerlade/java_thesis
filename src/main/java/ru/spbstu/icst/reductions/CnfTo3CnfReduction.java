package ru.spbstu.icst.reductions;

import javafx.util.Pair;
import ru.spbstu.icst.controllers.CnfScreenController;
import ru.spbstu.icst.controllers.Controller;
import ru.spbstu.icst.problems.*;

import java.util.*;

public class CnfTo3CnfReduction extends Reduction{

    // Contsructors part

    public CnfTo3CnfReduction() {
        this(new CnfFormula());
    }

    public CnfTo3CnfReduction(Problem problemA) {
        super(problemA, new Cnf3Formula(), "controllers/CnfScreen.fxml");
    }

    // Logical part
    public void readInputFromString(String input) throws Exception {
        CnfFormula formula = (CnfFormula) this.problemA;
        formula.readInputFromString(input);
        this.problemA = formula;
    }

    public void readSolutionFromString(String solution) throws Exception {
        CnfFormula formula = (CnfFormula) this.problemB;
        formula.readSolutionFromString(solution);
        this.problemB = formula;
    }

    /**
     * Given formula in CNF reduce it to 3CNF formula.
     * Forward method, converts input of problem A to input of problem B (A -> B)
     */
    public void forward() throws Exception {
        // TODO here we can not copy but use already existing variables and transforming solution will be easier though
        // Interpret existing problems as specific problems
        CnfFormula cnfFormula = (CnfFormula) this.problemA;
        Cnf3Formula cnf3Formula = (Cnf3Formula) this.problemB;

        int newLiteralCounter = 0;

        for (Clause clause : cnfFormula.clauses) {
            // Here we will store copies of literals of current clause
            Stack<Literal> clauseStack = new Stack<>();

            // Put copies of literals
            for (Literal literal : clause) {
                clauseStack.add(0, (Literal) literal.clone());
            }

            if (clause.size() < 4) {
                Clause newClause = new Clause(clause);
                cnf3Formula.addNewClause(newClause);
            } else {
                // Split into new groups
                // TODO check that names of variables are not used
                while (clauseStack.size() > 3) {
                    // Get two elements from current big clause
                    ArrayList<Literal> newClauseList = new ArrayList<>(Arrays.asList(clauseStack.pop(), clauseStack.pop()));
                    Clause newClause = new Clause(newClauseList);

                    // Init extra literal
                    Literal extraLiteral = new Literal("x" + newLiteralCounter);
                    extraLiteral.setIsDummy(true);

                    // Add extra literal to our two literals in clause
                    newClause.addLiteral(extraLiteral);
                    newLiteralCounter += 1;

                    // Update extra variable to put it in next clause
                    Literal copyExtraLiteral = (Literal) extraLiteral.clone();
                    copyExtraLiteral.setNegation(true);
                    copyExtraLiteral.setIsDummy(true);
                    clauseStack.push(copyExtraLiteral);

                    // Add current clause to
                    cnf3Formula.addNewClause(newClause);
                }

                // Put final 3 values into clauses of 3CNF
                ArrayList<Literal> lastClause = new ArrayList<>(clauseStack);
                Collections.reverse(lastClause);
                cnf3Formula.addNewClause(new Clause(lastClause));
            }
        }

        // Save result of reduction
        this.problemA = cnfFormula;
        this.problemB = cnf3Formula;
    }

    public void forwardAndBackward() throws Exception {
        this.forward();
        this.problemB.solve();
        this.backward();
    }

    /**
     * Given solution to 3CNF problem -> convert this into CNF problem solution
     * Backward method.
     */
    public void backward(CnfFormula formula, Cnf3Formula cnf3Formula) throws Exception {
        // How to make backward step on Cnf formula?
        // We have solution for problem B (3CNF) and we need to make solution for problem A (CNF) from it.

        int firstIdx = 0;
        int secondIdx = 0;

        while (firstIdx < formula.allLiterals.size()) {
            Literal first = formula.allLiterals.get(firstIdx++);
            Literal second = cnf3Formula.allLiterals.get(secondIdx++);

            while (!Objects.equals(first.toString(), second.toString())) {
                second = cnf3Formula.allLiterals.get(secondIdx++);
            }

            first.setLiteralValue(second.getLiteralValue());
        }
    }

    // Support part

    public List<Pair<String, String>> createSteps() throws Exception {
        // Two variables representing two problems
        CnfFormula formula = (CnfFormula) this.problemA;
        Cnf3Formula cnf3Formula = new Cnf3Formula();

        List<Pair<String, String>> output = new ArrayList<>();

        int newLiteralCounter = 0;
        for (Clause clause : formula.clauses) {
            // Here we will store copies of literals of current clause
            Stack<Literal> clauseStack = new Stack<>();

            // Put copies of literals
            for (Literal literal : clause) {
                clauseStack.add(0, (Literal) literal.clone());
            }

            if (clause.size() < 4) {
                Clause newClause = new Clause(clause);
                cnf3Formula.addNewClause(newClause);
                output.add(makePrintablePair(clause, newClause));
            } else {
                while (clauseStack.size() > 3) {
                    // Get two elements from current big clause
                    ArrayList<Literal> newClauseList = new ArrayList<>(Arrays.asList(clauseStack.pop(), clauseStack.pop()));
                    Clause newClause = new Clause(newClauseList);
                    Literal extraLiteral = new Literal("x" + newLiteralCounter);
                    extraLiteral.setIsDummy(true);

                    // Add extra literal to our two literals in clause
                    newClause.addLiteral(extraLiteral);
                    newLiteralCounter += 1;

                    // Update extra variable to put it in next clause
                    Literal copyExtraLiteral = (Literal) extraLiteral.clone();
                    copyExtraLiteral.setNegation(true);
                    copyExtraLiteral.setIsDummy(true);
                    clauseStack.push(copyExtraLiteral);

                    // Add current clause to
                    cnf3Formula.addNewClause(newClause);
                    output.add(makePrintablePair(clause, newClause));
                }

                // Put final 3 values into clauses of 3CNF
                ArrayList<Literal> lastLiterals = new ArrayList<>(clauseStack.stream().toList());
                Collections.reverse(lastLiterals);
                Clause lastClause = new Clause(lastLiterals);

                cnf3Formula.addNewClause(lastClause);
                output.add(makePrintablePair(clause, lastClause));
            }
        }

        this.problemB = cnf3Formula;
        return output;
    }

    private Pair<String, String> makePrintablePair(Clause clause1, Clause clause2) {
        String clause1AsString = clause1.toString();
        String clause2AsString = clause2.toString();
        return new Pair<>(clause1AsString, clause2AsString);
    }

    public String getReducedInput() {
        return this.problemB.toString();
    }

    @Override
    public Controller getScreenController() {
        return new CnfScreenController();
    }

    @Override
    public void resetProblems() {
        this.problemA = new CnfFormula();
        this.problemB = new Cnf3Formula();
    }
}
