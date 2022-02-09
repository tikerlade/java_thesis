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
    ArrayList<ArrayList<Literal>> clauses;

    String inputFormula;

    HashMap<BooleanOperation, Character> allowedOperations = new HashMap<>() {{
        put(BooleanOperation.NEGATION, '-');
        put(BooleanOperation.AND, '&');
        put(BooleanOperation.OR, '|');
    }};

    public CnfFormula (String formulaString) throws Exception {
        // Save users formula
        inputFormula = formulaString;

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
            }
        }

        // At this point buffer still contain something
        Literal lastLiteral = new Literal(buffer.toString(), literalCounter++);
        listOfLiterals.add(lastLiteral);

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
}


