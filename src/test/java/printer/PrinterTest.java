package printer;

import expression.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrinterTest {
    private final String expressionCode = "a -> (b <-> a || b && !a) && true || false";
    private final Expression expression = new Implication(
            new Variable("a"),
            new Disjunction(
                    new Conjunction(
                            new Equivalence(
                                    new Variable("b"),
                                    new Disjunction(
                                            new Variable("a"),
                                            new Conjunction(
                                                    new Variable("b"),
                                                    new Negation(new Variable("a"))
                                            )
                                    )
                            ),
                            new Constant(true)
                    ),
                    new Constant(false)
            )
    );

    private final String implicationsCode = "a -> (b -> c)";
    private final Expression implications = new Implication(
            new Variable("a"),
            new Implication(new Variable("b"), new Variable("c")));

    @Test
    public void testPrint() {
        Printer printer = new Printer();
        assertEquals(expressionCode, printer.print(expression));
    }

    @Test
    public void testAssociativity() {
        Printer printer = new Printer();
        assertEquals(implicationsCode, printer.print(implications));
    }

}