/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automarker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Chris
 * 
 * parameterise ambiguity
 * 
 */
public class Automarker {
    private final boolean debug;
    private final Stream stream;
    private List<String> finalResultsList;
    private Student student;
    private BufferedWriter writer;
    
    public Automarker(String stdSolution){
        debug = true;
        stream = new Stream();
        student = null;

        Solution [] solutions = null;//loadFile(stdSolution);
        //solutions = markSolutions(solutions);
        
        //check tests
        performTests("t2");
        if (student!=null){
            student.setSolutions(solutions);
            createReport();
        }else{
            System.err.println("error with student creation");
        }
    }
    
    //returns the Student which has a solutions file contained within it's object
    public Student result() /**CHANGE*/
    {
        return student;
    }
    
    //method used for test cases
    private void performTests(String tests){
        Debug("TESTING");
        Solution [] solns = TestCases.loadTest(tests);
        if(tests.equals("performance")){
            long before = System.currentTimeMillis();
            solns = markSolutions(solns);
            long after = System.currentTimeMillis();
            System.out.println("time taken: " + (after-before)/1000.0);
        }else{
            solns = markSolutions(solns);
            boolean check=false;
            String q_description = "";
            for(Solution sol: solns){
                check = TestCases.checkAnswer(sol);
                q_description = sol.getQuestion().getDescription();
                Debug(q_description.substring(0,q_description.indexOf(":")) + ": " + check);
                //System.out.println("\n"+sol.toString());
            }
        }
    }
    
    //method for quick System.out.println() for objects only when debugging
    private void Debug(Object o){
        if(debug)
            System.out.println(o);
    }
    
    /***
     * loadFile reads in a student's file which contains their solutions and places these solutions in the
     * "solutions" global array
     * 
     * @param fName filename
     */
     
    private Solution [] loadFile(String fName){
        String line="", sol="", quest="",require="", start="", answer="", date=""; //Strings for solution
        int studIndex=-1, expressionIndex = 0, expressionEndIndex = 0, reasoningIndex = 0; //a few indices
        String reasoning = "",expression = ""; 
        char firstChar ='a';
        double mark = 0;
        int numS = 0;   //number of solutions
        List<Solution> solutionList = new ArrayList<>();
        List<Line> lineList = new ArrayList<>();
        boolean questionSection = false;
        
        try {
            File qFile = new File(fName);
            BufferedReader reader=new BufferedReader(new FileReader(qFile));
            while((line=reader.readLine())!=null){
                if (student == null){                                           //first line is student number and name
                    String[] id = line.split("#");                              //# separates student number and student name
                    student = new Student(id[0],id[1]);
                    continue;
                }
                
                if(line.equals("") || numS==0){                                 //blank line separating solutions
                    //Debug("NEW");
                    if(questionSection){
                        quest+="\n";
                        continue;
                    }
                    numS+=1;
                    if(numS>1){
                        solutionList.add(new Solution(new Question(quest,mark,require,start),lineList.toArray(new Line[1])));
                    }
                    mark=0;
                    require="";
                    start="";
                    lineList = new ArrayList<>();
                    continue;
                }
            	firstChar = line.charAt(0);
                //Question starts with '#', mark starts with '>', requirement starts with '!'
                
                if (!questionSection){                                          //read in solution specific info
                    if (firstChar =='S'){
                        sol = line;
                    }else if(firstChar=='D'){
                        date = line;
                    }else if(firstChar=='Q'){
                        questionSection = true;
                    }
                    else{
                        //Debug("line");
                        expressionIndex = line.indexOf("->") + 3;           //3 for "-> "
                        expressionEndIndex = line.indexOf("\t[");           //include "[" in case student used tab in answer
                        reasoningIndex = expressionEndIndex + 2;
                        reasoning = "";
                        expression = "";
                        if(expressionEndIndex<0){
                            expression = line.substring(expressionIndex);
                        }else{
                            expression = line.substring(expressionIndex,expressionEndIndex+1);
                            reasoning = line.substring(reasoningIndex); 
                        }
                        Line linetemp = new Line(expression,firstChar,reasoning);
                        //Debug(linetemp);
                        lineList.add(linetemp);
                    }
                }else{ //read in question info
                    if (firstChar=='#'){
                        quest = line.substring(1);
                    }else if(firstChar=='<'){
                        mark = Double.parseDouble(line.substring(1));
                    }else if(firstChar=='!'){
                        require = line.substring(1);
                    }else if(firstChar=='>'){
                        start = line.substring(1);
                    }else if(line.equals("A:")){
                        questionSection = false;
                    }else{
                        quest+="\n" + line;
                    }
                }
            }
            reader.close();
            solutionList.add(new Solution(new Question(quest,mark,require,start),lineList.toArray(new Line[1])));
            //questions loaded succesfully
 
        } catch (IOException e) {
            System.err.print("error reading file: " + e.toString());
        }
        //Debug(solutionList);
        return solutionList.toArray(new Solution[1]);
    }
    
