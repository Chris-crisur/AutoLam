package automarker;

/*      package lambda;
/*     */ 
import automarker.Expr;
/*     */ import java.util.HashMap;


/*     */ 
/*     */ public class Parser {
/*     */
/*     */   
/*     */   public static class ParseException extends Exception {
/*     */     public ParseException(Parser.Scanner paramScanner, String paramString) {
/*  19 */       super(paramString);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Scanner {
/*     */     String source;
/*     */     int pos;
/*     */     int next;
/*     */     int cur;
/*     */     String data;
/*     */     
/*     */     Scanner(String paramString) {
/*  31 */       this.source = paramString;
/*  32 */       this.pos = 0;
/*  33 */       this.next = 0;
/*  34 */       chomp();
/*     */     }
/*     */     
/*     */     int getCurrent() {
/*  38 */       return this.cur;
/*     */     }
/*     */     
/*     */     String getCurrentData() {
/*  42 */       return this.data;
/*     */     }
/*     */     
/*     */     void chomp() {
/*  46 */       int i = this.source.length();
/*  47 */       this.pos = this.next;
/*  48 */       while ((this.pos < i) && (Character.isWhitespace(this.source.charAt(this.pos)))) this.pos += 1;
/*  49 */       if (this.pos == i) {
/*  50 */         this.next = i;
/*  51 */         this.cur = 9;
/*  52 */         return;
/*     */       }
/*  54 */       this.next = (this.pos + 1);
/*  55 */       switch (this.source.charAt(this.pos)) {
/*     */       case '\\': case 'λ':
/*  57 */         this.cur = 0; break;
/*  58 */       case '(':  this.cur = 1; break;
/*  59 */       case ')':  this.cur = 2; break;
/*  60 */       case '[':  this.cur = 3; break;
/*  61 */       case ']':  this.cur = 4; break;
/*  62 */       case '{':  this.cur = 5; break;
/*  63 */       case '}':  this.cur = 6; break;
/*  64 */       case '.':  this.cur = 7; break;
/*     */       
/*     */       default: 
/*  67 */         this.cur = 8;
/*  68 */         while (this.next < i) {
/*  69 */           char c = this.source.charAt(this.next);
/*  70 */           if ((Character.isWhitespace(c)) || 
/*  71 */             ("\\().".indexOf(c) >= 0)) break;
/*  72 */           this.next += 1;
/*     */         }
/*  74 */         this.data = this.source.substring(this.pos, this.next); }
/*     */     }
/*     */   }
/*     */   
/*     */   private static Expr parseFactor(Scanner paramScanner, HashMap paramHashMap) throws Parser.ParseException {
/*     */     String str;
/*     */     Symbol localSymbol1;
/*     */     Object localObject;
/*  82 */     switch (paramScanner.getCurrent())
/*     */     {
/*     */     case 8: 
/*  85 */       str = paramScanner.getCurrentData();
/*  86 */       localSymbol1 = (Symbol)paramHashMap.get(str);
/*  87 */       if (localSymbol1 == null) {
/*  88 */         localSymbol1 = new Symbol(str);
/*  89 */         paramHashMap.put(str, localSymbol1);
/*     */       }
/*  91 */       localObject = new Expr.Ident(localSymbol1);
/*  92 */       paramScanner.chomp();
/*  93 */       break;
/*     */     
/*     */     case 1: 
/*  96 */       paramScanner.chomp();
/*  97 */       localObject = parseExpr(paramScanner, paramHashMap);
/*  98 */       if (paramScanner.getCurrent() != 2) {
/*  99 */         throw new ParseException(paramScanner, "Right parenthesis missing");
/*     */       }
/* 101 */       paramScanner.chomp();
/* 102 */       break;
/*     */     case 3: 
/* 104 */       paramScanner.chomp();
/* 105 */       localObject = parseExpr(paramScanner, paramHashMap);
/* 106 */       if (paramScanner.getCurrent() != 4) {
/* 107 */         throw new ParseException(paramScanner, "Right bracket missing");
/*     */       }
/* 109 */       paramScanner.chomp();
/* 110 */       break;
/*     */     case 5: 
/* 112 */       paramScanner.chomp();
/* 113 */       localObject = parseExpr(paramScanner, paramHashMap);
/* 114 */       if (paramScanner.getCurrent() != 6) {
/* 115 */         throw new ParseException(paramScanner, "Right brace missing");
/*     */       }
/* 117 */       paramScanner.chomp();
/* 118 */       break;
/*     */     
/*     */     case 0: 
/* 121 */       paramScanner.chomp();
/* 122 */       if (paramScanner.getCurrent() != 8) {
/* 123 */         throw new ParseException(paramScanner, "Parameter name missing following lambda");
/*     */       }
/* 125 */       str = paramScanner.getCurrentData();
/* 126 */       localSymbol1 = (Symbol)paramHashMap.get(str);
/* 127 */       Symbol localSymbol2 = new Symbol(str);
/* 128 */       paramHashMap.put(str, localSymbol2);
/* 129 */       paramScanner.chomp();
/* 130 */       if (paramScanner.getCurrent() != 7) {
/* 131 */         throw new ParseException(paramScanner, "Period missing following parameter name");
/*     */       }
/* 133 */       paramScanner.chomp();
/* 134 */       localObject = new Expr.Abstract(localSymbol2, parseExpr(paramScanner, paramHashMap));
/* 135 */       if (localSymbol1 == null) paramHashMap.remove(str); else
/* 136 */         paramHashMap.put(str, localSymbol1);
/* 137 */       break;
/*     */     case 2: case 4: case 6: 
/*     */     case 7: default: 
/* 140 */       throw new ParseException(paramScanner, "Unexpected token");
/*     */     }
/* 142 */     return (Expr)localObject;
/*     */   }
/*     */   
/*     */   private static Expr parseExpr(Scanner paramScanner, HashMap paramHashMap) throws Parser.ParseException {
/* 146 */     Object localObject = parseFactor(paramScanner, paramHashMap);
/* 147 */     while ((paramScanner.getCurrent() != 9) && (paramScanner.getCurrent() != 2)) {
/* 148 */       localObject = new Expr.Apply((Expr)localObject, parseFactor(paramScanner, paramHashMap));
/*     */     }
/* 150 */     return (Expr)localObject;
/*     */   }
/*     */   
/*     */   public static Expr parse(String paramString) throws Parser.ParseException {
/* 154 */     Scanner localScanner = new Scanner(paramString);
/* 155 */     HashMap localHashMap = new HashMap();
/* 156 */     Expr localExpr = parseExpr(localScanner, localHashMap);
/* 157 */     if (localScanner.getCurrent() != 9) {
/* 158 */       throw new ParseException(localScanner, "Could not parse all of expression");
/*     */     }
/* 160 */     return localExpr;
/*     */   }
/*     */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\Parser.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */