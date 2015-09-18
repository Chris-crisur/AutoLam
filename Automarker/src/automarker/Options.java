package automarker;

import javax.swing.JMenu;

class Options
{  
	//default values
	private static Option[] options = { new IntOption("Maximum Reductions", "max_reductions", 100, new int[] { 25, 50, 100, 200, 300, 400, 500, 1000 }), new EvaluationOrderOption(2), new BooleanOption("Enable Eta Reductions", "eta_reductions", true), new BooleanOption("Use Applied Calculus", "use_applied", true), new IntOption("Maximum Length", "max_length", 300, new int[] { 100, 300, 1000, Integer.MAX_VALUE }), new BooleanOption("Show Intermediate Steps", "show_intermediate", true), new BooleanOption("Substitute Symbols", "substitute_symbols", true), new BooleanOption("Vary Parentheses", "vary_parentheses", false), new PrintLambdaOption(true), new FontSizeOption(12), new BooleanOption("Show Browser", "show_browser", true) };
static abstract interface Listener
{
  public abstract void valueChanged(Options.Option paramOption);
}

static abstract class Option
{
   private java.util.LinkedList listeners = new java.util.LinkedList();
  
  private String name;
  
   Option(String paramString) { this.name = paramString; }
			

  
  abstract javax.swing.JMenuItem createMenuItem();
  
  abstract void setValue(String paramString) throws NumberFormatException;
  
  abstract String getParameterDescription();
   public String getName() { return this.name; }
  
   void addListener(Options.Listener paramListener) { this.listeners.add(paramListener); }
   void removeListener(Options.Listener paramListener) { this.listeners.remove(paramListener); }
  
  protected void fireChanged() {
     java.util.Iterator localIterator = this.listeners.iterator();
     while (localIterator.hasNext()) {
       Options.Listener localListener = (Options.Listener)localIterator.next();
       localListener.valueChanged(this);
    }
  }
}

static class BooleanOption extends Options.Option
{
  protected String title;
  private boolean value;
  
  private class Item extends javax.swing.JCheckBoxMenuItem implements java.awt.event.ActionListener {
    Item(String string) {
       super(string);
       setSelected(Options.BooleanOption.this.getValue());
       addActionListener(this);
    }
    
     public void actionPerformed(java.awt.event.ActionEvent paramActionEvent) { Options.BooleanOption.this.setValue(isSelected()); }
  }
  
  BooleanOption(String paramString1, String paramString2, boolean paramBoolean)
  {
     super(paramString2);
     this.title = paramString1;
     this.value = paramBoolean;
  }
  
   javax.swing.JMenuItem createMenuItem() { return new Item(this.title); }
  
   String getParameterDescription() { return "{yes,no}"; }
  
   void setValue(String paramString) throws NumberFormatException { if (paramString.equals("yes")) { setValue(true);
     } else if (paramString.equals("no")) setValue(false); else
       throw new NumberFormatException("cannot set yes/no value to '" + paramString + "'");
  }
  
  void setValue(boolean paramBoolean) {
     this.value = paramBoolean;
     fireChanged();
  }
  
   boolean getValue() { return this.value; }
}

private static class PrintLambdaOption extends Options.BooleanOption
{
  private class Item extends javax.swing.JCheckBoxMenuItem implements Options.Listener, java.awt.event.ActionListener
  {
    Item(String string) {
       super(string);
       setSelected(Options.PrintLambdaOption.this.getValue());
       addActionListener(this);
       Options.getFontSizeOption().addListener(this);
       valueChanged(null);
    }
    
     public void actionPerformed(java.awt.event.ActionEvent paramActionEvent) { Options.PrintLambdaOption.this.setValue(isSelected()); }
    
    public void valueChanged(Options.Option paramOption) {
       setEnabled(getFont().canDisplay('λ'));
    }
  }
  
  PrintLambdaOption(boolean paramBoolean) {
     super("Print Lambda", "print_lambda", paramBoolean);
  }
  
   javax.swing.JMenuItem createMenuItem() { return new Item("Print Lambda"); }
  
