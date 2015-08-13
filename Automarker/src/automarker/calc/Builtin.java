/*     */ package automarker.calc;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ 
/*     */ abstract class Builtin {
/*   6 */   private static abstract class BinaryOperator extends Builtin { BinaryOperator() {}
/*     */     
/*   8 */     int numArguments() { return 2; }
/*     */     
/*     */     boolean canApply(Expr[] paramArrayOfExpr) {
/*  11 */       if (((paramArrayOfExpr[0] instanceof Expr.Ident)) && ((paramArrayOfExpr[1] instanceof Expr.Ident)))
/*     */       {
/*  13 */         String str1 = ((Expr.Ident)paramArrayOfExpr[0]).sym.toString();
/*  14 */         String str2 = ((Expr.Ident)paramArrayOfExpr[1]).sym.toString();
/*     */         try {
/*  16 */           Integer.parseInt(str1);
/*  17 */           Integer.parseInt(str2);
/*  18 */           return true;
/*     */         } catch (NumberFormatException localNumberFormatException) {}
/*     */       }
/*  21 */       return false;
/*     */     }
/*     */     
/*  24 */     Expr apply(Expr[] paramArrayOfExpr) { String str1 = ((Expr.Ident)paramArrayOfExpr[0]).sym.toString();
/*  25 */       String str2 = ((Expr.Ident)paramArrayOfExpr[1]).sym.toString();
/*  26 */       String str3 = computeResult(Integer.parseInt(str1), Integer.parseInt(str2));
/*     */       
/*  28 */       return new Expr.Ident(new Symbol(str3)); }
/*     */     
/*     */     
/*     */     
/*     */     abstract String computeResult(int paramInt1, int paramInt2); }
/*  33 */   private static class AddOperator extends Builtin.BinaryOperator {  AddOperator() { super(); }
/*  34 */     String computeResult(int paramInt1, int paramInt2) { return "" + (paramInt1 + paramInt2); } }
/*     */   
/*  36 */   private static class SubOperator extends Builtin.BinaryOperator {  SubOperator() { super(); } 
/*  37 */     String computeResult(int paramInt1, int paramInt2) { return "" + (paramInt1 - paramInt2); } }
/*     */   
/*  39 */   private static class MultOperator extends Builtin.BinaryOperator {  MultOperator() { super(); } 
/*  40 */     String computeResult(int paramInt1, int paramInt2) { return "" + paramInt1 * paramInt2; } }
/*     */   
/*  42 */   private static class DivOperator extends Builtin.BinaryOperator {  DivOperator() { super(); } 
/*  43 */     String computeResult(int paramInt1, int paramInt2) { return "" + paramInt1 / paramInt2; } }
/*     */   
/*  45 */   private static class EqOperator extends Builtin.BinaryOperator {  EqOperator() { super(); } 
/*  46 */     String computeResult(int paramInt1, int paramInt2) { return "" + (paramInt1 == paramInt2); } }
/*     */   
/*  48 */   private static class NeOperator extends Builtin.BinaryOperator {  NeOperator() { super(); } 
/*  49 */     String computeResult(int paramInt1, int paramInt2) { return "" + (paramInt1 != paramInt2); } }
/*     */   
/*  51 */   private static class LtOperator extends Builtin.BinaryOperator {  LtOperator() { super(); } 
/*  52 */     String computeResult(int paramInt1, int paramInt2) { return "" + (paramInt1 < paramInt2); } }
/*     */   
/*  54 */   private static class GtOperator extends Builtin.BinaryOperator {  GtOperator() { super(); } 
/*  55 */     String computeResult(int paramInt1, int paramInt2) { return "" + (paramInt1 > paramInt2); } }
/*     */   
/*  57 */   private static class LeOperator extends Builtin.BinaryOperator {  LeOperator() { super(); } 
/*  58 */     String computeResult(int paramInt1, int paramInt2) { return "" + (paramInt1 <= paramInt2); } }
/*     */   
/*  60 */   private static class GeOperator extends Builtin.BinaryOperator {  GeOperator() { super(); } 
/*  61 */     String computeResult(int paramInt1, int paramInt2) { return "" + (paramInt1 >= paramInt2); }
/*     */   }
/*     */   
/*  64 */   private static class IfOperator extends Builtin { IfOperator() {  }
/*     */     
/*  66 */     int numArguments() { return 3; }
/*     */     
/*     */     boolean canApply(Expr[] paramArrayOfExpr) {
/*  69 */       if ((paramArrayOfExpr[0] instanceof Expr.Ident)) {
/*  70 */         String str = ((Expr.Ident)paramArrayOfExpr[0]).sym.toString();
/*  71 */         if ((str.equals("true")) || (str.equals("false"))) return true;
/*     */       }
/*  73 */       return false;
/*     */     }
/*     */     
/*  76 */     Expr apply(Expr[] paramArrayOfExpr) { String str = ((Expr.Ident)paramArrayOfExpr[0]).sym.toString();
/*  77 */       if (str.equals("true")) return paramArrayOfExpr[1];
/*  78 */       return paramArrayOfExpr[2];
/*     */     }
/*     */     
/*     */ 
/*     */    
/*     */   }
/*     */   
/*     */ 
/*  86 */   static HashMap builtins = new HashMap();
/*     */   
/*     */   static {
/*  89 */     builtins.put("+", new AddOperator());
/*  90 */     builtins.put("-", new SubOperator());
/*  91 */     builtins.put("*", new MultOperator());
/*  92 */     builtins.put("/", new DivOperator());
/*  93 */     builtins.put("=", new EqOperator());
/*  94 */     builtins.put("/=", new NeOperator());
/*  95 */     builtins.put("<", new LtOperator());
/*  96 */     builtins.put(">", new GtOperator());
/*  97 */     builtins.put("<=", new LeOperator());
/*  98 */     builtins.put(">=", new GeOperator());
/*  99 */     builtins.put("if", new IfOperator());
/*     */   }
/*     */   
/*     */   static Builtin get(String paramString, int paramInt) {
/* 103 */     Builtin localBuiltin = (Builtin)builtins.get(paramString);
/* 104 */     if ((localBuiltin != null) && (localBuiltin.numArguments() == paramInt)) return localBuiltin;
/* 105 */     return null;
/*     */   }
/*     */   
/*     */   abstract Expr apply(Expr[] paramArrayOfExpr);
/*     */   
/*     */   abstract boolean canApply(Expr[] paramArrayOfExpr);
/*     */   
/*     */   abstract int numArguments();
/*     */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\Builtin.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */