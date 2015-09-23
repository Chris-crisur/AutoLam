package automarker;

import automarker.Expr;
import java.util.HashMap;

class ExprWrapper {

    private static final Boolean TRUE = Boolean.TRUE;
    private static final Boolean FALSE = Boolean.FALSE;

    private static class EqualsVisitor implements Expr.VisitorP {

        EqualsVisitor(ExprWrapper param1) {
            this();
        }
        HashMap sym_map = new HashMap();

        public Object visitAbstract(Expr.Abstract paramAbstract, Object paramObject) {
            if (!(paramObject instanceof Expr.Abstract)) {
                return ExprWrapper.FALSE;
            }
            Expr.Abstract localAbstract = (Expr.Abstract) paramObject;
            this.sym_map.put(paramAbstract.sym, localAbstract.sym);
            Object localObject = paramAbstract.expr.visitP(this, localAbstract.expr);
            this.sym_map.remove(paramAbstract.sym);
            return localObject;
        }

        public Object visitApply(Expr.Apply paramApply, Object paramObject) {
            if (!(paramObject instanceof Expr.Apply)) {
                return ExprWrapper.FALSE;
            }
            Expr.Apply localApply = (Expr.Apply) paramObject;
            if (paramApply.left.visitP(this, localApply.left) == ExprWrapper.FALSE) {
                return ExprWrapper.FALSE;
            }
            if (paramApply.right.visitP(this, localApply.right) == ExprWrapper.FALSE) {
                return ExprWrapper.FALSE;
            }
            return ExprWrapper.TRUE;
        }

        public Object visitIdent(Expr.Ident paramIdent, Object paramObject) {
            if (!(paramObject instanceof Expr.Ident)) {
                return ExprWrapper.FALSE;
            }
            Expr.Ident localIdent = (Expr.Ident) paramObject;
            Symbol localSymbol = (Symbol) this.sym_map.get(paramIdent.sym);
            if (localSymbol == null) {
                if (paramIdent.sym.toString().equals(localIdent.sym.toString())) {
                    return ExprWrapper.TRUE;
                }
                return ExprWrapper.FALSE;
            }

            return localIdent.sym == localSymbol ? ExprWrapper.TRUE : ExprWrapper.FALSE;
        }

        private EqualsVisitor() {
        }
    }

    private final Expr expr;

    private static class HashCodeComputer implements Expr.Visitor {

        HashCodeComputer(ExprWrapper param1) {
            this();
        }
        private HashMap sym_map = new HashMap();
        int last_ident = 0;

        public Object visitAbstract(Expr.Abstract paramAbstract) {
            this.last_ident += 1;
            this.sym_map.put(paramAbstract.sym, new Integer(this.last_ident));
            Object localObject = paramAbstract.expr.visit(this);
            this.sym_map.remove(paramAbstract.sym);
            return localObject;
        }

        public Object visitApply(Expr.Apply paramApply) {
            int i = ((Integer) paramApply.left.visit(this)).intValue();
            int j = ((Integer) paramApply.right.visit(this)).intValue();
            return new Integer(i * 31 + j);
        }

        public Object visitIdent(Expr.Ident paramIdent) {
            Integer localInteger = (Integer) this.sym_map.get(paramIdent.sym);
            if (localInteger == null) {
                localInteger = new Integer(paramIdent.sym.toString().hashCode());
            }
            return localInteger;
        }

        private HashCodeComputer() {
        }
    }

    private boolean hash_code_known = false;
    private int hash_code_val;

    public ExprWrapper(Expr paramExpr) {
        this.expr = paramExpr;
    }

    public int hashCode() {
        if (!this.hash_code_known) {
            Integer localInteger = (Integer) this.expr.visit(new HashCodeComputer(null));
            this.hash_code_val = localInteger.intValue();
            this.hash_code_known = true;
        }
        return this.hash_code_val;
    }

    public boolean equals(Object paramObject) {
        if ((paramObject instanceof ExprWrapper)) {
            ExprWrapper localExprWrapper = (ExprWrapper) paramObject;
            if (localExprWrapper.hashCode() != hashCode()) {
                return false;
            }
            if (localExprWrapper.expr == this.expr) {
                return true;
            }
            return this.expr.visitP(new EqualsVisitor(null), localExprWrapper.expr) == TRUE;
        }
        if ((paramObject instanceof Expr)) {
            return equals(new ExprWrapper((Expr) paramObject));
        }
        return false;
    }
}