  char getLambdaChar() {
     if ((getValue()) && (Options.getFont().canDisplay('λ'))) {
       return 'λ';
    }
     return '\\';
  }
}

static class IntOption extends Options.Option
{
  protected String title;
  private int value;
  private int[] options;
  
  private class SubItem extends javax.swing.JRadioButtonMenuItem implements java.awt.event.ActionListener
  {
    int value;
    
    SubItem(int paramInt, javax.swing.ButtonGroup paramButtonGroup) {
       super(Integer.toString(paramInt));
       this.value = paramInt;
       paramButtonGroup.add(this);
       setSelected(Options.IntOption.this.getValue() == paramInt);
       addActionListener(this);
    }
    
     public void actionPerformed(java.awt.event.ActionEvent paramActionEvent) { if (isSelected()) {
         Options.IntOption.this.setValue(this.value);
      }
    }
  }
  
  IntOption(String paramString1, String string, int paramInt, int[] paramArrayOfInt) {
     super(string);
     this.title = paramString1;
     setValue(paramInt);
     this.options = paramArrayOfInt;
  }
  
   javax.swing.JMenuItem createMenuItem() { JMenu localJMenu = new JMenu(this.title);
     javax.swing.ButtonGroup localButtonGroup = new javax.swing.ButtonGroup();
     int i = 0;
     int j = getValue();
     for (int k = 0; k < this.options.length; k++) {
       if ((i == 0) && (j < this.options[k])) {
         localJMenu.add(new SubItem(j, localButtonGroup));
         i = 1;
      }
       if (j == this.options[k]) i = 1;
       localJMenu.add(new SubItem(this.options[k], localButtonGroup));
    }
     if (i == 0) localJMenu.add(new SubItem(j, localButtonGroup));
     return localJMenu; }
  
   String getParameterDescription() { return "#"; }
  
   void setValue(String paramString) throws NumberFormatException { setValue(Integer.parseInt(paramString)); }
  
  void setValue(int paramInt) {
     this.value = paramInt;
     fireChanged();
  }
  
   int getValue() { return this.value; }
}

private static class EvaluationOrderOption extends Options.IntOption
{
  private class SubItem extends javax.swing.JRadioButtonMenuItem implements java.awt.event.ActionListener {
    int value;
    
    SubItem(int paramInt, String paramString, javax.swing.ButtonGroup paramButtonGroup) {
       super(paramString);
       this.value = paramInt;
       paramButtonGroup.add(this);
       setSelected(Options.EvaluationOrderOption.this.getValue() == paramInt);
       addActionListener(this);
    }
    
     public void actionPerformed(java.awt.event.ActionEvent paramActionEvent) { if (isSelected()) Options.EvaluationOrderOption.this.setValue(this.value);
    }
  }
  
  EvaluationOrderOption(int paramInt) {
     super("Evaluation Order","evaluation_order", paramInt, null);
  }
  
  javax.swing.JMenuItem createMenuItem() {
     JMenu localJMenu = new JMenu("Evaluation Order");
     javax.swing.ButtonGroup localButtonGroup = new javax.swing.ButtonGroup();
     localJMenu.add(new SubItem(0, "Applicative (Eager)", localButtonGroup));
    
     localJMenu.add(new SubItem(1, "Normal (Lazy)", localButtonGroup));
    
     localJMenu.add(new SubItem(2, "Normal Without Thunks", localButtonGroup));
    
     return localJMenu;
  }
  
   String getParameterDescription() { return "{applicative,normal,normal_slow}"; }
  
  void setValue(String paramString) throws NumberFormatException {
     if (paramString.equals("applicative")) {
       setValue(0);
     } else if (paramString.equals("normal")) {
       setValue(1);
     } else if (paramString.equals("normal_slow")) {
       setValue(2);
    } else {
       throw new NumberFormatException("invalid evaluation order");
    }
  }
}

private static class FontSizeOption extends Options.IntOption {
  private java.awt.Font font;
  
  FontSizeOption(int paramInt) {
     super("Font Size","font_size", paramInt, new int[] { 10, 12, 14, 18, 24 });
  }
  
