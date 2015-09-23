package automarker;

/*
 * Decompiled with CFR 0_101.
 */
import automarker.Context;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

//Classes Referenced Gui, Expr, ExprWrapper
class Engine {

    //private Gui gui;
    private ArrayList<Expr> expressions;

    //private Runner runner = null;
    /*Engine(Gui gui) {
        this.gui = gui;
    }
    */
    Engine() {
    }
    
    ArrayList<Expr> addDefinition(String string) {
        return process(string);

    }

    private ArrayList<Expr> process(String string) {
        Expr expr;
        //JTextArea jTextArea = this.gui.getOutputArea();
       // Context context = this.gui.getContext();
        Context context = new Context();
        if (Options.getSubstituteSymbolsOption().getValue()) {
            //context2 = context;
        }
        boolean bl = Options.getVaryParenthesesOption().getValue();
        boolean bl2 = Options.getShowIntermediateOption().getValue();
        int n = Options.getMaxLengthOption().getValue();
        try {
            expr = Parser.parse(string);
        } catch (Parser.ParseException pe) {
            System.out.println(pe.getMessage());
            return null;
        }
        
        expr = context.substitute(expr);
        
        expressions = new ArrayList<>();
        expressions.add(expr);
           
        Expr expr3 = expr;
        int n2 = expr.size();
        Expr expr4 = Simplify.simplify(expr);
        HashSet<ExprWrapper> hashSet = new HashSet<>();
        Expr[] arrexpr = new Expr[100];
        int n3 = -1;
        int n4 = 0;

        while (expr4 != expr) {
            expr = expr4;
            if (bl2) {

                expressions.add(expr);
            }
            int n5 = expr.size();
            ExprWrapper exprWrapper = new ExprWrapper(expr);
            if (++n4 > Options.getMaxReductionsOption().getValue() || hashSet.contains(exprWrapper)) {
                //jTextArea.append("\n   = ... ");
                expr = expr3;
                expressions.add(expr);
                break;
            }
            if (++n3 == arrexpr.length) {
                n3 = 0;
            }
            if (arrexpr[n3] != null) {
                hashSet.remove(arrexpr[n3]);
            }
            arrexpr[n3] = expr;
            hashSet.add(exprWrapper);
            if (n5 < n2) {
                expr3 = expr;
                n2 = n5;
            }
            expr4 = Simplify.simplify(expr);
        }
        if (!bl2) {
            expressions.add(expr);
        }

        //display list of expressions
        //System.out.println("list of expr:");
        //for (int i = 0; i < expressions.size(); i++) {
        //    System.out.println(expressions.get(i));
        //}
        return expressions;
    }

}
