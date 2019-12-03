package parser;

public class SyntaxException extends RuntimeException {
    public final String expected;
    public final int position;

    public SyntaxException(String expected, int atPosition) {
        super(expected + " expected at position " + atPosition);
        this.expected = expected;
        this.position = atPosition;
    }
}
