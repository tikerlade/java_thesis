package reductions;

import problems.Cnf3Formula;
import problems.CnfFormula;
import problems.Literal;

import java.util.*;

public class CnfTo3CnfReduction {

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
}
