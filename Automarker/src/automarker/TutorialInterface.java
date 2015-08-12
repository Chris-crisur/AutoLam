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
public class TutorialInterface {
    private int incr;
    private Solution [] solutions;
    private Question [] questions;
    
    public static void main(String [] args){
        TutorialInterface TI = new TutorialInterface();
    }
    
    public TutorialInterface(){
        incr=0;
        loadQuestions();
        createSolutions();
        String welcome = loadWelcomeMessage();
        StudentTutorial ST = new StudentTutorial(); //TODO: add welcome message and questions
        ST.setVisible(true);
    }
    
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
        questions[1] = new Question(1,"Question 1",10.0,"");
        questions[2] = new Question(2,"Question 2", 20.0,"Associative");
    }
    
    private Question getQuestion(int id){
        return questions[id];
    }
    
}
