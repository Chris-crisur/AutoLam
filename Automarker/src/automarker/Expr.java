package automarker;

import automarker.Context;
import java.util.HashSet;

abstract class Expr {

    private static char[] left_parens = "(([(({".toCharArray();
    private static char[] right_parens = "))]))}".toCharArray();

    abstract Object visit(Visitor paramVisitor);

    abstract Object visitP(VisitorP paramVisitorP, Object paramObject);

    static abstract interface Visitor {

        public abstract Object visitAbstract(Expr.Abstract paramAbstract);

        public abstract Object visitApply(Expr.Apply paramApply);

        public abstract Object visitIdent(Expr.Ident paramIdent);
    }

    static abstract interface VisitorP {

        public abstract Object visitAbstract(Expr.Abstract paramAbstract, Object paramObject);

        public abstract Object visitApply(Expr.Apply paramApply, Object paramObject);

        public abstract Object visitIdent(Expr.Ident paramIdent, Object paramObject);
    }

    static class Abstract extends Expr {

        Abstract(Symbol paramSymbol, Expr paramExpr) {
            this.sym = paramSymbol;
            this.expr = paramExpr;
        }

        final Symbol sym;
        final Expr expr;

        void addBounds(HashSet paramHashSet1, HashSet paramHashSet2) {
            paramHashSet1.add(this.sym);
            this.expr.addBounds(paramHashSet1, paramHashSet2);
        }

        Object visit(Expr.Visitor paramVisitor) {
            return paramVisitor.visitAbstract(this);
        }

        Object visitP(Expr.VisitorP paramVisitorP, Object paramObject) {
            return paramVisitorP.visitAbstract(this, paramObject);
        }

        int size() {
            return 1 + this.expr.size();
        }

        boolean uses(Symbol paramSymbol) {
            return this.expr.uses(paramSymbol);
        }
    }

    static class Apply extends Expr {

        final Expr left;
        final Expr right;

        Apply(Expr paramExpr1, Expr paramExpr2) {
            this.left = paramExpr1;
            this.right = paramExpr2;
        }

        void addBounds(HashSet paramHashSet1, HashSet paramHashSet2) {
            this.left.addBounds(paramHashSet1, paramHashSet2);
            this.right.addBounds(paramHashSet1, paramHashSet2);
        }

        Object visit(Expr.Visitor paramVisitor) {
            return paramVisitor.visitApply(this);
        }

        Object visitP(Expr.VisitorP paramVisitorP, Object paramObject) {
            return paramVisitorP.visitApply(this, paramObject);
        }

        int size() {
            return 1 + this.left.size() + this.right.size();
        }

        boolean uses(Symbol paramSymbol) {
            return (this.left.uses(paramSymbol)) || (this.right.uses(paramSymbol));
        }
    }

    static class Ident extends Expr {

        final Symbol sym;

        Ident(Symbol paramSymbol) {
            this.sym = paramSymbol;
        }

        void addBounds(HashSet paramHashSet1, HashSet paramHashSet2) {
            if (!paramHashSet1.contains(this.sym)) {
                paramHashSet2.add(this.sym);
            }
        }

        Object visit(Expr.Visitor paramVisitor) {
            return paramVisitor.visitIdent(this);
        }

        Object visitP(Expr.VisitorP paramVisitorP, Object paramObject) {
            return paramVisitorP.visitIdent(this, paramObject);
        }

        int size() {
            return 1;
        }

        boolean uses(Symbol paramSymbol) {
            return this.sym == paramSymbol;
        }
    }

    private static class PrintVisitor implements Expr.VisitorP {

        boolean substitute_at_top = true;

        Context context;
        int max_len;
        boolean vary_parens;
        StringBuffer output = new StringBuffer();
        int paren_depth = 0;
        int paren_cur = -1;
        HashSet bounds = new HashSet();
        HashSet frees = new HashSet();
        HashSet free_names = new HashSet();
        HashSet ident_names = new HashSet();
        java.util.HashMap sym_map = new java.util.HashMap();
        boolean exceeded = false;
        int last_alloc = -1;
        boolean visited = false;

        PrintVisitor(Expr paramExpr, Context paramContext, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
            this.context = paramContext;
            this.max_len = paramInt;
            this.vary_parens = paramBoolean1;
            this.substitute_at_top = paramBoolean2;

            paramExpr.addBounds(this.bounds, this.frees);
            java.util.Iterator localIterator = this.frees.iterator();
            Symbol localSymbol;
            while (localIterator.hasNext()) {
                localSymbol = (Symbol) localIterator.next();
                this.free_names.add(localSymbol.toString());
                this.ident_names.add(localSymbol.toString());
            }
            localIterator = this.bounds.iterator();
            while (localIterator.hasNext()) {
                localSymbol = (Symbol) localIterator.next();
                this.ident_names.add(localSymbol.toString());
            }
        }

