package automarker;

/*      package lambda;
/*     */ 
import automarker.Expr;
/*     */ import java.util.HashMap;


/*     */ 
/*     */ class Simplify
/*     */ {
/*     */   static final int EAGER_EVALUATION = 0;
/*     */   static final int LAZY_EVALUATION = 1;
/*     */   static final int LAZY_SLOW_EVALUATION = 2;
/*     */   
/*     */   private static abstract class CandidateFinder implements Expr.Visitor {
/*     */     boolean use_etas;
/*     */     boolean use_applied;
/*     */     
/*     */     CandidateFinder(boolean paramBoolean1, boolean paramBoolean2) {
/*  16 */       this.use_etas = paramBoolean1;
/*  17 */       this.use_applied = paramBoolean2;
/*     */     }
/*     */     
/*     */     public Object visitAbstract(Expr.Abstract paramAbstract) {
/*  21 */       if (isCandidate(paramAbstract)) return paramAbstract;
/*  22 */       return paramAbstract.expr.visit(this); }
/*     */     
/*     */     public abstract Object visitApply(Expr.Apply paramApply);
/*     */     
/*  26 */     public Object visitIdent(Expr.Ident paramIdent) { if (isCandidate(paramIdent)) return paramIdent;
/*  27 */       return null;
/*     */     }
/*     */     
/*     */     boolean isCandidate(Expr paramExpr) { Object localObject1;
/*     */       Expr localExpr;
/*  32 */       if ((paramExpr instanceof Expr.Apply)) {
/*  33 */         localObject1 = ((Expr.Apply)paramExpr).left;
/*  34 */         localExpr = ((Expr.Apply)paramExpr).right;
/*  35 */         if ((localObject1 instanceof Expr.Abstract)) return true;
/*     */       }
/*     */       Object localObject2;
/*     */       Object localObject3;
/*  39 */       if ((this.use_etas) && ((paramExpr instanceof Expr.Abstract))) {
/*  40 */         localObject1 = ((Expr.Abstract)paramExpr).sym;
/*  41 */         localExpr = ((Expr.Abstract)paramExpr).expr;
/*  42 */         if ((localExpr instanceof Expr.Apply)) {
/*  43 */           localObject2 = ((Expr.Apply)localExpr).left;
/*  44 */           localObject3 = ((Expr.Apply)localExpr).right;
/*  45 */           if (((localObject3 instanceof Expr.Ident)) && (((Expr.Ident)localObject3).sym == localObject1) && (!((Expr)localObject2).uses((Symbol)localObject1)))
/*     */           {
/*     */ 
/*  48 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*  54 */       if (this.use_applied) {
/*  55 */         localObject1 = paramExpr;
/*  56 */         int i = 0;
/*  57 */         while ((localObject1 instanceof Expr.Apply)) {
/*  58 */           localObject1 = ((Expr.Apply)localObject1).left;
/*  59 */           i++;
/*     */         }
/*  61 */         if ((localObject1 instanceof Expr.Ident)) {
/*  62 */           localObject2 = ((Expr.Ident)localObject1).sym.toString();
/*  63 */           localObject3 = Builtin.get((String)localObject2, i);
/*  64 */           if (localObject3 != null) {
/*  65 */             Expr[] arrayOfExpr = new Expr[i];
/*  66 */             localObject1 = paramExpr;
/*  67 */             int j = i - 1;
/*  68 */             while ((localObject1 instanceof Expr.Apply)) {
/*  69 */               arrayOfExpr[j] = ((Expr.Apply)localObject1).right;
/*  70 */               j--;
/*  71 */               localObject1 = ((Expr.Apply)localObject1).left;
/*     */             }
/*  73 */             if (((Builtin)localObject3).canApply(arrayOfExpr)) { return true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*  78 */       return false; }
/*     */     
/*     */     Expr getReplacement(Expr paramExpr) { Expr localExpr1;
/*     */       Expr localExpr2;
/*     */       Object localObject;
/*  83 */       if ((paramExpr instanceof Expr.Apply)) {
/*  84 */         localExpr1 = ((Expr.Apply)paramExpr).left;
/*  85 */         localExpr2 = ((Expr.Apply)paramExpr).right;
/*  86 */         if ((localExpr1 instanceof Expr.Abstract)) {
/*  87 */           localObject = (Expr.Abstract)localExpr1;
/*  88 */           return (Expr)((Expr.Abstract)localObject).expr.visit(new Simplify.SymbolSubstitutor(((Expr.Abstract)localObject).sym, localExpr2));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  94 */       if ((this.use_etas) && ((paramExpr instanceof Expr.Abstract))) {
/*  95 */         localExpr1 = ((Expr.Abstract)paramExpr).expr;
/*  96 */         if ((localExpr1 instanceof Expr.Apply)) {
/*  97 */           localExpr2 = ((Expr.Apply)localExpr1).left;
/*  98 */           return localExpr2;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 103 */       if (this.use_applied) {
/* 104 */         localExpr1 = paramExpr;
/* 105 */         int i = 0;
/* 106 */         while ((localExpr1 instanceof Expr.Apply)) {
/* 107 */           localExpr1 = ((Expr.Apply)localExpr1).left;
/* 108 */           i++;
/*     */         }
/* 110 */         if ((localExpr1 instanceof Expr.Ident)) {
/* 111 */           localObject = ((Expr.Ident)localExpr1).sym.toString();
/* 112 */           Builtin localBuiltin = Builtin.get((String)localObject, i);
/* 113 */           if (localBuiltin != null) {
/* 114 */             Expr[] arrayOfExpr = new Expr[i];
/* 115 */             localExpr1 = paramExpr;
/* 116 */             int j = i - 1;
/* 117 */             while ((localExpr1 instanceof Expr.Apply)) {
/* 118 */               arrayOfExpr[j] = ((Expr.Apply)localExpr1).right;
/* 119 */               j--;
/* 120 */               localExpr1 = ((Expr.Apply)localExpr1).left;
/*     */             }
/* 122 */             return localBuiltin.apply(arrayOfExpr);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 127 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LazyCandidateFinder
/*     */     extends Simplify.CandidateFinder {
/* 133 */     LazyCandidateFinder(boolean paramBoolean1, boolean paramBoolean2) { super(paramBoolean1, paramBoolean2); }
/*     */     
/*     */     public Object visitApply(Expr.Apply paramApply) {
/* 136 */       if (isCandidate(paramApply)) { return paramApply;
/*     */       }
/* 138 */       Object localObject = paramApply.left.visit(this);
/* 139 */       if (localObject != null) return localObject;
/* 140 */       return paramApply.right.visit(this);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EagerCandidateFinder
/*     */     extends Simplify.CandidateFinder {
/* 146 */     EagerCandidateFinder(boolean paramBoolean1, boolean paramBoolean2) { super(paramBoolean1, paramBoolean2); }
/*     */     
/*     */     public Object visitApply(Expr.Apply paramApply) {
/* 149 */       Object localObject = paramApply.right.visit(this);
/* 150 */       if (localObject != null) { return localObject;
/*     */       }
/* 152 */       if (isCandidate(paramApply)) { return paramApply;
/*     */       }
/* 154 */       return paramApply.left.visit(this);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SymbolSubstitutor implements Expr.Visitor {
/*     */     Symbol to_find;
/*     */     Expr to_replace;
/*     */     
/*     */     SymbolSubstitutor(Symbol paramSymbol, Expr paramExpr) {
/* 163 */       this.to_find = paramSymbol;
/* 164 */       this.to_replace = paramExpr;
/*     */     }
/*     */     
/* 167 */     public Object visitAbstract(Expr.Abstract paramAbstract) { if (paramAbstract.sym == this.to_find) { return paramAbstract;
/*     */       }
/* 169 */       Expr localExpr = (Expr)paramAbstract.expr.visit(this);
/* 170 */       return localExpr == paramAbstract.expr ? paramAbstract : new Expr.Abstract(paramAbstract.sym, localExpr);
/*     */     }
/*     */     
/* 173 */     public Object visitApply(Expr.Apply paramApply) { Expr localExpr1 = (Expr)paramApply.left.visit(this);
/* 174 */       Expr localExpr2 = (Expr)paramApply.right.visit(this);
/* 175 */       if ((localExpr1 == paramApply.left) && (localExpr2 == paramApply.right)) return paramApply;
/* 176 */       return new Expr.Apply(localExpr1, localExpr2);
/*     */     }
/*     */     
/* 179 */     public Object visitIdent(Expr.Ident paramIdent) { return paramIdent.sym == this.to_find ? this.to_replace : paramIdent; }
/*     */   }
/*     */   
/*     */   private static class LazySubstitutor implements Expr.Visitor
/*     */   {
/* 184 */     HashMap creations = new HashMap();
/*     */     Expr source;
/*     */     Expr dest;
/*     */     
/*     */     LazySubstitutor(Expr paramExpr1, Expr paramExpr2) {
/* 189 */       this.source = paramExpr1;
/* 190 */       this.dest = paramExpr2;
/*     */     }
/*     */     
/* 193 */     public Object visitAbstract(Expr.Abstract paramAbstract) { if (paramAbstract == this.source) { return this.dest;
/*     */       }
/* 195 */       Expr localExpr = (Expr)paramAbstract.expr.visit(this);
/* 196 */       if (localExpr == paramAbstract.expr) {
/* 197 */         return paramAbstract;
/*     */       }
/* 199 */       Object localObject = (Expr)this.creations.get(paramAbstract);
/* 200 */       if (localObject == null) {
/* 201 */         localObject = new Expr.Abstract(paramAbstract.sym, localExpr);
/* 202 */         this.creations.put(paramAbstract, localObject);
/*     */       }
/* 204 */       return localObject;
/*     */     }
/*     */     
/*     */     public Object visitApply(Expr.Apply paramApply) {
/* 208 */       if (paramApply == this.source) { return this.dest;
/*     */       }
/* 210 */       Expr localExpr1 = (Expr)paramApply.left.visit(this);
/* 211 */       Expr localExpr2 = (Expr)paramApply.right.visit(this);
/* 212 */       if ((localExpr1 == paramApply.left) && (localExpr2 == paramApply.right)) {
/* 213 */         return paramApply;
/*     */       }
/* 215 */       Object localObject = (Expr)this.creations.get(paramApply);
/* 216 */       if (localObject == null) {
/* 217 */         localObject = new Expr.Apply(localExpr1, localExpr2);
/* 218 */         this.creations.put(paramApply, localObject);
/*     */       }
/* 220 */       return localObject;
/*     */     }
/*     */     
/*     */     public Object visitIdent(Expr.Ident paramIdent) {
/* 224 */       return paramIdent == this.source ? this.dest : paramIdent;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EagerSubstitutor implements Expr.Visitor {
/*     */     Expr source;
/*     */     Expr dest;
/*     */     
/*     */     EagerSubstitutor(Expr paramExpr1, Expr paramExpr2) {
/* 233 */       this.source = paramExpr1;
/* 234 */       this.dest = paramExpr2;
/*     */     }
/*     */     
/* 237 */     public Object visitAbstract(Expr.Abstract paramAbstract) { if (paramAbstract == this.source) { return this.dest;
/*     */       }
/* 239 */       Expr localExpr = (Expr)paramAbstract.expr.visit(this);
/* 240 */       return localExpr == paramAbstract.expr ? paramAbstract : new Expr.Abstract(paramAbstract.sym, localExpr);
/*     */     }
/*     */     
/* 243 */     public Object visitApply(Expr.Apply paramApply) { if (paramApply == this.source) { return this.dest;
/*     */       }
/* 245 */       Expr localExpr1 = (Expr)paramApply.right.visit(this);
/* 246 */       if (localExpr1 != paramApply.right) return new Expr.Apply(paramApply.left, localExpr1);
/* 247 */       Expr localExpr2 = (Expr)paramApply.left.visit(this);
/* 248 */       if (localExpr2 != paramApply.left) return new Expr.Apply(localExpr2, paramApply.right);
/* 249 */       return paramApply;
/*     */     }
/*     */     
/* 252 */     public Object visitIdent(Expr.Ident paramIdent) { return paramIdent == this.source ? this.dest : paramIdent; }
/*     */   }
/*     */   
/*     */   private static class LazySlowSubstitutor implements Expr.Visitor
/*     */   {
/*     */     Expr source;
/*     */     Expr dest;
/*     */     
/*     */     LazySlowSubstitutor(Expr paramExpr1, Expr paramExpr2) {
/* 261 */       this.source = paramExpr1;
/* 262 */       this.dest = paramExpr2;
/*     */     }
/*     */     
/* 265 */     public Object visitAbstract(Expr.Abstract paramAbstract) { if (paramAbstract == this.source) { return this.dest;
/*     */       }
/* 267 */       Expr localExpr = (Expr)paramAbstract.expr.visit(this);
/* 268 */       return localExpr == paramAbstract.expr ? paramAbstract : new Expr.Abstract(paramAbstract.sym, localExpr);
/*     */     }
/*     */     
/* 271 */     public Object visitApply(Expr.Apply paramApply) { if (paramApply == this.source) { return this.dest;
/*     */       }
/* 273 */       Expr localExpr1 = (Expr)paramApply.left.visit(this);
/* 274 */       if (localExpr1 != paramApply.left) return new Expr.Apply(localExpr1, paramApply.right);
/* 275 */       Expr localExpr2 = (Expr)paramApply.right.visit(this);
/* 276 */       if (localExpr2 != paramApply.right) return new Expr.Apply(paramApply.left, localExpr2);
/* 277 */       return paramApply;
/*     */     }
/*     */     
/* 280 */     public Object visitIdent(Expr.Ident paramIdent) { return paramIdent == this.source ? this.dest : paramIdent; }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static Expr simplify(Expr paramExpr)
/*     */   {
/* 287 */     boolean bool1 = Options.getEtaReductionsOption().getValue();
/* 288 */     boolean bool2 = Options.getUseAppliedOption().getValue();
/* 289 */     int i = Options.getEvaluationOrderOption().getValue();
/* 290 */     Object localObject1; if (i == 0) {
/* 291 */       localObject1 = new EagerCandidateFinder(bool1, bool2);
/*     */     } else {
/* 293 */       localObject1 = new LazyCandidateFinder(bool1, bool2);
/*     */     }
/*     */     
/* 296 */     Expr localExpr1 = (Expr)paramExpr.visit((Expr.Visitor)localObject1);
/* 297 */     if (localExpr1 == null) return paramExpr;
/* 298 */     Expr localExpr2 = ((CandidateFinder)localObject1).getReplacement(localExpr1);
/*     */     
/*     */     Object localObject2;
/* 301 */     if (i == 0) {
/* 302 */       localObject2 = new EagerSubstitutor(localExpr1, localExpr2);
/* 303 */     } else if (i == 2) {
/* 304 */       localObject2 = new LazySlowSubstitutor(localExpr1, localExpr2);
/*     */     } else {
/* 306 */       localObject2 = new LazySubstitutor(localExpr1, localExpr2);
/*     */     }
/* 308 */     return (Expr)paramExpr.visit((Expr.Visitor)localObject2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\Simplify.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */