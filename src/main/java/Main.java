public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("This project will complete reductions of search problems.");

        CnfFormula formula = new CnfFormula("(a | b | c) * a");
        System.out.println(formula);
    }
}