package automarker;

import automarker.Expr;
import java.util.HashMap;

abstract class Builtin {

    private static abstract class BinaryOperator extends Builtin {

        BinaryOperator(Builtin param1) {
            this();
        }

        int numArguments() {
            return 2;
        }

        boolean canApply(Expr[] paramArrayOfExpr) {
            if (((paramArrayOfExpr[0] instanceof Expr.Ident)) && ((paramArrayOfExpr[1] instanceof Expr.Ident))) {
                String str1 = ((Expr.Ident) paramArrayOfExpr[0]).sym.toString();
                String str2 = ((Expr.Ident) paramArrayOfExpr[1]).sym.toString();
                try {
                    Integer.parseInt(str1);
                    Integer.parseInt(str2);
                    return true;
                } catch (NumberFormatException localNumberFormatException) {
                }
            }
            return false;
        }

        Expr apply(Expr[] paramArrayOfExpr) {
            String str1 = ((Expr.Ident) paramArrayOfExpr[0]).sym.toString();
            String str2 = ((Expr.Ident) paramArrayOfExpr[1]).sym.toString();
            String str3 = computeResult(Integer.parseInt(str1), Integer.parseInt(str2));

            return new Expr.Ident(new Symbol(str3));
        }

        private BinaryOperator() {
        }

        abstract String computeResult(int paramInt1, int paramInt2);
    }

    private static class AddOperator extends Builtin.BinaryOperator {

        private AddOperator() {
            super();
        }

        AddOperator(Builtin param1) {
            this();
        }

        String computeResult(int paramInt1, int paramInt2) {
            return "" + (paramInt1 + paramInt2);
        }
    }

    private static class SubOperator extends Builtin.BinaryOperator {

        private SubOperator() {
            super();
        }

        SubOperator(Builtin param1) {
            this();
        }

        String computeResult(int paramInt1, int paramInt2) {
            return "" + (paramInt1 - paramInt2);
        }
    }

    private static class MultOperator extends Builtin.BinaryOperator {

        private MultOperator() {
            super();
        }

        MultOperator(Builtin param1) {
            this();
        }

        String computeResult(int paramInt1, int paramInt2) {
            return "" + paramInt1 * paramInt2;
        }
    }

    private static class DivOperator extends Builtin.BinaryOperator {

        private DivOperator() {
            super();
        }

        DivOperator(Builtin param1) {
            this();
        }

        String computeResult(int paramInt1, int paramInt2) {
            return "" + paramInt1 / paramInt2;
        }
    }

    private static class EqOperator extends Builtin.BinaryOperator {

        private EqOperator() {
            super();
        }

        EqOperator(Builtin param1) {
            this();
        }

        String computeResult(int paramInt1, int paramInt2) {
            return "" + (paramInt1 == paramInt2);
        }
    }

    private static class NeOperator extends Builtin.BinaryOperator {

        private NeOperator() {
            super();
        }

        NeOperator(Builtin param1) {
            this();
        }

        String computeResult(int paramInt1, int paramInt2) {
            return "" + (paramInt1 != paramInt2);
        }
    }

    private static class LtOperator extends Builtin.BinaryOperator {

        private LtOperator() {
            super();
        }

        LtOperator(Builtin param1) {
            this();
        }

        String computeResult(int paramInt1, int paramInt2) {
            return "" + (paramInt1 < paramInt2);
        }
    }

    private static class GtOperator extends Builtin.BinaryOperator {

        private GtOperator() {
            super();
        }

        GtOperator(Builtin param1) {
            this();
        }

        String computeResult(int paramInt1, int paramInt2) {
            return "" + (paramInt1 > paramInt2);
        }
    }

    private static class LeOperator extends Builtin.BinaryOperator {

        private LeOperator() {
            super();
        }

        LeOperator(Builtin param1) {
            this();
        }

        String computeResult(int paramInt1, int paramInt2) {
            return "" + (paramInt1 <= paramInt2);
        }
    }

    private static class GeOperator extends Builtin.BinaryOperator {

        private GeOperator() {
            super();
        }

        GeOperator(Builtin param1) {
            this();
        }

        String computeResult(int paramInt1, int paramInt2) {
            return "" + (paramInt1 >= paramInt2);
        }
    }

    private static class IfOperator extends Builtin {

        IfOperator(Builtin param1) {
            this();
        }

        int numArguments() {
            return 3;
        }

        boolean canApply(Expr[] paramArrayOfExpr) {
            if ((paramArrayOfExpr[0] instanceof Expr.Ident)) {
                String str = ((Expr.Ident) paramArrayOfExpr[0]).sym.toString();
                if ((str.equals("true")) || (str.equals("false"))) {
                    return true;
                }
            }
            return false;
        }

        Expr apply(Expr[] paramArrayOfExpr) {
            String str = ((Expr.Ident) paramArrayOfExpr[0]).sym.toString();
            if (str.equals("true")) {
                return paramArrayOfExpr[1];
            }
            return paramArrayOfExpr[2];
        }

        private IfOperator() {
        }
    }

    static HashMap builtins = new HashMap();

    static {
        builtins.put("+", new AddOperator(null));
        builtins.put("-", new SubOperator(null));
        builtins.put("*", new MultOperator(null));
        builtins.put("/", new DivOperator(null));
        builtins.put("=", new EqOperator(null));
        builtins.put("/=", new NeOperator(null));
        builtins.put("<", new LtOperator(null));
        builtins.put(">", new GtOperator(null));
        builtins.put("<=", new LeOperator(null));
        builtins.put(">=", new GeOperator(null));
        builtins.put("if", new IfOperator(null));
    }

    static Builtin get(String paramString, int paramInt) {
        Builtin localBuiltin = (Builtin) builtins.get(paramString);
        if ((localBuiltin != null) && (localBuiltin.numArguments() == paramInt)) {
            return localBuiltin;
        }
        return null;
    }

    abstract Expr apply(Expr[] paramArrayOfExpr);

    abstract boolean canApply(Expr[] paramArrayOfExpr);

    abstract int numArguments();
}