  int getValue()
  {
     return this.font.getSize();
  }
  
  java.awt.Font getFont() {
     return this.font;
  }
  
  void setValue(int paramInt) {
     this.font = new java.awt.Font("monospaced", 1, paramInt);
     fireChanged();
  }
}

 
  

















 private static java.util.HashMap option_map = null;
 private static char lambda = '?';

private static Option get(String paramString) {
   if (option_map == null) {
     option_map = new java.util.HashMap();
     for (int i = 0; i < options.length; i++) {
       option_map.put(options[i].getName(), options[i]);
    }
  }
   return (Option)option_map.get(paramString);
}



static IntOption getMaxReductionsOption()
{
   return (IntOption)get("max_reductions");
}

static IntOption getEvaluationOrderOption() { return (IntOption)get("evaluation_order"); }

static BooleanOption getEtaReductionsOption() {
   return (BooleanOption)get("eta_reductions");
}

static BooleanOption getUseAppliedOption() { return (BooleanOption)get("use_applied"); }

static IntOption getMaxLengthOption() {
   return (IntOption)get("max_length");
}

static BooleanOption getPrintLambdaOption() { return (BooleanOption)get("print_lambda"); }

static BooleanOption getShowIntermediateOption() {
   return (BooleanOption)get("show_intermediate");
}

static BooleanOption getSubstituteSymbolsOption() { return (BooleanOption)get("substitute_symbols"); }

static BooleanOption getVaryParenthesesOption() {
   return (BooleanOption)get("vary_parentheses");
}

static IntOption getFontSizeOption() { return (IntOption)get("font_size"); }

static BooleanOption getShowBrowserOption() {
   return (BooleanOption)get("show_browser");
}



static char getLambdaChar()
{
   return ((PrintLambdaOption)getPrintLambdaOption()).getLambdaChar();
}

static java.awt.Font getFont() {
    return ((FontSizeOption)getFontSizeOption()).getFont();
}



static void printOptions()
{
   for (int i = 0; i < options.length; i++) {
     System.err.print("  -" + options[i].getName() + "=");
     System.err.println(options[i].getParameterDescription());
  }
}

 static void parseOptions(String[] paramArrayOfString) { if (paramArrayOfString == null) return;
   for (int i = 0; i < paramArrayOfString.length; i++) {
     int j = paramArrayOfString[i].indexOf("=");
     if ((paramArrayOfString[i] != null) && (paramArrayOfString[i].charAt(0) == '-') && (j >= 0))
    {
       String str1 = paramArrayOfString[i].substring(1, j);
       String str2 = paramArrayOfString[i].substring(j + 1);
      try {
         Option localOption = get(str1);
         if (localOption != null) {
           localOption.setValue(str2);
           paramArrayOfString[i] = null;
        }
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
  }
}


static JMenu getMenu()
{
   JMenu localJMenu1 = new JMenu("Options");
   JMenu localJMenu2 = new JMenu("Engine");
   localJMenu1.add(localJMenu2);
   JMenu localJMenu3 = new JMenu("Printing");
   localJMenu1.add(localJMenu3);
   JMenu localJMenu4 = new JMenu("Interface");
   localJMenu1.add(localJMenu4);
  
   localJMenu2.add(getMaxReductionsOption().createMenuItem());
   localJMenu2.add(getEvaluationOrderOption().createMenuItem());
   localJMenu2.add(getUseAppliedOption().createMenuItem());
 localJMenu2.add(getEtaReductionsOption().createMenuItem());
  
 localJMenu3.add(getMaxLengthOption().createMenuItem());
 localJMenu3.add(getPrintLambdaOption().createMenuItem());
 localJMenu3.add(getShowIntermediateOption().createMenuItem());
 localJMenu3.add(getSubstituteSymbolsOption().createMenuItem());
 localJMenu3.add(getVaryParenthesesOption().createMenuItem());
  
 localJMenu4.add(getFontSizeOption().createMenuItem());
 localJMenu4.add(getShowBrowserOption().createMenuItem());
  
 return localJMenu1;
}
}
