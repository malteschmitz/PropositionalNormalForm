package expression;

public class Equivalence extends Expression {
    public final Expression leftHandSide;
    public final Expression rightHandSide;

    public Equivalence(Expression leftHandSide, Expression rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Equivalence that = (Equivalence) o;

        if (!leftHandSide.equals(that.leftHandSide)) return false;
        return rightHandSide.equals(that.rightHandSide);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitEquivalence(this);
    }
}
