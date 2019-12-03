package expression;

abstract public class Expression {
    public abstract<T> T accept(Visitor<T> visitor);
}
