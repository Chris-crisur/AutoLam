package automarker;

  
  class Symbol {
    static int last_code_assigned = 0;
    String data;
    int hash_code;
    
    Symbol(String paramString) {
      this.data = paramString.intern();
      last_code_assigned += 1;
      this.hash_code = last_code_assigned;
    }
    
    public int hashCode() { return this.hash_code; }
    
    public boolean equals(Object paramObject) {
      if ((paramObject instanceof String)) return this.data.equals(paramObject);
      return this == paramObject;
    }
    
    public String toString() {
      return this.data;
    }
    
    public String getId() {
      return super.toString();
    }
  }

