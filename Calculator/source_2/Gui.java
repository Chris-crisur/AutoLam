/*     */ package lambda;
/*     */
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.FocusEvent;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.text.JTextComponent;
/*     */
/*     */ class Gui extends javax.swing.JFrame
/*     */ {
/*     */   private static final String HELP_BASE_DIR = "lambda/doc/";
/*     */   private JTextField sym_name;
/*     */   private JTextField input;
/*     */   private javax.swing.JTextArea output;
/*     */   private javax.swing.JScrollPane output_pane;
/*     */   private SymbolBrowser browser;
/*     */   private javax.swing.JScrollPane browser_pane;
/*     */   private javax.swing.JSplitPane main_pane;
/*  20 */   private Context context = new Context();
/*  21 */   private Engine engine = new Engine(this);
/*  22 */   private javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
/*     */
/*     */   private class MenuFile extends javax.swing.JMenu implements java.awt.event.ActionListener {
/*  25 */     JMenuItem open = new JMenuItem("Import...");
/*  26 */     JMenuItem save = new JMenuItem("Export...");
/*  27 */     JMenuItem quit = new JMenuItem("Quit");
/*     */
/*  29 */     MenuFile() { super();
/*  30 */       add(this.open);this.open.addActionListener(this);
/*  31 */       add(this.save);this.save.addActionListener(this);
/*  32 */       add(this.quit);this.quit.addActionListener(this); }
/*     */
/*     */     public void actionPerformed(ActionEvent paramActionEvent) { int i;
/*  35 */       if (paramActionEvent.getSource() == this.open) {
/*  36 */         i = Gui.this.chooser.showOpenDialog(Gui.this);
/*  37 */         if (i == 0) {
/*     */           try {
/*  39 */             Gui.this.context.importFile(Gui.this.chooser.getSelectedFile());
/*     */           } catch (java.io.IOException localIOException1) {
/*  41 */             javax.swing.JOptionPane.showMessageDialog(Gui.this, "Error importing: " + localIOException1, "Import Error", 0);
/*     */           }
/*     */
/*     */         }
/*     */       }
/*  46 */       else if (paramActionEvent.getSource() == this.save) {
/*  47 */         i = Gui.this.chooser.showSaveDialog(Gui.this);
/*  48 */         if (i == 0) {
/*     */           try {
/*  50 */             Gui.this.context.exportFile(Gui.this.chooser.getSelectedFile());
/*     */           } catch (java.io.IOException localIOException2) {
/*  52 */             javax.swing.JOptionPane.showMessageDialog(Gui.this, "Error exporting: " + localIOException2, "Export Error", 0);
/*     */           }
/*     */
/*     */         }
/*     */       }
/*  57 */       else if (paramActionEvent.getSource() == this.quit) {
/*  58 */         new Gui.MyWindowListener(Gui.this, null).windowClosing(null);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   private class MenuEdit extends javax.swing.JMenu implements java.awt.event.ActionListener, java.awt.event.FocusListener
/*     */   {
/*  65 */     JMenuItem cut = new JMenuItem("Cut");
/*  66 */     JMenuItem copy = new JMenuItem("Copy");
/*  67 */     JMenuItem paste = new JMenuItem("Paste");
/*  68 */     JTextComponent component = null;
/*     */
/*  70 */     MenuEdit() { super();
/*  71 */       add(this.cut);this.cut.addActionListener(this);
/*  72 */       add(this.copy);this.copy.addActionListener(this);
/*  73 */       add(this.paste);this.paste.addActionListener(this);
/*     */     }
/*     */
/*  76 */     public void actionPerformed(ActionEvent paramActionEvent) { int i = this.component != Gui.this.output ? 1 : 0;
/*  77 */       if (paramActionEvent.getSource() == this.cut) {
/*  78 */         if (i != 0) this.component.cut();
/*  79 */       } else if (paramActionEvent.getSource() == this.copy) {
/*  80 */         this.component.copy();
/*  81 */       } else if ((paramActionEvent.getSource() == this.paste) &&
/*  82 */         (i != 0)) this.component.paste();
/*     */     }
/*     */
/*     */     public void focusGained(FocusEvent paramFocusEvent) {
/*  86 */       if (paramFocusEvent.isTemporary()) return;
/*  87 */       Object localObject = paramFocusEvent.getSource();
/*  88 */       if ((localObject instanceof JTextComponent)) {
/*  89 */         boolean bool = ((JTextComponent)localObject).isEditable();
/*  90 */         this.cut.setEnabled(bool);
/*  91 */         this.copy.setEnabled(true);
/*  92 */         this.paste.setEnabled(bool);
/*     */       } else {
/*  94 */         this.cut.setEnabled(false);
/*  95 */         this.copy.setEnabled(false);
/*  96 */         this.paste.setEnabled(false);
/*     */       }
/*     */     }
/*     */
/*     */     public void focusLost(FocusEvent paramFocusEvent) {}
/*     */   }
/*     */
/*     */   private class MenuHelp extends javax.swing.JMenu implements java.awt.event.ActionListener {
/* 104 */     JMenuItem help = new JMenuItem("Help");
/* 105 */     JMenuItem about = new JMenuItem("About");
/*     */
/* 107 */     HelpFrame frame = null;
/* 108 */     java.net.URL index = null;
/*     */
/*     */     MenuHelp() {
/* 111 */       super();
/* 112 */       add(this.help);this.help.addActionListener(this);
/* 113 */       add(this.about);this.about.addActionListener(this);
/*     */
/* 115 */       this.frame = new HelpFrame("doc/index.html");
/* 116 */       this.frame.setTitle("Logisim Help");
/* 117 */       this.index = this.frame.getCurrent();
/* 118 */       this.frame.addContentsItem("Contents", this.index);
/*     */     }
/*     */
/* 121 */     public void actionPerformed(ActionEvent paramActionEvent) { if (paramActionEvent.getSource() == this.help) {
/* 122 */         this.frame.load(this.index);
/* 123 */         this.frame.show();
/* 124 */       } else if (paramActionEvent.getSource() == this.about) {
/* 125 */         javax.swing.JOptionPane.showMessageDialog(Gui.this, "Lambda Calculator, v1.01. (c) 2003, Carl Burch. See www.cburch.com/proj/lambda for details.");
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   private class SelectAllListener
/*     */     implements java.awt.event.FocusListener
/*     */   {
/* 133 */     SelectAllListener(Gui.1 param1) { this(); }
/*     */     public void focusLost(FocusEvent paramFocusEvent) {}
/* 135 */     public void focusGained(FocusEvent paramFocusEvent) { JTextComponent localJTextComponent = (JTextComponent)paramFocusEvent.getSource();
/* 136 */       localJTextComponent.selectAll();
/*     */     }
/*     */
/*     */     private SelectAllListener() {} }
/*     */
/* 141 */   private class MyWindowListener extends java.awt.event.WindowAdapter { MyWindowListener(Gui.1 param1) { this(); }
/*     */
/* 143 */     public void windowClosing(java.awt.event.WindowEvent paramWindowEvent) { int i = javax.swing.JOptionPane.showConfirmDialog(Gui.this, "Are you sure you want to exit?", "Confirm Exit", 0);
/*     */
/*     */
/* 146 */       if (i == 0)
/* 147 */         System.exit(0);
/*     */     }
/*     */
/*     */     private MyWindowListener() {} }
/*     */
/* 152 */   private class MySymNameListener implements java.awt.event.ActionListener { MySymNameListener(Gui.1 param1) { this(); }
/*     */
/* 154 */     public void actionPerformed(ActionEvent paramActionEvent) { Gui.this.input.requestFocus(); }
/*     */
/*     */     private MySymNameListener() {} }
/*     */
/* 158 */   private class MyInputListener implements java.awt.event.ActionListener { MyInputListener(Gui.1 param1) { this(); }
/*     */
/* 160 */     public void actionPerformed(ActionEvent paramActionEvent) { String str1 = Gui.this.sym_name.getText();
/* 161 */       String str2 = Gui.this.input.getText();
/* 162 */       Gui.this.engine.addDefinition(str1, str2);
/* 163 */       Gui.this.sym_name.setText("");
/* 164 */       Gui.this.sym_name.requestFocus();
/*     */     }
/*     */
/*     */     private MyInputListener() {}
/*     */   }
/*     */
/*     */   private class SymbolBrowser extends javax.swing.JList implements javax.swing.event.ListSelectionListener, javax.swing.event.DocumentListener, Options.Listener {
/*     */     int sel_index;
/*     */     String input_val;
/*     */
/*     */     SymbolBrowser() {
/* 175 */       super();
/* 176 */       setSelectionMode(0);
/* 177 */       addListSelectionListener(this);
/* 178 */       Options.getShowBrowserOption().addListener(this);
/*     */     }
/*     */
/*     */     public void valueChanged(javax.swing.event.ListSelectionEvent paramListSelectionEvent) {
/* 182 */       String str1 = (String)Gui.this.browser.getSelectedValue();
/* 183 */       if (str1 == null) { return;
/*     */       }
/* 185 */       Expr localExpr = Gui.this.context.get(str1);
/* 186 */       if ((localExpr != null) && (this.sel_index != Gui.this.browser.getSelectedIndex())) {
/* 187 */         String str2 = localExpr.toStringSubstituteBelow(Gui.this.context, Integer.MAX_VALUE, Options.getVaryParenthesesOption().getValue());
/*     */
/*     */
/* 190 */         this.input_val = str2.replace('\\', Options.getLambdaChar());
/* 191 */         this.sel_index = Gui.this.browser.getSelectedIndex();
/* 192 */         Gui.this.input.setText(this.input_val);
/*     */       }
/*     */     }
/*     */
/*     */     public void changedUpdate(javax.swing.event.DocumentEvent paramDocumentEvent) {}
/*     */
/* 198 */     public void insertUpdate(javax.swing.event.DocumentEvent paramDocumentEvent) { if (Gui.this.input.getText().equals(this.input_val)) {
/* 199 */         Gui.this.browser.setSelectedIndex(this.sel_index);
/*     */       } else
/* 201 */         Gui.this.browser.clearSelection();
/*     */     }
/*     */
/*     */     public void removeUpdate(javax.swing.event.DocumentEvent paramDocumentEvent) {
/* 205 */       if (Gui.this.input.getText().equals(this.input_val)) {
/* 206 */         Gui.this.browser.setSelectedIndex(this.sel_index);
/*     */       } else {
/* 208 */         Gui.this.browser.clearSelection();
/*     */       }
/*     */     }
/*     */
/*     */     public void valueChanged(Options.Option paramOption) {
/* 213 */       if (((Options.BooleanOption)paramOption).getValue()) {
/* 214 */         Gui.this.main_pane.setLeftComponent(Gui.this.browser_pane);
/*     */       } else {
/* 216 */         Gui.this.main_pane.remove(Gui.this.browser_pane);
/*     */       }
/* 218 */       Gui.this.pack();
/*     */     }
/*     */   }
/*     */
/* 222 */   private class FontListener implements Options.Listener { FontListener(Gui.1 param1) { this(); }
/*     */
/* 224 */     public void valueChanged(Options.Option paramOption) { java.awt.Font localFont = Options.getFont();
/* 225 */       Gui.this.sym_name.setFont(localFont);
/* 226 */       Gui.this.input.setFont(localFont);
/* 227 */       Gui.this.output.setFont(localFont);
/* 228 */       Gui.this.pack();
/*     */     }
/*     */
/*     */     private FontListener() {} }
/*     */
/* 233 */   public Gui() { super("Lambda Calculator");
/* 234 */     Options.getFontSizeOption().addListener(new FontListener(null));
/* 235 */     addWindowListener(new MyWindowListener(null));
/* 236 */     java.awt.Font localFont = Options.getFont();
/*     */
/* 238 */     javax.swing.JMenuBar localJMenuBar = new javax.swing.JMenuBar();
/* 239 */     MenuFile localMenuFile = new MenuFile();
/* 240 */     MenuEdit localMenuEdit = new MenuEdit();
/* 241 */     javax.swing.JMenu localJMenu = Options.getMenu();
/* 242 */     MenuHelp localMenuHelp = new MenuHelp();
/* 243 */     setJMenuBar(localJMenuBar);
/* 244 */     localJMenuBar.add(localMenuFile);
/* 245 */     localJMenuBar.add(localMenuEdit);
/* 246 */     localJMenuBar.add(localJMenu);
/* 247 */     localJMenuBar.add(localMenuHelp);
/*     */
/* 249 */     this.sym_name = LambdaText.createTextField(7);
/* 250 */     this.sym_name.setFont(localFont);
/* 251 */     this.sym_name.addActionListener(new MySymNameListener(null));
/* 252 */     this.sym_name.addFocusListener(localMenuEdit);
/* 253 */     this.sym_name.addFocusListener(new SelectAllListener(null));
/*     */
/* 255 */     this.input = LambdaText.createTextField(30);
/* 256 */     this.input.setFont(localFont);
/* 257 */     this.input.addActionListener(new MyInputListener(null));
/* 258 */     this.input.addFocusListener(localMenuEdit);
/* 259 */     this.input.addFocusListener(new SelectAllListener(null));
/*     */
/* 261 */     this.output = LambdaText.createTextArea();
/* 262 */     this.output.setFont(localFont);
/* 263 */     this.output.setEditable(false);
/* 264 */     this.output.addFocusListener(localMenuEdit);
/* 265 */     this.output_pane = new javax.swing.JScrollPane(this.output);
/*     */
/* 267 */     this.browser = new SymbolBrowser();
/* 268 */     this.input.getDocument().addDocumentListener(this.browser);
/* 269 */     this.browser.addFocusListener(localMenuEdit);
/* 270 */     this.browser_pane = new javax.swing.JScrollPane(this.browser);
/*     */
/* 272 */     java.awt.Container localContainer = getContentPane();
/*     */
/* 274 */     this.main_pane = new javax.swing.JSplitPane(1);
/* 275 */     this.main_pane.setRightComponent(this.output_pane);
/* 276 */     if (Options.getShowBrowserOption().getValue()) {
/* 277 */       this.main_pane.setLeftComponent(this.browser_pane);
/* 278 */       this.main_pane.setDividerLocation(70);
/*     */     }
/*     */
/* 281 */     localContainer.add(makeInputPanel(), "North");
/* 282 */     localContainer.add(this.main_pane, "Center");
/*     */
/* 284 */     pack();
/*     */   }
/*     */
/*     */   private JPanel makeInputPanel() {
/* 288 */     JPanel localJPanel = new JPanel();
/* 289 */     java.awt.GridBagLayout localGridBagLayout = new java.awt.GridBagLayout();
/* 290 */     java.awt.GridBagConstraints localGridBagConstraints = new java.awt.GridBagConstraints();
/* 291 */     localJPanel.setLayout(localGridBagLayout);
/*     */
/* 293 */     localGridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
/* 294 */     javax.swing.JLabel localJLabel1 = new javax.swing.JLabel("Define");
/* 295 */     localGridBagLayout.setConstraints(localJLabel1, localGridBagConstraints);
/* 296 */     localJPanel.add(localJLabel1);
/*     */
/* 298 */     localGridBagLayout.setConstraints(this.sym_name, localGridBagConstraints);
/* 299 */     localJPanel.add(this.sym_name);
/*     */
/* 301 */     javax.swing.JLabel localJLabel2 = new javax.swing.JLabel("as");
/* 302 */     localGridBagLayout.setConstraints(localJLabel2, localGridBagConstraints);
/* 303 */     localJPanel.add(localJLabel2);
/*     */     
/* 305 */     localGridBagConstraints.weightx = 1.0D;
/* 306 */     localGridBagConstraints.fill = 2;
/* 307 */     localGridBagLayout.setConstraints(this.input, localGridBagConstraints);
/* 308 */     localJPanel.add(this.input);
/*     */
/* 310 */     return localJPanel;
/*     */   }
/*     */
/* 313 */   javax.swing.JTextArea getOutputArea() { return this.output; }
/* 314 */   Context getContext() { return this.context; }
/*     */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\Gui.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
