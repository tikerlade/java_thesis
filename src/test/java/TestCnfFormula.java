import org.junit.jupiter.api.Test;
import ru.spbstu.icst.exceptions.InputException;
import ru.spbstu.icst.problems.CnfFormula;
import ru.spbstu.icst.problems.Literal;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCnfFormula {

    @Test
    public void testSimple() {
        String[] testFormulas = {"(-a)", "(-abc)", " (abc ) ", "(-abc | abc  |  d)", "(a | -b | c ) & (a  |c)&(a) "};
        String[][][] actualNames = {{{"a"}}, {{"abc"}}, {{"abc"}}, {{"abc", "abc", "d"}}, {{"a", "b", "c"}, {"a", "c"}, {"a"}}};
        Boolean[][][] actualNegations = {{{true}}, {{true}}, {{false}}, {{true, false, false}}, {{false, true, false}, {false, false}, {false}}};
        Integer[][][] actualIndexes = {{{0}}, {{0}}, {{0}}, {{0, 1, 2}}, {{0, 1, 2}, {3, 4}, {5}}};


        // Firstly run empty constructor
        CnfFormula empty = new CnfFormula();
        assertEquals(empty.clauses.size(), 0);

        try {
            for (int i = 0; i < testFormulas.length; i++) {
                CnfFormula testFormula = new CnfFormula(testFormulas[i]);

                    for (int j = 0; j < testFormula.clauses.size(); j++) {
                        ArrayList<Literal> clause = testFormula.clauses.get(j);

                        for (int k = 0; k < clause.size(); k++) {
                            Literal literal = clause.get(k);

                            assertEquals(literal.getName(), actualNames[i][j][k]);
                            assertEquals(literal.getNegation(), actualNegations[i][j][k]);
                            assertEquals(literal.getIndex(), actualIndexes[i][j][k]);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testErrors() {
        // TODO match messages for exceptions
        // TODO fix last exception to return correct answer - allowed characters introduction
        String[] testFormulas = {"()", "a", "(a | b", "(a | b | c) & a", "(a | b | c) * a"};
        String[] actualExceptionMessages = {
                "problems.Literal must consist at least of one character.",
                "Clauses must be covered in parentheses.",
                "problems.Literal must consist at least of one character.",
                "Clauses must be covered in parentheses.",
                "Not allowed character: a, clause must end with closing parentheses."
        };

        try {
            for (int i = 0; i < testFormulas.length; i++) {
                int finalI = i;
                InputException exception = assertThrows(InputException.class, () -> new CnfFormula(testFormulas[finalI]));
                assertEquals(actualExceptionMessages[i], exception.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testToString() {
        String[] testFormulas = {"(-a)", "(-abc)", " (abc ) ", "(-abc | abc  |  d)", "(a | -b |   c) & (a|c)&(a) "};
        String[] actualStrings = {"(-a)", "(-abc)", "(abc)", "(-abc | abc | d)", "(a | -b | c) & (a | c) & (a)"};

        try {
            for (int i = 0; i < testFormulas.length; i++) {
                CnfFormula formula = new CnfFormula(testFormulas[i]);

                assertEquals(actualStrings[i], formula.toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
