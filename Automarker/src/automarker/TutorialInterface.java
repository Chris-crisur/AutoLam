/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automarker;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Calendar;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import javax.swing.*;

//import automarker.StudentTutorial.LinePanel.MyInputListener;

/**
 *
 * @author Chris
 */
public class TutorialInterface extends JFrame implements ActionListener{
    private final String [] REDUCTIONS = {"alpha","beta","eta","conversion"};
    private Student student;

    //component declarations
    
    private JScrollPane welcomeScrollPane;   
    private JPanel mainPanel;
    private JScrollPane mainPane;
    private JTextArea welcomeArea;
    private JScrollPane [] qPane;
    
    private JButton submit;
    private JButton next;
    private JButton upload;
    private JButton prev;
    private int qIndex;
    
    
    public Solution [] solutions;
    private Question [] questions;
    private javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
    List<Question> questionList;
    
   /** public static void main(String [] args){
        TutorialInterface TI = new TutorialInterface();
    }*/
    
    public TutorialInterface(Student stud){
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        //</editor-fold>*/
        
        //signIn();
        
        //setLayout(new BorderLayout());
        this.student = stud;
        String welcome = loadWelcomeMessage();
        qIndex = -1;
        //initComponents();
        //setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
    
    private void signIn(){
        String studentNumber = JOptionPane.showInputDialog(this, "What is your student number?", JOptionPane.QUESTION_MESSAGE);
        String studentName = JOptionPane.showInputDialog(this, "What is your name?", JOptionPane.QUESTION_MESSAGE);
        
        student = new Student(studentName, studentNumber);
    }
    
    private void initComponents() {
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
        
        prev = new JButton("Back");
        prev.addActionListener(this);
        prev.setActionCommand("PREVIOUS");
        
        submit = new JButton("Submit");
        submit.addActionListener(this);
        submit.setActionCommand("SUBMIT");

        welcomeArea.setColumns(20);
        welcomeArea.setRows(5);
        welcomeArea.setText("Welcome to the Lambda Calculus tutorial");
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
        return "WELCOME!";
    }
    
    public List<Question> loadQuestions(String txtFile){
        boolean qLoaded = false;
        //read questions from txt file
        //select a file to be read
        String line="", quest="",require="", start="";
        char firstChar ='a';
        double mark = 0;
        int numQ = 0;
         questionList = new ArrayList<Question>();
        try {
            File qFile = new File(txtFile);
    		
            BufferedReader reader=new BufferedReader(new FileReader(qFile));
            while((line=reader.readLine())!=null){
            	firstChar = line.charAt(0);
                //Question starts with '#', mark starts with '>', requirement starts with '!'
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
                    quest+="\n" + line;
                }
            }
            reader.close();
            questionList.add(new Question(quest,mark,require,start));
            qLoaded = true; //questions loaded succesfully
 
        } catch (IOException e) {
        	welcomeArea.setText("Error occured while loading the file:"+e.toString());
            System.err.print("error reading file: " + e.toString());
        }
        System.out.println(questionList);
        questions = questionList.toArray(new Question[numQ]);
        solutions = new Solution[numQ];
        return questionList; 
    }
    String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    public void saveSolutions(){
        StringBuilder solutionBuilder = new StringBuilder(student.getStudentNum()+"#"+ student.getName());
        solutionBuilder.append("\n"+date);
        for(int i=0;i<solutions.length;i++){
                solutionBuilder.append(solutions[i].outputFormat());
                solutionBuilder.append("\n");
        }  
        System.out.println(solutionBuilder.toString());
        Writer writer;
        try{	
            writer = new BufferedWriter(new FileWriter(new File("solutions.lam"), false));	//new writer
            writer.write(solutionBuilder.toString());//write string
            writer.close();
            JOptionPane.showMessageDialog(this, "Find the output textfile in you current working directory.");
        } catch (IOException ex) {
            System.err.print("save solutions error: " + ex.toString());
        } 
        
    }
    
    private void submitSolutions(){
        //TODO: save solutions and close program
        saveSolutions();
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
            boolean chris = true; //TODO: take out later
            if (!chris){
                int i = chooser.showOpenDialog(this);
                if(i== chooser.APPROVE_OPTION){
                    boolean result = false;//loadQuestions(chooser.getSelectedFile().getPath());
                    if(result){
                        mainPanel.remove(upload);   //next replaces upload button
                        mainPanel.add(next,BorderLayout.EAST);
                        nextQuestion();
                    }else{
                        welcomeArea.append("Error loading questions");
                    }
                }
            }else{
                boolean result = false;//loadQuestions("questionsTutorial.txt");
                    if(result){
                        mainPanel.remove(upload);   //next replaces upload button
                        mainPanel.add(next,BorderLayout.EAST);
                        nextQuestion();
                    }else{
                        welcomeArea.append("Error loading questions");
                    }
            }
        }
        
    }

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
        if(qIndex>=questions.length-1){
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
    
    
}