    /***
     * Method which processes solutions array to assign marks to each line of the solutions
     * 
     * For each solution, assess if the reduction is correct
     * There are some common operations done regardless of reduction type
     * Then reduction-specific processing is done
     * If the reduction is correct, a mark is awarded
     * 
     * Then assess if the reasoning (between the "[ ]") is correct
     * Some common operations are performed
     * Then reduction-specific reasoning processing is performed
     * If the reasoning is correct, a mark is awarded.
     * Partial marks are possible where alpha reduction reasoning is wrongly 
     * generalised or where multiple alpha; so reasoning partially correct
     * could also happen when only one alpha conversion was done, but two
     * were required, then there may have been correct conversion of the one,
     * but overall wrong
     * 
     * Multiple alpha reductions can be performed in one step
     * A premature alpha reduction can be performed and marked correctly
     * An unnecessary alpha reduction is not marked, but not penalised by default
     * 
     * A false beta ('B') or eta ('n') reduction occurs a mark penalty - where the final mark is divided by the mark penalty (1 is start)
     * Multiple errors increases the mark penalty (increasing size of division)
     * 
     * Conversion ('>') is not awarded marks, but not marked incorrect if wrong
     * 
     * Arithmetic reduction ('=') is implemented
     * 
     * The automarker defaults to Normal order reduction, but a student can use Applicative order if Normal order was not required in the question
     * If Normal order or Applicative order were explicitly required, only the appropriate reductions are considered and lines marked accordingly.
     * 
     * 
     * @param solutions - array with the student's answers to questions
     * @return solutions - the array of solutions with each question and line marked
     */
    private Solution [] markSolutions(Solution [] solutions){
        
        Options.getEtaReductionsOption().setValue(false);   //eta reduction is off by default
        Options.getEvaluationOrderOption().setValue(2); //default evaluation is normal (without thunks)
        
        for (Solution sol: solutions){
            
            //common reduction steps
            String qStart = sol.getQuestion().getStart();
            String require = sol.getQuestion().getRequirements();
            if(require.equalsIgnoreCase("Applicative")){
                Options.getEvaluationOrderOption().setValue(0); //set evaulation to applicative
                Debug("Applicative");
            }else{
                Options.getEvaluationOrderOption().setValue(2); //back to default normal (without thunks)
                Debug("Normal");
            }
            Line [] lines = sol.getLines();
            
            Line prev = new Line(qStart,'s',"");
            List<Expr> originalResult = stream.runExpr(qStart); //reductions from question
            List<Expr> finalResults = new ArrayList<>();
            List<Expr> tempResults;
            Expr previousResult = originalResult.get(originalResult.size()-1);
            Expr tempResult;
            
            finalResultsList = new ArrayList<>();
            //due diligence process for accuracy of calculator
            //what can happen is the final answer can change depending on alpha 
            //conversion of calculator in terms of symbols in final result (semantically
            //the same) (see test cases for "(λx.x x) (λx.λy.x y)", "((λx.λy.λz. x y)(λx.x y z) λz.z)", and "(λx.λy. y y) y")
            for(Expr expr:originalResult){
                tempResults = stream.runExpr(expr.toString());
                tempResult = tempResults.get(tempResults.size()-1);
                finalResults.add(tempResult);
                finalResultsList.add(tempResult.toString());
                if(!previousResult.toString().equals(tempResult.toString())){
                    System.out.println(sol.getID()+"\tPOSSIBLE ERROR IN CALCULATOR");
                    System.out.println("list of possible results from " + qStart + " : " + finalResultsList);
                    //System.exit(0);
                }
                previousResult = tempResult;
            }
            Debug("originalResult: " + originalResult);
            Debug("finalResults: " + finalResults);
            
            List<Expr> currResult = originalResult;             //current set of reductions
            Expr prevReducedExpr = null;
            Expr currReducedExpr = null;
            Expr prevLastExpr;
            
            Expr currAlphaExpr = currResult.get(0);                     //calculator automatically alpha converts immediately before reducution
            Expr prevAlphaExpr = currAlphaExpr;
            Expr currLastExpr = currResult.get(currResult.size()-1);    //final reduced form
            String prevAlphaStr="";
            String currAlphaStr="";
            String trackAlphaChange="";
            
            boolean markAwarded=false;
            int markPenalty = 1; //divisor
            int numA = -1;
            int numAExpr = -1;
            for (Line curr: lines){
                markAwarded=false;
                Debug("\nLINE: " + curr);
                  
                if(currResult.size()==1 && !(curr.getReduction()=='>'||curr.getReduction()=='→')){
                    //check if really last by enabling eta
                    Debug("size == 1: " + currResult);
                    Options.getEtaReductionsOption().setValue(true);
                    Debug(Options.getEtaReductionsOption().getValue());

                    currResult = stream.runExpr(prev.getExpression()); 
                    Options.getEtaReductionsOption().setValue(false);
                    Debug("currResult: " + currResult);
                    
                    if(currResult.size()==1){
                        if((curr.getReduction()=='b'||curr.getReduction()=='B'||curr.getReduction()=='β')){
                            //may be the case that there is a redex in the middle of the expression that the calculator doesn't detect correctly
                            //detects by eleminating non-reducible symbols before said redex
                            Debug("currResult size still == 1: " + currResult);
                            if(prevReducedExpr==null){
                                prevReducedExpr = originalResult.get(0);
                                try {
                                    currReducedExpr = Parser.parse(curr.getExpression());
                                } catch (Parser.ParseException pe) {
                                    System.out.println(pe.getMessage());
                                    break;
                                }
                            }else{ 
                                prevReducedExpr = currResult.get(0); 
                            }
                            String [] symbols = prevReducedExpr.toString().split(" ");
                            String [] tmpArray;
                            Debug("prevReducedExpr: " + prevReducedExpr);
                            int prevResultsLen = stream.runExpr(prevReducedExpr.toString()).size();
                            boolean symBeforeLam = !symbols[0].contains("\\");
                            boolean symLamAfter = false;
                            int index = -1;
                            for(int i=1;i<symbols.length-1;i++){
                                if(symbols[i].contains("\\")){
                                    symLamAfter = true;
                                    index=i;
                                    break;
                                }
                            }

                            if(symBeforeLam && symLamAfter){
                                Debug("symBeforeLam && symLamAfter, index: " + index);
                                StringBuilder sbPrev = new StringBuilder();
                                StringBuilder sbCurr = new StringBuilder();
                                String [] newSymbols = currReducedExpr.toString().split(" ");
                                for(int i=index;i<symbols.length;i++){
                                    sbPrev.append(symbols[i]).append(" ");
                                }
                                for(int i=index;i<newSymbols.length;i++){
                                    sbCurr.append(newSymbols[i]).append(" ");
                                }
                                List<Expr> tempResultsPrev = stream.runExpr(sbPrev.toString());
                                if(tempResultsPrev.size()>prevResultsLen){
                                    List<Expr> tempResultsCurr = stream.runExpr(sbCurr.toString());
                                    Debug("tempResultsPrev: " + tempResultsPrev);
                                    Debug("tempResultsCurr: " + tempResultsCurr);
                                    if(tempResultsCurr.size()>1)
                                        markAwarded = doBetaReduction(tempResultsPrev.get(tempResultsPrev.size()-1), tempResultsCurr.get(tempResultsCurr.size()-1), tempResultsPrev.get(1),tempResultsCurr.get(1), curr);
                                    else
                                        markAwarded = doBetaReduction(tempResultsPrev.get(tempResultsPrev.size()-1), tempResultsCurr.get(tempResultsCurr.size()-1), tempResultsPrev.get(1),tempResultsCurr.get(0), curr);
                                }
                            }
                        }else{
                            //if really done, break
                            break;
                        }
                    }
                }
                
                prevAlphaExpr = currAlphaExpr;    //should be used to keep track of alpha reductions
                if(currResult.size()>1){
                    prevReducedExpr = currResult.get(1);    //first line of reduction
                }else if(prevReducedExpr == null){
                    prevReducedExpr = currResult.get(0);
                }
                prevLastExpr = currLastExpr;
                
                Debug("prev.getExpression: " + prev.getExpression());
                Debug("curr.getExpression: " + curr.getExpression());
                currResult = stream.runExpr(curr.getExpression());  //get new reductions based on current line
                Debug("currResult: " + currResult);
                try {
                    currReducedExpr = Parser.parse(curr.getExpression());
                } catch (Parser.ParseException pe) {
                    System.out.println(pe.getMessage());
                    break;
                }
                currAlphaExpr = currResult.get(0);
                currLastExpr = currResult.get(currResult.size()-1);
                Debug("prev last reduced form: " + prevLastExpr);
                Debug("current last reduced form: " + currLastExpr);
                
                /*** important to note statements ***/
                //prevLastExpr.equals(currLastExpr); //false
                //prevLastExpr == currLastExpr;     //false
                //prevLastExpr.toString().equals(currLastExpr.toString()); //true
                
                //reduction specific processing for reduction marks
                switch(curr.getReduction()){
                    case 'a':
                    case 'α':
                        /*** ALPHA REDUCTION ***/
                        /*
                        check number of symbols converted by the calculator in the previous and current lines (which is done automatically for conflicts)
                        for each character in the previous line parsed by the calculator, 
                        if it is an 'i' and the previous char is a lambda symbol (alpha conversion can only affect lambda symbols),
                        then check the character of the student's current line at the same relative point.
                        If the characters are different, means an alpha reduction has started to take place correctly
                        find bound variables (which have the same iX format as lambda symbol, and check if these were changed
                        in the current student line too.
                        If there was an alpha reduction performed at an earlier stage than strictly required (according to calculator),
                        then can check the total number of alpha converted symbols in all steps, and if the reduction was actually necessary.
                        A necessary reduction would decrease the number of symbols in the solution.
                        */
                        Debug("ALPHA");
                        prevAlphaStr = prevAlphaExpr.toString();
                        trackAlphaChange=prevAlphaStr;          //used to change symbols of prevAlphaStr according to what user performed
                        currAlphaStr = currAlphaExpr.toString();
                        Debug("previous alpha string: " + prevAlphaStr);
                        Debug("current alpha string:  " + currAlphaStr);

                        numA = countAlpha(prevAlphaStr);
                        numAExpr = countAlpha(currAlphaStr);
                        Debug("# alpha's in prev: " + (numA+1) + "\n# alpha's in curr: " + (numAExpr+1));
                        if(numAExpr<numA){ 
                            //there are less alpha reductions performed by calculator
                            //can award marks proportional to number of alpha reductions
                            if(numAExpr>-1){ //default is -1
                                //calculator changes ambiguous symbols automatically, so if there is still ambiguity, wrong
                                break;
                            }
                            int iOffset=0;  //if there are multiple 'i's, indices can shift between comparison lines
                            int alphaNum = -1;
                            boolean bound=false;
                            int numIs = 0;
                            int numIsTrack = 0;
                            
                            for(int i = 0;i<prevAlphaStr.length();i++){
                                char c = prevAlphaStr.charAt(i);
                                if(c=='i'){ 
                                    if(prevAlphaStr.charAt(i-1)=='\\' || prevAlphaStr.charAt(i-1)=='λ'){    //check affected a lambda symbol
                                        alphaNum = Character.getNumericValue(prevAlphaStr.charAt(i+1));
                                        numIs = countI(prevAlphaStr.substring(0,i));
                                        char c2 = currAlphaStr.charAt(i-numIs);                             //retrieve character of line at same index
                                        Debug("alphaNum: i" + alphaNum +
                                                "i: " + i +
                                                "iOffset: " + iOffset + 
                                                "c2: " + c2 + 
                                                "numIs: " + numIs + 
                                                "charAt(" + (i-numIs) + ")");
                                        if(c != c2){                                                        //if character changed, good!
                                            numIsTrack = countI(prevAlphaStr.substring(0,i-numIs)) - countI(trackAlphaChange.substring(0,i-numIs));
                                            trackAlphaChange = trackAlphaChange.substring(0,i-numIsTrack) + c2 + trackAlphaChange.substring(i-numIsTrack+2);
                                            Debug(" numIsTrack: " + numIsTrack + 
                                                    "\n prevAlphaStr: " + prevAlphaStr +
                                                    "\n trackAlphaChange: " + trackAlphaChange);
                                            if(c2 != prev.getExpression().charAt(i-iOffset)){               //check lambda bound symbol changed                                                
                                                int boundVarI = prevAlphaStr.indexOf("i"+alphaNum,i+1);     //find corresponding bound var
                                                if(boundVarI<0){
                                                    //no bound vars
                                                    Debug("no bound vars");
                                                    bound=true;
                                                }else{
                                                    //check all bound vars changed
                                                    while(boundVarI>0){
                                                        Debug("bound variable index: " + boundVarI);
                                                        if(prevAlphaStr.charAt(boundVarI-1) == '\\' || //calculator can start numbering again 
                                                                prevAlphaStr.charAt(boundVarI-1)=='λ'){//if no more related alpha conversions
                                                            Debug("lambda encountered");
                                                            break;
                                                        }
                                                        
                                                        numIs = countI(prevAlphaStr.substring(0,boundVarI));
                                                        char c3 = currAlphaStr.charAt(boundVarI-numIs);
                                                        Debug(" iOffset: " + iOffset + 
                                                                "\n numIs: " + numIs + 
                                                                "\n charAt(" + (boundVarI-numIs) + ")" + 
                                                                "\n c3: " + c3);
                                                        if(c3==c2){                                             //bound var also changed
                                                            Debug("bound var changed");
                                                            bound = true;
                                                            numIsTrack = countI(prevAlphaStr.substring(0,boundVarI)) - countI(trackAlphaChange.substring(0,boundVarI-numIs));
                                                            trackAlphaChange = trackAlphaChange.substring(0,boundVarI-numIsTrack) + c2 + trackAlphaChange.substring(boundVarI-numIsTrack+2);
                                                            Debug(prevAlphaStr.substring(0,boundVarI) + 
                                                                    "\n " + trackAlphaChange.substring(0,boundVarI-numIs) + 
                                                                    "\n " + countI(prevAlphaStr.substring(0,boundVarI-numIs)) + " - " +  countI(trackAlphaChange.substring(0,boundVarI-numIs)) + 
                                                                    "\n numIsTrack: " + numIsTrack + 
                                                                    "\n prevAlphaStr: " + prevAlphaStr + 
                                                                    "\n " + trackAlphaChange.substring(0,boundVarI-numIsTrack) + "\t[" + c2 + "]\t"+trackAlphaChange.substring(boundVarI-numIsTrack+2) + 
                                                                    "\n trackAlphaChange: " + trackAlphaChange);
                                                        }else{
                                                            Debug("bound var not changed");
                                                            bound = false;                                      //bound var not changed
                                                            break;
                                                        }
                                                        boundVarI = prevAlphaStr.indexOf("i"+String.valueOf(alphaNum),boundVarI+1); //find other bound var
                                                    }//end while
                                                }
                                                if(bound){
                                                    Debug("award mark");
                                                    curr.addMark(1);
                                                    markAwarded=true;
                                                    bound = false;
                                                }
                                            }
                                        }
                                        iOffset+=1;
                                    } 
                                }
                            }//end for loop
                            if(!trackAlphaChange.equals(currAlphaStr)){
                                //premature alpha reduction possible
                                //check total number of alpha conversions in all steps. If reduced, good
                                List<Expr> prevResult = stream.runExpr(prev.getExpression());
                                List<Expr> prevResultTrack = stream.runExpr(trackAlphaChange);
                                int numAlphasPrevResult = 0;
                                int numAlphasTrack = 0;
                                int numAlphasCurrResult = 0;
                                for(Expr expr: prevResult){
                                    numAlphasPrevResult += countAlpha(expr.toString());
                                }
                                for(Expr expr: prevResultTrack){
                                    numAlphasTrack += countAlpha(expr.toString());
                                }
                                for(Expr expr: currResult){
                                    numAlphasCurrResult += countAlpha(expr.toString());
                                }
                                Debug("prevResult: " + prevResult + 
                                        "\n prevResultTrack: " + prevResultTrack + 
                                        "\n currResult: " + currResult + 
                                        "\n numAlphasPrevResult: " + numAlphasPrevResult + 
                                        "\n numAlphasTrack: " + numAlphasTrack + 
                                        "\n numAlphasCurrResult: " + numAlphasCurrResult);

                                if(numAlphasCurrResult<numAlphasTrack && numAlphasTrack<numAlphasPrevResult){
                                    //the user alphas were required at some point anyway, mark awarded
                                    Debug("award mark");
                                    curr.addMark(1);
                                    markAwarded=true;
                                }
                            }
                        }else{
                            //premature alpha reduction possible
                            //check total number of alpha conversions in all steps. If reduced, good
                            List<Expr> prevResult = stream.runExpr(prev.getExpression());
                            int numAlphasPrevResult = 0;
                            int numAlphasCurrResult = 0;
                            for(Expr expr: prevResult){
                                numAlphasPrevResult += countAlpha(expr.toString());
                            }
                            for(Expr expr: currResult){
                                numAlphasCurrResult += countAlpha(expr.toString());
                            }
                            Debug("prevResult: " + prevResult + 
                                        "\n currResult: " + currResult + 
                                        "\n numAlphasPrevResult: " + numAlphasPrevResult + 
                                        "\n numAlphasCurrResult: " + numAlphasCurrResult);
                            
                            if(numAlphasCurrResult<numAlphasPrevResult){
                                Debug("award mark");
                                curr.addMark(1);
                                markAwarded=true;
                            }else if(numAlphasCurrResult==numAlphasPrevResult){
                                Debug("mark not awarded, but not penalised");
                                markAwarded=true;
                            }
                            
                        }
                        break;
                    case 'b':
                    case 'B':
                    case 'β':
                        /*** BETA REDUCTIONS ***/
                        /*
                        check beta reduction using doBetaReduction method
                        may be alpha conversions still required in future but not performed yet by student,
                        if so, change prevReducedExpr and prevLastExpr to have these alpha values
                        */
                        Debug("BETA");
                        if(!markAwarded) //see the situation where currResult.size()==1
                            markAwarded = doBetaReduction(prevLastExpr, currLastExpr, prevReducedExpr, currReducedExpr, curr);
                        
                        
                        if(markAwarded){
                            Debug("markAwarded, prevAlphaStr before: " + prevAlphaStr);
                            prevAlphaStr = prevReducedExpr.toString();
                            Debug("markAwarded, prevAlphaStr after: " + prevAlphaStr);
                        }
                        else{ 
                            prevAlphaStr = prevReducedExpr.toString();
                            prevAlphaExpr = prevReducedExpr;    //prevReducedExpr will change below, keep Alpha state(s)
                            currAlphaStr = currAlphaExpr.toString();
                            Debug("previous alpha string: " + prevAlphaStr);
                            Debug("current alpha string: " + currAlphaStr);
                            numA = countAlpha(prevAlphaStr);
                            numAExpr = countAlpha(currAlphaStr);
                            Debug("# alpha's in prev: " + (numA+1) + "\n# alpha's in curr: " + (numAExpr+1));
                            if(numAExpr<numA){
                               int [] aIndex = new int[numA+1];
                               int [] aIndexLast = new int[numA+1];
                               Debug("alpha indices:");
                               for(int i=0;i<aIndex.length;i++){
                                   aIndex[i] = prevReducedExpr.toString().indexOf("i"+i);
                                   aIndexLast[i] = prevLastExpr.toString().indexOf("i"+i);
                                   Debug(aIndex[i]);
                                   try {
                                        prevReducedExpr = Parser.parse(
                                                prevReducedExpr.toString().replace(
                                                                    "i"+i,
                                                        String.valueOf(currReducedExpr.toString().charAt(aIndex[i]))
                                                ));
                                        if(aIndexLast[i]>0){
                                            prevLastExpr = Parser.parse(
                                                    prevLastExpr.toString().replace( 
                                                                        "i"+i,
                                                            String.valueOf(currLastExpr.toString().charAt(aIndexLast[i]))
                                                    ));
                                        }
                                    } catch (Parser.ParseException pe) {
                                        System.err.println(pe.getMessage());
                                        break;
                                    }
                                   
                               }
                               Debug(" prevReducedExpr: " + prevReducedExpr + "\ncurrReducedExpr: " + currReducedExpr + 
                                        "\n prevLastExpr: " + prevLastExpr + "\ncurrLastExpr" + currLastExpr); 
                               
                               markAwarded = doBetaReduction(prevLastExpr, currLastExpr, prevReducedExpr, currReducedExpr, curr);
                               
                            }
                        }
                        
                        if(!markAwarded && require.equals("")){
                            //try applicative (default normal)
                            //don't try applicative if require is Normal (or anything else)
                            Debug("mark not awarded and require empty");
                            Options.getEvaluationOrderOption().setValue(0);
                            currResult = stream.runExpr(curr.getExpression());  //get new reductions based on current line
                            Debug("currResult: " + currResult);
                            try {
                                currReducedExpr = Parser.parse(curr.getExpression());
                            } catch (Parser.ParseException pe) {
                                System.out.println(pe.getMessage());
                                break;
                            }
                            prevReducedExpr = currResult.get(0);
                            currLastExpr = currResult.get(currResult.size()-1);
                            markAwarded = doBetaReduction(prevLastExpr, currLastExpr, prevReducedExpr, currReducedExpr, curr);
                            Options.getEvaluationOrderOption().setValue(2);
                        }
                        
                        
                        break;    
                    case 'n':
                    case 'η':
                        /*** ETA REDUCTIONS ***/
                        /*
                        if an eta reduction was performed, then the initial currResult performed by default before the switch
                        statement needs to be re-calculated with eta reduction enabled. 
                        Eta reductions happen relatively infrequently, so not too expensive to re-compute.
                        Can then check correctness using beta reduction method, but with eta results instead.
                        */
                        Debug("ETA");
                        Options.getEtaReductionsOption().setValue(true);
                        Debug(Options.getEtaReductionsOption().getValue());
                        currResult = stream.runExpr(prev.getExpression()); 
                        Options.getEtaReductionsOption().setValue(false);
                        Debug("currResult: " + currResult);

                        prevReducedExpr = currResult.get(1);
                        prevLastExpr = currResult.get(currResult.size()-1);
                        Debug("prev last reduced form: " + prevLastExpr);
                        Debug("current last reduced form: " + currLastExpr);

                        markAwarded = doBetaReduction(prevLastExpr, currLastExpr, prevReducedExpr, currReducedExpr, curr);

                        if(currResult.size()>2)
                            currResult.set(1,currResult.get(2)); //move an element up the list to compensate for the prev expression used to recalculate currResult
                        
                        break;
                    case '>':
                    case '→':
                    case '=':
                        /*** CONVERSION or ARITHMETIC ***/
                        /*
                        "divergent" should be stated using conversion
                        if a student has a line the same as the previous, perhaps due to duplication
                        or changing of brackets. As long as semantics of expression is the same, 
                        don't penalise but no marks are awarded
                        */
                        Debug("CONVERSION");
                        Debug("prevReducedExpr: " + prevReducedExpr);
                        Debug("currReducedExpr: " + currReducedExpr);
                        if(curr.getExpression().equalsIgnoreCase("divergent")){
                            String lastOriginal = originalResult.get(originalResult.size()-1).toString();
                            String secondLastOriginal = originalResult.get(originalResult.size()-2).toString();
                            if(lastOriginal.equals(secondLastOriginal)){
                                Debug("award mark");
                                curr.setMark(1);
                                markAwarded=true;
                            }
                        }
                        //user may have changed brackets, as long as still same semantic expression, fine
                        else if(prevReducedExpr.toString().equals(currReducedExpr.toString()) ){ 
                            markAwarded=true;
                            if(curr.getReduction()=='='){
                                curr.setMark(1);
                            }else{
                                //mark isn't actually awarded, just not penalised
                                Debug("no mark penalty");
                            }
                        }
                        else{
                            //mark isn't actually awarded, just not penalised
                            markAwarded = true;
                        }
                        break;
                    default:
                        break;
                }
                
                /*** REASONING ***/
                //common processing for reasoning
                Debug("-------REASONING-------");
                //check change of symbols correct
                String reasoning = curr.getReasoning();
                int splitIndex = reasoning.indexOf("/");
                if(splitIndex<0){
                    if(curr.getReduction()=='n'||curr.getReduction()=='η'){
                        Debug("award mark for reasoning");
                        curr.addMark(1);
                    }else{
                        System.out.println("no split in reasoning");
                    }
                }else{
                    String replacement = reasoning.substring(0,splitIndex);
                    String target = reasoning.substring(splitIndex+1);
                    String prevStr = "";//prev.getExpression();
                    try {
                        prevStr = Parser.parse(prev.getExpression()).toString();
                    } catch (Parser.ParseException pe) {
                        System.out.println(pe.getMessage());
                        break;
                    }
                    String currStr = currReducedExpr.toString();
                    Debug("prevStr: " + prevStr);
                    Debug("currStr: " + currStr);
                    String newStr = "";
                    Debug("target: " + target);
                    Debug("replacement: " + replacement);
                    
                    //reduction specific processing for reasoning marks
                    switch(curr.getReduction()){
                        case 'a':
                        case 'α':
                        case '>':
                        case '→':
                        case '=':   
                            if(curr.getReduction()=='a'||curr.getReduction()=='α')
                                Debug("ALPHA");
                            else
                                Debug("CONVERSION");
                            Debug("prevStr: " + prevStr);
                            //there can be multiple alpha conversions done in a step, separated by a comma (',')
                            //alpha conversions are described similarly to beta reductions: 
                            //g/h means g replaces h, where h is a lambda expression
                            String [] alphaChanges = reasoning.split(",");
                            String [] targets = new String[alphaChanges.length];
                            String [] replacements = new String[alphaChanges.length];
                            for(int i=0;i<alphaChanges.length;i++){
                                splitIndex = alphaChanges[i].indexOf("/");
                                if(splitIndex<0){
                                    System.out.println("no split in reasoning");
                                }else{
                                    replacements[i] = alphaChanges[i].substring(0,splitIndex);
                                    targets[i] = alphaChanges[i].substring(splitIndex+1);
                                }
                            }
                            //the calculator automatically alpha-converts
                            //but separate expressions may have the same iX value
                            //changing these to a unique number helps with application
                            //of reasoning marking
                            int countUniqueAlphas = countAlpha(prevStr);
                            String compareStr = currStr;
                            Debug("countUniqueAlphas+1: " + (countUniqueAlphas+1) + " targets.length: " + targets.length);
                            Debug("currStr: " + currStr);
                            for(int i=0;i<alphaChanges.length;i++){
                                //for each unique iX
                                compareStr = compareStr.replace(replacements[i],targets[i]);
                                Debug("replacements[i]: " + replacements[i] + " targets[i]: " + targets[i]);
                            }
                            
                            //count # in prev.getExpression and compareStr
                            //compareStr is currStr with each replacement replaced by it's corresponding target
                            int countTargetBefore = 0;
                            int countTargetAfter = 0;
                            int targetIndex = 0;
                            boolean [] replacementCorrect = new boolean[alphaChanges.length];
                            String prevExprStr = prev.getExpression();
                            
                            Debug("prevExprStr: " + prevExprStr);
                            Debug("compareStr: " + compareStr);
                            for(int i=0;i<alphaChanges.length;i++){
                                countTargetBefore = 0;
                                countTargetAfter = 0;
                                targetIndex = 0;
                                Debug("targets[i]: " + targets[i]);
                                
                                while(targetIndex>-1){
                                    targetIndex = prevExprStr.indexOf(targets[i],targetIndex+1);
                                    if(targetIndex>-1)
                                        countTargetBefore += 1;
                                    else
                                        break;
                                }
                                targetIndex = 0;
                                
                                while(targetIndex>-1){
                                    targetIndex = compareStr.indexOf(targets[i],targetIndex+1);
                                    if(targetIndex>-1)
                                        countTargetAfter += 1;
                                    else
                                        break;
                                }
                                Debug("countTargetBefore: " + countTargetBefore);
                                Debug("countTargetAfter: " + countTargetAfter);
                                if(countTargetBefore==countTargetAfter){
                                    //if currStr correctly converted back to prev.getExpression() (from compareStr to prevExprStr)
                                    //using replace method then number of targets will be the same
                                    replacementCorrect[i] = true;
                                    Debug("replacementCorrect: " + replacementCorrect[i]);
                                    Debug("award half mark for reasoning (correctly replaced)");
                                    if(curr.getReduction()=='>'||curr.getReduction()=='→'){
                                        Debug("CONVERSION correct");//no marks for conversion
                                    }else if(curr.getReduction()=='='){
                                        curr.addMark(0.5);
                                        Debug("EQUALS correct");
                                    }else
                                        curr.addMark(0.5);
                                }
                            }
                            
                            Debug("compareStr before parse: " + compareStr);
                            try {
                                compareStr = Parser.parse(compareStr).toString();
                            } catch (Parser.ParseException pe) {
                                System.out.println(pe.getMessage());
                            }
                            
                            //parsing automatically does alpha conversion where necessary
                            //thus compareStr will be the same after parsing as prevStr
                            //if alpha converted correctly
                            Debug("compareStr: " + compareStr);
                            
                            if(compareStr.equals(prevStr)){
                                Debug("award half mark(s) for reasoning (compareStr.equals(prevStr)): ");
                                Debug("alphaChanges.length/2: " + alphaChanges.length/2.0);
                                if(curr.getReduction()=='>'||curr.getReduction()=='→'){
                                    Debug("CONVERSION correct");//no marks for conversion
                                }else if(curr.getReduction()=='='){
                                    curr.addMark(alphaChanges.length/2.0);
                                    Debug("EQUALS correct");
                                }
                                else{
                                    curr.addMark(alphaChanges.length/2.0);
                                }
                            }else if(curr.getReduction()=='>'||curr.getReduction()=='→'){
                                    //could be converting, in which case should equal currStr not prevStr
                                    if(compareStr.equals(currStr)){
                                        Debug("CONVERSION correct");//no marks for conversion
                                    }
                            }
                            
                            if(curr.getMark()==1&&curr.getReduction()=='='){
                                //mark not awarded for reasoning, but still correct line, have an extra check just for '=' (arithmetic)
                                int tarInd = prevStr.indexOf(target);
                                compareStr = prevStr.substring(0,tarInd) + replacement + prevStr.substring(tarInd+target.length());
                                Debug("last chance for compareStr: " + compareStr);
                                if(compareStr.equals(currStr)){
                                    curr.addMark(1);
                                    Debug("EQUALS correct");
                                }
                            }
                            
                            break;
                        case 'b':
                        case 'B':
                        case 'β':
                            Debug("BETA");
                            if(replacement.contains("\\")||replacement.contains("λ")){
                                if(replacement.charAt(0)!='('||replacement.charAt(replacement.length()-1)!=')'){
                                    Debug("lambda expression, but not with parentheses");
                                    replacement = "(" + replacement + ")";//add brackets
                                    //break;
                                }
                            }
                            int pNum = 1;
                            int indexTarget = prevStr.indexOf("\\"+target);//find bound var if Normal
                            if(require.equals("Applicative")){
                                //indexTarget = prevStr.lastIndexOf("\\"+target);
                                //works better without
                            }
                            int numIReplace = 1;
                            String tempPrevStr = prevStr;
                            if(indexTarget<0&&tempPrevStr.contains("i0")){
                                //if target is the same as replacement, calculator may have alpha converted (unnecssarily perhaps)
                                tempPrevStr = tempPrevStr.replace("i0",target);
                                indexTarget = tempPrevStr.indexOf("\\"+target);//find bound var if Normal
                                while(tempPrevStr.contains("i"+numIReplace)){
                                    tempPrevStr=tempPrevStr.replace("i"+numIReplace,"i"+(numIReplace-1));
                                    numIReplace+=1;
                                }
                            }
                            Debug("prevStr: " + prevStr);
                            Debug("tempPrevStr: " + tempPrevStr);
                            prevStr = tempPrevStr;
                            int initialIndex = indexTarget;
                            Debug("indexTarget: " + indexTarget);
                            char cP = '?';
                            if(indexTarget>=0){
                                int p=indexTarget;
                                cP = prevStr.charAt(p-1);
                                if(cP=='('){
                                    newStr = prevStr.substring(0,indexTarget-1);
                                }else{
                                    newStr = prevStr.substring(0,indexTarget);
                                }
                                p=p+target.length()+2; //move to past '.' of lambda expression
                                Debug("newStr from prevStr: " + newStr);
                                Debug("p: " + p + " prevStr.length(): " + prevStr.length());
                                while(pNum>0){
                                    cP = prevStr.charAt(p);
                                    //Debug("cP: "+cP);
                                    if(prevStr.length()>=p+target.length()&&prevStr.substring(p,p+target.length()).equals(target)){
                                        newStr=newStr+replacement;    
                                    }else if(newStr.equals(currStr)){
                                        break;
                                    }else if(cP == '('){
                                        pNum+=1;
                                        newStr=newStr+cP;
                                    }else if(cP == ')'){
                                        pNum-=1;
                                        if(pNum<1)
                                            break;
                                        newStr=newStr+cP;
                                    }else{
                                        newStr=newStr+cP;
                                    }
                                    p+=1;
                             //       Debug(newStr);
                                }
                                Debug("newStr: " + newStr);
                                
                                if(currStr.charAt(0)=='(')
                                    newStr = "(" + newStr + ")";//add parentheses for separation and maintenance of logic
                                
                                //based on number of parenthese, may need to include more at end of newStr before adding remaining
                                int cntP = 0;
                                for(int i=0;i<newStr.length();i++){
                                    if(newStr.charAt(i)=='(')
                                        cntP+=1;
                                    else if(newStr.charAt(i)==')')
                                        cntP-=1;
                                }
                                for(int i=1;i<cntP;i++)
                                    newStr= newStr+")";
                                
                                Debug(prevStr.length() + " >? " + (p+3+replacement.length()));
                                if(prevStr.length()>(p+3+replacement.length())){
                                    newStr = newStr + " " + prevStr.substring(p+3+replacement.length());//3: 1 for parenthesis, 1 for space, 1 for last p not added
                                }
                            }
                            
                            Debug("newStr: " + newStr + " length: " + newStr.length());
                            String newStrParsed = null;
                            while(newStrParsed==null&&newStr.length()>1){
                                try {
                                    newStrParsed = Parser.parse(newStr).toString();
                                } catch (Parser.ParseException pe) {
                                    Debug("ParseException: " + pe.getMessage());
                                    if(pe.getMessage().equals("Right parenthesis missing")){
                                        //
                                        if(tempPrevStr.charAt(tempPrevStr.length()-1) == ')')
                                            newStr = newStr+")";
                                        else 
                                            newStr = newStr.substring(0,initialIndex-2) + newStr.substring(initialIndex-1);
                                            //for that rare occasion where this is better based on end of String before processing
                                            //occurs in: 
                                            //a b ((λc.c) d) e
                                            //  = a b d e
                                            //but automarker here detects as 
                                            //    a b (d e),  
                                        Debug("newStr: " + newStr);
                                    }else if(newStr.charAt(newStr.length()-1)==')'){
                                        newStr = newStr.substring(0,newStr.length()-2);
                                    }
                                    else{
                                        newStrParsed="";
                                    }
                                }
                            }

                            Debug("newStrParsed: " + newStrParsed);
                            if(newStr.equals(currStr)||(newStrParsed!=null && newStrParsed.equals(currStr))){
                                Debug("award mark for reasoning");
                                curr.addMark(1);
                            }
                            break;    
                        case 'n':
                        case 'η':
                            Debug("ETA");
                            Debug("award mark for reasoning");
                            curr.addMark(1);
                            break;
                        //case '>':
                        //case '→':
                        //    Debug("CONVERSION");

                        //    break;
                        default:
                            break;
                    }
                }    
                if(!markAwarded){ 
                        //&& (curr.getReduction()=='B'||curr.getReduction()=='b'||curr.getReduction()=='β')){
                    markPenalty+=1;
                }
                Debug("line mark: " + curr.getMark());
                prev = curr;
                
            }
            sol.setMark(markPenalty);
            
            Debug("------------------------------\n" + sol + "\n\n---------------------------------------------------------------------------");
        }
        return solutions;
    }
    
