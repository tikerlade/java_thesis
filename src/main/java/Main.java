import problems.CnfFormula;
import reductions.CnfTo3CnfReduction;

public class Main {
    public static boolean m(Integer[] arr) {
        arr[0] = 1;
        return true;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("This project will complete reductions of search problems.");

//
//        Integer[] arr = {0, 1, 2, 3};
//
//        for (int n: arr) {
//            System.out.print(n);
//        }
//        Boolean v = m(arr);
//        System.out.println();
//
//        for (int n: arr) {
//            System.out.print(n);
//        }

        CnfTo3CnfReduction reduction = new CnfTo3CnfReduction();
        CnfFormula formula = new CnfFormula("(a | b) & (a | -b)");
        System.out.println(formula);

        System.out.println(reduction.forward(formula));

        formula.solve();


        formula.getLiteralsValueMatrix(7);
    }
}