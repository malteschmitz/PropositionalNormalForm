package converter;

import expression.*;

/**
 * Generates a disjunctive normal form (DNF) for a given expression in negation normal form (NNF). You must not call
 * this visitor with expressions which are not desugared or not already in negation normal form.
 */
public class DisjunctiveNormalFormGenerator extends Visitor<Expression> {
    @Override
    public Expression visitDisjunction(Disjunction disjunction) {
        // disjunctions are allowed on the outer level. Continue with translating the operands.
        return new Disjunction(visit(disjunction.leftHandSide), visit(disjunction.rightHandSide));
    }

    @Override
    public Expression visitConjunction(Conjunction conjunction) {
        // Translate the operands first
        Expression lhs = visit(conjunction.leftHandSide);
        Expression rhs = visit(conjunction.rightHandSide);
        // If any of the translated operands is a disjunction then we still have a disjunction inside a conjunction
        // which is not allowed in a DNF. Thus we replace this conjunction with a disjunction of conjunctions.
        if (lhs instanceof Disjunction) {
            Disjunction disjunction = (Disjunction) lhs;
            Expression l = disjunction.leftHandSide;
            Expression r = disjunction.rightHandSide;
            return new Disjunction(visit(new Conjunction(l, rhs)), visit(new Conjunction(r, rhs)));
        } else if (rhs instanceof Disjunction) {
            Disjunction disjunction = (Disjunction) rhs;
            Expression l = disjunction.leftHandSide;
            Expression r = disjunction.rightHandSide;
            return new Disjunction(visit(new Conjunction(lhs, l)), visit(new Conjunction(lhs, r)));
        } else {
            // Neither of the operands is a disjunction so we can return the current conjunction with its
            // translated operands.
            return new Conjunction(lhs, rhs);
        }
    }

    @Override
    public Expression visitNegation(Negation negation) {
        // We are visiting an expression in NNF, thus the negation is only allowed in a literal
        return negation;
    }

    @Override
    public Expression visitVariable(Variable variable) {
        // Variables are allowed in DNF
        return variable;
    }

    @Override
    public Expression visitConstant(Constant constant) {
        // Constants are allowed in CNF
        return constant;
    }
}
