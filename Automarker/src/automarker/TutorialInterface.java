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
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
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
    private JScrollPane mainPane;
    private JTextArea welcomeArea;
    private QuestionPanel [] qPanel;
    private JScrollPane [] qPane;
    private JButton submit;
    
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
        
        //TODO: student sign in
        setLayout(new BorderLayout());

        loadQuestions();
        solutions = new Solution[questions.length];
        
        String welcome = loadWelcomeMessage();
        initComponents(questions.length);
        setVisible(true);
        
        
    }
    
    @SuppressWarnings("unchecked")
    
    private void initComponents(int numQ) {
// <editor-fold defaultstate="collapsed" desc="">//GEN-BEGIN:initComponents
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        
        mainPane = new JScrollPane(mainPanel);
        mainPane.setPreferredSize(new Dimension(1920, 1000));
        
        welcomeScrollPane = new JScrollPane();
        welcomeArea = new JTextArea();
        qPanel = new QuestionPanel[numQ];
        qPane = new JScrollPane[numQ];
        
        submit = new JButton("Submit");
        submit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                submitSolutions();
            }
        
        });
        

        welcomeArea.setColumns(20);
        welcomeArea.setRows(5);
        welcomeArea.setText("Welcome to the Lambda Calculus tutorial");
        welcomeScrollPane.setViewportView(welcomeArea);
        
        mainPanel.add(welcomeScrollPane);
        
        for (int i=0;i<numQ;i++){
            qPanel[i] = new QuestionPanel(questions[i]);
            qPane[i] = new JScrollPane(qPanel[i]);
            qPane[i].setPreferredSize(new Dimension(500, 400));
            mainPanel.add(qPane[i]);
        }
        mainPanel.add(submit);
        mainPanel.validate();
        add(mainPane);
        //questionArea.setText("Question 1:\nReduce (\\x.x c) b");

        

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public String loadWelcomeMessage(){
        return "WELCOME!";
    }
    
    public void loadQuestions(){
        //TODO: read questions from txt file
        questions = new Question[10];
        questions[0] = new Question("Question 1",10.0,"R");
        questions[1] = new Question("Question 2", 20.0,"Associative");
        for (int i = 2;i<10;i++){
            questions[i] = new Question("Question",((int)(Math.random()*10+1)),"None");
        }
    }
    
    public void saveSolutions(){
        //TODO: save solutions to file
        for(int i=0;i<questions.length;i++){
            solutions[i] = qPanel[i].getSolution();
            System.out.println(solutions[i]);
        }
    }
    
    public void submitSolutions(){
        //TODO: save solutions and close program
        saveSolutions();
    }
    
    private Question getQuestion(int id){
        return questions[id];
    }
    
    class LinePanel extends JPanel implements KeyListener{
        private final String [] REDUCTIONS = {"alpha","beta","eta","conversion"};
        private JComboBox reductionBox;
        private JTextField expressionField;
        private JTextField reasonField;
        public LinePanel(){
            super(new BorderLayout());
            reductionBox = new JComboBox();
            reductionBox.setModel(new DefaultComboBoxModel(new String[] { "a", "b", "e", "c" }));
            expressionField = new JTextField(50);
            reasonField = new JTextField(30);
            add(reductionBox, BorderLayout.WEST);
            add(expressionField, BorderLayout.CENTER);
            add(reasonField, BorderLayout.EAST);
        }

        public Line getLine(){
            String str = (String)reductionBox.getSelectedItem();
            char reductionChar = str.charAt(0);
            Line line = new Line(expressionField.getText(),reductionChar,reasonField.getText());
            return line;
        }
        
        @Override
        public void keyTyped(KeyEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyPressed(KeyEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyReleased(KeyEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
        
        
    class QuestionPanel extends JPanel{
        private JScrollPane questionScroll;
        private JTextArea questionArea;
        private LinePanel lineP;
        private JButton addLine;
        private Question question;
        
        public QuestionPanel(Question q){
            super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            question = q;
            questionArea = new JTextArea();
            questionArea.setColumns(20);
            questionScroll = new JScrollPane();
            questionArea.setText("Question " + q.getId() + ": " + q.getDescription() + "\nRequirements: " + q.getRequirements() + "\nMarks: " + q.getMaxMark());
            questionArea.setEditable(false);
            lineP = new LinePanel();
            addLine = new JButton();
            addLine.setText("Add line");
            questionScroll.setViewportView(questionArea);
            
            add(questionScroll);
            add(addLine);
            add(lineP);
            add(new LinePanel());
            add(new LinePanel());
            add(new LinePanel());
            add(new LinePanel());
            add(new LinePanel());
            add(new LinePanel());
            
            addLine.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    add(new LinePanel());
                }
            });
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
