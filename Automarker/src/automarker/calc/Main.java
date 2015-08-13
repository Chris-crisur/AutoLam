/*    */ package automarker.calc;
/*    */ 
import java.io.File;
import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] paramArrayOfString) {
/*  8 */     Gui localGui = null;
/*    */     
/*    */ 
/* 11 */     Options.parseOptions(paramArrayOfString);
/* 12 */     if (paramArrayOfString != null) {
/* 13 */       for (int i = 0; i < paramArrayOfString.length; i++) {
/* 14 */         if (paramArrayOfString[i] != null) {
/*    */           try {
/* 16 */             File localFile = new File(paramArrayOfString[i]);
/* 17 */             if (!localFile.canRead()) {
/* 18 */               System.err.println("cannot open file \"" + paramArrayOfString[i] + "\"");
/*    */               
/* 20 */               System.err.println();
/* 21 */               printUsage();
/*    */             }
/* 23 */             if (localGui == null) localGui = new Gui();
/* 24 */             localGui.getContext().importFile(localFile);
/*    */           } catch (IOException localIOException) {
/* 26 */             System.err.println("error loading " + paramArrayOfString[i] + ":");
/* 27 */             System.err.println("  " + localIOException);
/* 28 */             System.err.println();
/* 29 */             printUsage();
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 35 */     if (localGui == null) localGui = new Gui();
/* 36 */     localGui.show();
/*    */   }
/*    */   
/* 39 */   private static void printUsage() { System.err.println("usage: java " + Main.class.getName() + " [options] [filenames]");
/*    */     
/* 41 */     System.err.println();
/* 42 */     System.err.println("options:");
/* 43 */     Options.printOptions();
/* 44 */     System.exit(0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\Main.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */