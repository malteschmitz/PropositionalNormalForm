package expression;

public abstract class Visitor<T> {
    public abstract T visitEquivalence(Equivalence equivalence);
    public abstract T visitImplication(Implication implication);
    public abstract T visitDisjunction(Disjunction disjunction);
    public abstract T visitConjunction(Conjunction conjunction);
    public abstract T visitNegation(Negation negation);
    public abstract T visitVariable(Variable variable);
    public abstract T visitConstant(Constant constant);
    public T visit(Expression expression) {
        return expression.accept(this);
    }
}
