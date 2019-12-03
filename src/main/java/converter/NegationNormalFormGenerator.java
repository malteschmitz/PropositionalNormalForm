package converter;

import expression.*;

/**
 * Converts a desugared expression into negation normal form. You must not call this visor with Implication or
 * Equivalence.
 */
public class NegationNormalFormGenerator extends Visitor<Expression> {

    /**
     * Returns a negation of the given expression by recursively applying DeMorgan rules until a double negation or
     * a variable or a constant is reached. In case of a double negation `visit` of the outer visitor is called to
     * ensure the proper creation of a negation normal form for the corresponding subexpression.
     */
    private class NegationVisitor extends Visitor<Expression> {

        @Override
        public Expression visitDisjunction(Disjunction disjunction) {
            // !(a && b) = !a || !b
            return new Conjunction(visit(disjunction.leftHandSide), visit(disjunction.rightHandSide));
        }

        @Override
        public Expression visitConjunction(Conjunction conjunction) {
            // !(a || b) = !a && !b
            return new Disjunction(visit(conjunction.leftHandSide), visit(conjunction.rightHandSide));
        }

        @Override
        public Expression visitNegation(Negation negation) {
            // !!a = a
            return NegationNormalFormGenerator.this.visit(negation.operand);
        }

        @Override
        public Expression visitVariable(Variable variable) {
            // !a is allowed in NNF
            return new Negation(variable);
        }

        @Override
        public Expression visitConstant(Constant constant) {
            // !true = false, !false = true
            return new Constant(!constant.value);
        }
    }

    private NegationVisitor negator = new NegationVisitor();

    @Override
    public Expression visitDisjunction(Disjunction disjunction) {
        // no negation, continue with translation of the operands
        return new Disjunction(visit(disjunction.leftHandSide), visit(disjunction.rightHandSide));
    }

    @Override
    public Expression visitConjunction(Conjunction conjunction) {
        // no negation, continue with translation of the operands
        return new Conjunction(visit(conjunction.leftHandSide), visit(conjunction.rightHandSide));
    }

    @Override
    public Expression visitNegation(Negation negation) {
        // use NegationVisitor to negate operand by applying DeMorgan or removal of double-negation
        return negator.visit(negation.operand);
    }

    @Override
    public Expression visitVariable(Variable variable) {
        // no negation, done
        return variable;
    }

    @Override
    public Expression visitConstant(Constant constant) {
        // no negation, done
        return constant;
    }
}
