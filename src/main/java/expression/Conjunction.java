package expression;

public class Conjunction extends Expression {
    public final Expression leftHandSide;
    public final Expression rightHandSide;

    // generated method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conjunction that = (Conjunction) o;

        if (!leftHandSide.equals(that.leftHandSide)) return false;
        return rightHandSide.equals(that.rightHandSide);
    }

    public Conjunction(Expression leftHandSide, Expression rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitConjunction(this);
    }
}
