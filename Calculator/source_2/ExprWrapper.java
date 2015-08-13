/*    */ package lambda;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ class ExprWrapper {
/*  6 */   private static final Boolean TRUE = Boolean.TRUE;
/*  7 */   private static final Boolean FALSE = Boolean.FALSE;
/*    */   
/*  9 */   private static class EqualsVisitor implements Expr.VisitorP { EqualsVisitor(ExprWrapper.1 param1) { this(); }
/* 10 */     HashMap sym_map = new HashMap();
/*    */     
/*    */     public Object visitAbstract(Expr.Abstract paramAbstract, Object paramObject) {
/* 13 */       if (!(paramObject instanceof Expr.Abstract)) return ExprWrapper.FALSE;
/* 14 */       Expr.Abstract localAbstract = (Expr.Abstract)paramObject;
/* 15 */       this.sym_map.put(paramAbstract.sym, localAbstract.sym);
/* 16 */       Object localObject = paramAbstract.expr.visitP(this, localAbstract.expr);
/* 17 */       this.sym_map.remove(paramAbstract.sym);
/* 18 */       return localObject;
/*    */     }
/*    */     
/* 21 */     public Object visitApply(Expr.Apply paramApply, Object paramObject) { if (!(paramObject instanceof Expr.Apply)) return ExprWrapper.FALSE;
/* 22 */       Expr.Apply localApply = (Expr.Apply)paramObject;
/* 23 */       if (paramApply.left.visitP(this, localApply.left) == ExprWrapper.FALSE) return ExprWrapper.FALSE;
/* 24 */       if (paramApply.right.visitP(this, localApply.right) == ExprWrapper.FALSE) return ExprWrapper.FALSE;
/* 25 */       return ExprWrapper.TRUE;
/*    */     }
/*    */     
/* 28 */     public Object visitIdent(Expr.Ident paramIdent, Object paramObject) { if (!(paramObject instanceof Expr.Ident)) return ExprWrapper.FALSE;
/* 29 */       Expr.Ident localIdent = (Expr.Ident)paramObject;
/* 30 */       Symbol localSymbol = (Symbol)this.sym_map.get(paramIdent.sym);
/* 31 */       if (localSymbol == null) {
/* 32 */         if (paramIdent.sym.toString().equals(localIdent.sym.toString())) {
/* 33 */           return ExprWrapper.TRUE;
/*    */         }
/* 35 */         return ExprWrapper.FALSE;
/*    */       }
/*    */       
/* 38 */       return localIdent.sym == localSymbol ? ExprWrapper.TRUE : ExprWrapper.FALSE; }
/*    */     
/*    */     private EqualsVisitor() {} }
/*    */   
/*    */   private final Expr expr;
/* 43 */   private static class HashCodeComputer implements Expr.Visitor { HashCodeComputer(ExprWrapper.1 param1) { this(); }
/* 44 */     private HashMap sym_map = new HashMap();
/* 45 */     int last_ident = 0;
/*    */     
/*    */     public Object visitAbstract(Expr.Abstract paramAbstract) {
/* 48 */       this.last_ident += 1;
/* 49 */       this.sym_map.put(paramAbstract.sym, new Integer(this.last_ident));
/* 50 */       Object localObject = paramAbstract.expr.visit(this);
/* 51 */       this.sym_map.remove(paramAbstract.sym);
/* 52 */       return localObject;
/*    */     }
/*    */     
/* 55 */     public Object visitApply(Expr.Apply paramApply) { int i = ((Integer)paramApply.left.visit(this)).intValue();
/* 56 */       int j = ((Integer)paramApply.right.visit(this)).intValue();
/* 57 */       return new Integer(i * 31 + j);
/*    */     }
/*    */     
/* 60 */     public Object visitIdent(Expr.Ident paramIdent) { Integer localInteger = (Integer)this.sym_map.get(paramIdent.sym);
/* 61 */       if (localInteger == null) localInteger = new Integer(paramIdent.sym.toString().hashCode());
/* 62 */       return localInteger;
/*    */     }
/*    */     
/*    */     private HashCodeComputer() {} }
/*    */   
/* 67 */   private boolean hash_code_known = false;
/*    */   private int hash_code_val;
/*    */   
/*    */   public ExprWrapper(Expr paramExpr) {
/* 71 */     this.expr = paramExpr;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 75 */     if (!this.hash_code_known) {
/* 76 */       Integer localInteger = (Integer)this.expr.visit(new HashCodeComputer(null));
/* 77 */       this.hash_code_val = localInteger.intValue();
/* 78 */       this.hash_code_known = true;
/*    */     }
/* 80 */     return this.hash_code_val;
/*    */   }
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 84 */     if ((paramObject instanceof ExprWrapper)) {
/* 85 */       ExprWrapper localExprWrapper = (ExprWrapper)paramObject;
/* 86 */       if (localExprWrapper.hashCode() != hashCode()) return false;
/* 87 */       if (localExprWrapper.expr == this.expr) return true;
/* 88 */       return this.expr.visitP(new EqualsVisitor(null), localExprWrapper.expr) == TRUE; }
/* 89 */     if ((paramObject instanceof Expr)) {
/* 90 */       return equals(new ExprWrapper((Expr)paramObject));
/*    */     }
/* 92 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\ExprWrapper.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */