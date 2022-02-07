import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Formulas in CNF are used in both SAT problem and 3-SAT problem.
 * CNF - Conjunctive Normal Form
 */
public class CnfFormula {
    Integer literalCounter;
    List<Literal> literals;
    ArrayList<ArrayList<Literal>> clauses;

    String inputFormula;

    // TODO regex - variable name cannot start with 0-9
    String variableRegex = "[a-zA-Z0-9]+";

    HashMap<BooleanOperation, Character> allowedOperations = new HashMap<>() {{
            put(BooleanOperation.NEGATION, '-');
            put(BooleanOperation.AND, '&');
            put(BooleanOperation.OR, '|');
        }};


    public Literal getLiteralFromString(String literalString) throws Exception {
        Literal newLiteral = new Literal();

        // Remove leading
        literalString = literalString.strip();

        // Empty literals are not valid
        if (literalString.length() < 1) {
            throw new Exception();
        }

        // Check negation usage
        if (literalString.charAt(0) == allowedOperations.get(BooleanOperation.NEGATION)) {
            newLiteral.setNegation(true);
            literalString = literalString.substring(1);
        }

        // From now and until the end - only allowed chars can be used
        if (Pattern.matches(variableRegex, literalString)) {
            throw new Exception();
        }

        // If we reached this point - variable structure is okay
        // Increase number of literals we saw so far
        literalCounter++;

        // Set variable name as we parsed
        newLiteral.setName(literalString);

        // Set index of literal
        newLiteral.setIndex(literalCounter);

        // Return literal we created
        return newLiteral;
    }

    public ArrayList<Literal> getClauseFromString(String clauseString) throws Exception {
        ArrayList<Literal> listOfLiterals = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();

        // Remove leading and following spaces
        clauseString = clauseString.strip();

        // In valid clause must be at least two parenthesis
        if (clauseString.length() < 2) {
            throw new Exception();
        }

        // Collect literals by splitting clause at OR operations
        for (Character ch : clauseString.toCharArray()) {
            // Not reached OR -> collect character to buffer
            if (ch != allowedOperations.get(BooleanOperation.OR)) {
                buffer.append(ch);
            }

            // When OR reached -> try to parse literal
            else {
                Literal newLiteral = getLiteralFromString(buffer.toString());
                listOfLiterals.add(newLiteral);
                buffer.setLength(0);
            }
        }

        // At this point buffer still contain something
        Literal lastLiteral = getLiteralFromString(buffer.toString());
        listOfLiterals.add(lastLiteral);

        return listOfLiterals;
    }

    public ArrayList<ArrayList<Literal>> getFormulaFromString(String formulaString) throws Exception {
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

        return clauses;
    }
}
