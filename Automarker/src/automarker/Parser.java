package automarker;

import automarker.Expr;
import java.util.HashMap;

public class Parser {

    public static class ParseException extends Exception {

        public ParseException(Parser.Scanner paramScanner, String paramString) {
            super(paramString);
        }
    }

    private static class Scanner {

        String source;
        int pos;
        int next;
        int cur;
        String data;

        Scanner(String paramString) {
            this.source = paramString;
            this.pos = 0;
            this.next = 0;
            chomp();
        }

        int getCurrent() {
            return this.cur;
        }

        String getCurrentData() {
            return this.data;
        }

        void chomp() {
            int i = this.source.length();
            this.pos = this.next;
            while ((this.pos < i) && (Character.isWhitespace(this.source.charAt(this.pos)))) {
                this.pos += 1;
            }
            if (this.pos == i) {
                this.next = i;
                this.cur = 9;
                return;
            }
            this.next = (this.pos + 1);
            switch (this.source.charAt(this.pos)) {
                case '\\':
                case 'λ':
                    this.cur = 0;
                    break;
                case '(':
                    this.cur = 1;
                    break;
                case ')':
                    this.cur = 2;
                    break;
                case '[':
                    this.cur = 3;
                    break;
                case ']':
                    this.cur = 4;
                    break;
                case '{':
                    this.cur = 5;
                    break;
                case '}':
                    this.cur = 6;
                    break;
                case '.':
                    this.cur = 7;
                    break;

                default:
                    this.cur = 8;
                    while (this.next < i) {
                        char c = this.source.charAt(this.next);
                        if ((Character.isWhitespace(c))
                                || ("\\().".indexOf(c) >= 0)) {
                            break;
                        }
                        this.next += 1;
                    }
                    this.data = this.source.substring(this.pos, this.next);
            }
        }
    }

    private static Expr parseFactor(Scanner paramScanner, HashMap paramHashMap) throws Parser.ParseException {
        String str;
        Symbol localSymbol1;
        Object localObject;
        switch (paramScanner.getCurrent()) {
            case 8:
                str = paramScanner.getCurrentData();
                localSymbol1 = (Symbol) paramHashMap.get(str);
                if (localSymbol1 == null) {
                    localSymbol1 = new Symbol(str);
                    paramHashMap.put(str, localSymbol1);
                }
                localObject = new Expr.Ident(localSymbol1);
                paramScanner.chomp();
                break;

            case 1:
                paramScanner.chomp();
                localObject = parseExpr(paramScanner, paramHashMap);
                if (paramScanner.getCurrent() != 2) {
                    throw new ParseException(paramScanner, "Right parenthesis missing");
                }
                paramScanner.chomp();
                break;
            case 3:
                paramScanner.chomp();
                localObject = parseExpr(paramScanner, paramHashMap);
                if (paramScanner.getCurrent() != 4) {
                    throw new ParseException(paramScanner, "Right bracket missing");
                }
                paramScanner.chomp();
                break;
            case 5:
                paramScanner.chomp();
                localObject = parseExpr(paramScanner, paramHashMap);
                if (paramScanner.getCurrent() != 6) {
                    throw new ParseException(paramScanner, "Right brace missing");
                }
                paramScanner.chomp();
                break;

            case 0:
                paramScanner.chomp();
                if (paramScanner.getCurrent() != 8) {
                    throw new ParseException(paramScanner, "Parameter name missing following lambda");
                }
                str = paramScanner.getCurrentData();
                localSymbol1 = (Symbol) paramHashMap.get(str);
                Symbol localSymbol2 = new Symbol(str);
                paramHashMap.put(str, localSymbol2);
                paramScanner.chomp();
                if (paramScanner.getCurrent() != 7) {
                    throw new ParseException(paramScanner, "Period missing following parameter name");
                }
                paramScanner.chomp();
                localObject = new Expr.Abstract(localSymbol2, parseExpr(paramScanner, paramHashMap));
                if (localSymbol1 == null) {
                    paramHashMap.remove(str);
                } else {
                    paramHashMap.put(str, localSymbol1);
                }
                break;
            case 2:
            case 4:
            case 6:
            case 7:
            default:
                throw new ParseException(paramScanner, "Unexpected token");
        }
        return (Expr) localObject;
    }

    private static Expr parseExpr(Scanner paramScanner, HashMap paramHashMap) throws Parser.ParseException {
        Object localObject = parseFactor(paramScanner, paramHashMap);
        while ((paramScanner.getCurrent() != 9) && (paramScanner.getCurrent() != 2)) {
            localObject = new Expr.Apply((Expr) localObject, parseFactor(paramScanner, paramHashMap));
        }
        return (Expr) localObject;
    }

    public static Expr parse(String paramString) throws Parser.ParseException {
        Scanner localScanner = new Scanner(paramString);
        HashMap localHashMap = new HashMap();
        Expr localExpr = parseExpr(localScanner, localHashMap);
        if (localScanner.getCurrent() != 9) {
            throw new ParseException(localScanner, "Could not parse all of expression");
        }
        return localExpr;
    }
}