        private String allocate() {
            for (;;) {
                this.last_alloc -= 1;
                if (this.last_alloc >= 0) {
                    if (this.ident_names.contains("i" + this.last_alloc)) {
                        break;
                    }
                }
            }
            String str;
            do {
                this.last_alloc += 1;
                str = "i" + this.last_alloc;
            } while (this.ident_names.contains(str));
            return str;
        }

        private char getLeftParen() {
            this.paren_depth += 1;
            if (!this.vary_parens) {
                return '(';
            }
            this.paren_cur += 1;
            if (this.paren_cur == Expr.left_parens.length) {
                this.paren_cur = 0;
            }
            return Expr.left_parens[this.paren_cur];
        }

        private char getRightParen() {
            this.paren_depth -= 1;
            if (!this.vary_parens) {
                return ')';
            }
            char c = Expr.right_parens[this.paren_cur];
            this.paren_cur -= 1;
            if (this.paren_cur < 0) {
                this.paren_cur = (Expr.right_parens.length - 1);
            }
            return c;
        }

        private boolean append(char paramChar) {
            return append("" + paramChar);
        }

        private boolean append(String paramString) {
            if ((this.output.length() + paramString.length() + this.paren_depth + 5 <= this.max_len) && (!this.exceeded)) {
                this.output.append(paramString);
                return true;
            }
            if (!this.exceeded) {
                this.output.append(" ... ");
                this.exceeded = true;
                return false;
            }
            return false;
        }

        boolean checkForSubstitution(Expr paramExpr) {
            if (this.context != null) {
                if ((this.substitute_at_top) || (this.visited)) {
                    String str = this.context.invert(paramExpr);
                    if (str != null) {
                        append(str);
                        return true;
                    }
                }
                this.visited = true;
            }
            return false;
        }

        public Object visitAbstract(Expr.Abstract paramAbstract, Object paramObject) {
            if (this.exceeded) {
                return null;
            }
            if (checkForSubstitution(paramAbstract)) {
                return null;
            }
            String str = paramAbstract.sym.toString();
            if (this.free_names.contains(str)) {
                str = allocate();
                this.sym_map.put(paramAbstract.sym, str);
            }
            this.free_names.add(str);
            this.ident_names.add(str);

            boolean bool = false;
            if (paramObject == Boolean.TRUE) {
                bool = append(getLeftParen());
            }
            append("\\" + str + ".");
            paramAbstract.expr.visitP(this, Boolean.FALSE);
            if (paramObject == Boolean.TRUE) {
                if (bool) {
                    this.output.append(getRightParen());
                } else {
                    getRightParen();
                }
            }
            this.free_names.remove(str);
            this.ident_names.remove(str);
            if (str != paramAbstract.sym.toString()) {
                this.sym_map.remove(paramAbstract.sym);
            }
            return null;
        }

        public Object visitApply(Expr.Apply paramApply, Object paramObject) {
            if (this.exceeded) {
                return null;
            }
            if (checkForSubstitution(paramApply)) {
                return null;
            }
            paramApply.left.visitP(this, Boolean.TRUE);
            append(" ");
            if ((paramApply.right instanceof Expr.Apply)) {
                boolean bool = append(getLeftParen());
                paramApply.right.visitP(this, Boolean.FALSE);
                if (bool) {
                    this.output.append(getRightParen());
                } else {
                    getRightParen();
                }
            } else {
                paramApply.right.visitP(this, Boolean.TRUE);
            }

            return null;
        }

        public Object visitIdent(Expr.Ident paramIdent, Object paramObject) {
            if (checkForSubstitution(paramIdent)) {
                return null;
            }
            String str = (String) this.sym_map.get(paramIdent.sym);
            append(str != null ? str : paramIdent.sym.toString());
            return null;
        }
    }

    abstract void addBounds(HashSet paramHashSet1, HashSet paramHashSet2);

    abstract boolean uses(Symbol paramSymbol);

    abstract int size();

    public String toString() {
        return toString(null, Integer.MAX_VALUE, false);
    }

    public String toString(Context paramContext) {
        return toString(paramContext, Integer.MAX_VALUE, false);
    }

    public String toString(Context paramContext, int paramInt, boolean paramBoolean) {
        PrintVisitor localPrintVisitor = new PrintVisitor(this, paramContext, paramInt, paramBoolean, true);

        visitP(localPrintVisitor, Boolean.FALSE);
        return localPrintVisitor.output.toString();
    }

    public String toStringSubstituteBelow(Context paramContext, int paramInt, boolean paramBoolean) {
        PrintVisitor localPrintVisitor = new PrintVisitor(this, paramContext, paramInt, paramBoolean, false);

        visitP(localPrintVisitor, Boolean.FALSE);
        return localPrintVisitor.output.toString();
    }

    public boolean equals(Object paramObject) {
        if ((paramObject instanceof Expr)) {
            return this == paramObject;
        }
        if ((paramObject instanceof ExprWrapper)) {
            return ((ExprWrapper) paramObject).equals(this);
        }
        return false;
    }
}


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\Expr.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
