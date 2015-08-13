/*    */ package automarker.calc;
/*    */ 
/*    */ import javax.swing.text.Document;
/*    */ 
/*    */ class LambdaText {
/*    */   public static final char LAMBDA = 'λ';
/*    */   
/*    */   private static class MyDocumentFilter extends javax.swing.text.DocumentFilter {
/*    */     public MyDocumentFilter() {
/* 10 */       
/*    */     }
/*    */     
/*    */     public void insertString(javax.swing.text.DocumentFilter.FilterBypass paramFilterBypass, int paramInt, String paramString, javax.swing.text.AttributeSet paramAttributeSet) throws javax.swing.text.BadLocationException {
/* 14 */       char c = Options.getLambdaChar();
/* 15 */       if (c != '\\') paramString = paramString.replace('\\', c);
/* 16 */       super.insertString(paramFilterBypass, paramInt, paramString, paramAttributeSet);
/*    */     }
/*    */     
/*    */     public void remove(javax.swing.text.DocumentFilter.FilterBypass paramFilterBypass, int paramInt1, int paramInt2) throws javax.swing.text.BadLocationException {
/* 20 */       super.remove(paramFilterBypass, paramInt1, paramInt2);
/*    */     }
/*    */     
/*    */     public void replace(javax.swing.text.DocumentFilter.FilterBypass paramFilterBypass, int paramInt1, int paramInt2, String paramString, javax.swing.text.AttributeSet paramAttributeSet) throws javax.swing.text.BadLocationException
/*    */     {
/* 25 */       char c = Options.getLambdaChar();
/* 26 */       if (c != '\\') paramString = paramString.replace('\\', c);
/* 27 */       super.replace(paramFilterBypass, paramInt1, paramInt2, paramString, paramAttributeSet);
/*    */     }
/*    */     
/*    */     //private MyDocumentFilter() {} 
            }
/*    */   
/*    */   private static class LambdaListener implements Options.Listener { private javax.swing.text.JTextComponent item;
/* 33 */     private char last_lambda = '?';
/*    */     
/* 35 */     LambdaListener(javax.swing.text.JTextComponent paramJTextComponent) { this.item = paramJTextComponent; }
/*    */     
/*    */     public void valueChanged(Options.Option paramOption) {
/* 38 */       this.item.setText(this.item.getText().replace('λ', '\\'));
/*    */     }
/*    */   }
/*    */   
/*    */   private static class TextField extends javax.swing.JTextField {
/*    */     TextField(int paramInt) {
/* 44 */       super();
/* 45 */       Options.getPrintLambdaOption().addListener(new LambdaText.LambdaListener(this));
/* 46 */       super.setDocument(LambdaText.createDocument(getDocument()));
/*    */     }
/*    */     
/* 49 */     public void setDocument(Document paramDocument) { super.setDocument(LambdaText.createDocument(paramDocument)); }
/*    */   }
/*    */   
/*    */   private static class TextArea extends javax.swing.JTextArea
/*    */   {
/*    */     TextArea()
/*    */     {
/* 56 */       Options.getPrintLambdaOption().addListener(new LambdaText.LambdaListener(this));
/* 57 */       super.setDocument(LambdaText.createDocument(getDocument()));
/*    */     }
/*    */     
/* 60 */     public void setDocument(Document paramDocument) { super.setDocument(LambdaText.createDocument(paramDocument)); }
/*    */   }
/*    */   
/*    */   private static Document createDocument(Document paramDocument)
/*    */   {
/* 65 */     if ((paramDocument != null) && ((paramDocument instanceof javax.swing.text.PlainDocument))) {
/* 66 */       javax.swing.text.PlainDocument localPlainDocument = (javax.swing.text.PlainDocument)paramDocument;
/* 67 */       localPlainDocument.setDocumentFilter(new MyDocumentFilter());
/*    */     }
/* 69 */     return paramDocument;
/*    */   }
/*    */   
/*    */   static javax.swing.JTextField createTextField(int paramInt) {
/* 73 */     return new TextField(paramInt);
/*    */   }
/*    */   
/* 76 */   static javax.swing.JTextArea createTextArea() { return new TextArea(); }
/*    */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\LambdaText.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */