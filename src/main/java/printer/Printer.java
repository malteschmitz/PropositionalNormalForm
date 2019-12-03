package printer;

import expression.*;

public class Printer extends Visitor<String> {
    public static class PrecedenceVisitor extends Visitor<Integer> {
        @Override
        public Integer visitEquivalence(Equivalence equivalence) {
            return 0;
        }

        @Override
        public Integer visitImplication(Implication implication) {
            return 1;
        }

        @Override
        public Integer visitDisjunction(Disjunction disjunction) {
            return 2;
        }

        @Override
        public Integer visitConjunction(Conjunction conjunction) {
            return 3;
        }

        @Override
        public Integer visitNegation(Negation negation) {
            return 4;
        }

        @Override
        public Integer visitVariable(Variable variable) {
            return 5;
        }

        @Override
        public Integer visitConstant(Constant constant) {
            return 6;
        }
    }

    private PrecedenceVisitor precedenceVisitor = new PrecedenceVisitor();

    private String addBrackets(Expression outer, Expression inner) {
        return addBrackets(outer, inner, false);
    }

    private String addBrackets(Expression outer, Expression inner, boolean strict) {
        int outerPrecedence = precedenceVisitor.visit(outer);
        int innerPrecedence = precedenceVisitor.visit(inner);
        if (outerPrecedence > innerPrecedence || strict && (outerPrecedence == innerPrecedence)) {
            return "(" + visit(inner) + ")";
        } else {
            return visit(inner);
        }
    }

    public String visitEquivalence(Equivalence equivalence) {
        return addBrackets(equivalence, equivalence.leftHandSide) + " <-> " + addBrackets(equivalence, equivalence.rightHandSide);
    }

    public String visitImplication(Implication implication) {
        return addBrackets(implication, implication.leftHandSide, true) + " -> " + addBrackets(implication, implication.rightHandSide, true);
    }

    public String visitConjunction(Conjunction conjunction) {
        return addBrackets(conjunction, conjunction.leftHandSide) + " && " + addBrackets(conjunction, conjunction.rightHandSide);
    }

    public String visitDisjunction(Disjunction disjunction) {
        return addBrackets(disjunction, disjunction.leftHandSide) + " || " + addBrackets(disjunction, disjunction.rightHandSide);
    }

    public String visitNegation(Negation negation) {
        return "!" + addBrackets(negation, negation.operand);
    }

    public String visitVariable(Variable variable) {
        return variable.name;
    }

    public String visitConstant(Constant constant) {
        return constant.value ? "true" : "false";
    }

    public String print(Expression expression) {
        return visit(expression);
    }
}
