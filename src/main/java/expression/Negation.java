package expression;

public class Negation extends Expression {
    public final Expression operand;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Negation negation = (Negation) o;

        return operand.equals(negation.operand);
    }

    public Negation(Expression operand) {
        this.operand = operand;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitNegation(this);
    }
}
