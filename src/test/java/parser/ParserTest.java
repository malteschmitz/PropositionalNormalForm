package parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import expression.*;

public class ParserTest {
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

    private final String implicationsCode = "a -> b -> c";
    private final Expression implications = new Implication(new Variable("a"), new Implication(new Variable("b"), new Variable("c")));

    private final String implicationsLeftCode = "(a -> b) -> c";
    private final Expression implicationsLeft = new Implication(new Implication(new Variable("a"), new Variable("b")), new Variable("c"));

    @Test
    public void testParse() {
        Parser parser = new Parser(expressionCode);
        assertEquals(expression, parser.parse());
    }

    @Test
    public void testAssociativity() {
        Parser parser = new Parser(implicationsCode);
        assertEquals(implications, parser.parse());
    }

    @Test
    public void testLeftAssociativity() {
        Parser parser = new Parser(implicationsLeftCode);
        assertEquals(implicationsLeft, parser.parse());
    }
}
