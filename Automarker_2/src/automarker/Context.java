package automarker;

import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.event.ListDataListener;

class Context implements javax.swing.ListModel {

    private HashMap map = new HashMap();
    private HashMap inverse = new HashMap();
    private TreeSetSeq names = new TreeSetSeq();
    private java.util.LinkedList listeners = new java.util.LinkedList();

    private class Substitutor implements Expr.Visitor {

        public Object visitAbstract(Expr.Abstract paramAbstract) {
            Expr localExpr = null;
            String str = paramAbstract.sym.toString();
            Symbol localSymbol = paramAbstract.sym;
            if (Context.this.map.containsKey(str)) {
                localExpr = (Expr) Context.this.map.get(str);
                Context.this.map.remove(str);
            }
            Expr.Abstract localAbstract = new Expr.Abstract(localSymbol, (Expr) paramAbstract.expr.visit(this));
            if (localSymbol != paramAbstract.sym) {
                Context.this.map.put(str, localExpr);
            }
            return localAbstract;
        }

        public Object visitApply(Expr.Apply paramApply) {
            return new Expr.Apply((Expr) paramApply.left.visit(this), (Expr) paramApply.right.visit(this));
        }

        public Object visitIdent(Expr.Ident paramIdent) {
            Expr localExpr = (Expr) Context.this.map.get(paramIdent.sym.toString());
            if (localExpr != null) {
                return localExpr;
            }
            return paramIdent;
        }
    }

    Expr substitute(Expr paramExpr) {
        return (Expr) paramExpr.visit(new Substitutor());
    }

    void put(String paramString, Expr paramExpr) {
        paramString = paramString.trim();
        if (insert(paramString, paramExpr)) {
            int i = this.names.getIndex(paramString);
            fireEvent(1, i, i);
        }
    }

    Expr get(String paramString) {
        return (Expr) this.map.get(paramString.trim());
    }

    String invert(Expr paramExpr) {
        String str = (String) this.inverse.get(new ExprWrapper(paramExpr));
        return str;
    }

    private boolean insert(String paramString, Expr paramExpr) {
        boolean bool = true;
        Expr localExpr = (Expr) this.map.get(paramString);
        if (localExpr == null) {
            this.names.add(paramString);
        } else {
            this.inverse.remove(new ExprWrapper(localExpr));
            bool = false;
        }
        this.map.put(paramString, paramExpr);
        this.inverse.put(new ExprWrapper(paramExpr), paramString);
        return bool;
    }

    void exportFile(java.io.File paramFile) throws java.io.IOException {
        java.io.PrintWriter localPrintWriter = new java.io.PrintWriter(new java.io.FileWriter(paramFile));
        Iterator localIterator = this.map.keySet().iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            localPrintWriter.println(str);
            localPrintWriter.println("  " + this.map.get(str));
        }
        localPrintWriter.close();
    }

    void importFile(java.io.File paramFile) throws java.io.IOException {
        LineNumberReader localLineNumberReader = new LineNumberReader(new java.io.FileReader(paramFile));
        for (;;) {
            int i = localLineNumberReader.getLineNumber();
            String str1 = localLineNumberReader.readLine();
            if (str1 != null) {
                if ((str1.length() == 0) || (str1.charAt(0) == ' ')) {
                    localLineNumberReader.close();
                    throw new java.io.IOException(i + ": expected symbol definition");
                }
                String str2 = localLineNumberReader.readLine();
                if ((str2 == null) || (str2.length() == 0) || (str2.charAt(0) != ' ')) {
                    localLineNumberReader.close();
                    throw new java.io.IOException(i + ": symbol " + str1 + " missing definition");
                }
                try {
                    insert(str1, Parser.parse(str2));
                } catch (Parser.ParseException localParseException) {
                    localLineNumberReader.close();
                    throw new java.io.IOException(i + 1 + ": expression parse: " + localParseException.getMessage());
                }
            }
        }
        /*       localLineNumberReader.close();
         /*       fireEvent(0, 0, this.names.size());
         */    }

    public void addListDataListener(ListDataListener paramListDataListener) {
        this.listeners.add(paramListDataListener);
    }

    public Object getElementAt(int paramInt) {
        return this.names.get(paramInt);
    }

    public int getSize() {
        return this.names.size();
    }

    public void removeListDataListener(ListDataListener paramListDataListener) {
        this.listeners.remove(paramListDataListener);
    }

    private void fireEvent(int paramInt1, int paramInt2, int paramInt3) {
        javax.swing.event.ListDataEvent localListDataEvent = new javax.swing.event.ListDataEvent(this, paramInt1, paramInt2, paramInt3);
        Iterator localIterator = this.listeners.iterator();
        while (localIterator.hasNext()) {
            ListDataListener localListDataListener = (ListDataListener) localIterator.next();
            if (paramInt1 == 0) {
                localListDataListener.contentsChanged(localListDataEvent);
            } else if (paramInt1 == 1) {
                localListDataListener.intervalAdded(localListDataEvent);
            } else if (paramInt1 == 2) {
                localListDataListener.intervalAdded(localListDataEvent);
            }
        }
    }
}


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\Context.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
