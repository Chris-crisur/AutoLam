package automarker;

import automarker.Options;
import javax.swing.text.Document;

class LambdaText {

    public static final char LAMBDA = 'λ';

    private static class MyDocumentFilter extends javax.swing.text.DocumentFilter {

        MyDocumentFilter(LambdaText param1) {
            this();
        }

        public void insertString(javax.swing.text.DocumentFilter.FilterBypass paramFilterBypass, int paramInt, String paramString, javax.swing.text.AttributeSet paramAttributeSet) throws javax.swing.text.BadLocationException {
            char c = Options.getLambdaChar();
            if (c != '\\') {
                paramString = paramString.replace('\\', c);
            }
            super.insertString(paramFilterBypass, paramInt, paramString, paramAttributeSet);
        }

        public void remove(javax.swing.text.DocumentFilter.FilterBypass paramFilterBypass, int paramInt1, int paramInt2) throws javax.swing.text.BadLocationException {
            super.remove(paramFilterBypass, paramInt1, paramInt2);
        }

        public void replace(javax.swing.text.DocumentFilter.FilterBypass paramFilterBypass, int paramInt1, int paramInt2, String paramString, javax.swing.text.AttributeSet paramAttributeSet) throws javax.swing.text.BadLocationException {
            char c = Options.getLambdaChar();
            if (c != '\\') {
                paramString = paramString.replace('\\', c);
            }
            super.replace(paramFilterBypass, paramInt1, paramInt2, paramString, paramAttributeSet);
        }

        private MyDocumentFilter() {
        }
    }

    private static class LambdaListener implements Options.Listener {

        private javax.swing.text.JTextComponent item;
        private char last_lambda = 'λ';

        LambdaListener(javax.swing.text.JTextComponent paramJTextComponent) {
            this.item = paramJTextComponent;
        }

        public void valueChanged(Options.Option paramOption) {
            this.item.setText(this.item.getText().replace('λ', '\\'));
        }
    }

    private static class TextField extends javax.swing.JTextField {

        TextField(int paramInt) {
            super(paramInt);
            Options.getPrintLambdaOption().addListener(new LambdaText.LambdaListener(this));
            super.setDocument(LambdaText.createDocument(getDocument()));
        }

        public void setDocument(Document paramDocument) {
            super.setDocument(LambdaText.createDocument(paramDocument));
        }
    }

    private static class TextArea extends javax.swing.JTextArea {

        TextArea() {
            Options.getPrintLambdaOption().addListener(new LambdaText.LambdaListener(this));
            super.setDocument(LambdaText.createDocument(getDocument()));
        }

        public void setDocument(Document paramDocument) {
            super.setDocument(LambdaText.createDocument(paramDocument));
        }
    }

    private static Document createDocument(Document paramDocument) {
        if ((paramDocument != null) && ((paramDocument instanceof javax.swing.text.PlainDocument))) {
            javax.swing.text.PlainDocument localPlainDocument = (javax.swing.text.PlainDocument) paramDocument;
            localPlainDocument.setDocumentFilter(new MyDocumentFilter(null));
        }
        return paramDocument;
    }

    static javax.swing.JTextField createTextField(int paramInt) {
        return new TextField(paramInt);
    }

    static javax.swing.JTextArea createTextArea() {
        return new TextArea();
    }
}
