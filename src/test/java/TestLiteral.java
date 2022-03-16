import ru.spbstu.icst.exceptions.InputException;
import org.junit.jupiter.api.Test;
import ru.spbstu.icst.problems.Literal;

import static org.junit.jupiter.api.Assertions.*;

public class TestLiteral {

    @Test
    public void testSimple() {
        String[] testNames = {"a", "abc", " a ", "    abc  "};
        String[] actualNames = {"a", "abc", "a", "abc"};

        try {
            for (int i = 0; i < testNames.length; i++) {
                Literal testLiteral = new Literal(testNames[i], 0);

                assertEquals(testLiteral.getName(), actualNames[i]);
                assertEquals(testLiteral.getNegation(), false);
                assertEquals(0, testLiteral.getIndex());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testWithNegation() {
        String[] testNames = {"-a", "-abc", " -a ", "    -abc  "};
        String[] actualNames = {"a", "abc", "a", "abc"};

        try {
            for (int i = 0; i < testNames.length; i++) {
                Literal testLiteral = new Literal(testNames[i]);

                assertEquals(testLiteral.getName(), actualNames[i]);
                assertEquals(testLiteral.getNegation(), true);
                assertNull(testLiteral.getIndex());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testExceptions() {
        String[] testNames = {"- a", "&abc", "    ", ""};
        String[] actualExceptionMessages = {
                "Not allowed character: ' ' was used.",
                "Not allowed character: '&' was used.",
                "problems.Literal must consist at least of one character.",
                "problems.Literal must consist at least of one character."
        };

        try {
            for (int i = 0; i < testNames.length; i++) {
                int finalI = i;
                InputException exception = assertThrows(InputException.class, () -> new Literal(testNames[finalI]));
                assertEquals(actualExceptionMessages[i], exception.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testToString() {
        String[] testNames = {"-a", "abc", " -a   ", "    -abc  "};
        String[] actualNames = {"-a", "abc", "-a", "-abc"};

        try {
            for (int i = 0; i < testNames.length; i++) {
                Literal testLiteral = new Literal(testNames[i]);
                assertEquals(testLiteral.toString(), actualNames[i]);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSetters() {
        try {
            Literal testLiteral = new Literal("x");

            // Test prerequisites
            assertEquals(false, testLiteral.getNegation());
            assertEquals("x", testLiteral.getName());
            assertNull(testLiteral.getIndex());

            // Test index setter
            testLiteral.setIndex(10);
            assertEquals(10, testLiteral.getIndex());

            // Test name setter
            testLiteral.setName("y");
            assertEquals("y", testLiteral.getName());

            // Test negation setter
            testLiteral.setNegation(true);
            assertEquals(true, testLiteral.getNegation());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}