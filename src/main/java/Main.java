import converter.Desugar;
import converter.DisjunctiveNormalFormGenerator;
import converter.NegationNormalFormGenerator;
import expression.Expression;
import parser.Parser;
import printer.Printer;

public class Main {
    private static Printer printer = new Printer();

    private static void print(String name, Expression expression) {
        System.out.println(name + ":");
        System.out.println(printer.print(expression));
        System.out.println();
    }

    public static void main(String[] args) {
        // Use all command line arguments as one input expression
        String code = String.join(" ", args);

        // print parsed expression
        Parser parser = new Parser(code);
        Expression expression = parser.parse();
        print("Input", expression);

        // print desugared expression
        Desugar desugar = new Desugar();
        Expression desugared = desugar.visit(expression);
        print("Desugared", desugared);

        // print negation normal form
        NegationNormalFormGenerator negationNormalFormGenerator = new NegationNormalFormGenerator();
        Expression nnf = negationNormalFormGenerator.visit(desugared);
        print("Negation Normal Form", nnf);

        // print disjunctive normal form
        DisjunctiveNormalFormGenerator disjunctiveNormalFormGenerator = new DisjunctiveNormalFormGenerator();
        Expression dnf = disjunctiveNormalFormGenerator.visit(nnf);
        print("Disjunctive Normal Form", dnf);
    }
}
