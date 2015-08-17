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
public class Automarker {

    private Solution [] solutions;
    
    public Automarker(){
        //TODO: sign in
        
    }
    
    public void loadFiles(){
        solutions = new Solution[1];
        Line [] lines = new Line[2];
        lines[0] = new Line("(\\x.x) f", 'a', "x->f");
        lines[1] = new Line("f",'b',"\\x.x/f");
        solutions[0] = new Solution(new Question("Question 1\n (\\x.x) x", 20.0, "Normal","(\\x.x) x"),lines);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Automarker();
    }
    
}
