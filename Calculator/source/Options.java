/*     */ package lambda;
/*     */ 
/*     */ import javax.swing.JMenu;
/*     */ 
/*     */ class Options
/*     */ {
/*     */   static abstract interface Listener
/*     */   {
/*     */     public abstract void valueChanged(Options.Option paramOption);
/*     */   }
/*     */   
/*     */   static abstract class Option
/*     */   {
/*  14 */     private java.util.LinkedList listeners = new java.util.LinkedList();
/*     */     
/*     */     private String name;
/*     */     
/*  18 */     Option(String paramString) { this.name = paramString; }
/*     */     
/*     */     abstract javax.swing.JMenuItem createMenuItem();
/*     */     
/*     */     abstract void setValue(String paramString) throws NumberFormatException;
/*     */     
/*     */     abstract String getParameterDescription();
/*  25 */     public String getName() { return this.name; }
/*     */     
/*  27 */     void addListener(Options.Listener paramListener) { this.listeners.add(paramListener); }
/*  28 */     void removeListener(Options.Listener paramListener) { this.listeners.remove(paramListener); }
/*     */     
/*     */     protected void fireChanged() {
/*  31 */       java.util.Iterator localIterator = this.listeners.iterator();
/*  32 */       while (localIterator.hasNext()) {
/*  33 */         Options.Listener localListener = (Options.Listener)localIterator.next();
/*  34 */         localListener.valueChanged(this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class BooleanOption extends Options.Option
/*     */   {
/*     */     protected String title;
/*     */     private boolean value;
/*     */     
/*     */     private class Item extends javax.swing.JCheckBoxMenuItem implements java.awt.event.ActionListener {
/*     */       Item() {
/*  46 */         super();
/*  47 */         setSelected(Options.BooleanOption.this.getValue());
/*  48 */         addActionListener(this);
/*     */       }
/*     */       
/*  51 */       public void actionPerformed(java.awt.event.ActionEvent paramActionEvent) { Options.BooleanOption.this.setValue(isSelected()); }
/*     */     }
/*     */     
/*     */     BooleanOption(String paramString1, String paramString2, boolean paramBoolean)
/*     */     {
/*  56 */       super();
/*  57 */       this.title = paramString1;
/*  58 */       this.value = paramBoolean;
/*     */     }
/*     */     
/*  61 */     javax.swing.JMenuItem createMenuItem() { return new Item(); }
/*     */     
/*  63 */     String getParameterDescription() { return "{yes,no}"; }
/*     */     
/*  65 */     void setValue(String paramString) throws NumberFormatException { if (paramString.equals("yes")) { setValue(true);
/*  66 */       } else if (paramString.equals("no")) setValue(false); else
/*  67 */         throw new NumberFormatException("cannot set yes/no value to '" + paramString + "'");
/*     */     }
/*     */     
/*     */     void setValue(boolean paramBoolean) {
/*  71 */       this.value = paramBoolean;
/*  72 */       fireChanged();
/*     */     }
/*     */     
/*  75 */     boolean getValue() { return this.value; }
/*     */   }
/*     */   
/*     */   private static class PrintLambdaOption extends Options.BooleanOption
/*     */   {
/*     */     private class Item extends javax.swing.JCheckBoxMenuItem implements Options.Listener, java.awt.event.ActionListener
/*     */     {
/*     */       Item() {
/*  83 */         super();
/*  84 */         setSelected(Options.PrintLambdaOption.this.getValue());
/*  85 */         addActionListener(this);
/*  86 */         Options.getFontSizeOption().addListener(this);
/*  87 */         valueChanged(null);
/*     */       }
/*     */       
/*  90 */       public void actionPerformed(java.awt.event.ActionEvent paramActionEvent) { Options.PrintLambdaOption.this.setValue(isSelected()); }
/*     */       
/*     */       public void valueChanged(Options.Option paramOption) {
/*  93 */         setEnabled(getFont().canDisplay('λ'));
/*     */       }
/*     */     }
/*     */     
/*     */     PrintLambdaOption(boolean paramBoolean) {
/*  98 */       super("print_lambda", paramBoolean);
/*     */     }
/*     */     
/* 101 */     javax.swing.JMenuItem createMenuItem() { return new Item(); }
/*     */     
/*     */     char getLambdaChar() {
/* 104 */       if ((getValue()) && (Options.getFont().canDisplay('λ'))) {
/* 105 */         return 'λ';
/*     */       }
/* 107 */       return '\\';
/*     */     }
/*     */   }
/*     */   
/*     */   static class IntOption extends Options.Option
/*     */   {
/*     */     protected String title;
/*     */     private int value;
/*     */     private int[] options;
/*     */     
/*     */     private class SubItem extends javax.swing.JRadioButtonMenuItem implements java.awt.event.ActionListener
/*     */     {
/*     */       int value;
/*     */       
/*     */       SubItem(int paramInt, javax.swing.ButtonGroup paramButtonGroup) {
/* 122 */         super();
/* 123 */         this.value = paramInt;
/* 124 */         paramButtonGroup.add(this);
/* 125 */         setSelected(Options.IntOption.this.getValue() == paramInt);
/* 126 */         addActionListener(this);
/*     */       }
/*     */       
/* 129 */       public void actionPerformed(java.awt.event.ActionEvent paramActionEvent) { if (isSelected()) {
/* 130 */           Options.IntOption.this.setValue(this.value);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     IntOption(String paramString1, String paramString2, int paramInt, int[] paramArrayOfInt) {
/* 136 */       super();
/* 137 */       this.title = paramString1;
/* 138 */       setValue(paramInt);
/* 139 */       this.options = paramArrayOfInt;
/*     */     }
/*     */     
/* 142 */     javax.swing.JMenuItem createMenuItem() { JMenu localJMenu = new JMenu(this.title);
/* 143 */       javax.swing.ButtonGroup localButtonGroup = new javax.swing.ButtonGroup();
/* 144 */       int i = 0;
/* 145 */       int j = getValue();
/* 146 */       for (int k = 0; k < this.options.length; k++) {
/* 147 */         if ((i == 0) && (j < this.options[k])) {
/* 148 */           localJMenu.add(new SubItem(j, localButtonGroup));
/* 149 */           i = 1;
/*     */         }
/* 151 */         if (j == this.options[k]) i = 1;
/* 152 */         localJMenu.add(new SubItem(this.options[k], localButtonGroup));
/*     */       }
/* 154 */       if (i == 0) localJMenu.add(new SubItem(j, localButtonGroup));
/* 155 */       return localJMenu; }
/*     */     
/* 157 */     String getParameterDescription() { return "#"; }
/*     */     
/* 159 */     void setValue(String paramString) throws NumberFormatException { setValue(Integer.parseInt(paramString)); }
/*     */     
/*     */     void setValue(int paramInt) {
/* 162 */       this.value = paramInt;
/* 163 */       fireChanged();
/*     */     }
/*     */     
/* 166 */     int getValue() { return this.value; }
/*     */   }
/*     */   
/*     */   private static class EvaluationOrderOption extends Options.IntOption
/*     */   {
/*     */     private class SubItem extends javax.swing.JRadioButtonMenuItem implements java.awt.event.ActionListener {
/*     */       int value;
/*     */       
/*     */       SubItem(int paramInt, String paramString, javax.swing.ButtonGroup paramButtonGroup) {
/* 175 */         super();
/* 176 */         this.value = paramInt;
/* 177 */         paramButtonGroup.add(this);
/* 178 */         setSelected(Options.EvaluationOrderOption.this.getValue() == paramInt);
/* 179 */         addActionListener(this);
/*     */       }
/*     */       
/* 182 */       public void actionPerformed(java.awt.event.ActionEvent paramActionEvent) { if (isSelected()) Options.EvaluationOrderOption.this.setValue(this.value);
/*     */       }
/*     */     }
/*     */     
/*     */     EvaluationOrderOption(int paramInt) {
/* 187 */       super("evaluation_order", paramInt, null);
/*     */     }
/*     */     
/*     */     javax.swing.JMenuItem createMenuItem() {
/* 191 */       JMenu localJMenu = new JMenu("Evaluation Order");
/* 192 */       javax.swing.ButtonGroup localButtonGroup = new javax.swing.ButtonGroup();
/* 193 */       localJMenu.add(new SubItem(0, "Applicative (Eager)", localButtonGroup));
/*     */       
/* 195 */       localJMenu.add(new SubItem(1, "Normal (Lazy)", localButtonGroup));
/*     */       
/* 197 */       localJMenu.add(new SubItem(2, "Normal Without Thunks", localButtonGroup));
/*     */       
/* 199 */       return localJMenu;
/*     */     }
/*     */     
/* 202 */     String getParameterDescription() { return "{applicative,normal,normal_slow}"; }
/*     */     
/*     */     void setValue(String paramString) throws NumberFormatException {
/* 205 */       if (paramString.equals("applicative")) {
/* 206 */         setValue(0);
/* 207 */       } else if (paramString.equals("normal")) {
/* 208 */         setValue(1);
/* 209 */       } else if (paramString.equals("normal_slow")) {
/* 210 */         setValue(2);
/*     */       } else {
/* 212 */         throw new NumberFormatException("invalid evaluation order");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FontSizeOption extends Options.IntOption {
/*     */     private java.awt.Font font;
/*     */     
/*     */     FontSizeOption(int paramInt) {
/* 221 */       super("font_size", paramInt, new int[] { 10, 12, 14, 18, 24 });
/*     */     }
/*     */     
/*     */     int getValue()
/*     */     {
/* 226 */       return this.font.getSize();
/*     */     }
/*     */     
/*     */     java.awt.Font getFont() {
/* 230 */       return this.font;
/*     */     }
/*     */     
/*     */     void setValue(int paramInt) {
/* 234 */       this.font = new java.awt.Font("monospaced", 1, paramInt);
/* 235 */       fireChanged();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 240 */   private static Option[] options = { new IntOption("Maximum Reductions", "max_reductions", 100, new int[] { 25, 50, 100, 200, 300, 400, 500, 1000 }), new EvaluationOrderOption(1), new BooleanOption("Enable Eta Reductions", "eta_reductions", true), new BooleanOption("Use Applied Calculus", "use_applied", true), new IntOption("Maximum Length", "max_length", 300, new int[] { 100, 300, 1000, Integer.MAX_VALUE }), new BooleanOption("Show Intermediate Steps", "show_intermediate", true), new BooleanOption("Substitute Symbols", "substitute_symbols", true), new BooleanOption("Vary Parentheses", "vary_parentheses", false), new PrintLambdaOption(true), new FontSizeOption(12), new BooleanOption("Show Browser", "show_browser", true) };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 259 */   private static java.util.HashMap option_map = null;
/* 260 */   private static char lambda = '?';
/*     */   
/*     */   private static Option get(String paramString) {
/* 263 */     if (option_map == null) {
/* 264 */       option_map = new java.util.HashMap();
/* 265 */       for (int i = 0; i < options.length; i++) {
/* 266 */         option_map.put(options[i].getName(), options[i]);
/*     */       }
/*     */     }
/* 269 */     return (Option)option_map.get(paramString);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static IntOption getMaxReductionsOption()
/*     */   {
/* 276 */     return (IntOption)get("max_reductions");
/*     */   }
/*     */   
/* 279 */   static IntOption getEvaluationOrderOption() { return (IntOption)get("evaluation_order"); }
/*     */   
/*     */   static BooleanOption getEtaReductionsOption() {
/* 282 */     return (BooleanOption)get("eta_reductions");
/*     */   }
/*     */   
/* 285 */   static BooleanOption getUseAppliedOption() { return (BooleanOption)get("use_applied"); }
/*     */   
/*     */   static IntOption getMaxLengthOption() {
/* 288 */     return (IntOption)get("max_length");
/*     */   }
/*     */   
/* 291 */   static BooleanOption getPrintLambdaOption() { return (BooleanOption)get("print_lambda"); }
/*     */   
/*     */   static BooleanOption getShowIntermediateOption() {
/* 294 */     return (BooleanOption)get("show_intermediate");
/*     */   }
/*     */   
/* 297 */   static BooleanOption getSubstituteSymbolsOption() { return (BooleanOption)get("substitute_symbols"); }
/*     */   
/*     */   static BooleanOption getVaryParenthesesOption() {
/* 300 */     return (BooleanOption)get("vary_parentheses");
/*     */   }
/*     */   
/* 303 */   static IntOption getFontSizeOption() { return (IntOption)get("font_size"); }
/*     */   
/*     */   static BooleanOption getShowBrowserOption() {
/* 306 */     return (BooleanOption)get("show_browser");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static char getLambdaChar()
/*     */   {
/* 313 */     return ((PrintLambdaOption)getPrintLambdaOption()).getLambdaChar();
/*     */   }
/*     */   
/*     */   static java.awt.Font getFont() {
/* 317 */     return ((FontSizeOption)getFontSizeOption()).getFont();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static void printOptions()
/*     */   {
/* 324 */     for (int i = 0; i < options.length; i++) {
/* 325 */       System.err.print("  -" + options[i].getName() + "=");
/* 326 */       System.err.println(options[i].getParameterDescription());
/*     */     }
/*     */   }
/*     */   
/* 330 */   static void parseOptions(String[] paramArrayOfString) { if (paramArrayOfString == null) return;
/* 331 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 332 */       int j = paramArrayOfString[i].indexOf("=");
/* 333 */       if ((paramArrayOfString[i] != null) && (paramArrayOfString[i].charAt(0) == '-') && (j >= 0))
/*     */       {
/* 335 */         String str1 = paramArrayOfString[i].substring(1, j);
/* 336 */         String str2 = paramArrayOfString[i].substring(j + 1);
/*     */         try {
/* 338 */           Option localOption = get(str1);
/* 339 */           if (localOption != null) {
/* 340 */             localOption.setValue(str2);
/* 341 */             paramArrayOfString[i] = null;
/*     */           }
/*     */         }
/*     */         catch (NumberFormatException localNumberFormatException) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static JMenu getMenu()
/*     */   {
/* 352 */     JMenu localJMenu1 = new JMenu("Options");
/* 353 */     JMenu localJMenu2 = new JMenu("Engine");localJMenu1.add(localJMenu2);
/* 354 */     JMenu localJMenu3 = new JMenu("Printing");localJMenu1.add(localJMenu3);
/* 355 */     JMenu localJMenu4 = new JMenu("Interface");localJMenu1.add(localJMenu4);
/*     */     
/* 357 */     localJMenu2.add(getMaxReductionsOption().createMenuItem());
/* 358 */     localJMenu2.add(getEvaluationOrderOption().createMenuItem());
/* 359 */     localJMenu2.add(getUseAppliedOption().createMenuItem());
/* 360 */     localJMenu2.add(getEtaReductionsOption().createMenuItem());
/*     */     
/* 362 */     localJMenu3.add(getMaxLengthOption().createMenuItem());
/* 363 */     localJMenu3.add(getPrintLambdaOption().createMenuItem());
/* 364 */     localJMenu3.add(getShowIntermediateOption().createMenuItem());
/* 365 */     localJMenu3.add(getSubstituteSymbolsOption().createMenuItem());
/* 366 */     localJMenu3.add(getVaryParenthesesOption().createMenuItem());
/*     */     
/* 368 */     localJMenu4.add(getFontSizeOption().createMenuItem());
/* 369 */     localJMenu4.add(getShowBrowserOption().createMenuItem());
/*     */     
/* 371 */     return localJMenu1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\Options.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */