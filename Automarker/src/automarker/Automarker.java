/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automarker;

import java.util.List;

/**
 *
 * @author Chris
 */
public class Automarker {

    private Solution [] solutions;
    
    Automarker(){
        //TODO: sign in
        loadFiles();
        Stream stream = new Stream();
        for (Solution sol: solutions){
            String qStart = sol.getQuestion().getStart();
            Line [] lines = sol.getLines();
            
            Line prev = new Line(qStart, 's', null);
            List<Expr> currResult = stream.runExpr(prev.getExpression());
            Expr reducedExpr;
            Expr currExpr = null;
            Expr prevLastExpr;
            Expr currLastExpr = currResult.get(currResult.size()-1);
            //new String[] { "α", "β", "η", "→" }
            for (Line curr: lines){
                reducedExpr = currResult.get(1);
                prevLastExpr = currLastExpr;
                
                
                if(curr.getReduction() == 'α' || curr.getReduction() == 'a'){
                    
                }
                currResult = stream.runExpr(curr.getExpression());
                System.out.println(currResult);
                try {
                    currExpr = Parser.parse(curr.getExpression());
                } catch (Parser.ParseException pe) {
                    System.out.println(pe.getMessage());
                    //jTextArea.setText(pe.getMessage());
                }
                currLastExpr = currResult.get(currResult.size()-1);
                System.out.println("prev last reduced form: " + prevLastExpr);
                System.out.println("current last reduced form: " + currLastExpr);
                //System.out.println(prevLastExpr.equals(currLastExpr)); //false
                //System.out.println(prevLastExpr == currLastExpr);     //false
                //System.out.println(prevLastExpr.toString().equals(currLastExpr.toString())); //true
                
                if(prevLastExpr.toString().equals(currLastExpr.toString())){
                    //reduced to same form
                    System.out.println("equal");
                    System.out.println("prev reduced form: " + reducedExpr);
                    System.out.println("curr reduced form: " + currExpr);
                    
                    if(currExpr!=null && reducedExpr.toString().equals(currExpr.toString())){
                        //same next line
                        System.out.println("award mark");
                        curr.setMark(1);
                    }
                }
                
                //prevResult = currResult;
                
                
            }
            sol.setMark();
            System.out.println(sol.getMark());
        }
        
        
        
    }
    
    private void loadFiles(){
        solutions = new Solution[1];
        Line [] lines = new Line[2];
        lines[0] = new Line("(\\f.f) x", 'a', "x/f");
        lines[1] = new Line("x",'B',"x/f");
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
