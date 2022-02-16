package problems;

import exceptions.InputException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Formulas in CNF are used in both SAT problem and 3-SAT problem.
 * CNF - Conjunctive Normal Form
 */
public class CnfFormula {
    Integer literalCounter = 0;
    public ArrayList<ArrayList<Literal>> clauses;
    String inputFormula;
    HashMap<String, ArrayList<Literal>> varToLiterals = new HashMap<>(); // here we'll store all literals which have the same variable name
    ArrayList<Literal> allLiterals = new ArrayList<>();

    HashMap<BooleanOperation, Character> allowedOperations = new HashMap<>() {{
        put(BooleanOperation.NEGATION, '-');
        put(BooleanOperation.AND, '&');
        put(BooleanOperation.OR, '|');
    }};

    public CnfFormula() {
        this.inputFormula = "";
        this.clauses = new ArrayList<>();
    }

    public CnfFormula (String formulaString) throws Exception {
        // Save users formula
        this.inputFormula = formulaString;

        ArrayList<ArrayList<Literal>> clauses = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();

        // Collect literals by splitting clause at AND operations
        for (Character ch: formulaString.toCharArray()) {
            // Not reached AND -> collect character to buffer
            if (ch != allowedOperations.get(BooleanOperation.AND)) {
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
        this.clauses = clauses;
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
            if (ch != allowedOperations.get(BooleanOperation.OR)) {
                buffer.append(ch);
            }

            // When OR reached -> try to parse literal
            else {
                Literal newLiteral = new Literal(buffer.toString(), literalCounter++);
                listOfLiterals.add(newLiteral);
                buffer.setLength(0);

                // Save information as metainfo
                ArrayList<Literal> literals;

                if (varToLiterals.containsKey(newLiteral.toString())) {
                    literals = varToLiterals.get(newLiteral.toString());
                    literals.add(newLiteral);
                } else {
                    literals = new ArrayList<>();
                    literals.add(newLiteral);
                    varToLiterals.put(newLiteral.toString(), literals);
                }

                this.allLiterals.add(newLiteral);
            }
        }

        // At this point buffer still contain something
        Literal lastLiteral = new Literal(buffer.toString(), literalCounter++);
        listOfLiterals.add(lastLiteral);

        // TODO wrap into function
        // Save information as metainfo
        ArrayList<Literal> literals;

        if (varToLiterals.containsKey(lastLiteral.toString())) {
            literals = varToLiterals.get(lastLiteral.toString());
            literals.add(lastLiteral);
        } else {
            literals = new ArrayList<>();
            literals.add(lastLiteral);
            varToLiterals.put(lastLiteral.toString(), literals);
        }

        this.allLiterals.add(lastLiteral);

        return listOfLiterals;
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

    /**
     * Generates matrix of boolean values sized 2^colNum * colNum - potential values for boolean variables
     * @param colNum number of columns in matrix - number of all literals in formula
     * @return matrix with values for variables
     */
    public Boolean[][] getLiteralsValueMatrix(Integer colNum) {
        String stringFormat = "%" + colNum + "s";
        int rowNum = (int) Math.pow(2, colNum);
        Boolean[][] values = new Boolean[rowNum][colNum];

        for (int row = 0; row < rowNum; row++) {
            String binRowNum = String.format(stringFormat, Integer.toBinaryString(row)).replace(' ', '0');
            Boolean[] rowValue = new Boolean[colNum];

            for (int i = 0; i < colNum; i++) {
                rowValue[i] = binRowNum.charAt(i) != '0';
            }

            values[row] = rowValue;
        }

        return values;
    }

    public void solve() {
        // TODO complete this function to solve problems on users input
        int varNum = this.varToLiterals.values().stream().map(ArrayList::size).reduce(0, Integer::sum);
        Boolean[][] literalsValue = getLiteralsValueMatrix(varNum);

        for (Boolean[] row : literalsValue) {
            boolean skipCurrentRow = false;

            // Set values
            for (int i = 0; i < varNum; i++) {
                this.allLiterals.get(i).setValue(row[i]);
            }

            // Check that the same value has not been set to opposite literals
            for (String key : varToLiterals.keySet()) {
                Boolean refValue = varToLiterals.get(key).get(0).value;

                // Values for all literals with the same key must be the same
                for (Literal literal : varToLiterals.get(key)) {
                    if (literal.getValue() != refValue) {
                        skipCurrentRow = true;
                        break;
                    }
                }

                // Maybe no further check required
                if (skipCurrentRow) {
                    continue;
                }

                // Check opposite literals value
                String oppositeKey = Literal.getOppositeLiteralName(key);
                if (this.varToLiterals.containsKey(oppositeKey)) {
                    Boolean refOppositeValue = !refValue; // must be opposite to value of original literal
                    for (Literal oppositeLiteral : varToLiterals.get(oppositeKey)) {
                        if (oppositeLiteral.getValue() != refOppositeValue) {
                            skipCurrentRow = true;
                            break;
                        }
                    }
                }
            }

            if (skipCurrentRow) {
                continue;
            }

            // Check satisfiability
            if (isSatisfyingSet()) {
                System.out.println("Satisfying set found.");
            }
        }
    }

    /**
     * Given boolean values for each of variables - want to know is it satisfying set.
     * @return is formula currently satisfyable or not
     */
    public boolean isSatisfyingSet() {
        boolean isSatisfyed = true;

        for (ArrayList<Literal> clause : this.clauses) {
            boolean localSatisfyed = false;

            for (Literal literal : clause) {
                localSatisfyed = localSatisfyed | literal.getValue();
            }

            isSatisfyed = isSatisfyed & localSatisfyed;
        }

        return isSatisfyed;
    }
}


