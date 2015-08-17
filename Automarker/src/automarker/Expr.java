package automarker;

/*    
/*     */ 
import automarker.Context;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ abstract class Expr {
/*   6 */   private static char[] left_parens = "(([(({".toCharArray();
/*   7 */   private static char[] right_parens = "))]))}".toCharArray();
/*     */   abstract Object visit(Visitor paramVisitor);
/*     */   
/*     */   abstract Object visitP(VisitorP paramVisitorP, Object paramObject);
/*     */   
/*     */   static abstract interface Visitor { public abstract Object visitAbstract(Expr.Abstract paramAbstract);
/*     */     
/*     */     public abstract Object visitApply(Expr.Apply paramApply);
/*     */     
/*     */     public abstract Object visitIdent(Expr.Ident paramIdent);
/*     */   }
/*     */   
/*     */   static abstract interface VisitorP { public abstract Object visitAbstract(Expr.Abstract paramAbstract, Object paramObject);
/*     */     
/*     */     public abstract Object visitApply(Expr.Apply paramApply, Object paramObject);
/*     */     
/*     */     public abstract Object visitIdent(Expr.Ident paramIdent, Object paramObject);
/*     */   }
/*     */   
/*  26 */   static class Abstract extends Expr { Abstract(Symbol paramSymbol, Expr paramExpr) { this.sym = paramSymbol;
/*  27 */       this.expr = paramExpr; }
/*     */     
/*     */     final Symbol sym;
/*     */     final Expr expr;
/*  31 */     void addBounds(HashSet paramHashSet1, HashSet paramHashSet2) { paramHashSet1.add(this.sym);
/*  32 */       this.expr.addBounds(paramHashSet1, paramHashSet2);
/*     */     }
/*     */     
/*     */     Object visit(Expr.Visitor paramVisitor) {
/*  36 */       return paramVisitor.visitAbstract(this);
/*     */     }
/*     */     
/*  39 */     Object visitP(Expr.VisitorP paramVisitorP, Object paramObject) { return paramVisitorP.visitAbstract(this, paramObject); }
/*     */     
/*     */     int size()
/*     */     {
/*  43 */       return 1 + this.expr.size();
/*     */     }
/*     */     
/*     */     boolean uses(Symbol paramSymbol) {
/*  47 */       return this.expr.uses(paramSymbol);
/*     */     }
/*     */   }
/*     */   
/*     */   static class Apply extends Expr {
/*     */     final Expr left;
/*     */     final Expr right;
/*     */     
/*     */     Apply(Expr paramExpr1, Expr paramExpr2) {
/*  56 */       this.left = paramExpr1;
/*  57 */       this.right = paramExpr2;
/*     */     }
/*     */     
/*     */     void addBounds(HashSet paramHashSet1, HashSet paramHashSet2) {
/*  61 */       this.left.addBounds(paramHashSet1, paramHashSet2);
/*  62 */       this.right.addBounds(paramHashSet1, paramHashSet2);
/*     */     }
/*     */     
/*     */     Object visit(Expr.Visitor paramVisitor) {
/*  66 */       return paramVisitor.visitApply(this);
/*     */     }
/*     */     
/*  69 */     Object visitP(Expr.VisitorP paramVisitorP, Object paramObject) { return paramVisitorP.visitApply(this, paramObject); }
/*     */     
/*     */     int size()
/*     */     {
/*  73 */       return 1 + this.left.size() + this.right.size();
/*     */     }
/*     */     
/*     */     boolean uses(Symbol paramSymbol) {
/*  77 */       return (this.left.uses(paramSymbol)) || (this.right.uses(paramSymbol));
/*     */     }
/*     */   }
/*     */   
/*     */   static class Ident extends Expr {
/*     */     final Symbol sym;
/*     */     
/*     */     Ident(Symbol paramSymbol) {
/*  85 */       this.sym = paramSymbol;
/*     */     }
/*     */     
/*     */     void addBounds(HashSet paramHashSet1, HashSet paramHashSet2) {
/*  89 */       if (!paramHashSet1.contains(this.sym)) {
/*  90 */         paramHashSet2.add(this.sym);
/*     */       }
/*     */     }
/*     */     
/*     */     Object visit(Expr.Visitor paramVisitor) {
/*  95 */       return paramVisitor.visitIdent(this);
/*     */     }
/*     */     
/*  98 */     Object visitP(Expr.VisitorP paramVisitorP, Object paramObject) { return paramVisitorP.visitIdent(this, paramObject); }
/*     */     
/*     */     int size()
/*     */     {
/* 102 */       return 1;
/*     */     }
/*     */     
/*     */     boolean uses(Symbol paramSymbol) {
/* 106 */       return this.sym == paramSymbol;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PrintVisitor implements Expr.VisitorP {
/* 111 */     boolean substitute_at_top = true;
/*     */     
/*     */     Context context;
/*     */     int max_len;
/*     */     boolean vary_parens;
/* 116 */     StringBuffer output = new StringBuffer();
/* 117 */     int paren_depth = 0;
/* 118 */     int paren_cur = -1;
/* 119 */     HashSet bounds = new HashSet();
/* 120 */     HashSet frees = new HashSet();
/* 121 */     HashSet free_names = new HashSet();
/* 122 */     HashSet ident_names = new HashSet();
/* 123 */     java.util.HashMap sym_map = new java.util.HashMap();
/* 124 */     boolean exceeded = false;
/* 125 */     int last_alloc = -1;
/* 126 */     boolean visited = false;
/*     */     
/*     */     PrintVisitor(Expr paramExpr, Context paramContext, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/* 130 */       this.context = paramContext;
/* 131 */       this.max_len = paramInt;
/* 132 */       this.vary_parens = paramBoolean1;
/* 133 */       this.substitute_at_top = paramBoolean2;
/*     */       
/* 135 */       paramExpr.addBounds(this.bounds, this.frees);
/* 136 */       java.util.Iterator localIterator = this.frees.iterator();
/* 137 */       Symbol localSymbol; while (localIterator.hasNext()) {
/* 138 */         localSymbol = (Symbol)localIterator.next();
/* 139 */         this.free_names.add(localSymbol.toString());
/* 140 */         this.ident_names.add(localSymbol.toString());
/*     */       }
/* 142 */       localIterator = this.bounds.iterator();
/* 143 */       while (localIterator.hasNext()) {
/* 144 */         localSymbol = (Symbol)localIterator.next();
/* 145 */         this.ident_names.add(localSymbol.toString());
/*     */       }
/*     */     }
/*     */     
/*     */     private String allocate() {
/* 150 */       for (;;) { this.last_alloc -= 1;
/* 149 */         if (this.last_alloc >= 0) if (this.ident_names.contains("i" + this.last_alloc))
/*     */             break;
/*     */       }
/*     */       String str;
/* 153 */       do { this.last_alloc += 1;
/* 154 */         str = "i" + this.last_alloc;
/* 155 */       } while (this.ident_names.contains(str)); return str;
/*     */     }
/*     */     
/*     */     private char getLeftParen() {
/* 159 */       this.paren_depth += 1;
/* 160 */       if (!this.vary_parens) { return '(';
/*     */       }
/* 162 */       this.paren_cur += 1;
/* 163 */       if (this.paren_cur == Expr.left_parens.length) this.paren_cur = 0;
/* 164 */       return Expr.left_parens[this.paren_cur];
/*     */     }
/*     */     
/* 167 */     private char getRightParen() { this.paren_depth -= 1;
/* 168 */       if (!this.vary_parens) { return ')';
/*     */       }
/* 170 */       char c = Expr.right_parens[this.paren_cur];
/* 171 */       this.paren_cur -= 1;
/* 172 */       if (this.paren_cur < 0) this.paren_cur = (Expr.right_parens.length - 1);
/* 173 */       return c;
/*     */     }
/*     */     
/* 176 */     private boolean append(char paramChar) { return append("" + paramChar); }
/*     */     
/*     */     private boolean append(String paramString) {
/* 179 */       if ((this.output.length() + paramString.length() + this.paren_depth + 5 <= this.max_len) && (!this.exceeded))
/*     */       {
/* 181 */         this.output.append(paramString);
/* 182 */         return true; }
/* 183 */       if (!this.exceeded) {
/* 184 */         this.output.append(" ... ");
/* 185 */         this.exceeded = true;
/* 186 */         return false;
/*     */       }
/* 188 */       return false;
/*     */     }
/*     */     
/*     */     boolean checkForSubstitution(Expr paramExpr) {
/* 192 */       if (this.context != null) {
/* 193 */         if ((this.substitute_at_top) || (this.visited)) {
/* 194 */           String str = this.context.invert(paramExpr);
/* 195 */           if (str != null) { append(str);return true;
/*     */           } }
/* 197 */         this.visited = true;
/*     */       }
/* 199 */       return false;
/*     */     }
/*     */     
/* 202 */     public Object visitAbstract(Expr.Abstract paramAbstract, Object paramObject) { if (this.exceeded) return null;
/* 203 */       if (checkForSubstitution(paramAbstract)) { return null;
/*     */       }
/* 205 */       String str = paramAbstract.sym.toString();
/* 206 */       if (this.free_names.contains(str)) {
/* 207 */         str = allocate();
/* 208 */         this.sym_map.put(paramAbstract.sym, str);
/*     */       }
/* 210 */       this.free_names.add(str);
/* 211 */       this.ident_names.add(str);
/*     */       
/* 213 */       boolean bool = false;
/* 214 */       if (paramObject == Boolean.TRUE) bool = append(getLeftParen());
/* 215 */       append("\\" + str + ".");
/* 216 */       paramAbstract.expr.visitP(this, Boolean.FALSE);
/* 217 */       if (paramObject == Boolean.TRUE) {
/* 218 */         if (bool) this.output.append(getRightParen()); else {
/* 219 */           getRightParen();
/*     */         }
/*     */       }
/* 222 */       this.free_names.remove(str);
/* 223 */       this.ident_names.remove(str);
/* 224 */       if (str != paramAbstract.sym.toString()) {
/* 225 */         this.sym_map.remove(paramAbstract.sym);
/*     */       }
/* 227 */       return null;
/*     */     }
/*     */     
/* 230 */     public Object visitApply(Expr.Apply paramApply, Object paramObject) { if (this.exceeded) return null;
/* 231 */       if (checkForSubstitution(paramApply)) { return null;
/*     */       }
/* 233 */       paramApply.left.visitP(this, Boolean.TRUE);
/* 234 */       append(" ");
/* 235 */       if ((paramApply.right instanceof Expr.Apply)) {
/* 236 */         boolean bool = append(getLeftParen());
/* 237 */         paramApply.right.visitP(this, Boolean.FALSE);
/* 238 */         if (bool) this.output.append(getRightParen()); else
/* 239 */           getRightParen();
/*     */       } else {
/* 241 */         paramApply.right.visitP(this, Boolean.TRUE);
/*     */       }
/*     */       
/* 244 */       return null;
/*     */     }
/*     */     
/* 247 */     public Object visitIdent(Expr.Ident paramIdent, Object paramObject) { if (checkForSubstitution(paramIdent)) { return null;
/*     */       }
/* 249 */       String str = (String)this.sym_map.get(paramIdent.sym);
/* 250 */       append(str != null ? str : paramIdent.sym.toString());
/* 251 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   abstract void addBounds(HashSet paramHashSet1, HashSet paramHashSet2);
/*     */   
/*     */   abstract boolean uses(Symbol paramSymbol);
/*     */   
/*     */   abstract int size();
/*     */   
/*     */   public String toString() {
/* 262 */     return toString(null, Integer.MAX_VALUE, false);
/*     */   }
/*     */   
/* 265 */   public String toString(Context paramContext) { return toString(paramContext, Integer.MAX_VALUE, false); }
/*     */   
/*     */   public String toString(Context paramContext, int paramInt, boolean paramBoolean)
/*     */   {
/* 269 */     PrintVisitor localPrintVisitor = new PrintVisitor(this, paramContext, paramInt, paramBoolean, true);
/*     */     
/* 271 */     visitP(localPrintVisitor, Boolean.FALSE);
/* 272 */     return localPrintVisitor.output.toString();
/*     */   }
/*     */   
/*     */   public String toStringSubstituteBelow(Context paramContext, int paramInt, boolean paramBoolean)
/*     */   {
/* 277 */     PrintVisitor localPrintVisitor = new PrintVisitor(this, paramContext, paramInt, paramBoolean, false);
/*     */     
/* 279 */     visitP(localPrintVisitor, Boolean.FALSE);
/* 280 */     return localPrintVisitor.output.toString();
/*     */   }
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 284 */     if ((paramObject instanceof Expr))
/* 285 */       return this == paramObject;
/* 286 */     if ((paramObject instanceof ExprWrapper)) {
/* 287 */       return ((ExprWrapper)paramObject).equals(this);
/*     */     }
/* 289 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\Expr.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */