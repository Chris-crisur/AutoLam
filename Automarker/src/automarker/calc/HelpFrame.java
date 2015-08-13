/*     */ package automarker.calc;
/*     */ 
/*     */ import java.net.URL;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JMenuItem;
/*     */ 
/*     */ public class HelpFrame extends javax.swing.JFrame implements javax.swing.event.HyperlinkListener
/*     */ {
/*     */   protected javax.swing.JMenu menu_file;
/*     */   protected javax.swing.JMenu menu_go;
/*     */   protected JEditorPane editor;
/*     */   protected javax.swing.JScrollPane scroll_pane;
/*     */   
/*     */   protected class ContentMenuItem extends JMenuItem implements java.awt.event.ActionListener
/*     */   {
/*     */     URL url;
/*     */     
/*     */     public ContentMenuItem(String paramString, URL paramURL)
/*     */     {
/*  20 */       super();
/*  21 */       this.url = paramURL;
/*  22 */       addActionListener(this);
/*     */     }
/*     */     
/*     */ 
/*  26 */     public void actionPerformed(java.awt.event.ActionEvent paramActionEvent) { HelpFrame.this.load(this.url); }
/*     */   }
/*     */   
/*     */   protected class History { protected History() {}
/*     */     
/*  31 */     java.util.LinkedList urls = new java.util.LinkedList();
/*  32 */     protected int pos = -1;
/*     */     
/*     */     public void init(URL paramURL) {
/*  35 */       this.pos = 0;
/*  36 */       this.urls.add(paramURL);
/*  37 */       HelpFrame.this.getURL(paramURL);
/*     */     }
/*     */     
/*  40 */     public URL getCurrent() { return (URL)this.urls.get(this.pos); }
/*     */     
/*     */     public void back() {
/*  43 */       if (this.pos - 1 >= 0) {
/*  44 */         this.pos -= 1;
/*  45 */         HelpFrame.this.getURL((URL)this.urls.get(this.pos));
/*     */       }
/*     */     }
/*     */     
/*  49 */     public void forward() { if (this.pos + 1 < this.urls.size()) {
/*  50 */         this.pos += 1;
/*  51 */         HelpFrame.this.getURL((URL)this.urls.get(this.pos));
/*     */       }
/*     */     }
/*     */     
/*  55 */     public void addURL(URL paramURL) { if (HelpFrame.this.getURL(paramURL)) {
/*  56 */         while (this.urls.size() > this.pos + 1) {
/*  57 */           this.urls.remove(this.pos + 1);
/*     */         }
/*  59 */         this.urls.add(paramURL);
/*  60 */         this.pos = (this.urls.size() - 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   protected History history = new History();
/*  70 */   protected URL base_url = null;
/*     */   
/*     */   public HelpFrame(String paramString) {
/*  73 */     super("Help");
/*  74 */     setSize(500, 400);
/*     */     
/*  76 */     javax.swing.JMenuBar localJMenuBar = new javax.swing.JMenuBar();
/*     */     
/*     */ 
/*  79 */     this.menu_file = new javax.swing.JMenu("File");
/*  80 */     JMenuItem localJMenuItem = new JMenuItem("Close");
/*  81 */     localJMenuItem.addActionListener(new java.awt.event.ActionListener() {
/*     */       public void actionPerformed(java.awt.event.ActionEvent paramAnonymousActionEvent) {
/*  83 */         HelpFrame.this.hide();
/*     */       }
/*  85 */     });
/*  86 */     this.menu_file.add(localJMenuItem);
/*  87 */     localJMenuBar.add(this.menu_file);
/*     */     
/*  89 */     this.menu_go = new javax.swing.JMenu("Go");
/*  90 */     localJMenuItem = new JMenuItem("Back");
/*  91 */     localJMenuItem.addActionListener(new java.awt.event.ActionListener() {
/*     */       public void actionPerformed(java.awt.event.ActionEvent paramAnonymousActionEvent) {
/*  93 */         HelpFrame.this.history.back();
/*     */       }
/*  95 */     });
/*  96 */     localJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(66, 8));
/*     */     
/*  98 */     this.menu_go.add(localJMenuItem);
/*  99 */     localJMenuItem = new JMenuItem("Forward");
/* 100 */     localJMenuItem.addActionListener(new java.awt.event.ActionListener() {
/*     */       public void actionPerformed(java.awt.event.ActionEvent paramAnonymousActionEvent) {
/* 102 */         HelpFrame.this.history.forward();
/*     */       }
/* 104 */     });
/* 105 */     localJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(70, 8));
/*     */     
/* 107 */     this.menu_go.add(localJMenuItem);
/* 108 */     this.menu_go.addSeparator();
/* 109 */     localJMenuBar.add(this.menu_go);
/*     */     
/* 111 */     setJMenuBar(localJMenuBar);
/*     */     
/* 113 */     this.editor = new JEditorPane();
/* 114 */     this.editor.setEditable(false);
/* 115 */     this.editor.setContentType("text/html");
/* 116 */     this.editor.addHyperlinkListener(this);
/*     */     
/* 118 */     this.scroll_pane = new javax.swing.JScrollPane(this.editor);
/* 119 */     this.scroll_pane.setVerticalScrollBarPolicy(22);
/* 120 */     this.scroll_pane.setMinimumSize(new java.awt.Dimension(10, 10));
/* 121 */     getContentPane().add(this.scroll_pane);
/*     */     try
/*     */     {
/* 124 */       computeBaseURL();
/* 125 */       this.history.init(new URL(this.base_url, paramString));
/*     */     } catch (Exception localException) {
/* 127 */       showError("Initialization error: " + localException.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   private void computeBaseURL() throws Exception {
/* 132 */     String str = System.getProperty("java.class.path");
/* 133 */     if (str == null) {
/* 134 */       throw new Exception("can't determine help base location");
/*     */     }
/*     */     
/* 137 */     java.io.File localFile = new java.io.File(str);
/* 138 */     if (localFile.isDirectory()) {
/* 139 */       this.base_url = new URL("file:" + localFile + "/index.html");
/* 140 */     } else if (localFile.exists())
/*     */     {
/* 142 */       this.base_url = new URL("jar:file:" + localFile + "!/index.html");
/*     */     } else {
/* 144 */       throw new Exception("can't find help base location");
/*     */     }
/*     */   }
/*     */   
/*     */   public void addContentsItem(String paramString, URL paramURL) {
/* 149 */     this.menu_go.add(new ContentMenuItem(paramString, paramURL));
/*     */   }
/*     */   
/*     */   protected void showError(String paramString) {
/* 153 */     this.editor.setText("<h1>Error</h1>\n<p>" + paramString + "</p>\n");
/*     */   }
/*     */   
/*     */   public URL getCurrent()
/*     */   {
/* 158 */     return this.history.getCurrent();
/*     */   }
/*     */   
/*     */   public void load(String paramString) {
/*     */     try {
/* 163 */       load(new URL(this.base_url, paramString));
/*     */     } catch (java.io.IOException localIOException) {
/* 165 */       showError("Could not find file: " + localIOException.getMessage());
/*     */     }
/*     */   }
/*     */   
/* 169 */   public void load(URL paramURL) { this.history.addURL(paramURL); }
/*     */   
/*     */   protected boolean getURL(URL paramURL)
/*     */   {
/*     */     try {
/*     */       try {
/* 175 */         javax.swing.text.html.HTMLDocument localHTMLDocument = (javax.swing.text.html.HTMLDocument)this.editor.getDocument();
/* 176 */         localHTMLDocument.setBase(paramURL);
/* 177 */         load(paramURL.openStream());
/*     */       } catch (java.io.IOException localIOException) {
/* 179 */         throw new Exception("Couldn't open URL " + paramURL + " (protocol " + paramURL.getProtocol() + "): " + localIOException.getMessage());
/*     */       }
/*     */       
/*     */ 
/* 183 */       return true;
/*     */     } catch (Throwable localThrowable) {
/* 185 */       showError(localThrowable.getMessage()); }
/* 186 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void load(java.io.InputStream paramInputStream)
/*     */   {
/* 193 */     StringBuffer localStringBuffer = new StringBuffer();
/* 194 */     java.io.InputStreamReader localInputStreamReader = new java.io.InputStreamReader(paramInputStream);
/* 195 */     java.io.BufferedReader localBufferedReader = new java.io.BufferedReader(localInputStreamReader);
/*     */     try {
/*     */       for (;;) {
/* 198 */         String str = localBufferedReader.readLine();
/* 199 */         if (str == null) break;
/* 200 */         localStringBuffer.append(str);
/*     */       }
/*     */     } catch (java.io.IOException localIOException1) {}
/*     */     try {
/* 204 */       paramInputStream.close();
/*     */     }
/*     */     catch (java.io.IOException localIOException2) {}
/*     */     
/* 208 */     this.editor.getEditorKit().createDefaultDocument();
/* 209 */     this.editor.setText(localStringBuffer.toString());
/* 210 */     this.editor.setCaretPosition(0);
/*     */   }
/*     */   
/*     */   public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent paramHyperlinkEvent) {
/* 214 */     if (paramHyperlinkEvent.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
/* 215 */       JEditorPane localJEditorPane = (JEditorPane)paramHyperlinkEvent.getSource();
/* 216 */       Object localObject; if ((paramHyperlinkEvent instanceof javax.swing.text.html.HTMLFrameHyperlinkEvent)) {
/* 217 */         localObject = (javax.swing.text.html.HTMLFrameHyperlinkEvent)paramHyperlinkEvent;
/* 218 */         javax.swing.text.html.HTMLDocument localHTMLDocument = (javax.swing.text.html.HTMLDocument)localJEditorPane.getDocument();
/* 219 */         localHTMLDocument.processHTMLFrameHyperlinkEvent((javax.swing.text.html.HTMLFrameHyperlinkEvent)localObject);
/*     */       } else {
/*     */         try {
/* 222 */           localObject = paramHyperlinkEvent.getURL();
/* 223 */           if (localObject == null) {
/* 224 */             localObject = new URL(this.history.getCurrent(), paramHyperlinkEvent.getDescription());
/*     */           }
/* 226 */           load((URL)localObject);
/*     */         } catch (Throwable localThrowable) {
/* 228 */           localThrowable.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Chris\Google Drive\University\2015 - 3rd Year UCT\CSC\3003S\Capstone\Lambda\examples\lambda.jar!\lambda\HelpFrame.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */