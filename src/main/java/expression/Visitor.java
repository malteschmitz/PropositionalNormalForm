package expression;

/**
 * Abstract expression visitor. An Expression accepts this visitor and every class inheriting from Expression
 * calls the corresponding visit method of this visitor.
 * @param <T> result type
 */
public abstract class Visitor<T> {
    public T visitEquivalence(Equivalence equivalence) {
        throw new UnsupportedOperationException("visitEquivalence is not implemented");
    }

    public T visitImplication(Implication implication) {
        throw new UnsupportedOperationException("visitImplication is not implemented");
    }

    public T visitDisjunction(Disjunction disjunction) {
        throw new UnsupportedOperationException("visitDisjunction is not implemented");
    }

    public T visitConjunction(Conjunction conjunction) {
        throw new UnsupportedOperationException("visitConjunction is not implemented");
    }

    public T visitNegation(Negation negation) {
        throw new UnsupportedOperationException("visitNegation is not implemented");
    }

    public T visitVariable(Variable variable) {
        throw new UnsupportedOperationException("visitVariable is not implemented");
    }

    public T visitConstant(Constant constant) {
        throw new UnsupportedOperationException("visitConstant is not implemented");
    }

    public T visit(Expression expression) {
        return expression.accept(this);
    }
}
