/*    */ package automarker.calc;;
/*    */ 
/*    */ class TreeSetSeq {
/*    */   private static class Node {
/*    */     Comparable data;
/*    */     Node parent;
/*    */     Node left;
/*    */     Node right;
/*    */     int left_size;
/*    */     
/*    */     Node(Node paramNode, Comparable paramComparable) {
/* 12 */       this.data = paramComparable;
/* 13 */       this.parent = paramNode;
/* 14 */       this.left = null;
/* 15 */       this.right = null;
/* 16 */       this.left_size = 0;
/*    */     }
/*    */   }
/*    */   
/* 20 */   private Node root = null;
/* 21 */   int all_size = 0;
/*    */   
/*    */ 
/*    */ 
/*    */   public void add(Comparable paramComparable)
/*    */   {
/* 27 */     if (this.root == null) {
/* 28 */       this.root = new Node(null, paramComparable);
/* 29 */       this.all_size = 1;
/* 30 */       return;
/*    */     }
/*    */     
/* 33 */     this.all_size += 1;
/* 34 */     Node localNode = this.root;
/*    */     for (;;) {
/* 36 */       if (paramComparable.compareTo(localNode.data) < 0) {
/* 37 */         localNode.left_size += 1;
/* 38 */         if (localNode.left == null) {
/* 39 */           localNode.left = new Node(localNode, paramComparable);
/* 40 */           break;
/*    */         }
/* 42 */         localNode = localNode.left;
/*    */       } else {
/* 44 */         if (localNode.right == null) {
/* 45 */           localNode.right = new Node(localNode, paramComparable);
/* 46 */           break;
/*    */         }
/* 48 */         localNode = localNode.right;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public int getIndex(Comparable paramComparable) {
/* 54 */     if (this.root == null) { return -1;
/*    */     }
/* 56 */     int i = 0;
/* 57 */     Node localNode = this.root;
/* 58 */     while (localNode != null) {
/* 59 */       int j = paramComparable.compareTo(localNode.data);
/* 60 */       if (j < 0) {
/* 61 */         localNode = localNode.left;
/* 62 */       } else { if (j == 0) {
/* 63 */           return i;
/*    */         }
/* 65 */         i += localNode.left_size + 1;
/* 66 */         localNode = localNode.right;
/*    */       }
/*    */     }
/* 69 */     return -1;
/*    */   }
/*    */   
/*    */   public int size() {
/* 73 */     return this.all_size;
/*    */   }
/*    */   
/*    */   public Object get(int paramInt) {
/* 77 */     if ((paramInt < 0) || (paramInt >= size())) { return null;
/*    */     }
/* 79 */     Node localNode = this.root;
/* 80 */     int i = paramInt;
/*    */     for (;;) {
/* 82 */       int j = localNode.left_size;
/* 83 */       if (i < j) {
/* 84 */         localNode = localNode.left;
/* 85 */       } else { if (i == j) {
/* 86 */           return localNode.data;
/*    */         }
/* 88 */         i -= j + 1;
/* 89 */         localNode = localNode.right;
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\TreeSetSeq.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */