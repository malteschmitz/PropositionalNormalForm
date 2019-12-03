package expression;

public class Implication extends Expression {
    public final Expression leftHandSide;
    public final Expression rightHandSide;

    public Implication(Expression leftHandSide, Expression rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    // generated method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Implication that = (Implication) o;

        if (!leftHandSide.equals(that.leftHandSide)) return false;
        return rightHandSide.equals(that.rightHandSide);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitImplication(this);
    }
}
