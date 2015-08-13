/*    */ package lambda;
/*    */ 
/*    */ class Symbol {
/*  4 */   static int last_code_assigned = 0;
/*    */   String data;
/*    */   int hash_code;
/*    */   
/*    */   Symbol(String paramString) {
/*  9 */     this.data = paramString.intern();
/* 10 */     last_code_assigned += 1;
/* 11 */     this.hash_code = last_code_assigned;
/*    */   }
/*    */   
/* 14 */   public int hashCode() { return this.hash_code; }
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 17 */     if ((paramObject instanceof String)) return this.data.equals(paramObject);
/* 18 */     return this == paramObject;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 22 */     return this.data;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 26 */     return super.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\Symbol.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */