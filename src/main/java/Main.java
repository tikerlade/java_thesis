import problems.Cnf3Formula;
import problems.CnfFormula;
import reductions.CnfTo3CnfReduction;

public class Main {
    public static boolean m(Integer[] arr) {
        arr[0] = 1;
        return true;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("This project will complete reductions of search problems.");

        CnfTo3CnfReduction reduction = new CnfTo3CnfReduction();

        CnfFormula formula = new CnfFormula("(a | b | c | d |   e) & (a | -b)");
//        CnfFormula formula = new CnfFormula("(a | b | c | d )");
        Cnf3Formula cnf3Formula = reduction.forward(formula);

        System.out.println(formula);
        System.out.println(cnf3Formula);
//
//        formula.solve();
//        System.out.println(formula.getSatisfyingSet());
//
//        cnf3Formula.solve();
//        System.out.println(cnf3Formula.getSatisfyingSet());

        System.out.println(reduction.forwardAndBackward(formula));
    }
}