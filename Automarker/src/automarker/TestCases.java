/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automarker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Chris
 */
class TestCases {
    
    public static boolean checkAnswer(Solution sol){
        Question q = sol.getQuestion();
        String expectedAnswer = q.getDescription();
        String actualAnswer = sol.getMark()+"/"+q.getMaxMark();
        double aA = sol.getMark();
        double eA = 0;
        try{
            int newLine = expectedAnswer.indexOf("\n");
            if(newLine>0)
                eA = Double.parseDouble(expectedAnswer.substring(expectedAnswer.indexOf(":")+1,newLine));
            else
                eA = Double.parseDouble(expectedAnswer.substring(expectedAnswer.indexOf(":")+1));
        }catch(Exception e){
            System.out.println("not a test case " + e);
        }
        if(aA==eA){
            return true;
        }else if(Math.round(aA*100.0)/100.0==Math.round(eA*100.0)/100.0){ //rount to 2 decimal places
            return true;
        }else{
            return false;
        }
    }
    
    public static Solution [] loadTest(String index){
        
        List<Solution> solutionList = new ArrayList<>();
        
        switch(index){
            case "performance":
                for(int i=0;i<100;i++){
                    solutionList.addAll(Arrays.asList(loadTest("all")));
                }
                break;
            case "all":
                
            case "alpha":
                
            case "a1":
                solutionList.add(alphaTestOne());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a2":
                solutionList.add(alphaTestTwo());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a3":
                solutionList.add(alphaTestThree());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a4":
                solutionList.add(alphaTestFour());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a5":
                solutionList.add(alphaTestFive());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a6":
                solutionList.add(alphaTestSix());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a7":
                solutionList.add(alphaTestSeven());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a8":
                solutionList.add(alphaTestEight());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a9":
                solutionList.add(alphaTestNine());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a10":
                solutionList.add(alphaTest10());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a11":
                solutionList.add(alphaTest11());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a12":
                solutionList.add(alphaTest12());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a13":
                solutionList.add(alphaTest13());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a14":
                solutionList.add(alphaTest14());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a15":
                solutionList.add(alphaTest15());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a16":
                solutionList.add(alphaTest16());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a17":
                solutionList.add(alphaTest17());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a18":
                solutionList.add(alphaTest18());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a19":
                solutionList.add(alphaTest19());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a20":
                solutionList.add(alphaTest20());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a21":
                solutionList.add(alphaTest21());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "a22":
                solutionList.add(alphaTest22());
                if(!index.equals("all")&&!index.equals("alpha"))
                    break;
            case "beta":
                if(index.equals("alpha"))
                    break;
            case "b1":
                solutionList.add(betaTestOne());
                if(!index.equals("all")&&!index.equals("beta"))
                    break;
            case "b2":
                solutionList.add(betaTestTwo());
                if(!index.equals("all")&&!index.equals("beta"))
                    break;
            case "b3":
                solutionList.add(betaTestThree());
                if(!index.equals("all")&&!index.equals("beta"))
                    break;
            case "b4":
                solutionList.add(betaTestFour());
                if(!index.equals("all")&&!index.equals("beta"))
                    break;
            case "b5":
                solutionList.add(betaTestFive());
                if(!index.equals("all")&&!index.equals("beta"))
                    break;
            case "b6":
                solutionList.add(betaTestSix());
                if(!index.equals("all")&&!index.equals("beta"))
                    break;
            case "b7":
                solutionList.add(betaTestSeven());
                if(!index.equals("all")&&!index.equals("beta"))
                    break;
            case "b8":
                solutionList.add(betaTestEight());
                if(!index.equals("all")&&!index.equals("beta"))
                    break;
            case "b9":
                solutionList.add(betaTestNine());
                if(!index.equals("all")&&!index.equals("beta"))
                    break;
            case "eta":
                if(index.equals("beta"))
                    break;
            case "e1":
                solutionList.add(etaTestOne());
                if(!index.equals("all")&&!index.equals("eta"))
                    break;
            case "conversion":
                if(index.equals("eta")){
                    break;
                }
            case "c1":
                solutionList.add(conversionTestOne());
                if(!index.equals("all")&&!index.equals("conversion"))
                    break;
            case "tutorial":
                if(index.equals("conversion")){
                    break;
                }
            case "t1":
                solutionList.add(conversionTestOne());
                if(!index.equals("all")&&!index.equals("tutorial"))
                    break;
            case "t2":
                solutionList.add(tutorialTestTwo());
                if(!index.equals("all")&&!index.equals("tutorial"))
                    break;
            case "t3":
                solutionList.add(tutorialTestThree());
                if(!index.equals("all")&&!index.equals("tutorial"))
                    break;
                
                break;
            default:
                System.out.println("no such test");
                break;
        }
        
        return solutionList.toArray(new Solution[1]);
    }
    
    
    /***
     * one alpha conversion required at start
     * 
     * Correct answer
     * 
     * @expected_output Answer: 
                        a -> (λx.λv. x v)(λx.x y z)	[y/v] (1.0)
                        b -> λv.(λx.x y z) v	[(λx.x y z)/b] (1.0)
                        b -> λv.v y z	[] (1.0)
                        Final mark: 3.0 out of 3.0
     */
    private static Solution alphaTestOne(){
        String descrip = "A1:6";
        String require = "";
        String start = "(λx.λy. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv. x v)(λx.x y z)",'a',"v/y"));
        lineArr.add(new Line("λv.(λx.x y z) v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.v y z",'b',"v/x"));   //ETA disabled
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * one alpha conversion required at start (changed free of bound variable)
     * wrong alpha conversion initially
     * right beta conversions after
     * mark penalty means 2 right beta's divided 
     * by 2 equals 1 a final mark
     * 
     * Wrong answer
     * 
     * @expected_output 
     */
    private static Solution alphaTestTwo(){
        String descrip = "A2:3";  
        String require = "";
        String start = "(λx.λy. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λy. x y)(λx.x v z)",'a',"v/y")); //wrong - changed free var
        lineArr.add(new Line("λy.(λx.x v z) y",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λy.y v z",'b',"y/x"));  //wrong from expected result
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * one alpha conversion required at start (changed free of bound variable)
     * wrong alpha conversion initially
     * right beta conversions after
     * mark penalty means 2 right beta's divided 
     * by 2 equals 1 a final mark
     * 
     * Wrong answer
     * 
     * @expected_output 
     */
    private static Solution alphaTestThree(){
        String descrip = "A3:3";  
        String require = "";
        String start = "(λx.λy. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv. x y)(λx.x y z)",'a',"v/y")); //wrong - changed only lambda part
        lineArr.add(new Line("λv.(λx.x y z) y",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.y y z",'b',"y/x"));  //wrong from expected result
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * Correct answer
     * 
     * @expected_output \
     */
    private static Solution alphaTestFour(){
        String descrip = "A4:6";
        String require = "";
        String start = "(λx.λy.x y y) (λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv. x v v)(λx.x y z)",'a',"v/y"));
        lineArr.add(new Line("λv.(λx.x y z) v v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.v y z v",'b',"v/x"));   //ETA disabled
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * Wrong answer
     * 
     * @expected_output \
     */
    private static Solution alphaTestFive(){
        String descrip = "A5:3";
        String require = "";
        String start = "(λx.λy.x y y) (λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv. x v y)(λx.x y z)",'a',"v/y"));    //wrong - changed only 1st bound var
        lineArr.add(new Line("λv.(λx.x y z) v y",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.v y z y",'b',"v/x"));   //ETA disabled
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * Wrong answer
     * 
     * @expected_output \
     */
    private static Solution alphaTestSix(){
        String descrip = "A6:3";
        String require = "";
        String start = "(λx.λy.x y y) (λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv. x y v)(λx.x y z)",'a',"v/y"));    //wrong - changed only 2nd bound var
        lineArr.add(new Line("λv.(λx.x y z) y v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.y y z v",'b',"y/x"));   //ETA disabled
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * two alpha conversions required
     * two steps taken (both need to done simultaneously)
     * see A8
     * 
     * Wrong answer
     * 
     * @expected_output 
     */
    private static Solution alphaTestSeven(){
        String descrip = "A7:4";
        String require = "";
        String start = "(λx.λy.λz. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv.λz. x v)(λx.x y z)",'a',"v/y"));   //needed to change both y and z
        lineArr.add(new Line("λv.λz.(λx.x y z) v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.λu.(λx.x y z) v",'a',"u/z"));//only changed 1 z, which is correct  overall but wrong in context
        lineArr.add(new Line("λv.λu.v y z",'b',"v/x"));  
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * two alpha conversions required
     * one step taken (but mark must be awarded twice)
     * 
     * Correct answer
     * 
     * @expected_output 
     */
    private static Solution alphaTestEight(){
        String descrip = "A8:8";
        String require = "";
        String start = "(λx.λy.λz. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv.λu. x v)(λx.x y z)",'a',"v/y,u/z"));
        lineArr.add(new Line("λv.λu.(λx.x y z) v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.λu.v y z",'b',"v/x")); 
        
        return new Solution(new Question(descrip,8,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * two alpha conversions required
     * two steps taken
     * 
     * Wrong answer
     * 
     * @expected_output 
     */
    private static Solution alphaTestNine(){
        String descrip = "A9:4";
        String require = "";
        String start = "(λx.λy.λz. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv.λz. x v)(λx.x y z)",'a',"v/y"));   //didn't change z
        lineArr.add(new Line("λv.λz.(λx.x y z) v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.λu.(λx.x y u) v",'a',"u/z"));  //at this point, souldn't change z to u - redundant
        lineArr.add(new Line("λv.λu.v y u",'b',"v/x"));  
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    /***
     * two alpha conversions required
     * only one alpha conversion done (wrong) Half mark for correct reasoning
     * beta reductions done correctly, but final answer wrong
     * 
     * Wrong answer
     * 
     * @expected_output 
     */
    private static Solution alphaTest10(){
        String descrip = "A10:3.5";
        String require = "";
        String start = "(λx.λy.λz. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv.λz. x v)(λx.x y z)",'a',"v/y"));   //didn't change z
        lineArr.add(new Line("λv.λz.(λx.x y z) v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.λz.v y z",'b',"v/x"));  
        
        return new Solution(new Question(descrip,8,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * Correct answer
     * 
     * ((λx.λy.λz. x y)(λx.x y) λz.z)
     * (λx.λi0.λz.x i0) (λx.x y) (λz.z)
        = (λi0.λz.(λx.x y) i0) (λz.z)
        = λz.(λx.x y) (λi0.i0)
        = λz.(λi0.i0) y
        = λz.y
     * 
     * @expected_output
     */
    private static Solution alphaTest11(){
        String descrip = "A11:12";
        String require = "";
        String start = "((λx.λy.λz. x y)(λx.x y) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λv.λz. x v)(λx.x y) λz.z)",'a',"v/y"));     
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λz.z)",'b',"(λx.x y)/x"));
        lineArr.add(new Line("(λz. (λx.x y) λz.z)",'b',"(λz.z)/v"));
        lineArr.add(new Line("(λz. (λx.x y) λu.u)",'a',"u/z"));
        lineArr.add(new Line("(λz. (λu.u) y)",'b',"(λu.u)/x")); 
        lineArr.add(new Line("λz. y",'b',"y/u"));
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * Alpha reduction performed at different point
     * 
     * Correct answer
     * 
     * ((λx.λy.λz. x y)(λx.x y) λz.z)
     * (λx.λi0.λz.x i0) (λx.x y) (λz.z)
        = (λi0.λz.(λx.x y) i0) (λz.z)
        = λz.(λx.x y) (λi0.i0)
        = λz.(λi0.i0) y
        = λz.y
     * 
     * @expected_output
     */
    private static Solution alphaTest12(){
        String descrip = "A12:12";
        String require = "";
        String start = "((λx.λy.λz. x y)(λx.x y) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λv.λz. x v)(λx.x y) λz.z)",'a',"v/y"));     
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λz.z)",'b',"(λx.x y)/x"));
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λu.u)",'a',"u/z"));   //don't need to do at this point, but still correct
        lineArr.add(new Line("(λz. (λx.x y) λu.u)",'b',"(λu.u)/v")); 
        lineArr.add(new Line("(λz. (λu.u) y)",'b',"(λu.u)/x")); 
        lineArr.add(new Line("λz. y",'b',"y/u"));
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * Alpha reduction performed at different point
     * All done at start
     * 
     * Correct answer
     * 
     * ((λx.λy.λz. x y)(λx.x y) λz.z)
     * (λx.λi0.λz.x i0) (λx.x y) (λz.z)
        = (λi0.λz.(λx.x y) i0) (λz.z)
        = λz.(λx.x y) (λi0.i0)
        = λz.(λi0.i0) y
        = λz.y
     * 
     * @expected_output
     */
    private static Solution alphaTest13(){
        String descrip = "A13:12";
        String require = "";
        String start = "((λx.λy.λz. x y)(λx.x y) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λv.λz. x v)(λx.x y) λu.u)",'a',"v/y,u/z"));     
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λu.u)",'b',"(λx.x y)/x"));
        lineArr.add(new Line("(λz. (λx.x y) λu.u)",'b',"(λu.u)/v")); 
        lineArr.add(new Line("(λz. (λu.u) y)",'b',"(λu.u)/x")); 
        lineArr.add(new Line("λz. y",'b',"y/u"));
        
        return new Solution(new Question(descrip,12,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * Alpha reduction performed at different point
     * All done at start
     * Different z changed from previous
     * 
     * Correct answer
     * 
     * ((λx.λy.λz. x y)(λx.x y) λz.z)
     * (λx.λi0.λz.x i0) (λx.x y) (λz.z)
        = (λi0.λz.(λx.x y) i0) (λz.z)
        = λz.(λx.x y) (λi0.i0)
        = λz.(λi0.i0) y
        = λz.y
     * 
     * @expected_output
     */
    private static Solution alphaTest14(){
        String descrip = "A14:12";
        String require = "";
        String start = "((λx.λy.λz. x y)(λx.x y) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λv.λu. x v)(λx.x y) λz.z)",'a',"v/y,u/z"));     
        lineArr.add(new Line("((λv.λu. (λx.x y) v) λz.z)",'b',"(λx.x y)/x"));
        lineArr.add(new Line("(λu. (λx.x y) λz.z)",'b',"(λz.z)/v")); 
        lineArr.add(new Line("(λu. (λz.z) y)",'b',"(λz.z)/x")); 
        lineArr.add(new Line("λu. y",'b',"y/z"));
        
        return new Solution(new Question(descrip,12,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * Different z changed from A11
     * 
     * Correct answer
     * 
     * ((λx.λy.λz. x y)(λx.x y) λz.z)
     * (λx.λi0.λz.x i0) (λx.x y) (λz.z)
        = (λi0.λz.(λx.x y) i0) (λz.z)
        = λz.(λx.x y) (λi0.i0)
        = λz.(λi0.i0) y
        = λz.y
     * 
     * @expected_output
     */
    private static Solution alphaTest15(){
        String descrip = "A15:12";
        String require = "";
        String start = "((λx.λy.λz. x y)(λx.x y) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λv.λz. x v)(λx.x y) λz.z)",'a',"v/y"));     
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λz.z)",'b',"(λx.x y)/x"));
        lineArr.add(new Line("(λz. (λx.x y) λz.z)",'b',"(λz.z)/v"));
        lineArr.add(new Line("(λu. (λx.x y) λz.z)",'a',"u/z"));
        lineArr.add(new Line("(λu. (λz.z) y)",'b',"(λz.z)/x")); 
        lineArr.add(new Line("λu. y",'b',"y/z"));
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * Two alpha reductions performed at start
     * 
     * Correct answer
     * 
     * @expected_output
     */
    private static Solution alphaTest16(){
        String descrip = "A16:12";
        String require = "";
        String start = "((λx.λy.λz. x y)(λx.x y) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λv.λz. x v)(λx.x y) λz.z)",'a',"v/y"));     
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λz.z)",'b',"(λx.x y)/x"));
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λu.u)",'a',"u/z"));   //don't need to do at this point, but still correct
        lineArr.add(new Line("(λz. (λx.x y) λu.u)",'b',"(λu.u)/v")); 
        lineArr.add(new Line("(λz. (λu.u) y)",'b',"(λu.u)/x")); 
        lineArr.add(new Line("λz. y",'b',"y/u"));
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
   
   
    //(λ b.b b)((λ a.a)(λ b.a b))
    private static Solution alphaTest17(){
        String descrip = "A17:12";
        String require = "Normal";
        String start = "(λb.b b)((λa.a)(λb.a b))";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λb.b b)((λc.c)(λb.a b))",'a',"c/a"));
        lineArr.add(new Line("((λc.c)(λb.a b))((λc.c)(λb.a b))",'b',"((λc.c)(λb.a b))/b"));
        //lineArr.add(new Line("(λc.c)(λb.a b)((λc.c)(λb.a b))",'b',"((λc.c)(λb.a b))/b")); //check if different?
        lineArr.add(new Line("(λb.a b)((λc.c)(λb.a b))",'b',"(λb.a b)/c"));
        lineArr.add(new Line("a ((λc.c)(λb.a b))",'b',"((λc.c)(λb.a b))/b"));
        lineArr.add(new Line("a (λb.a b)",'b',"(λb.a b)/c"));
        lineArr.add(new Line("a a",'n',"(λb.a b)/c"));
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
   
    /***
     * three alpha conversions required at start
     *
     *
    ((λx.λy.λz. x y)(λx.x y z) λz.z)
    ((λx.λy.λz. x y)(λx.x y z) λy.y)
        (λx.λi0.λi1.x i0) (λx.x y z) (λi0.i0)
       = (λi0.λi1.(λx.x y z) i0) (λi0.i0)
       = λi0.(λx.x y z) (λi1.i1)
       = λi0.(λi1.i1) y z
       = λi0.y z
    */
     
    private static Solution alphaTest18(){
        String descrip = "A18:14";
        String require = "Normal";
        String start = "((λx.λy.λz. x y)(λx.x y z) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λa.λb. x a)(λx.x y z) λc.c)",'a',"a/y,b/z,c/z"));
        //lineArr.add(new Line("((λx.λa.λb. x a)(λx.x y z) λc.c)",'a',"a/y,c/z,b/z"));
        lineArr.add(new Line("(λa.λb.(λx.x y z) a) (λc.c)",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λb.(λx.x y z) (λc.c)",'b',"(λc.c)/a"));
        lineArr.add(new Line("λb.(λc.c) y z",'b',"(λc.c)/x"));
        lineArr.add(new Line("λb.y z",'b',"y/c"));
        
        return new Solution(new Question(descrip,14,require,start),lineArr.toArray(new Line[1]));
    }
    
    private static Solution alphaTest19(){
        String descrip = "A19:14";
        String require = "Normal";
        String start = "((λx.λy.λz. x y)(λx.x y z) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λa.λb. x a)(λx.x y z) λc.c)",'a',"a/y,c/z,b/z")); //different order, still correct
        lineArr.add(new Line("(λa.λb.(λx.x y z) a) (λc.c)",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λb.(λx.x y z) (λc.c)",'b',"(λc.c)/a"));
        lineArr.add(new Line("λb.(λc.c) y z",'b',"λc.c/x"));
        lineArr.add(new Line("λb.y z",'b',"y/c"));
        
        return new Solution(new Question(descrip,14,require,start),lineArr.toArray(new Line[1]));
    }
    
    private static Solution alphaTest20(){
        String descrip = "A20:12.5";
        String require = "Normal";
        String start = "((λx.λy.λz. x y)(λx.x y z) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λa.λb. x a)(λx.x y z) λc.c)",'a',"a/z,c/z,b/y")); //wrong alpha reasonings
        lineArr.add(new Line("(λa.λb.(λx.x y z) a) (λc.c)",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λb.(λx.x y z) (λc.c)",'b',"(λc.c)/a"));
        lineArr.add(new Line("λb.(λc.c) y z",'b',"λc.c/x")); 
        lineArr.add(new Line("λb.y z",'b',"y/c"));
        
        return new Solution(new Question(descrip,14,require,start),lineArr.toArray(new Line[1]));
    }
   
    private static Solution alphaTest21(){
        String descrip = "A21:4.5";
        String require = "";
        String start = "(λx.λy. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv. x v)(λx.x v z)",'a',"v/y"));  //wrong change of y to v in latter part
        lineArr.add(new Line("λv.(λx.x v z) v",'b',"(λx.x y z)/x"));    //wrong reasoning, correct reduction
        lineArr.add(new Line("λv.(λx.x v z) v",'a',"u/z")); //wrong reasoning, correct reduction
        lineArr.add(new Line("λv.v v z",'b',"v/x"));  //correct
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    private static Solution alphaTest22(){
        String descrip = "A22:1.5";
        String require = "";
        String start = "(λx.λy. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv.λz. x v)(λx.x y u)",'a',"v/y")); //semi-right reasoning (v did replace y)
        lineArr.add(new Line("λv.λz.(λx.x y u) v",'b',"(λx.x y z)/x"));//right reduction, wrong reasoning
        lineArr.add(new Line("λv.λz.(λx.x y u) v",'a',"u/z"));//wrong (same as above line)
        lineArr.add(new Line("λv.λz.v y z",'b',"v/x"));  //wrong
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    //complex
    //(λx.λy.λz. x y)(λx.x y z (λz.λy.z y) x c)a b
    /*
    (λx.λi0.λi1.x i0) (λx.x y z (λi0.λi1.i0 i1) x c) a b
   = (λi0.λi1.(λx.x y z [λi2.λi3.i2 i3] x c) i0) a b
   = (λi0.(λx.x y z [λi1.λi2.i1 i2] x c) a) b
   = (λx.x y z (λi0.λi1.i0 i1) x c) a
   = a y z (λi0.λi1.i0 i1) a c
   = a y z (λi0.i0) a c //eta
    */
    //((λx.λy.λz. x y)(λx.x y z)((λz.λy.z y) x c z ) z)a b
    /*applicative
    (λi0.λi1.λi2.i0 i1) (λi0.i0 y z) ((λi0.λi1.i0 i1) x c z) z a b
   = (λi0.λi1.λi2.i0 i1) (λi0.i0 y z) ((λi0.x i0) c z) z a b
   = (λi0.λi1.λi2.i0 i1) (λi0.i0 y z) (x c z) z a b
   = (λi0.λi1.(λi2.i2 y z) i0) (x c z) z a b
   = (λi0.(λi1.i1 y z) (x c z)) z a b
   = (λi0.i0 y z) (x c z) a b
   = x c z y z a b
    /*
    normal
    (λi0.λi1.λi2.i0 i1) (λi0.i0 y z) ((λi0.λi1.i0 i1) x c z) z a b
   = (λi0.λi1.(λi2.i2 y z) i0) ((λi0.λi1.i0 i1) x c z) z a b
   = (λi0.(λi1.i1 y z) ([λi1.λi2.i1 i2] x c z)) z a b
   = (λi0.i0 y z) ((λi0.λi1.i0 i1) x c z) a b
   = (λi0.λi1.i0 i1) x c z y z a b
   = (λi0.x i0) c z y z a b
   = x c z y z a b
    */
    //(λx.λy.λz. x y)(λx.x y z)(λz.z)
    /*
    (λx.λi0.λi1.x i0) (λx.x y z) (λi0.i0)
   = (λi0.λi1.(λx.x y z) i0) (λi0.i0)
   = λi0.(λx.x y z) (λi1.i1)
   = λi0.(λi1.i1) y z
   = λi0.y z
    
    */
    private static Solution alphaTest1x(){
        String descrip = "A10:2";
        String require = "";
        String start = "((λx.λy.λz. x y)(λx.x y z (λz.λy.z y) x c z ) z )a b";
        List<Line> lineArr = new ArrayList<>();
        
         
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    private static Solution betaTestOne(){
        String descrip = "B1:1";
        String require = "";
        String start = "(λx.λy. y y) y";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("λy.y y",'b',"y/x"));
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    //(λ b.b b)((λ a.a)(λ b.a b))
    private static Solution betaTestTwo(){
        String descrip = "B2:12";
        String require = "Normal";
        String start = "(λb.b b)((λa.a)(λb.a b))";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λb.b b)((λc.c)(λb.a b))",'a',"c/a"));
        lineArr.add(new Line("((λc.c)(λb.a b))((λc.c)(λb.a b))",'b',"((λc.c)(λb.a b))/b"));
        //lineArr.add(new Line("(λc.c)(λb.a b)((λc.c)(λb.a b))",'b',"((λc.c)(λb.a b))/b")); //check if different?
        lineArr.add(new Line("(λb.a b)((λc.c)(λb.a b))",'b',"(λb.a b)/c"));
        lineArr.add(new Line("a ((λc.c)(λb.a b))",'b',"((λc.c)(λb.a b))/b"));
        lineArr.add(new Line("a (λb.a b)",'b',"(λb.a b)/c"));
        lineArr.add(new Line("a a",'n',"(λb.a b)/c"));
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    //(λ b.b b)((λ a.a)(λ b.a b))
    private static Solution betaTestThree(){
        String descrip = "B3:10";
        String require = "Applicative";
        String start = "(λb.b b)((λa.a)(λb.a b))";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λb.b b)((λc.c)(λb.a b))",'a',"c/a"));
        lineArr.add(new Line("(λb.b b)(λb.a b)",'b',"(λb.a b)/c"));
        lineArr.add(new Line("(λb.a b)(λb.a b)",'b',"(λb.a b)/b"));
        lineArr.add(new Line("a (λb.a b)",'b',"(λb.a b)/b"));
        lineArr.add(new Line("a a",'n',"(λb.a b)/c"));
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    //(λ b.b b)((λ a.a)(λ b.a b))
    private static Solution betaTestFour(){
        String descrip = "B4:8";
        String require = "Applicative";
        String start = "(λb.b b)((λa.a)(λb.a b))";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λb.b b)((λc.c)(λb.a b))",'a',"c/a"));
        lineArr.add(new Line("(λb.b b)((λc.c) a)",'n',"")); //eta
        lineArr.add(new Line("(λb.b b) a ",'b',"a/c"));
        lineArr.add(new Line("a a",'b',"a/b"));
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    //(λ b.b b)((λ a.a)(λ b.a b))
    /***
     * 
     * no mention, but applicative
     * 
     */
    private static Solution betaTestFive(){
        String descrip = "B5:10";
        String require = "";
        String start = "(λb.b b)((λa.a)(λb.a b))";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λb.b b)((λc.c)(λb.a b))",'a',"c/a"));
        lineArr.add(new Line("(λb.b b)(λb.a b)",'b',"(λb.a b)/c"));
        lineArr.add(new Line("(λb.a b)(λb.a b)",'b',"(λb.a b)/b"));
        lineArr.add(new Line("a (λb.a b)",'b',"(λb.a b)/b"));
        lineArr.add(new Line("a a",'n',"(λb.a b)/c"));
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    private static Solution betaTestSix(){
        String descrip = "B6:2";
        String require = "";
        String start = "a b ((λc.c) d) e";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("a b d e",'b',"d/c"));
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    private static Solution betaTestSeven(){
        String descrip = "B7:2";
        String require = "";
        String start = "a b (λc.c) d e";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("a b d e",'b',"d/c"));
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    private static Solution betaTestEight(){
        String descrip = "B8:0";
        String require = "";
        String start = "a b ((λc.c) d) e";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("a b (d e)",'b',"d/c"));   //falsely placed parenthesese
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    private static Solution betaTestNine(){
        String descrip = "B9:8";
        String require = "";
        String start = "(λa.b a) (λc.c c) (λd.d e)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("b (λc.c c) (λd.d e)",'b',"(λc.c c)/a"));
        lineArr.add(new Line("b (λd.d e) (λd.d e)",'b',"(λd.d e)/c"));
        lineArr.add(new Line("b (λd.d e) e",'b',"(λd.d e)/d"));
        lineArr.add(new Line("b e e",'b',"e/d"));
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    
    /***
     * one alpha conversion required at start (same as alphaTestOne
     * but also eta instead of beta reduction (correct answer)
     * 
     * Correct answer
     * 
     * @expected_output 
     */
    private static Solution etaTestOne(){
        /*  */
        String descrip = "E1:6";
        String require = "";
        String start = "(λx.λy. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv. x v)(λx.x y z)",'a',"v/y"));
        lineArr.add(new Line("λv.(λx.x y z) v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λx.x y z",'n',""));   //ETA enabled
        
        return new Solution(new Question(descrip,lineArr.size()*2,require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * #Reduce the following in any order. Give the steps of β- α- η-reduction, labelling them correctly, and conclude with the simplest form or state “diverges” if it does not terminate. Indicate what is being substituted with the [ ] notation.
            Let 	t = λx.λy.x 
                    f = λx.λy.y 
                    k = λx.λy.λz.(x y z)
            Obtain the normal form of the following λ-expression if it has one: (i f x) (i t y z)
            Hint: add all parentheses; it may be useful to use j = (i t y z) to abbreviate some of the expressions.
            <20.0
            >(k f x) (k t y z)
     * 
     * Correct answer
     * 
     * model answer:
     *  (((i f) x) ((i t) y) z) ; Adding all parentheses if they want
            = (λx.λy.λz.x y z) f x j ; expanding first i
                                     ; and substituting j as suggested
        1. => (λy.λz.f y z) x j ; [f/x]
        2. => (λz.f x z) j ; [x/y]
        3. => f x j ; [j/z]
        = (λx.λy.y) x j ; expand f
        4. => (λy.y) j ; [x/x] and discarded
        5. => j ; [j/y] identity
        = (λx.λy.λz.x y z) t y z ; expand j
        6. => (λy.λz.t y z) y z ; [t/x]
        7. => (λz.t y z) z ; [y/y]
        8. => t y z ; [z/z]
        = (λx.λy.x) y z ; expand t
        9. →α (λx.λa.x) y z ; α conversion to stop second free y being captured
        10. => (λa.y) z ; [y/x]
        11. => y ; [z/a] and discarded.
     * 
     */
    private static Solution conversionTestOne(){
        /*  */
        String descrip = "C1:22";
        String require = "";
        String start = "(k f x) (k t y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(k f x) j",'>',"j/(k t y z)"));
        lineArr.add(new Line("(λx.λy.λz.x y z) f x j",'>',"(λx.λy.λz.x y z)/k"));
        lineArr.add(new Line("(λy.λz.f y z) x j",'b',"f/x"));
        lineArr.add(new Line("(λz.f x z) j",'b',"x/y"));
        lineArr.add(new Line("f x j",'b',"j/z"));
        lineArr.add(new Line("(λx.λy.y) x j",'>',"(λx.λy.y)/f"));
        lineArr.add(new Line("(λy.y) j",'b',"x/x"));
        lineArr.add(new Line("j",'b',"j/y"));
        lineArr.add(new Line("(λx.λy.λz.x y z) t y z ",'>',"(λx.λy.λz.x y z) t y z/j"));
        lineArr.add(new Line("(λy.λz.t y z) y z",'b',"t/x"));
        lineArr.add(new Line("(λz.t y z) z",'b',"y/y"));
        lineArr.add(new Line("t y z",'b',"z/z"));
        lineArr.add(new Line("(λx.λy.x) y z",'>',"(λx.λy.x)/t"));
        lineArr.add(new Line("(λx.λa.x) y z",'a',"a/y"));
        lineArr.add(new Line("(λa.y) z ",'b',"y/x"));
        lineArr.add(new Line("y ",'b',"z/a"));
        
        return new Solution(new Question(descrip,22,require,start),lineArr.toArray(new Line[1]));
    }
    
    private static Solution tutorialTestTwo(){
        /*  modified to include an extra '*' */
        String descrip = "T1:18";
        String require = "Normal";
        String start = "(λx.(* (* x x) x)) ((λy.(+ y 1)) 2)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("* (* ((λy.+ y 1) 2) ((λy.+ y 1) 2)) ((λy.+ y 1) 2)",'b',"(λy.+ y 1) 2/x"));
        //lineArr.add(new Line("* (* ((λy.+ y 1) 2) ((λy.+ y 1) 2)) ((λy.+ y 1) 2)",'b',"((λy.+ y 1) 2)/x"));
        lineArr.add(new Line("* (* (+ 2 1) ((λy.+ y 1) 2)) ((λy.+ y 1) 2)",'b',"2/y"));
        lineArr.add(new Line("* (* 3 ((λy.+ y 1) 2)) ((λy.+ y 1) 2)",'=',"3/(+ 2 1)"));
        lineArr.add(new Line("* (* 3 (+ 2 1)) ((λy.+ y 1) 2)",'b',"2/y"));
        lineArr.add(new Line("* (* 3 3) ((λy.+ y 1) 2)",'=',"3/(+ 2 1)"));
        lineArr.add(new Line("* 9 ((λy.+ y 1) 2)",'=',"9/(* 3 3)"));
        lineArr.add(new Line("* 9 (+ 2 1)",'b',"2/y"));
        lineArr.add(new Line("* 9 3",'=',"3/(+ 2 1)"));
        lineArr.add(new Line("27",'=',"27/(* 9 3)"));
        
        return new Solution(new Question(descrip,18,require,start),lineArr.toArray(new Line[1]));
    }
    
    private static Solution tutorialTestThree(){
        /*  modified to include an extra '*' */
        String descrip = "T2:10";
        String require = "Applicative";
        String start = "(λx.(* (* x x) x)) ((λy.(+ y 1)) 2)";
        List<Line> lineArr = new ArrayList<>();
        
                lineArr.add(new Line("(λx.* (* x x) x) (+ 2 1)",'b',"2/y"));
        lineArr.add(new Line("(λx.* (* x x) x) 3",'=',"3/(+ 2 1)"));
        lineArr.add(new Line("* (* 3 3) 3",'b',"3/x"));
        lineArr.add(new Line("* 9 3",'=',"9/(* 3 3)"));
        lineArr.add(new Line("27",'=',"27/(* 9 3)"));
        
        return new Solution(new Question(descrip,10,require,start),lineArr.toArray(new Line[1]));
    }
    
}
