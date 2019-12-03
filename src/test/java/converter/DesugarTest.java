package converter;

import expression.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DesugarTest {
    private final Expression expression = new Implication(
            new Variable("a"),
            new Equivalence(new Variable("b"), new Variable("c")));
    private final Expression desugared = new Disjunction(
            new Negation(new Variable("a")),
            new Conjunction(
                    new Disjunction(new Negation(new Variable("b")), new Variable("c")),
                    new Disjunction(new Negation(new Variable("c")), new Variable("b"))));

    @Test
    public void testDesugar() {
        Desugar desugar = new Desugar();
        assertEquals(desugared, desugar.visit(expression));
    }

    @Test
    public void testEquivalenceToImplication() {
        Desugar desugar = new Desugar();
        Expression equivalence = new Equivalence(new Variable("a"), new Variable("b"));
        Expression implications = new Conjunction(
                new Implication(new Variable("a"), new Variable("b")),
                new Implication(new Variable("b"), new Variable("a")));
        assertEquals(desugar.visit(equivalence), desugar.visit(implications));
    }
}
