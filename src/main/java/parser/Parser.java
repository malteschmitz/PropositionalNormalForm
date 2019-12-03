package parser;

import expression.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parser {
    private int position;
    private final String input;

    public Parser(String input) {
        this.input = input;
    }

    private void whitespace() {
        while(position < input.length() && Character.isWhitespace(input.charAt(position))) {
            position += 1;
        }
    }

    private void consume(String token) {
        whitespace();
        if (position + token.length() <= input.length() && input.substring(position, position + token.length()).equals(token)) {
            position += token.length();
        } else {
            throw new SyntaxException(token, position);
        }
    }

    private boolean test(String token) {
        int start = position;
        boolean success;
        try {
            consume(token);
            success = true;
        } catch (SyntaxException se) {
            success = false;
        }
        position = start;
        return success;
    }

    private Expression equivalences() {
        List<Expression> expressions = new ArrayList<Expression>();
        expressions.add(implications());

        int start = position;
        while (test("<->")) {
            consume("<->");
            expressions.add(implications());
        }

        Expression expression = expressions.remove(0);
        for (Expression e: expressions) {
            expression = new Equivalence(expression, e);
        }
        return expression;
    }

    private Expression implications() {
        List<Expression> expressions = new ArrayList<Expression>();
        expressions.add(disjunctions());

        int start = position;
        while (test("->")) {
            consume("->");
            expressions.add(disjunctions());
        }

        // implications are right-associative
        Collections.reverse(expressions);
        Expression expression = expressions.remove(0);
        for (Expression e: expressions) {
            expression = new Implication(e, expression);
        }
        return expression;
    }

    private Expression disjunctions() {
        List<Expression> expressions = new ArrayList<Expression>();
        expressions.add(conjunctions());

        int start = position;
        while (test("||")) {
            consume("||");
            expressions.add(conjunctions());
        }

        Expression expression = expressions.remove(0);
        for (Expression e: expressions) {
            expression = new Disjunction(expression, e);
        }
        return expression;
    }

    private Expression conjunctions() {
        List<Expression> expressions = new ArrayList<Expression>();
        expressions.add(negation());

        int start = position;
        while (test("&&")) {
            consume("&&");
            expressions.add(negation());
        }

        Expression expression = expressions.remove(0);
        for (Expression e: expressions) {
            expression = new Conjunction(expression, e);
        }
        return expression;
    }

    private Expression negation() {
        boolean negate = false;
        if (test("!")) {
            consume("!");
            negate = true;
        }
        Expression atom = atom();
        if (negate) {
            return new Negation(atom);
        } else {
            return atom;
        }
    }

    private Expression atom() {
        int start = position;
        Expression result;
        try {
            result = constant();
        } catch (SyntaxException se) {
            position = start;
            try {
                result = variable();
            } catch (SyntaxException se2) {
                position = start;
                consume("(");
                result = equivalences();
                consume(")");
            }
        }
        return result;
    }

    private boolean isLowerLetter(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    private Variable variable() {
        whitespace();
        int start = position;
        while (position < input.length() && isLowerLetter(input.charAt(position))) {
            position += 1;
        }

        if (position > start) {
            return new Variable(input.substring(start, position));
        } else {
            throw new SyntaxException("Identifier", position);
        }
    }

    private Constant constant() {
        try {
            consume("true");
            return new Constant(true);
        } catch (SyntaxException se) {
            consume("false");
            return new Constant(false);
        }
    }

    public Expression parse() {
        position = 0;
        Expression expression = equivalences();
        whitespace();
        if (position < input.length()) {
            throw new SyntaxException("End of input", position);
        }
        return expression;
    }
}