    /***
     * countAlpha takes a string and counts the number of unique iX's where X = {INTEGER}.
     * It is important for an 'i' to be followed by a number (as the calculator does when alpha reducing/converting).
     * Thus no 'i' should be used in a typical student's answer.
     * 
     * @param alpha
     * @return number of unique 'i's
     */
    public int countAlpha(String alpha){
        int countI = -1;
        int iNum=-1;
        for(int i = 0;i<alpha.length();i++){
            char c = alpha.charAt(i);
            if(c=='i'){
                iNum = Character.getNumericValue(alpha.charAt(i+1));
                if(iNum>countI){
                    countI+=1;
                }
            }
        }
        return countI;
    }
    
    /***
     * countI takes a string and counts the total number of iX's where X = {INTEGER}.
     * This includes bound variables (as opposed to countAlpha)
     * 
     * @param alpha
     * @return 
     */
    public int countI(String alpha){
        int counter = 0;
        for( int i=0; i<alpha.length(); i++ ) {
            if(alpha.charAt(i) == 'i' ) {
                counter++;
            } 
        }
        return counter;
    }
    /***
     * doBetaReduction compares prevLastExpr to currLastExpr, then prevReducedExpr to currReducedExpr. If they are equal, a mark is assigned to curr
     * the last expressions are compared to ensure both expressions distill to the same final form (semantically the same)
     * the reduced expression from the previous calculation is compared to the reduced expression of the student's current line
     * 
     * @param prevLastExpr
     * @param currLastExpr
     * @param prevReducedExpr
     * @param currReducedExpr
     * @param curr
     * @return true if the mark was awarded, false otherwise
     */
    private boolean doBetaReduction(Expr prevLastExpr, Expr currLastExpr, Expr prevReducedExpr, Expr currReducedExpr, Line curr){
        if(prevLastExpr.toString().equals(currLastExpr.toString())){
            //reduced to same form
            Debug("equal");
            Debug("prev reduced form: " + prevReducedExpr);
            Debug("curr reduced form: " + currReducedExpr);

            if(currReducedExpr!=null && prevReducedExpr.toString().equals(currReducedExpr.toString())){
                //same next line
                Debug("award mark");
                curr.setMark(1);
                return true;

            }
        }else if(finalResultsList.contains(currLastExpr.toString())){
            //finalResultsList contain the final exprs derived from the original question at each step of the reduction
            //thus, if there was a possible problem with the question in the calculator, this can help address it
            Debug("finalResultsList.contains(currReducedExpr.toString())");
            //reduced to same form
            Debug("equal");
            Debug("prev reduced form: " + prevReducedExpr);
            Debug("curr reduced form: " + currReducedExpr);

            if(currReducedExpr!=null && prevReducedExpr.toString().equals(currReducedExpr.toString())){
                //same next line
                Debug("award mark");
                curr.setMark(1);
                return true;

            }
        }
        return false;
    }
    
    private void createReport()
    {
            try{
                writer = new BufferedWriter(new FileWriter(new File("Report"+student.getStudentNum()+".txt"), false));	//new writer
                writer.write(student.getSummaryReport().toString());
                writer.close();
            } catch (Exception ex) {
            System.err.print("ReadError: " + ex.toString());
            } 
    }
        
}
