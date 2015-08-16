/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automarker;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author Chris
 */
public class TutorialInterface extends JFrame implements ActionListener{
    private final String [] REDUCTIONS = {"alpha","beta","eta","conversion"};
    private Person student;

    //component declarations
    
    private JScrollPane welcomeScrollPane;   
    private JPanel mainPanel;
    private JScrollPane mainPane;
    private JTextArea welcomeArea;
    private QuestionPanel [] qPanel;
    private JScrollPane [] qPane;
    
    private JButton submit;
    private JButton next;
    private JButton upload;
    private JButton prev;
    private int qIndex;
    
    private Solution [] solutions;
    private Question [] questions;
    
    public static void main(String [] args){
        TutorialInterface TI = new TutorialInterface();
    }
    
    public TutorialInterface(){
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StudentTutorial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        signIn();
        
        setLayout(new BorderLayout());

        String welcome = loadWelcomeMessage();
        qIndex = -1;
        initComponents(welcome);
        setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
    
    private void signIn(){
        String studentNumber = JOptionPane.showInputDialog(this, "What is your student number?", JOptionPane.QUESTION_MESSAGE);
        String studentName = JOptionPane.showInputDialog(this, "What is your name?", JOptionPane.QUESTION_MESSAGE);
        
        student = new Student(studentName, studentNumber);
    }
    
    private void initComponents(String welcome) {
    // <editor-fold defaultstate="collapsed" desc="">
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainPanel = new JPanel();
        //mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        mainPanel.setLayout(new BorderLayout());
        
        mainPane = new JScrollPane(mainPanel);
        mainPane.setPreferredSize(new Dimension(1440, 400));
        
        welcomeScrollPane = new JScrollPane();
        welcomeArea = new JTextArea();
        
        upload = new JButton("Upload");
        upload.addActionListener(this);
        upload.setActionCommand("UPLOAD");
        
        next = new JButton("Next");
        next.addActionListener(this);
        next.setActionCommand("NEXT");
        
        prev = new JButton("Previous");
        prev.addActionListener(this);
        prev.setActionCommand("PREVIOUS");
        
        submit = new JButton("Submit");
        submit.addActionListener(this);
        submit.setActionCommand("SUBMIT");

        welcomeArea.setColumns(20);
        welcomeArea.setRows(5);
        welcomeArea.setText(welcome);
        welcomeArea.setEditable(false);
        welcomeScrollPane.setViewportView(welcomeArea);
        
        mainPanel.add(welcomeScrollPane, BorderLayout.CENTER);
        
        //mainPanel.add(submit);
        
        mainPanel.add(upload,BorderLayout.EAST);
        
        prev.setEnabled(false);
        mainPanel.add(prev,BorderLayout.WEST);
        
        mainPanel.validate();
        add(mainPane);
        //questionArea.setText("Question 1:\nReduce (\\x.x c) b");

        

        pack();
    }// </editor-fold>
    
    private String loadWelcomeMessage(){
        String welcomeMsg = "WELCOME to Lambda Tutorial"+
                "\na is for alpha, b is for beta, e is for eta, and c is for conversion (such as from true to ";
        return welcomeMsg;
    }
    
    private boolean loadQuestions(){
        boolean qLoaded = false;
        //TODO: read questions from txt file
        //TODO: select a file to be read
        String line="", quest="",require="", start="";
        char firstChar ='a';
        double mark = 0;
        int numQ = 0;
        List<Question> questionList = new ArrayList<>();
        try {
            File qFile = new File("questions.txt");
    		
            BufferedReader reader=new BufferedReader(new FileReader(qFile));
            while((line=reader.readLine())!=null){
            	firstChar = line.charAt(0);
                //Question starts with '#', mark starts with '<', requirement starts with '!', start expression starts with '>'
                if (firstChar=='#'){
                    numQ+=1;
                    if(numQ>1){
                        questionList.add(new Question(quest,mark,require,start));
                    }
                    quest = line.substring(1);
                    mark=0;
                    require="";
                    start="";
                }else if(firstChar=='<'){
                    mark = Double.parseDouble(line.substring(1));
                }else if(firstChar=='!'){
                    require = line.substring(1);
                }else if(firstChar=='>'){
                    start = line.substring(1);
                }else{
                    quest+=" " + line;
                }
            }
            reader.close();
            questionList.add(new Question(quest,mark,require,start));
 
        } catch (IOException e) {
            System.err.print("error reading file: " + e.toString());
        }
        System.out.println(questionList);
        questions = questionList.toArray(new Question[numQ]);
        //questions = new Question[10];
        /*questions[0] = new Question("Question 1",10.0,"R");
        questions[1] = new Question("Question 2", 20.0,"Associative");
        for (int i = 2;i<10;i++){
            questions[i] = new Question("Question",((int)(Math.random()*10+1)),"None");
        }
        int numQ = questions.length;
        */
        qPanel = new QuestionPanel[numQ];
        qPane = new JScrollPane[numQ];
        
        for (int i=0;i<numQ;i++){
            qPanel[i] = new QuestionPanel(questions[i]);
            qPane[i] = new JScrollPane(qPanel[i]);
            //qPane[i].setPreferredSize(new Dimension(500, 400));
            //mainPanel.add(qPane[i]);
        }
        
        solutions = new Solution[numQ];
        
        qLoaded = true; //questions loaded succesfully
        return qLoaded; 
    }
    
    private void saveSolutions(){
        
        //place all solutions in a StringBuilder
        StringBuilder solutionBuilder = new StringBuilder(((Student)student).getStudentNum());
        solutionBuilder.append("\n");
        for(int i=0;i<questions.length;i++){
                solutions[i] = qPanel[i].getSolution();
                
                solutionBuilder.append(solutions[i].outputFormat());
                solutionBuilder.append("\n");
        }  
        System.out.println(solutionBuilder.toString());
        Writer writer;
        try{	
            writer = new BufferedWriter(new FileWriter(new File("solutions.txt"), false));	//new writer
            writer.write(solutionBuilder.toString());//write string
            writer.close();
        } catch (IOException ex) {
            System.err.print("save solutions error: " + ex.toString());
        } 
        
    }
    
    private void submitSolutions(){
        //TODO: save solutions and close program
        saveSolutions();
    }
    
    private Question getQuestion(int id){
        return questions[id];
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if(action.equals("SUBMIT")){
            submitSolutions();
        }else if(action.equals("NEXT")){
            nextQuestion();
        }else if(action.equals("PREVIOUS")){
            previousQuestion();
        }else if(action.equals("UPLOAD")){
            boolean result = loadQuestions();
            if(result){
                mainPanel.remove(upload);
                mainPanel.add(next,BorderLayout.EAST);
                nextQuestion();
            }else{
                welcomeArea.append("Error loading questions");
            }
        }
        
    }
    
    //cannot go above or below number of questions
    private void nextQuestion() {
        if(qIndex<0){
            mainPanel.remove(welcomeScrollPane);
        }else{
            mainPanel.remove(qPane[qIndex]);
        }
        qIndex+=1;
        if (qIndex >= questions.length){
            next.setEnabled(false);
            mainPanel.add(submit, BorderLayout.CENTER);
        }else{
            mainPanel.add(qPane[qIndex], BorderLayout.CENTER);
            prev.setEnabled(true);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void previousQuestion() {
        if(qIndex>=questions.length){
            mainPanel.remove(submit);
        }else{
            mainPanel.remove(qPane[qIndex]);
        }
        qIndex-=1;
        if (qIndex < 0){
            prev.setEnabled(false);
            mainPanel.add(welcomeScrollPane, BorderLayout.CENTER);
        }else{
            mainPanel.add(qPane[qIndex], BorderLayout.CENTER);
            next.setEnabled(true);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    class LinePanel extends JPanel{
        private final String [] REDUCTIONS = {"alpha","beta","eta","conversion"};
        private JComboBox reductionBox;
        private JTextField expressionField;
        private JTextField reasonField;
        public LinePanel(){
            super(new BorderLayout());
            reductionBox = new JComboBox();
            reductionBox.setModel(new DefaultComboBoxModel(new String[] { "α", "β", "η", "→" }));
            expressionField = new JTextField(50);
            reasonField = new JTextField(30);
            ((AbstractDocument) expressionField.getDocument()).setDocumentFilter(new Formatter.LambdaFilter());
            ((AbstractDocument) reasonField.getDocument()).setDocumentFilter(new Formatter.LambdaFilter());

            add(reductionBox, BorderLayout.WEST);
            add(expressionField, BorderLayout.CENTER);
            add(reasonField, BorderLayout.EAST);
        }
        
        public boolean checkLine(){
            String expression = expressionField.getText();
            char [] expressionChar = expression.toCharArray();
            int numP=0; //parenthesis
            int numC=0; //curly bracket
            int numH=0; //hard bracket
            for (int i=0;i<expressionChar.length;i++ ){
                if(expressionChar[i]=='('){
                    numP+=1;
                }else if(expressionChar[i]==')'){
                    numP-=1;
                }else if(expressionChar[i]=='{'){
                    numC+=1;
                }else if(expressionChar[i]=='}'){
                    numC-=1;
                }else if(expressionChar[i]=='['){
                    numH+=1;
                }else if(expressionChar[i]==']'){
                    numH-=1;
                }
            }
            
            if (numP!=0 || numC!=0 || numH!=0 ){
                return false;
            }else{
                return true;
            }
              
        }
        
        public Line getLine(){
            String str = (String)reductionBox.getSelectedItem();
            char reductionChar = str.charAt(0);
            Line line = new Line(expressionField.getText(),reductionChar,reasonField.getText());
            return line;
        }
    }
        
        
    class QuestionPanel extends JPanel{
        private JScrollPane questionScroll;
        private JTextArea questionArea;
        private JTextField startField;
        private LinePanel lineP;
        private JButton addLine;
        private JButton parserB;
        private JTextField parseResult;
        private Question question;
        
        public QuestionPanel(Question q){
            super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            question = q;
            questionArea = new JTextArea();
            questionArea.setColumns(20);
            questionScroll = new JScrollPane();
            questionArea.setText("Question " + q.getId() + ": " + q.getDescription() + "\nRequirements: " + q.getRequirements() + "\nMarks: " + q.getMaxMark());
            questionArea.setEditable(false);
            startField = new JTextField(q.getStart());
            startField.setEditable(false);
            lineP = new LinePanel();
            
            JPanel extra = new JPanel(new FlowLayout());
            
            addLine = new JButton();
            addLine.setText("Add line");
            parserB = new JButton("Parse lines");
            parseResult = new JTextField(10);
            parseResult.setText("formatting ");
            
            questionScroll.setViewportView(questionArea);
            
            add(questionScroll);
            add(startField);
            
            extra.add(addLine);
            extra.add(parserB);
            extra.add(parseResult);
            add(extra);
            
            add(lineP);
            add(new LinePanel());
            add(new LinePanel());
            add(new LinePanel());
            add(new LinePanel());
            add(new LinePanel());
            add(new LinePanel());
            
            addLine.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    add(new LinePanel());
                }
            });
            parserB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    if(checkFormat()){
                        parseResult.setText("formatting good");
                    }else{
                        parseResult.setText("errors exist");
                    }
                }
            });
        
        }
        
        
        public boolean checkFormat(){
            int numComp = getComponentCount();
            Component [] comps = getComponents();
            for (int i=0;i<numComp;i++){
                if (comps[i] instanceof LinePanel){
                    if(!((LinePanel)comps[i]).checkLine()){
                        return false;
                    }
                }
            }
            return true;
        }
        
        public Solution getSolution(){
            int numComp = getComponentCount();
            Component [] comps = getComponents();
            int numLines = 0;
            List<LinePanel> linePanels = new ArrayList<>();
            for (int i=0;i<numComp;i++){
                if (comps[i] instanceof LinePanel){
                    linePanels.add((LinePanel)comps[i]);
                }
            }
            
            numLines = linePanels.size();
            
            Line [] lines = new Line [numLines];
            for (int i=0;i<numLines;i++){
                lines[i] = linePanels.get(i).getLine();
            }
            
            Solution sol = new Solution(question, lines);
            return sol;
        }
        
    }
    
}
