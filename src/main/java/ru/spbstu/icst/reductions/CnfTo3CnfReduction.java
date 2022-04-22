package ru.spbstu.icst.reductions;

import javafx.util.Pair;
import ru.spbstu.icst.controllers.CnfScreenController;
import ru.spbstu.icst.controllers.Controller;
import ru.spbstu.icst.problems.Cnf3Formula;
import ru.spbstu.icst.problems.CnfFormula;
import ru.spbstu.icst.problems.Literal;
import ru.spbstu.icst.problems.Problem;

import java.util.*;

public class CnfTo3CnfReduction extends Reduction{

    // Contsructors part

    public CnfTo3CnfReduction() {
        this(new CnfFormula());
    }

    public CnfTo3CnfReduction(Problem problemA) {
        super(problemA, new Cnf3Formula(), "cnf_screen.fxml");
    }

    // Logical part
    public void readInputFromString(String input) throws Exception {
        CnfFormula formula = (CnfFormula) this.problemA;
        formula.readInputFromString(input);
        this.problemA = formula;
    }

    public void readSolutionFromString(String solution) throws Exception {
        CnfFormula formula = (CnfFormula) this.problemA;
        formula.readSolutionFromString(solution);
        this.problemA = formula;
    }

    /**
     * Given formula in CNF reduce it to 3CNF formula.
     * Forward method, converts input of problem A to input of problem B (A -> B)
     * @return formula in 3CNF format
     */
    public Cnf3Formula forward(CnfFormula cnfFormula) throws Exception {
        // TODO here we can not copy but use already existing variables and transforming solution will be easier though
        Cnf3Formula cnf3Formula = new Cnf3Formula();
        int newLiteralCounter = 0;

        for (ArrayList<Literal> clause : cnfFormula.clauses) {
            // Here we will store copies of literals of current clause
            Stack<Literal> clauseStack = new Stack<>();

            // Put copies of literals
            for (Literal literal : clause) {
                clauseStack.add(0, (Literal) literal.clone());
            }

            if (clause.size() < 4) {
                ArrayList<Literal> newClause = new ArrayList<>(clause);
                cnf3Formula.addNewClause(newClause);
            } else {
                // Split into new groups
                // TODO check that names of variables are not used
                while (clauseStack.size() > 3) {
                    // Get two elements from current big clause
                    ArrayList<Literal> newClause = new ArrayList<>(Arrays.asList(clauseStack.pop(), clauseStack.pop()));
                    Literal extraLiteral = new Literal("x" + newLiteralCounter);
                    extraLiteral.setIsDummy(true);

                    // Add extra literal to our two literals in clause
                    newClause.add(extraLiteral);
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
                cnf3Formula.addNewClause(lastClause);
            }
        }

        return cnf3Formula;
    }

    public HashMap<String, Boolean> forwardAndBackward(CnfFormula formula) throws Exception {
        Cnf3Formula cnf3Formula = forward(formula);
        cnf3Formula.solve();
        return backward(formula, cnf3Formula);
    }

    /**
     * Given solution to 3CNF problem -> convert this into CNF problem solution
     * Backward method.
     * @return values for variables of formula
     */
    public HashMap<String, Boolean> backward(CnfFormula formula, Cnf3Formula cnf3Formula) throws Exception {
        int firstIdx = 0;
        int secondIdx = 0;

        while (firstIdx < formula.allLiterals.size()) {
            Literal first = formula.allLiterals.get(firstIdx++);
            Literal second = cnf3Formula.allLiterals.get(secondIdx++);

            while (!Objects.equals(first.toString(), second.toString())) {
                second = cnf3Formula.allLiterals.get(secondIdx++);
            }

            first.setValue(second.getValue());
        }

        return formula.getSatisfyingSet();
    }

    // Support part

    public List<Pair<String, String>> createSteps() throws Exception {
        // Two variables representing two problems
        CnfFormula formula = (CnfFormula) this.problemA;
        Cnf3Formula cnf3Formula = new Cnf3Formula();

        List<Pair<String, String>> output = new ArrayList<>();

        int newLiteralCounter = 0;
        for (ArrayList<Literal> clause : formula.clauses) {
            // Here we will store copies of literals of current clause
            Stack<Literal> clauseStack = new Stack<>();

            // Put copies of literals
            for (Literal literal : clause) {
                clauseStack.add(0, (Literal) literal.clone());
            }

            if (clause.size() < 4) {
                ArrayList<Literal> newClause = new ArrayList<>(clause);
                cnf3Formula.addNewClause(newClause);
                output.add(makePrintablePair(clause, newClause));
            } else {
                while (clauseStack.size() > 3) {
                    // Get two elements from current big clause
                    ArrayList<Literal> newClause = new ArrayList<>(Arrays.asList(clauseStack.pop(), clauseStack.pop()));
                    Literal extraLiteral = new Literal("x" + newLiteralCounter);
                    extraLiteral.setIsDummy(true);

                    // Add extra literal to our two literals in clause
                    newClause.add(extraLiteral);
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
                ArrayList<Literal> lastClause = new ArrayList<>(clauseStack);
                Collections.reverse(lastClause);
                cnf3Formula.addNewClause(lastClause);
                output.add(makePrintablePair(clause, lastClause));
            }
        }

        this.problemB = cnf3Formula;
        return output;
    }

    private Pair<String, String> makePrintablePair(ArrayList<Literal> literals1, ArrayList<Literal> literals2) {
        List<String> literals1AsString = literals1.stream().map(Literal::toString).toList();
        List<String> literals2AsString = literals2.stream().map(Literal::toString).toList();

        String clause1AsString = "(" + String.join(" | ", literals1AsString) + ")";
        String clause2AsString = "(" + String.join(" | ", literals2AsString) + ")";

        return new Pair<String, String>(clause1AsString, clause2AsString);

    }

    public String getReducedInput() {
        return this.problemB.toString();
    }

    @Override
    public Controller getScreenController() {
        return new CnfScreenController();
    }
}
