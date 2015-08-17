package automarker;

/*      package lambda;
/*     */ 
/*     */ import java.io.LineNumberReader;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;

/*     */ import javax.swing.event.ListDataListener;
/*     */ 
/*     */ class Context implements javax.swing.ListModel
/*     */ {
/*  10 */   private HashMap map = new HashMap();
/*  11 */   private HashMap inverse = new HashMap();
/*  12 */   private TreeSetSeq names = new TreeSetSeq();
/*  13 */   private java.util.LinkedList listeners = new java.util.LinkedList();
/*     */   
/*  15 */   private class Substitutor implements Expr.Visitor {  
/*     */     
/*  17 */     public Object visitAbstract(Expr.Abstract paramAbstract) { Expr localExpr = null;
/*  18 */       String str = paramAbstract.sym.toString();
/*  19 */       Symbol localSymbol = paramAbstract.sym;
/*  20 */       if (Context.this.map.containsKey(str)) {
/*  21 */         localExpr = (Expr)Context.this.map.get(str);
/*  22 */         Context.this.map.remove(str);
/*     */       }
/*  24 */       Expr.Abstract localAbstract = new Expr.Abstract(localSymbol, (Expr)paramAbstract.expr.visit(this));
/*  25 */       if (localSymbol != paramAbstract.sym) {
/*  26 */         Context.this.map.put(str, localExpr);
/*     */       }
/*  28 */       return localAbstract;
/*     */     }
/*     */     
/*  31 */     public Object visitApply(Expr.Apply paramApply) { return new Expr.Apply((Expr)paramApply.left.visit(this), (Expr)paramApply.right.visit(this)); }
/*     */     
/*     */     public Object visitIdent(Expr.Ident paramIdent)
/*     */     {
/*  35 */       Expr localExpr = (Expr)Context.this.map.get(paramIdent.sym.toString());
/*  36 */       if (localExpr != null) return localExpr;
/*  37 */       return paramIdent;
/*     */     }
/*     */      }
/*     */   
/*  42 */   Expr substitute(Expr paramExpr) { return (Expr)paramExpr.visit(new Substitutor()); }
/*     */   
/*     */   void put(String paramString, Expr paramExpr)
/*     */   {
/*  46 */     paramString = paramString.trim();
/*  47 */     if (insert(paramString, paramExpr)) {
/*  48 */       int i = this.names.getIndex(paramString);
/*  49 */       fireEvent(1, i, i);
/*     */     }
/*     */   }
/*     */   
/*     */   Expr get(String paramString) {
/*  54 */     return (Expr)this.map.get(paramString.trim());
/*     */   }
/*     */   
/*     */   String invert(Expr paramExpr) {
/*  58 */     String str = (String)this.inverse.get(new ExprWrapper(paramExpr));
/*  59 */     return str;
/*     */   }
/*     */   
/*  62 */   private boolean insert(String paramString, Expr paramExpr) { boolean bool = true;
/*  63 */     Expr localExpr = (Expr)this.map.get(paramString);
/*  64 */     if (localExpr == null) {
/*  65 */       this.names.add(paramString);
/*     */     } else {
/*  67 */       this.inverse.remove(new ExprWrapper(localExpr));
/*  68 */       bool = false;
/*     */     }
/*  70 */     this.map.put(paramString, paramExpr);
/*  71 */     this.inverse.put(new ExprWrapper(paramExpr), paramString);
/*  72 */     return bool;
/*     */   }
/*     */   
/*     */   void exportFile(java.io.File paramFile) throws java.io.IOException {
/*  76 */     java.io.PrintWriter localPrintWriter = new java.io.PrintWriter(new java.io.FileWriter(paramFile));
/*  77 */     Iterator localIterator = this.map.keySet().iterator();
/*  78 */     while (localIterator.hasNext()) {
/*  79 */       String str = (String)localIterator.next();
/*  80 */       localPrintWriter.println(str);
/*  81 */       localPrintWriter.println("  " + this.map.get(str));
/*     */     }
/*  83 */     localPrintWriter.close();
/*     */   }
/*     */   
/*  86 */   void importFile(java.io.File paramFile) throws java.io.IOException { LineNumberReader localLineNumberReader = new LineNumberReader(new java.io.FileReader(paramFile));
/*     */     for (;;) {
/*  88 */       int i = localLineNumberReader.getLineNumber();
/*  89 */       String str1 = localLineNumberReader.readLine();
/*  90 */       if (str1 != null) {
/*  91 */         if ((str1.length() == 0) || (str1.charAt(0) == ' ')) {
/*  92 */           localLineNumberReader.close();
/*  93 */           throw new java.io.IOException(i + ": expected symbol definition");
/*     */         }
/*  95 */         String str2 = localLineNumberReader.readLine();
/*  96 */         if ((str2 == null) || (str2.length() == 0) || (str2.charAt(0) != ' ')) {
/*  97 */           localLineNumberReader.close();
/*  98 */           throw new java.io.IOException(i + ": symbol " + str1 + " missing definition");
/*     */         }
/*     */         try
/*     */         {
/* 102 */           insert(str1, Parser.parse(str2));
/*     */         } catch (Parser.ParseException localParseException) {
/* 104 */           localLineNumberReader.close();
/* 105 */           throw new java.io.IOException(i + 1 + ": expression parse: " + localParseException.getMessage());
/*     */         }
/*     */       }
/*     */     }
/* 109      localLineNumberReader.close();
/* 110      fireEvent(0, 0, this.names.size());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addListDataListener(ListDataListener paramListDataListener)
/*     */   {
/* 117 */     this.listeners.add(paramListDataListener);
/*     */   }
/*     */   
/* 120 */   public Object getElementAt(int paramInt) { return this.names.get(paramInt); }
/*     */   
/*     */   public int getSize() {
/* 123 */     return this.names.size();
/*     */   }
/*     */   
/* 126 */   public void removeListDataListener(ListDataListener paramListDataListener) { this.listeners.remove(paramListDataListener); }
/*     */   
/*     */   private void fireEvent(int paramInt1, int paramInt2, int paramInt3) {
/* 129 */     javax.swing.event.ListDataEvent localListDataEvent = new javax.swing.event.ListDataEvent(this, paramInt1, paramInt2, paramInt3);
/* 130 */     Iterator localIterator = this.listeners.iterator();
/* 131 */     while (localIterator.hasNext()) {
/* 132 */       ListDataListener localListDataListener = (ListDataListener)localIterator.next();
/* 133 */       if (paramInt1 == 0) {
/* 134 */         localListDataListener.contentsChanged(localListDataEvent);
/* 135 */       } else if (paramInt1 == 1) {
/* 136 */         localListDataListener.intervalAdded(localListDataEvent);
/* 137 */       } else if (paramInt1 == 2) {
/* 138 */         localListDataListener.intervalAdded(localListDataEvent);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\Context.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */