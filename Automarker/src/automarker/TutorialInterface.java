/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automarker;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 *
 * @author Chris
 */
public class TutorialInterface extends JFrame{
    private final String [] REDUCTIONS = {"alpha","beta","eta","conversion"};
    //component declarations
    
    private JScrollPane welcomeScrollPane;   
    private JPanel mainPanel;
    private JTextArea welcomeArea;
    private QuestionPanel [] qPanel;
    
    private int incr;
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
        
        //TODO: add welcome message and questions
        setLayout(new BorderLayout());
        
        incr=0;
        loadQuestions();
        createSolutions();
        String welcome = loadWelcomeMessage();
        initComponents(questions.length);
        setVisible(true);
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents(int numQ) {

        mainPanel = new JPanel();
        welcomeScrollPane = new JScrollPane();
        welcomeArea = new JTextArea();
        qPanel = new QuestionPanel[numQ];
        
        

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        welcomeArea.setColumns(20);
        welcomeArea.setRows(5);
        welcomeArea.setText("Welcome to the Lambda Calculus tutorial");
        welcomeScrollPane.setViewportView(welcomeArea);

        for (int i=0;i<numQ;i++){
            qPanel[i] = new QuestionPanel(questions[i]);
            add(qPanel[i]);
        }
        //questionArea.setText("Question 1:\nReduce (\\x.x c) b");
        

        

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public String loadWelcomeMessage(){
        return "WELCOME!";
    }
    
    public void createSolutions(){
        incr+=1;
        Solution sol = new Solution(getQuestion(incr));
        
    }
    
    public void loadQuestions(){
        //TODO: read questions from txt file
        questions = new Question[2];
        questions[0] = new Question("Question 1",10.0,"R");
        questions[1] = new Question("Question 2", 20.0,"Associative");
    }
    
    private Question getQuestion(int id){
        return questions[id];
    }
    
    class LinePanel extends JPanel{
        private final String [] REDUCTIONS = {"alpha","beta","eta","conversion"};
        private JComboBox reductionBox;
        private JTextField expressionField;
        private JTextField reasonField;
        public LinePanel(){
            super(new BorderLayout());
            reductionBox = new JComboBox();
            reductionBox.setModel(new DefaultComboBoxModel(new String[] { "a", "b", "e", "c" }));
            expressionField = new JTextField();
            reasonField = new JTextField();
            add(reductionBox, BorderLayout.PAGE_START);
            add(expressionField, BorderLayout.CENTER);
            add(reasonField, BorderLayout.PAGE_END);
        }
    }
        
        
    class QuestionPanel extends JPanel{
        private JScrollPane questionScroll;
        private JTextArea questionArea;
        private LinePanel lineP;
        private JButton addLine;
        
        public QuestionPanel(Question q){
            super(new BorderLayout());
            System.out.println(q);
            questionArea = new JTextArea();
            questionArea.setColumns(20);
            questionScroll = new JScrollPane();
            questionArea.setText("Question " + q.getId() + ": " + q.getDescription() + "\nRequirements: " + q.getRequirements() + "\nMarks: " + q.getMaxMark());
            lineP = new LinePanel();
            addLine = new JButton();
            addLine.setText("Add line");
            questionScroll.setViewportView(questionArea);
            
            add(questionScroll, BorderLayout.PAGE_START);
            add(lineP, BorderLayout.LINE_END);
            add(addLine,BorderLayout.PAGE_END);
            
            addLine.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    add(new LinePanel(), BorderLayout.LINE_END);
                }
            });
        }
    }
    
}
