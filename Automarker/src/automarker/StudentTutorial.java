/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automarker;

/**
 *
 * @author Chris
 */
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

public class StudentTutorial extends JFrame {
    private final String [] REDUCTIONS = {"alpha","beta","eta","conversion"};
    private Question [] questions;
    //component declarations
    
    private JScrollPane welcomeScrollPane;   
    private JPanel mainPanel;
    private JTextArea welcomeArea;
    private QuestionPanel [] qPanel;
    
    public StudentTutorial(Question [] questions) {
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
        
        this.questions = questions;
        //TODO: add welcome message and questions
        setLayout(new BorderLayout());
        initComponents(questions.length);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
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

    /**
     * @param args the command line arguments
     *
    
    public static void main(String args[]) {
        /* Create and display the form *
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentTutorial().setVisible(true);
            }
        });
    }
    */
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


