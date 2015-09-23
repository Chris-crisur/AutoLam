package automarker;

import automarker.Expr;
import java.util.HashMap;

class Simplify {

    static final int EAGER_EVALUATION = 0;
    static final int LAZY_EVALUATION = 1;
    static final int LAZY_SLOW_EVALUATION = 2;

    private static abstract class CandidateFinder implements Expr.Visitor {

        boolean use_etas;
        boolean use_applied;

        CandidateFinder(boolean paramBoolean1, boolean paramBoolean2) {
            this.use_etas = paramBoolean1;
            this.use_applied = paramBoolean2;
        }

        public Object visitAbstract(Expr.Abstract paramAbstract) {
            if (isCandidate(paramAbstract)) {
                return paramAbstract;
            }
            return paramAbstract.expr.visit(this);
        }

        public abstract Object visitApply(Expr.Apply paramApply);

        public Object visitIdent(Expr.Ident paramIdent) {
            if (isCandidate(paramIdent)) {
                return paramIdent;
            }
            return null;
        }

        boolean isCandidate(Expr paramExpr) {
            Object localObject1;
            Expr localExpr;
            if ((paramExpr instanceof Expr.Apply)) {
                localObject1 = ((Expr.Apply) paramExpr).left;
                localExpr = ((Expr.Apply) paramExpr).right;
                if ((localObject1 instanceof Expr.Abstract)) {
                    return true;
                }
            }
            Object localObject2;
            Object localObject3;
            if ((this.use_etas) && ((paramExpr instanceof Expr.Abstract))) {
                localObject1 = ((Expr.Abstract) paramExpr).sym;
                localExpr = ((Expr.Abstract) paramExpr).expr;
                if ((localExpr instanceof Expr.Apply)) {
                    localObject2 = ((Expr.Apply) localExpr).left;
                    localObject3 = ((Expr.Apply) localExpr).right;
                    if (((localObject3 instanceof Expr.Ident)) && (((Expr.Ident) localObject3).sym == localObject1) && (!((Expr) localObject2).uses((Symbol) localObject1))) {

                        return true;
                    }
                }
            }

            if (this.use_applied) {
                localObject1 = paramExpr;
                int i = 0;
                while ((localObject1 instanceof Expr.Apply)) {
                    localObject1 = ((Expr.Apply) localObject1).left;
                    i++;
                }
                if ((localObject1 instanceof Expr.Ident)) {
                    localObject2 = ((Expr.Ident) localObject1).sym.toString();
                    localObject3 = Builtin.get((String) localObject2, i);
                    if (localObject3 != null) {
                        Expr[] arrayOfExpr = new Expr[i];
                        localObject1 = paramExpr;
                        int j = i - 1;
                        while ((localObject1 instanceof Expr.Apply)) {
                            arrayOfExpr[j] = ((Expr.Apply) localObject1).right;
                            j--;
                            localObject1 = ((Expr.Apply) localObject1).left;
                        }
                        if (((Builtin) localObject3).canApply(arrayOfExpr)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        Expr getReplacement(Expr paramExpr) {
            Expr localExpr1;
            Expr localExpr2;
            Object localObject;
            if ((paramExpr instanceof Expr.Apply)) {
                localExpr1 = ((Expr.Apply) paramExpr).left;
                localExpr2 = ((Expr.Apply) paramExpr).right;
                if ((localExpr1 instanceof Expr.Abstract)) {
                    localObject = (Expr.Abstract) localExpr1;
                    return (Expr) ((Expr.Abstract) localObject).expr.visit(new Simplify.SymbolSubstitutor(((Expr.Abstract) localObject).sym, localExpr2));
                }
            }

            if ((this.use_etas) && ((paramExpr instanceof Expr.Abstract))) {
                localExpr1 = ((Expr.Abstract) paramExpr).expr;
                if ((localExpr1 instanceof Expr.Apply)) {
                    localExpr2 = ((Expr.Apply) localExpr1).left;
                    return localExpr2;
                }
            }

            if (this.use_applied) {
                localExpr1 = paramExpr;
                int i = 0;
                while ((localExpr1 instanceof Expr.Apply)) {
                    localExpr1 = ((Expr.Apply) localExpr1).left;
                    i++;
                }
                if ((localExpr1 instanceof Expr.Ident)) {
                    localObject = ((Expr.Ident) localExpr1).sym.toString();
                    Builtin localBuiltin = Builtin.get((String) localObject, i);
                    if (localBuiltin != null) {
                        Expr[] arrayOfExpr = new Expr[i];
                        localExpr1 = paramExpr;
                        int j = i - 1;
                        while ((localExpr1 instanceof Expr.Apply)) {
                            arrayOfExpr[j] = ((Expr.Apply) localExpr1).right;
                            j--;
                            localExpr1 = ((Expr.Apply) localExpr1).left;
                        }
                        return localBuiltin.apply(arrayOfExpr);
                    }
                }
            }

            return null;
        }
    }

    private static class LazyCandidateFinder
            extends Simplify.CandidateFinder {

        LazyCandidateFinder(boolean paramBoolean1, boolean paramBoolean2) {
            super(paramBoolean1, paramBoolean2);
        }

        public Object visitApply(Expr.Apply paramApply) {
            if (isCandidate(paramApply)) {
                return paramApply;
            }
            Object localObject = paramApply.left.visit(this);
            if (localObject != null) {
                return localObject;
            }
            return paramApply.right.visit(this);
        }
    }

    private static class EagerCandidateFinder
            extends Simplify.CandidateFinder {

        EagerCandidateFinder(boolean paramBoolean1, boolean paramBoolean2) {
            super(paramBoolean1, paramBoolean2);
        }

        public Object visitApply(Expr.Apply paramApply) {
            Object localObject = paramApply.right.visit(this);
            if (localObject != null) {
                return localObject;
            }
            if (isCandidate(paramApply)) {
                return paramApply;
            }
            return paramApply.left.visit(this);
        }
    }

    private static class SymbolSubstitutor implements Expr.Visitor {

        Symbol to_find;
        Expr to_replace;

        SymbolSubstitutor(Symbol paramSymbol, Expr paramExpr) {
            this.to_find = paramSymbol;
            this.to_replace = paramExpr;
        }

        public Object visitAbstract(Expr.Abstract paramAbstract) {
            if (paramAbstract.sym == this.to_find) {
                return paramAbstract;
            }
            Expr localExpr = (Expr) paramAbstract.expr.visit(this);
            return localExpr == paramAbstract.expr ? paramAbstract : new Expr.Abstract(paramAbstract.sym, localExpr);
        }

        public Object visitApply(Expr.Apply paramApply) {
            Expr localExpr1 = (Expr) paramApply.left.visit(this);
            Expr localExpr2 = (Expr) paramApply.right.visit(this);
            if ((localExpr1 == paramApply.left) && (localExpr2 == paramApply.right)) {
                return paramApply;
            }
            return new Expr.Apply(localExpr1, localExpr2);
        }

        public Object visitIdent(Expr.Ident paramIdent) {
            return paramIdent.sym == this.to_find ? this.to_replace : paramIdent;
        }
    }

    private static class LazySubstitutor implements Expr.Visitor {

        HashMap creations = new HashMap();
        Expr source;
        Expr dest;

        LazySubstitutor(Expr paramExpr1, Expr paramExpr2) {
            this.source = paramExpr1;
            this.dest = paramExpr2;
        }

        public Object visitAbstract(Expr.Abstract paramAbstract) {
            if (paramAbstract == this.source) {
                return this.dest;
            }
            Expr localExpr = (Expr) paramAbstract.expr.visit(this);
            if (localExpr == paramAbstract.expr) {
                return paramAbstract;
            }
            Object localObject = (Expr) this.creations.get(paramAbstract);
            if (localObject == null) {
                localObject = new Expr.Abstract(paramAbstract.sym, localExpr);
                this.creations.put(paramAbstract, localObject);
            }
            return localObject;
        }

        public Object visitApply(Expr.Apply paramApply) {
            if (paramApply == this.source) {
                return this.dest;
            }
            Expr localExpr1 = (Expr) paramApply.left.visit(this);
            Expr localExpr2 = (Expr) paramApply.right.visit(this);
            if ((localExpr1 == paramApply.left) && (localExpr2 == paramApply.right)) {
                return paramApply;
            }
            Object localObject = (Expr) this.creations.get(paramApply);
            if (localObject == null) {
                localObject = new Expr.Apply(localExpr1, localExpr2);
                this.creations.put(paramApply, localObject);
            }
            return localObject;
        }

        public Object visitIdent(Expr.Ident paramIdent) {
            return paramIdent == this.source ? this.dest : paramIdent;
        }
    }

    private static class EagerSubstitutor implements Expr.Visitor {

        Expr source;
        Expr dest;

        EagerSubstitutor(Expr paramExpr1, Expr paramExpr2) {
            this.source = paramExpr1;
            this.dest = paramExpr2;
        }

        public Object visitAbstract(Expr.Abstract paramAbstract) {
            if (paramAbstract == this.source) {
                return this.dest;
            }
            Expr localExpr = (Expr) paramAbstract.expr.visit(this);
            return localExpr == paramAbstract.expr ? paramAbstract : new Expr.Abstract(paramAbstract.sym, localExpr);
        }

        public Object visitApply(Expr.Apply paramApply) {
            if (paramApply == this.source) {
                return this.dest;
            }
            Expr localExpr1 = (Expr) paramApply.right.visit(this);
            if (localExpr1 != paramApply.right) {
                return new Expr.Apply(paramApply.left, localExpr1);
            }
            Expr localExpr2 = (Expr) paramApply.left.visit(this);
            if (localExpr2 != paramApply.left) {
                return new Expr.Apply(localExpr2, paramApply.right);
            }
            return paramApply;
        }

        public Object visitIdent(Expr.Ident paramIdent) {
            return paramIdent == this.source ? this.dest : paramIdent;
        }
    }

    private static class LazySlowSubstitutor implements Expr.Visitor {

        Expr source;
        Expr dest;

        LazySlowSubstitutor(Expr paramExpr1, Expr paramExpr2) {
            this.source = paramExpr1;
            this.dest = paramExpr2;
        }

        public Object visitAbstract(Expr.Abstract paramAbstract) {
            if (paramAbstract == this.source) {
                return this.dest;
            }
            Expr localExpr = (Expr) paramAbstract.expr.visit(this);
            return localExpr == paramAbstract.expr ? paramAbstract : new Expr.Abstract(paramAbstract.sym, localExpr);
        }

        public Object visitApply(Expr.Apply paramApply) {
            if (paramApply == this.source) {
                return this.dest;
            }
            Expr localExpr1 = (Expr) paramApply.left.visit(this);
            if (localExpr1 != paramApply.left) {
                return new Expr.Apply(localExpr1, paramApply.right);
            }
            Expr localExpr2 = (Expr) paramApply.right.visit(this);
            if (localExpr2 != paramApply.right) {
                return new Expr.Apply(paramApply.left, localExpr2);
            }
            return paramApply;
        }

        public Object visitIdent(Expr.Ident paramIdent) {
            return paramIdent == this.source ? this.dest : paramIdent;
        }
    }

    static Expr simplify(Expr paramExpr) {
        boolean bool1 = Options.getEtaReductionsOption().getValue();
        boolean bool2 = Options.getUseAppliedOption().getValue();
        int i = Options.getEvaluationOrderOption().getValue();
        Object localObject1;
        if (i == 0) {
            localObject1 = new EagerCandidateFinder(bool1, bool2);
        } else {
            localObject1 = new LazyCandidateFinder(bool1, bool2);
        }

        Expr localExpr1 = (Expr) paramExpr.visit((Expr.Visitor) localObject1);
        if (localExpr1 == null) {
            return paramExpr;
        }
        Expr localExpr2 = ((CandidateFinder) localObject1).getReplacement(localExpr1);

        Object localObject2;
        if (i == 0) {
            localObject2 = new EagerSubstitutor(localExpr1, localExpr2);
        } else if (i == 2) {
            localObject2 = new LazySlowSubstitutor(localExpr1, localExpr2);
        } else {
            localObject2 = new LazySubstitutor(localExpr1, localExpr2);
        }
        return (Expr) paramExpr.visit((Expr.Visitor) localObject2);
    }
}
