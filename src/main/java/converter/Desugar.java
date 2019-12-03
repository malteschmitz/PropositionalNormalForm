package converter;

import expression.*;

/**
 * Replaces Equivalence and Implication with equivalent expressions using only conjunction, disjunction and negation.
 */
public class Desugar extends Visitor<Expression> {
    @Override
    public Expression visitEquivalence(Equivalence equivalence) {
        Expression lhs = visit(equivalence.leftHandSide);
        Expression rhs = visit(equivalence.rightHandSide);
        // a <-> b  =  (a -> b) && (b -> a)  =  (!a || b) && (!b || a)
        return new Conjunction(new Disjunction(new Negation(lhs), rhs), new Disjunction(new Negation(rhs), lhs));
    }

    @Override
    public Expression visitImplication(Implication implication) {
        Expression lhs = visit(implication.leftHandSide);
        Expression rhs = visit(implication.rightHandSide);
        // a -> b  =  !a || b
        return new Disjunction(new Negation(lhs), rhs);
    }

    @Override
    public Expression visitDisjunction(Disjunction disjunction) {
        // Keep disjunction and continue with translating the operands
        return new Disjunction(visit(disjunction.leftHandSide), visit(disjunction.rightHandSide));
    }

    @Override
    public Expression visitConjunction(Conjunction conjunction) {
        // Keep conjunction and continue with translating the operands
        return new Conjunction(visit(conjunction.leftHandSide), visit(conjunction.rightHandSide));
    }

    @Override
    public Expression visitNegation(Negation negation) {
        // Keep negation and continue with translating the operand
        return new Negation(visit(negation.operand));
    }

    @Override
    public Expression visitVariable(Variable variable) {
        // Keep variable
        return variable;
    }

    @Override
    public Expression visitConstant(Constant constant) {
        // Keep constant
        return constant;
    }
}
