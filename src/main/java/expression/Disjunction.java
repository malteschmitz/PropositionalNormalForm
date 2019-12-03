package expression;

public class Disjunction extends Expression {
    public final Expression leftHandSide;
    public final Expression rightHandSide;

    public Disjunction(Expression leftHandSide, Expression rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Disjunction that = (Disjunction) o;

        if (!leftHandSide.equals(that.leftHandSide)) return false;
        return rightHandSide.equals(that.rightHandSide);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitDisjunction(this);
    }
}
