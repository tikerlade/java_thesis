import org.junit.jupiter.api.Test;
import ru.spbstu.icst.problems.BooleanOperation;

import static org.junit.jupiter.api.Assertions.*;

public class TestBooleanOperation {

    @Test
    public void testToString() {
        String[] actualNames = {"-", "|", "&"};

        assertEquals(BooleanOperation.NEGATION.toString(), actualNames[0]);
        assertEquals(BooleanOperation.OR.toString(), actualNames[1]);
        assertEquals(BooleanOperation.AND.toString(), actualNames[2]);
    }

    @Test
    public void testComparison() {
        BooleanOperation[] operations = {BooleanOperation.NEGATION, BooleanOperation.OR, BooleanOperation.AND};

        for (int i = 0; i < operations.length; i++) {
            for (int j = 0; j < operations.length; j++) {
                if (i == j) {
                    assertTrue(operations[i].equalsName(operations[j].toString()));
                } else {
                    assertFalse(operations[i].equalsName(operations[j].toString()));
                }
            }
        }
    }
}
