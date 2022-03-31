package ru.spbstu.icst.problems;

import com.google.ortools.Loader;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.IntVar;
import ru.spbstu.icst.exceptions.InputException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Formulas in CNF are used in both SAT problem and 3-SAT problem.
 * CNF - Conjunctive Normal Form
 */
public class CnfFormula extends Problem {
    private final static String shortname = "SAT";
    private final static String fullname = "Satisfiability problem";

    private Integer literalCounter = 0;
    public ArrayList<ArrayList<Literal>> clauses;
    String inputFormula;
    public ArrayList<Literal> allLiterals = new ArrayList<>();
    boolean isSolved = false;

    public CnfFormula() {
        this.inputFormula = "";
        this.clauses = new ArrayList<>();
    }

    public CnfFormula (String formulaString) throws Exception {
        // Save users formula
        this.clauses = this.parseCnfFormulaToClauses(formulaString);
        this.inputFormula = formulaString;
    }

    public ArrayList<ArrayList<Literal>> parseCnfFormulaToClauses(String inputFormula) throws Exception {
        ArrayList<ArrayList<Literal>> clauses = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();

        // Collect literals by splitting clause at AND operations
        for (Character ch: inputFormula.toCharArray()) {
            // Not reached AND -> collect character to buffer
            if (!BooleanOperation.AND.equalsName(ch.toString())) {
                buffer.append(ch);
            }

            // When AND reached -> try to parse literal
            else {
                ArrayList<Literal> newClause = getClauseFromString(buffer.toString());
                clauses.add(newClause);
                buffer.setLength(0);
            }
        }

        // At this point buffer still contain something
        ArrayList<Literal> lastClause = getClauseFromString(buffer.toString());
        clauses.add(lastClause);

        // Save clauses as parsed formula
        return clauses;
    }

    public ArrayList<Literal> getClauseFromString(String clauseString) throws Exception {
        ArrayList<Literal> listOfLiterals = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();

        // Remove leading and following spaces
        clauseString = clauseString.strip();

        // In valid clause must be at least two parenthesis
        // TODO check that last and first characters are parenthesis
        if (clauseString.length() < 2) {
            throw new InputException("Clauses must be covered in parentheses.");
        }
        clauseString = clauseString.substring(1, clauseString.length()-1);

        // Collect literals by splitting clause at OR operations
        for (Character ch : clauseString.toCharArray()) {
            // Not reached OR -> collect character to buffer
            if (!BooleanOperation.OR.equalsName(ch.toString())) {
                buffer.append(ch);
            }

            // When OR reached -> try to parse literal
            else {
                Literal newLiteral = new Literal(buffer.toString(), literalCounter++);
                listOfLiterals.add(newLiteral);
                buffer.setLength(0);

                // Save information as metainfo
                addNewLiteral(newLiteral);
            }
        }

        // At this point buffer still contain something
        Literal lastLiteral = new Literal(buffer.toString(), literalCounter++);
        listOfLiterals.add(lastLiteral);

        // Save information as metainfo
        addNewLiteral(lastLiteral);

        return listOfLiterals;
    }

    private void addNewLiteral(Literal literal) {
        this.allLiterals.add(literal);
    }

    public void addNewClause(ArrayList<Literal> clause) {
        for (Literal literal : clause) {
            addNewLiteral(literal);
        }
        this.clauses.add(clause);
    }

    /**
     * Finds values for literals which will satisfy formula.
     */
    public void solve() {
        // Initialize model from or-tools
        Loader.loadNativeLibraries();
        CpModel cpModel = new CpModel();

        // Create pull of variables and constraints
        HashMap<String, IntVar> stringToVar = new HashMap<>();
        for (ArrayList<Literal> clause : clauses) {
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
            isSolved = true;

            for (ArrayList<Literal> clause : clauses) {
                for (Literal literal : clause) {
                    IntVar variable = stringToVar.get(literal.name);
                    boolean variableValue = solver.value(variable) == 1;
                    literal.setValue(literal.negation == variableValue);
                }
            }
        }
    }

    @Override
    public String toString() {
        List<List<String>> literalsAsString = clauses.stream().map(
                clause -> clause.stream().map(Literal::toString).toList()
        ).toList();

        List<String> clausesAsString = literalsAsString.stream().map(
                clause -> "(" + String.join(" | ", clause) + ")"
        ).toList();

        return String.join(" & ", clausesAsString);
    }

    @Override
    public void printSolution() {
        if (isSolved) {
            for (Literal literal : allLiterals) {
                System.out.println(literal.toString() + " = " + literal.getValue());
            }
        } else {
            System.out.println("No solution found.");
        }
    }

    @Override
    public void readSolution() {

    }

    /**
     * Given boolean values for each of variables - want to know is it satisfying set.
     * @return is formula currently satisfyable or not
     */
    public boolean isSatisfied() {
        boolean isSatisfied = true;

        for (ArrayList<Literal> clause : this.clauses) {
            boolean localSatisfyed = false;

            for (Literal literal : clause) {
                localSatisfyed = localSatisfyed | literal.getValue();
            }

            isSatisfied = isSatisfied & localSatisfyed;
        }

        return isSatisfied;
    }

    // TODO remove or reorganize this. Return String is redundant
    public HashMap<String, Boolean> getSatisfyingSet() throws Exception {
        if (!this.isSolved) {
            solve();

            if (!isSatisfied()) {
                throw new Exception("Satisfying set was not found!");
            }
        }

        HashMap<String, Boolean> varToValue = new HashMap<>();

        for (Literal literal: this.allLiterals) {
            varToValue.put(literal.toString(), literal.value);
        }

        return varToValue;
    }

    public String getShortname() {
        return CnfFormula.shortname;
    }

    @Override
    public void readInput() throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Get data from user
        System.out.print("Input your formula in CNF: ");
        String inputString = scanner.nextLine();

        // Parse user data and store it
        this.clauses = parseCnfFormulaToClauses(inputString);
        this.inputFormula = inputString;
    }
}


