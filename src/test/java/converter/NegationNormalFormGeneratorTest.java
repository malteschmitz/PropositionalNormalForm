package converter;

import expression.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NegationNormalFormGeneratorTest {
    private final Expression a = new Variable("a");
    private final Expression b = new Variable("b");
    private final Expression c = new Variable("c");
    private final Expression d = new Variable("d");
    private final Expression e = new Variable("e");
    private final Expression f = new Variable("f");
    private final Expression expression = new Conjunction(a, new Disjunction(b, new Conjunction(c,
            new Disjunction(d, new Conjunction(e, f)))));
    private final Expression negated = new Negation(expression);
    private final Expression nnf = new Disjunction(new Negation(a), new Conjunction(new Negation(b),
            new Disjunction(new Negation(c), new Conjunction(new Negation(d),
                    new Disjunction(new Negation(e), new Negation(f))))));
    private final Expression bool = new Disjunction(
            new Negation(new Disjunction(new Constant(true), new Constant(false))),
            new Disjunction(new Constant(true), new Constant(false)));
    private final Expression boolNnf = new Disjunction(
            new Conjunction(new Constant(false), new Constant(true)),
            new Disjunction(new Constant(true), new Constant(false)));

    private final NegationNormalFormGenerator negationNormalFormGenerator = new NegationNormalFormGenerator();

    @Test
    public void testNnf() {
        assertEquals(nnf, negationNormalFormGenerator.visit(negated));
    }

    @Test
    public void testNothingToDo() {
        assertEquals(expression, negationNormalFormGenerator.visit(expression));
    }

    @Test
    public void testBool() {
        assertEquals(boolNnf, negationNormalFormGenerator.visit(bool));
    }
}
