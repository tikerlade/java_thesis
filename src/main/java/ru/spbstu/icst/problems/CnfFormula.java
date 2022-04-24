package ru.spbstu.icst.problems;

import com.google.ortools.Loader;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.IntVar;

import java.util.*;

/**
 * Formulas in CNF are used in both SAT problem and 3-SAT problem.
 * CNF - Conjunctive Normal Form
 */
public class CnfFormula extends Problem {
    private final static String shortname = "SAT";
    private final static String fullname = "Satisfiability problem";
    private final static String description = "Given formula in Conjunctive Normal Form, required to find such values" +
            " for variables in  the formula which will make it satisfied.";

    String inputFormula;
    public ArrayList<Clause> clauses;
    public ArrayList<Literal> allLiterals = new ArrayList<>();
    ArrayList<Literal> satisfyingSet = new ArrayList<>();

    public CnfFormula() {
        this.clauses = new ArrayList<>();
        this.inputFormula = "";
    }

    public CnfFormula (String formulaString) throws Exception {
        this.parseCnfFormulaToClauses(formulaString);
        this.inputFormula = formulaString;
    }


    // --------------------------------------------------------------------------------------------------



    /**
     * Finds values for literals which will satisfy formula.
     */
    public void solve() {
        // Initialize model from or-tools
        Loader.loadNativeLibraries();
        CpModel cpModel = new CpModel();

        // Create pull of variables and constraints
        HashMap<String, IntVar> stringToVar = new HashMap<>();
        for (Clause clause : this.clauses) {
            com.google.ortools.sat.Literal[] googleClause = new com.google.ortools.sat.Literal[clause.size()];

            for (int i = 0; i < clause.size(); i++) {
                Literal literal = clause.get(i);

                // Maybe variable still not initialized
                if (!stringToVar.containsKey(literal.name)) {
                    IntVar tmp = cpModel.newBoolVar(literal.name);
                    stringToVar.put(literal.name, tmp);
                }

                // Put converted variable into array
                if (literal.negation) {
                    googleClause[i] = stringToVar.get(literal.name).not();
                } else {
                    googleClause[i] = stringToVar.get(literal.name);
                }
            }

            // Add clause as constraint
            cpModel.addBoolOr(googleClause);
        }

        // Find solution
        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(cpModel);

        // Convert solution into local notation
        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            solutionFound = true;

            for (Clause clause : clauses) {
                for (Literal literal : clause) {
                    IntVar variable = stringToVar.get(literal.name);
                    boolean variableValue = solver.value(variable) == 1;
                    literal.setLiteralValue(literal.negation == variableValue);
                }
            }
        }
    }

    @Override
    public void printSolution() {
        System.out.println(this.getStringSolution());
    }

    public String getStringSolution() {
        if (this.satisfyingSet == null || this.satisfyingSet.size() == 0) {
            this.createSatisfyingSet();
        }

        if (this.satisfyingSet.size() > 0) {
            ArrayList<String> satSetAsStrings = new ArrayList<>(this.satisfyingSet.stream().map(Literal::toString).toList());
            return String.join(" ", new HashSet<>(satSetAsStrings));
        }

        return "No solution found.";
    }

    @Override
    public void readSolution() {

    }



    /**
     * Given string, which contains space-separated variables names which values must be true.
     * @param trueVariables string which contains names of variables
     */
    public void readSolutionFromString(String trueVariables) throws Exception {
        ArrayList<Literal> tempLiterals = new ArrayList<>();

        // Remove all unnecessary whitespaces
        trueVariables = trueVariables.strip();

        // Collect literals by splitting string by spaces
        String[] splitted = trueVariables.split("\\s+");

        // Go for each variable name and create new Literal from it - store them in temp variable
        for (String literal: splitted) {
            Literal newLiteral = new Literal(literal);
            newLiteral.setLiteralValue(true);
            tempLiterals.add(newLiteral);
        }

        // Go through literals and try to assign true value for them
        HashSet<String> trueLiterals = new HashSet<>(tempLiterals.stream().map(Literal::toString).toList());
        for (Literal literal : allLiterals) {
            if (trueLiterals.contains(literal.toString())) {
                literal.setLiteralValue(true);
            } else if (trueLiterals.contains(literal.getOppositeLiteral().toString())) {
                literal.setLiteralValue(false);
            } else {
                literal.setLiteralValue(true);
                trueLiterals.add(literal.toString());
            }
        }

        this.createSatisfyingSet();




//        HashSet<String> literalsAsStrings = new HashSet<>();
//        for (Literal otherLiteral : allLiterals) {
//            otherLiteral.setLiteralValue(literalsAsStrings.contains(otherLiteral.toString()));
//        }


        // Match variables from temp with variables from formula
//        for (Literal trueLiteral : tempLiterals) {
//            for (Literal otherLiteral : allLiterals) {
//                if (trueLiteral.isStringReprEquals(otherLiteral)) {
//                    satisfyingSet.add(otherLiteral);
//                }
//            }
//        }
    }

    // TODO replace getSatisfyingSet with this method
    public void createSatisfyingSet() {
        for (Literal literal : this.allLiterals) {
            if (literal.getLiteralValue()) {
                this.satisfyingSet.add(literal);
            }
        }
    }

    // TODO remove or reorganize this. Return String is redundant
    public HashMap<String, Boolean> getSatisfyingSet() throws Exception {
        if (!this.solutionFound) {
            solve();
            if (!isSatisfied()) {
                throw new Exception("Satisfying set was not found!");
            }
        }

        HashMap<String, Boolean> varToValue = new HashMap<>();
        for (Literal literal: this.allLiterals) {
            varToValue.put(literal.toString(), literal.literalValue);
        }

        return varToValue;
    }



    @Override
    public void readInput() throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Get data from user
        System.out.print("Input your formula in CNF: ");
        String inputString = scanner.nextLine();

        // Parse user data and store it
        parseCnfFormulaToClauses(inputString);
        this.inputFormula = inputString;
    }


    // -------------------------------------------------------------------------------------------------------

    private void parseCnfFormulaToClauses(String inputFormula) throws Exception {
        StringBuilder buffer = new StringBuilder();

        // Collect literals by splitting clause at AND operations
        for (Character ch: inputFormula.toCharArray()) {
            // Not reached AND -> collect character to buffer
            if (!BooleanOperation.AND.equalsName(ch.toString())) {
                buffer.append(ch);
            }

            // When AND reached -> try to parse literal
            else {
                this.addNewClause(new Clause(buffer.toString()));
                buffer.setLength(0);
            }
        }

        // At this point buffer still contain something
        this.addNewClause(new Clause(buffer.toString()));
    }

    public void addNewClause(Clause newClause) {
        // Add clause into list of clauses
        this.clauses.add(newClause);

        // Collect all literals of clause
        this.allLiterals.addAll(newClause.getLiterals());
    }

    /**
     * Given string, which contains input formula - parse it into clauses.
     * @param input string which contains formula
     */
    public void readInputFromString(String input) throws Exception {
        this.parseCnfFormulaToClauses(input);
    }

    /**
     * Given boolean values for each of variables - want to know is it satisfying set.
     * @return is formula currently satisfied or not
     */
    public boolean isSatisfied() {
        // If solution was not found - formula is not satisfied
        if (!solutionFound) {
            return false;
        }

        boolean isSatisfied = true;
        for (Clause clause : this.clauses) {
            isSatisfied &= clause.isSatisfyed();
        }

        return isSatisfied;
    }


    @Override
    public String getShortname() {
        return CnfFormula.shortname;
    }

    @Override
    public String toString() {
        // Represent clauses as Strings
        List<String> clausesAsString = clauses.stream().map(Clause::toString).toList();

        // Join string representations of clauses with & sign
        return String.join(" & ", clausesAsString);
    }

}


