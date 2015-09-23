package automarker;

import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;

public class HelpFrame extends javax.swing.JFrame implements javax.swing.event.HyperlinkListener {

    protected javax.swing.JMenu menu_file;
    protected javax.swing.JMenu menu_go;
    protected JEditorPane editor;
    protected javax.swing.JScrollPane scroll_pane;

    protected class ContentMenuItem extends JMenuItem implements java.awt.event.ActionListener {

        URL url;

        public ContentMenuItem(String paramString, URL paramURL) {
            super();
            this.url = paramURL;
            addActionListener(this);
        }

        public void actionPerformed(java.awt.event.ActionEvent paramActionEvent) {
            HelpFrame.this.load(this.url);
        }
    }

    protected class History {

        protected History() {
        }

        java.util.LinkedList urls = new java.util.LinkedList();
        protected int pos = -1;

        public void init(URL paramURL) {
            this.pos = 0;
            this.urls.add(paramURL);
            HelpFrame.this.getURL(paramURL);
        }

        public URL getCurrent() {
            return (URL) this.urls.get(this.pos);
        }

        public void back() {
            if (this.pos - 1 >= 0) {
                this.pos -= 1;
                HelpFrame.this.getURL((URL) this.urls.get(this.pos));
            }
        }

        public void forward() {
            if (this.pos + 1 < this.urls.size()) {
                this.pos += 1;
                HelpFrame.this.getURL((URL) this.urls.get(this.pos));
            }
        }

        public void addURL(URL paramURL) {
            if (HelpFrame.this.getURL(paramURL)) {
                while (this.urls.size() > this.pos + 1) {
                    this.urls.remove(this.pos + 1);
                }
                this.urls.add(paramURL);
                this.pos = (this.urls.size() - 1);
            }
        }
    }

    protected History history = new History();
    protected URL base_url = null;

    public HelpFrame(String paramString) {
        super("Help");
        setSize(500, 400);

        javax.swing.JMenuBar localJMenuBar = new javax.swing.JMenuBar();

        this.menu_file = new javax.swing.JMenu("File");
        JMenuItem localJMenuItem = new JMenuItem("Close");
        localJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent paramAnonymousActionEvent) {
                HelpFrame.this.hide();
            }
        });
        this.menu_file.add(localJMenuItem);
        localJMenuBar.add(this.menu_file);

        this.menu_go = new javax.swing.JMenu("Go");
        localJMenuItem = new JMenuItem("Back");
        localJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent paramAnonymousActionEvent) {
                HelpFrame.this.history.back();
            }
        });
        localJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(66, 8));

        this.menu_go.add(localJMenuItem);
        localJMenuItem = new JMenuItem("Forward");
        localJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent paramAnonymousActionEvent) {
                HelpFrame.this.history.forward();
            }
        });
        localJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(70, 8));

        this.menu_go.add(localJMenuItem);
        this.menu_go.addSeparator();
        localJMenuBar.add(this.menu_go);

        setJMenuBar(localJMenuBar);

        this.editor = new JEditorPane();
        this.editor.setEditable(false);
        this.editor.setContentType("text/html");
        this.editor.addHyperlinkListener(this);

        this.scroll_pane = new javax.swing.JScrollPane(this.editor);
        this.scroll_pane.setVerticalScrollBarPolicy(22);
        this.scroll_pane.setMinimumSize(new java.awt.Dimension(10, 10));
        getContentPane().add(this.scroll_pane);
        try {
            computeBaseURL();
            this.history.init(new URL(this.base_url, paramString));
        } catch (Exception localException) {
            showError("Initialization error: " + localException.getMessage());
        }
    }

    private void computeBaseURL() throws Exception {
        String str = System.getProperty("java.class.path");
        if (str == null) {
            throw new Exception("can't determine help base location");
        }

        java.io.File localFile = new java.io.File(str);
        if (localFile.isDirectory()) {
            this.base_url = new URL("file:" + localFile + "/index.html");
        } else if (localFile.exists()) {
            this.base_url = new URL("jar:file:" + localFile + "!/index.html");
        } else {
            throw new Exception("can't find help base location");
        }
    }

    public void addContentsItem(String paramString, URL paramURL) {
        this.menu_go.add(new ContentMenuItem(paramString, paramURL));
    }

    protected void showError(String paramString) {
        this.editor.setText("<h1>Error</h1>\n<p>" + paramString + "</p>\n");
    }

    public URL getCurrent() {
        return this.history.getCurrent();
    }

    public void load(String paramString) {
        try {
            load(new URL(this.base_url, paramString));
        } catch (java.io.IOException localIOException) {
            showError("Could not find file: " + localIOException.getMessage());
        }
    }

    public void load(URL paramURL) {
        this.history.addURL(paramURL);
    }

    protected boolean getURL(URL paramURL) {
        try {
            try {
                javax.swing.text.html.HTMLDocument localHTMLDocument = (javax.swing.text.html.HTMLDocument) this.editor.getDocument();
                localHTMLDocument.setBase(paramURL);
                load(paramURL.openStream());
            } catch (java.io.IOException localIOException) {
                throw new Exception("Couldn't open URL " + paramURL + " (protocol " + paramURL.getProtocol() + "): " + localIOException.getMessage());
            }

            return true;
        } catch (Throwable localThrowable) {
            showError(localThrowable.getMessage());
        }
        return false;
    }

    protected void load(java.io.InputStream paramInputStream) {
        StringBuffer localStringBuffer = new StringBuffer();
        java.io.InputStreamReader localInputStreamReader = new java.io.InputStreamReader(paramInputStream);
        java.io.BufferedReader localBufferedReader = new java.io.BufferedReader(localInputStreamReader);
        try {
            for (;;) {
                String str = localBufferedReader.readLine();
                if (str == null) {
                    break;
                }
                localStringBuffer.append(str);
            }
        } catch (java.io.IOException localIOException1) {
        }
        try {
            paramInputStream.close();
        } catch (java.io.IOException localIOException2) {
        }

        this.editor.getEditorKit().createDefaultDocument();
        this.editor.setText(localStringBuffer.toString());
        this.editor.setCaretPosition(0);
    }

    public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent paramHyperlinkEvent) {
        if (paramHyperlinkEvent.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
            JEditorPane localJEditorPane = (JEditorPane) paramHyperlinkEvent.getSource();
            Object localObject;
            if ((paramHyperlinkEvent instanceof javax.swing.text.html.HTMLFrameHyperlinkEvent)) {
                localObject = (javax.swing.text.html.HTMLFrameHyperlinkEvent) paramHyperlinkEvent;
                javax.swing.text.html.HTMLDocument localHTMLDocument = (javax.swing.text.html.HTMLDocument) localJEditorPane.getDocument();
                localHTMLDocument.processHTMLFrameHyperlinkEvent((javax.swing.text.html.HTMLFrameHyperlinkEvent) localObject);
            } else {
                try {
                    localObject = paramHyperlinkEvent.getURL();
                    if (localObject == null) {
                        localObject = new URL(this.history.getCurrent(), paramHyperlinkEvent.getDescription());
                    }
                    load((URL) localObject);
                } catch (Throwable localThrowable) {
                    localThrowable.printStackTrace();
                }
            }
        }
    }
}
