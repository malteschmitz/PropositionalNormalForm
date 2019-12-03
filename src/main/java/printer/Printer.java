package printer;

import expression.*;

/**
 * Prints a given expression such that the parser would generate the same data structure. Adds only required
 * parenthesis.
 */
public class Printer extends Visitor<String> {
    private PrecedenceVisitor precedenceVisitor = new PrecedenceVisitor();

    /**
     * Convert the inner expression to a string and add parentheses if needed. Parentheses are needed if
     * the precedence would be different if one inserts the inner expression as operand without parenthesis
     * into the outermost operator of the outer expression.
     * @param outer the outer expression
     * @param inner the inner expression
     * @return the printed inner expression
     */
    private String addBrackets(Expression outer, Expression inner) {
        return addBrackets(outer, inner, false);
    }

    /**
     * Convert the inner expression to a string and add parentheses if needed. Parentheses are needed if
     * the precedence would be different if one inserts the inner expression as operand without parenthesis
     * into the outermost operator of the outer expression. The parameter strict can be used to enforce adding
     * parentheses even in those cases where the same operator is nested multiple times. This is useful for
     * non-associative operators.
     * @param outer the outer expression
     * @param inner the inner expression
     * @param strict indicates if parentheses should be added for operators nested into themselves
     * @return the printed inner expression
     */
    private String addBrackets(Expression outer, Expression inner, boolean strict) {
        int outerPrecedence = precedenceVisitor.visit(outer);
        int innerPrecedence = precedenceVisitor.visit(inner);
        if (outerPrecedence < innerPrecedence || strict && (outerPrecedence == innerPrecedence)) {
            return "(" + visit(inner) + ")";
        } else {
            return visit(inner);
        }
    }

    public String visitEquivalence(Equivalence equivalence) {
        return addBrackets(equivalence, equivalence.leftHandSide) + " <-> " +
                addBrackets(equivalence, equivalence.rightHandSide);
    }

    public String visitImplication(Implication implication) {
        // Parentheses are added strictly because implication is not associative, but right-associative.
        return addBrackets(implication, implication.leftHandSide, true) + " -> " +
                addBrackets(implication, implication.rightHandSide, true);
    }

    public String visitConjunction(Conjunction conjunction) {
        return addBrackets(conjunction, conjunction.leftHandSide) + " && " +
                addBrackets(conjunction, conjunction.rightHandSide);
    }

    public String visitDisjunction(Disjunction disjunction) {
        return addBrackets(disjunction, disjunction.leftHandSide) + " || " +
                addBrackets(disjunction, disjunction.rightHandSide);
    }

    public String visitNegation(Negation negation) {
        return "!" + addBrackets(negation, negation.operand);
    }

    public String visitVariable(Variable variable) {
        return variable.name;
    }

    public String visitConstant(Constant constant) {
        return constant.value ? "true" : "false";
    }

    public String print(Expression expression) {
        return visit(expression);
    }

    /**
     * Retrieves the precedence index for the outermost operator. A lower number represents a higher precedence.
     */
    public static class PrecedenceVisitor extends Visitor<Integer> {
        @Override
        public Integer visitEquivalence(Equivalence equivalence) {
            return 6;
        }

        @Override
        public Integer visitImplication(Implication implication) {
            return 5;
        }

        @Override
        public Integer visitDisjunction(Disjunction disjunction) {
            return 4;
        }

        @Override
        public Integer visitConjunction(Conjunction conjunction) {
            return 3;
        }

        @Override
        public Integer visitNegation(Negation negation) {
            return 2;
        }

        @Override
        public Integer visitVariable(Variable variable) {
            return 1;
        }

        @Override
        public Integer visitConstant(Constant constant) {
            return 1;
        }
    }
}
