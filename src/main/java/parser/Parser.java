package parser;

import expression.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parses an expression into an Expression data structure.
 */
public class Parser {
    // The parser is a stateful object which keeps track of the current position in the input string.
    //
    // The parser implements the following grammar:
    //     E ::= E "<->" E | E "->" E | E "||" E | E "&&" E | "!" E | "(" E ")" | "true" | "false" | ID
    //     ID ::= ("a" | "b" | ... | "z")+
    // With the following operator precedence:
    // 1. constants ("true", "false") and variables (ID)
    // 2. negation ("!")
    // 3. conjunction ("&&")
    // 4. disjunction ("||")
    // 5. implication ("->")
    // 6. equivalence ("<->")

    // The functions defined later call each other recursively to descent down the following equivalent
    // non-left-recursive grammar:
    //     EQUIVALENCE ::= IMPLICATIONS ("<->" IMPLICATIONS)*
    //     IMPLICATIONS ::= DISJUNCTIONS ("->" DISJUNCTIONS)*
    //     DISJUNCTIONS ::= CONJUNCTIONS ("||" CONJUNCTIONS)*
    //     CONJUNCTIONS ::= NEGATIONS ("&&" NEGATIONS)*
    //     NEGATIONS ::= "!"* ATOM
    //     ATOM ::= CONSTANT | VARIABLE | "(" EXPRESSION ")"
    //     CONSTANT ::= "true" | "false"
    //     VARIABLE ::= ("a" | "b" | ... | "z")+

    private final String input;
    private int position;

    /**
     * Create a new parser for the given expression.
     *
     * @param input the expression
     */
    public Parser(String input) {
        this.input = input;
    }

    private void whitespace() {
        // ignore all whitespaces
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            position += 1;
        }
    }

    private void consume(String token) {
        whitespace();
        // If the next string at the current position with the length of the token is equivalent to the given token
        // then increment the current position by the length of the token. Raise a syntax exception if the token
        // does not occur next at the current position of the input.
        if (position + token.length() <= input.length() &&
                input.substring(position, position + token.length()).equals(token)) {
            position += token.length();
        } else {
            throw new SyntaxException(token, position);
        }
    }

    private boolean test(String token) {
        // Store the current position in order to look ahead the current position and reset it later.
        int start = position;
        boolean success;
        try {
            // Try to consume the given token and convert the potential syntax exception into an return value.
            consume(token);
            success = true;
        } catch (SyntaxException se) {
            success = false;
        }
        position = start;
        return success;
    }

    private Expression equivalences() {
        List<Expression> operands = new ArrayList<Expression>();
        operands.add(implications());

        int start = position;
        while (test("<->")) {
            consume("<->");
            operands.add(implications());
        }

        Expression expression = operands.remove(0);
        for (Expression e : operands) {
            expression = new Equivalence(expression, e);
        }
        return expression;
    }

    private Expression implications() {
        List<Expression> operands = new ArrayList<Expression>();
        operands.add(disjunctions());

        int start = position;
        while (test("->")) {
            consume("->");
            operands.add(disjunctions());
        }

        // implications are right-associative
        Collections.reverse(operands);
        Expression expression = operands.remove(0);
        for (Expression e : operands) {
            expression = new Implication(e, expression);
        }
        return expression;
    }

    private Expression disjunctions() {
        List<Expression> operands = new ArrayList<Expression>();
        operands.add(conjunctions());

        int start = position;
        while (test("||")) {
            consume("||");
            operands.add(conjunctions());
        }

        Expression expression = operands.remove(0);
        for (Expression e : operands) {
            expression = new Disjunction(expression, e);
        }
        return expression;
    }

    private Expression conjunctions() {
        List<Expression> operands = new ArrayList<Expression>();
        operands.add(negation());

        int start = position;
        while (test("&&")) {
            consume("&&");
            operands.add(negation());
        }

        Expression expression = operands.remove(0);
        for (Expression e : operands) {
            expression = new Conjunction(expression, e);
        }
        return expression;
    }

    private Expression negation() {
        int numberOfNegations = 0;
        while(test("!")) {
            consume("!");
            numberOfNegations += 1;
        }
        Expression result = atom();
        for (int i = 0; i < numberOfNegations; i++) {
            result = new Negation(result);
        }
        return result;
    }

    private Expression atom() {
        int start = position;
        Expression result;
        // And atom can either be a constant, a variable or another expression in parentheses. The choice is
        // realized by catching the syntax exceptions until we reach the last possible option. If that fails, too,
        // we do not catch the syntax exception but pass it on to our caller, because now we know that none of the
        // options of the choice do match the input at the current position.
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
        // Reset position to allow multiple calls to parse
        position = 0;
        Expression expression = equivalences();
        // Raise an exception if we did not consume the entire input
        if (position < input.length()) {
            throw new SyntaxException("End of input", position);
        }
        return expression;
    }
}
