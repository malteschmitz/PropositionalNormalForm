import expression.Expression;
import parser.Parser;
import printer.Printer;

public class Main {
    public static void main(String[] args) {
        String code = String.join(" ", args);
        Parser parser = new Parser(code);
        Expression expression = parser.parse();
        Printer printer = new Printer();
        System.out.println(printer.print(expression));
    }
}
