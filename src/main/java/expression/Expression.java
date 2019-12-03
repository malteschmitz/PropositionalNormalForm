package expression;

/**
 * Common base class of all expressions. Every expression must accept the visitor.
 */
abstract public class Expression {
    public abstract<T> T accept(Visitor<T> visitor);
}
