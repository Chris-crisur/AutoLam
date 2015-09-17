/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automarker;

import java.util.ArrayList;
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
            case "beta":
                if(index.equals("alpha"))
                    break;
            case "b1":
                solutionList.add(betaTestOne());
                if(!index.equals("all")&&!index.equals("beta"))
                    break;
            case "eta":
                if(index.equals("beta"))
                    break;
            case "e1":
                solutionList.add(etaTestOne());
                if(!index.equals("all")&&!index.equals("eta"))
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
        String descrip = "A1:3";
        String require = "";
        String start = "(λx.λy. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv. x v)(λx.x y z)",'a',"y/v"));
        lineArr.add(new Line("λv.(λx.x y z) v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.v y z",'b',"v/x"));   //ETA disabled
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
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
        String descrip = "A2:1.5";  
        String require = "";
        String start = "(λx.λy. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λy. x y)(λx.x v z)",'a',"y/v")); //wrong - changed free var
        lineArr.add(new Line("λy.(λx.x v z) y",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λy.y v z",'b',"y/x"));  //wrong from expected result
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
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
        String descrip = "A3:1.5";  
        String require = "";
        String start = "(λx.λy. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv. x y)(λx.x y z)",'a',"y/v")); //wrong - changed only lambda part
        lineArr.add(new Line("λv.(λx.x y z) y",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.y y z",'b',"y/x"));  //wrong from expected result
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * Correct answer
     * 
     * @expected_output \
     */
    private static Solution alphaTestFour(){
        String descrip = "A4:3";
        String require = "";
        String start = "(λx.λy.x y y) (λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv. x v v)(λx.x y z)",'a',"y/v"));
        lineArr.add(new Line("λv.(λx.x y z) v v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.v y z v",'b',"v/x"));   //ETA disabled
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * Wrong answer
     * 
     * @expected_output \
     */
    private static Solution alphaTestFive(){
        String descrip = "A5:1.5";
        String require = "";
        String start = "(λx.λy.x y y) (λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv. x v y)(λx.x y z)",'a',"y/v"));    //wrong - changed only 1st bound var
        lineArr.add(new Line("λv.(λx.x y z) v y",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.v y z y",'b',"v/x"));   //ETA disabled
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * Wrong answer
     * 
     * @expected_output \
     */
    private static Solution alphaTestSix(){
        String descrip = "A6:1.5";
        String require = "";
        String start = "(λx.λy.x y y) (λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv. x y v)(λx.x y z)",'a',"y/v"));    //wrong - changed only 2nd bound var
        lineArr.add(new Line("λv.(λx.x y z) y v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.y y z v",'b',"y/x"));   //ETA disabled
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
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
        String descrip = "A7:1.33";
        String require = "";
        String start = "(λx.λy.λz. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv.λz. x v)(λx.x y z)",'a',"y/v"));   //needed to change both y and z
        lineArr.add(new Line("λv.λz.(λx.x y z) v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.λu.(λx.x y z) v",'a',"z/u"));//only changed 1 z, which is correct  overall but wrong in context
        lineArr.add(new Line("λv.λu.v y z",'b',"v/x"));  
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
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
        String descrip = "A8:4";
        String require = "";
        String start = "(λx.λy.λz. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv.λu. x v)(λx.x y z)",'a',"y/v,z/u"));
        lineArr.add(new Line("λv.λu.(λx.x y z) v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.λu.v y z",'b',"v/x")); 
        
        return new Solution(new Question(descrip,4,require,start),lineArr.toArray(new Line[1]));
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
        String descrip = "A9:1.33";
        String require = "";
        String start = "(λx.λy.λz. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv.λz. x v)(λx.x y z)",'a',"y/v"));   //didn't change z
        lineArr.add(new Line("λv.λz.(λx.x y z) v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.λu.(λx.x y u) v",'a',"z/u"));  //at this point, souldn't change z to u - redundant
        lineArr.add(new Line("λv.λu.v y u",'b',"v/x"));  
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
    }
    /***
     * two alpha conversions required
     * only one alpha conversion done
     * beta reductions done correctly, but final answer wrong
     * 
     * Wrong answer
     * 
     * @expected_output 
     */
    private static Solution alphaTest10(){
        String descrip = "A10:2";
        String require = "";
        String start = "(λx.λy.λz. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv.λz. x v)(λx.x y z)",'a',"y/v"));   //didn't change z
        lineArr.add(new Line("λv.λz.(λx.x y z) v",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("λv.λz.v y z",'b',"v/x"));  
        
        return new Solution(new Question(descrip,4,require,start),lineArr.toArray(new Line[1]));
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
        String descrip = "A11:6";
        String require = "";
        String start = "((λx.λy.λz. x y)(λx.x y) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λv.λz. x v)(λx.x y) λz.z)",'a',"y/v"));     
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λz.z)",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("(λz. (λx.x y) λz.z)",'b',"(λz.z)/v"));
        lineArr.add(new Line("(λz. (λx.x y) λu.u)",'a',"z/u"));
        lineArr.add(new Line("(λz. (λu.u) y)",'b',"(λu.u)/x")); 
        lineArr.add(new Line("λz. y",'b',"y/u"));
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
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
        String descrip = "A12:6";
        String require = "";
        String start = "((λx.λy.λz. x y)(λx.x y) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λv.λz. x v)(λx.x y) λz.z)",'a',"y/v"));     
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λz.z)",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λu.u)",'a',"z/u"));   //don't need to do at this point, but still correct
        lineArr.add(new Line("(λz. (λx.x y) λu.u)",'b',"(λu.u)/v")); 
        lineArr.add(new Line("(λz. (λu.u) y)",'b',"(λu.u)/x")); 
        lineArr.add(new Line("λz. y",'b',"y/u"));
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
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
        String descrip = "A13:6";
        String require = "";
        String start = "((λx.λy.λz. x y)(λx.x y) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λv.λz. x v)(λx.x y) λu.u)",'a',"y/v,z/u"));     
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λu.u)",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("(λz. (λx.x y) λu.u)",'b',"(λu.u)/v")); 
        lineArr.add(new Line("(λz. (λu.u) y)",'b',"(λu.u)/x")); 
        lineArr.add(new Line("λz. y",'b',"y/u"));
        
        return new Solution(new Question(descrip,6,require,start),lineArr.toArray(new Line[1]));
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
        String descrip = "A14:6";
        String require = "";
        String start = "((λx.λy.λz. x y)(λx.x y) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λv.λu. x v)(λx.x y) λz.z)",'a',"y/v,z/u"));     
        lineArr.add(new Line("((λv.λu. (λx.x y) v) λz.z)",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("(λu. (λx.x y) λz.z)",'b',"(λz.z)/v")); 
        lineArr.add(new Line("(λu. (λz.z) y)",'b',"(λz.z)/x")); 
        lineArr.add(new Line("λu. y",'b',"y/u"));
        
        return new Solution(new Question(descrip,6,require,start),lineArr.toArray(new Line[1]));
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
        String descrip = "A15:6";
        String require = "";
        String start = "((λx.λy.λz. x y)(λx.x y) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λv.λz. x v)(λx.x y) λz.z)",'a',"y/v"));     
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λz.z)",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("(λz. (λx.x y) λz.z)",'b',"(λz.z)/v"));
        lineArr.add(new Line("(λu. (λx.x y) λz.z)",'a',"z/u"));
        lineArr.add(new Line("(λu. (λz.z) y)",'b',"(λz.z)/x")); 
        lineArr.add(new Line("λu. y",'b',"y/u"));
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
    }
    
    /***
     * 
     * Two alpha reductions performed at start
     * 
     * Correct answer
     * 
     * ((λx.λy.λz. x y)(λx.x y z) λz.z)
        (λx.λi0.λi1.x i0) (λx.x y z) (λi0.i0)
       = (λi0.λi1.(λx.x y z) i0) (λi0.i0)
       = λi0.(λx.x y z) (λi1.i1)
       = λi0.(λi1.i1) y z
       = λi0.y z
     * 
     * @expected_output
     */
    private static Solution alphaTest16(){
        String descrip = "A16:6";
        String require = "";
        String start = "((λx.λy.λz. x y)(λx.x y) λz.z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("((λx.λv.λz. x v)(λx.x y) λz.z)",'a',"y/v"));     
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λz.z)",'b',"(λx.x y z)/x"));
        lineArr.add(new Line("((λv.λz. (λx.x y) v) λu.u)",'a',"z/u"));   //don't need to do at this point, but still correct
        lineArr.add(new Line("(λz. (λx.x y) λu.u)",'b',"(λu.u)/v")); 
        lineArr.add(new Line("(λz. (λu.u) y)",'b',"(λu.u)/x")); 
        lineArr.add(new Line("λz. y",'b',"y/u"));
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
    }
    
    //((λx.λy.λz. x y)(λx.x y) λz.z)
    //(λx.λy.λz. x y)(λx.x y) (λz.z) //equiv

    //((λx.λy.λz. x y)(λx.x y) λz.z x)
    
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
        
         
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
    }
    
    private static Solution betaTestOne(){
        String descrip = "B1:1";
        String require = "";
        String start = "(λx.λy. y y) y";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("λy.y y",'b',"y/x"));
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
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
        String descrip = "E1:3";
        String require = "";
        String start = "(λx.λy. x y)(λx.x y z)";
        List<Line> lineArr = new ArrayList<>();
        
        lineArr.add(new Line("(λx.λv. x v)(λx.x y z)",'a',"y/v"));
        lineArr.add(new Line("λv.(λx.x y z) v",'b',"(λx.x y z)/b"));
        lineArr.add(new Line("λx.x y z",'n',""));   //ETA enabled
        
        return new Solution(new Question(descrip,lineArr.size(),require,start),lineArr.toArray(new Line[1]));
    }
    
}
