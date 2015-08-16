/*     */ package automarker.calc;
/*     */ 
/*     */ import javax.swing.JTextArea;
/*     */ class Engine
/*     */ {
/*   7 */   private static int RUNNER_IDLE = 0;
/*   8 */   private static int RUNNER_REQUEST_READY = 1;
/*   9 */   private static int RUNNER_COMPUTING = 2;
/*  10 */   private static int RUNNER_REQUEST_MADE = 3;
/*  11 */   private static int RUNNER_CANCELED = 4;
/*     */   
/*  13 */   private class Runner extends Thread { Runner() { }
/*  14 */     int state = Engine.RUNNER_IDLE;
/*     */     String name;
/*     */     String expr;
/*     */     
/*     */     public void run()
/*     */     {
/*     */       for (;;)
/*     */       {
/*     */         String str1;
/*     */         String str2;
/*  24 */         synchronized (this) {
/*     */           //continue;
/*  26 */           try{ 
                        wait();
/*     */           }
/*     */           catch (InterruptedException localInterruptedException1) {}
/*  25 */           if (this.state != Engine.RUNNER_REQUEST_READY) {
/*     */             continue;
/*     */           }
/*  28 */           str1 = this.name;
/*  29 */           str2 = this.expr;
/*  30 */           this.state = Engine.RUNNER_COMPUTING;
/*     */         }
/*     */         
/*  33 */         Expr localExpr = Engine.this.process(str2);
/*     */         
/*  35 */         synchronized (this) {
/*  36 */           while (this.state == Engine.RUNNER_REQUEST_MADE)
/*  37 */             try { wait();
/*     */             } catch (InterruptedException localInterruptedException2) {}
/*  39 */           if ((this.state != Engine.RUNNER_CANCELED) && (!str1.equals("")) && (localExpr != null))
/*     */           {
/*  41 */             Engine.this.gui.getContext().put(str1, localExpr);
/*     */           }
/*  43 */           this.name = null;
/*  44 */           this.expr = null;
/*  45 */           this.state = Engine.RUNNER_IDLE;
/*  46 */           notifyAll();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     void requestProcessing(String paramString1, String paramString2) {
/*  52 */       int i = 0;
/*  53 */       synchronized (this) {
/*  54 */         if ((this.name != null) && (paramString1.equals(this.name)) && (paramString2.equals(this.expr)))
/*     */         {
/*  56 */           return;
/*     */         }
/*  58 */         if (this.state == Engine.RUNNER_COMPUTING) {
/*  59 */           this.state = Engine.RUNNER_REQUEST_MADE;
/*  60 */           i = 1;
/*     */         }
/*     */       }
/*     */       
/*  64 */       if (i != 0) {
    
}
/*  85 */       synchronized (this) {
/*  86 */         while (this.state != Engine.RUNNER_IDLE)
/*  87 */           try { wait();
/*     */           } catch (InterruptedException localInterruptedException) {}
/*  89 */         this.name = paramString1;
/*  90 */         this.expr = paramString2;
/*  91 */         this.state = Engine.RUNNER_REQUEST_READY;
/*  92 */         notifyAll();
/*     */       } }
/*     */     
/*     */    }
/*     */   
/*     */   private Gui gui;
/*  98 */   private Runner runner = null;
/*     */   
/*     */   Engine(Gui paramGui) {
/* 101 */     this.gui = paramGui;
/*     */   }
/*     */   
/*     */   void addDefinition(String paramString1, String paramString2) {
/* 105 */     if (this.runner == null) {
/* 106 */       this.runner = new Runner();
/* 107 */       this.runner.start();
/*     */     }
/* 109 */     this.runner.requestProcessing(paramString1, paramString2);
/*     */   }
/*     */   
/*     */   private Expr process(String paramString) {
/* 113 */     JTextArea localJTextArea = this.gui.getOutputArea();
/* 114 */     Context localContext1 = this.gui.getContext();
/*     */     
/* 116 */     Context localContext2 = null;
/* 117 */     if (Options.getSubstituteSymbolsOption().getValue()) {
/* 118 */       localContext2 = localContext1;
/*     */     }
/* 120 */     boolean bool1 = Options.getVaryParenthesesOption().getValue();
/*     */     
/* 122 */     boolean bool2 = Options.getShowIntermediateOption().getValue();
/*     */     
/* 124 */     int i = Options.getMaxLengthOption().getValue();
/*     */     
/*     */       Object localObject;
/*     */     try
/*     */     {
/* 129 */       localObject = Parser.parse(paramString);
/*     */     } catch (Parser.ParseException localParseException) {
/* 131 */       localJTextArea.setText(localParseException.getMessage());
/* 132 */       return null;
/*     */     }
/* 134 */     if (this.runner.state == RUNNER_CANCELED) { return null;
/*     */     }
/*     */     
/* 137 */     Object localObject1 = localContext1.substitute((Expr)localObject);
/* 138 */     localJTextArea.setText(((Expr)localObject1).toStringSubstituteBelow(localContext2, i, bool1));
/*     */     
/* 140 */     Object localObject2 = localObject1;
/* 141 */     Object localObject3 = localObject1;
/* 142 */     int j = ((Expr)localObject1).size();
/* 143 */     Expr localExpr = Simplify.simplify((Expr)localObject1);
/* 144 */     java.util.HashSet localHashSet = new java.util.HashSet();
/* 145 */     Expr[] arrayOfExpr = new Expr[100];
/* 146 */     int k = -1;
/* 147 */     int m = 0;
/* 148 */     while (localExpr != localObject1) {
/* 149 */       localObject1 = localExpr;
/* 150 */       if (bool2) {
/* 151 */         localJTextArea.append("\n   = ");
/* 152 */         localJTextArea.append(((Expr)localObject1).toString(localContext2, i, bool1));
/*     */       }
/*     */       
/* 155 */       int n = ((Expr)localObject1).size();
/* 156 */       ExprWrapper localExprWrapper = new ExprWrapper((Expr)localObject1);
/*     */       
/*     */ 
/* 159 */       m++;
/* 160 */       if ((this.runner.state == RUNNER_CANCELED) || (m > Options.getMaxReductionsOption().getValue()) || (localHashSet.contains(localExprWrapper)))
/*     */       {
/*     */ 
/* 163 */         localJTextArea.append("\n   = ... ");
/* 164 */         localObject1 = localObject3;
/* 165 */         localJTextArea.append(((Expr)localObject1).toString(localContext2, i, bool1));
/*     */         
/* 167 */         break;
/*     */       }
/*     */       
/*     */ 
/* 171 */       k++;
/* 172 */       if (k == arrayOfExpr.length) k = 0;
/* 173 */       if (arrayOfExpr[k] != null) {
/* 174 */         localHashSet.remove(arrayOfExpr[k]);
/*     */       }
/* 176 */       arrayOfExpr[k] = (Expr)localObject1;
/* 177 */       localHashSet.add(localExprWrapper);
/* 178 */       if (n < j) {
/* 179 */         localObject3 = localObject1;
/* 180 */         j = n;
/*     */       }
/*     */       
/*     */ 
/* 184 */       localExpr = Simplify.simplify((Expr)localObject1);
/*     */     }
/* 186 */     if (!bool2) {
/* 187 */       localJTextArea.append("\n   = ");
/* 188 */       localJTextArea.append(((Expr)localObject1).toString(localContext2, i, bool1));
/*     */     }
/* 190 */     return (Expr)localObject1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\Engine.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */