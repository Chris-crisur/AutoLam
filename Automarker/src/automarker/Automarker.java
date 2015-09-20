/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automarker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
    private boolean debug = true;
    private Solution [] solutions;
    private List<String> finalResultsList;
    
    Automarker(){
        //TODO: sign in
        //loadFiles();
        solutions = TestCases.loadTest("conversion");
        
        markSolutions();
        
        //check tests
        Debug("TESTING");
        boolean check=false;
        String q_description = "";
        for(Solution sol: solutions){
            check = TestCases.checkAnswer(sol);
            q_description = sol.getQuestion().getDescription();
            Debug(q_description.substring(0,q_description.indexOf(":")) + ": " + check);
            //System.out.println("\n"+sol.toString());
        }
        
    }
    
    private void loadFiles(){
        
        boolean sLoaded = false;
        //read solutions from txt file
        //select a file to be read
        String studnum=null, line="", sol="", quest="",require="", start="", answer="", date="";
        char firstChar ='a';
        double mark = 0;
        int numS = 0;
        List<Solution> solutionList = new ArrayList<>();
        List<Line> lineList = new ArrayList<>();
        boolean questionSection = false;
        try {
            File qFile = new File("tests" + File.separator + "solutionsPractise.txt");
    		
            BufferedReader reader=new BufferedReader(new FileReader(qFile));
            while((line=reader.readLine())!=null){
                if(studnum==null){
                    studnum = line;
                    continue;
                }
                if(line.equals("") || numS==0){//blank line separating solutions
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
                
                if (!questionSection){ //read in solution specific info
                    if (firstChar =='S'){
                        sol = line;
                    }else if(firstChar=='D'){
                        date = line;
                    }else if(firstChar=='Q'){
                        questionSection = true;
                    }
                    else{
                        //Debug("line");
                        int expressionIndex = line.indexOf("->") + 3; //3 for "-> "
                        int expressionEndIndex = line.indexOf("\t["); //include "[" in case student used tab in answer
                        int reasoningIndex = expressionEndIndex + 2;
                        String reasoning = "";
                        String expression = "";
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
            sLoaded = true; //questions loaded succesfully
 
        } catch (IOException e) {
        	//welcomeArea.setText("Error occured while loading the file:"+e.toString());
            System.err.print("error reading file: " + e.toString());
        }
        //Debug(solutionList);
        solutions = solutionList.toArray(new Solution[1]);
    }
    
    private void Debug(Object o){
        if(debug)
            System.out.println(o);
    }
    
    public void markSolutions(){
        Stream stream = new Stream();
        
        Options.getEtaReductionsOption().setValue(false);
        Options.getEvaluationOrderOption().setValue(2); //default evaluation is normal (without thunks)
        
        for (Solution sol: solutions){
            String qStart = sol.getQuestion().getStart();
            String qStartChanged = qStart;      //used for alpha conversion changes
            String require = sol.getQuestion().getRequirements();
            if(require.equalsIgnoreCase("Applicative")){
                Options.getEvaluationOrderOption().setValue(0); //set evaulation to applicative
                Debug("Applicative");
            }else{
                Options.getEvaluationOrderOption().setValue(2); //back to default normal (without thunks)
                Debug("Normal");
            }
            Line [] lines = sol.getLines();
            try {
                 Expr expr = Parser.parse(qStart);
                 Debug("parsed");
                 Debug(qStart);
                } catch (Parser.ParseException var9_9) {
                    System.out.println(var9_9.getMessage());
            }
            
            Line prev = new Line(qStart,'s',"");
            List<Expr> originalResult = stream.runExpr(qStart); //reductions from question
            List<Expr> finalResults = new ArrayList<>();
            List<Expr> tempResults;
            Expr previousResult = originalResult.get(originalResult.size()-1);
            Expr tempResult;
            
            finalResultsList = new ArrayList<>();
            //due diligence process for accuracy of caculator
            for(Expr expr:originalResult){
                tempResults = stream.runExpr(expr.toString());
                tempResult = tempResults.get(tempResults.size()-1);
                finalResults.add(tempResult);
                finalResultsList.add(tempResult.toString());
                if(!previousResult.toString().equals(tempResult.toString())){
                    System.out.println(sol.getID()+"\tPOSSIBLE ERROR IN CALCULATOR<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
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
            //new String[] { "α", "β", "η", "→" }
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
                    //if really done, break
                    if(currResult.size()==1){
                        break;
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
                    //jTextArea.setText(pe.getMessage());
                }
                currAlphaExpr = currResult.get(0);
                currLastExpr = currResult.get(currResult.size()-1);
                Debug("prev last reduced form: " + prevLastExpr);
                Debug("current last reduced form: " + currLastExpr);
                
                /*** important to note statements ***/
                //prevLastExpr.equals(currLastExpr); //false
                //prevLastExpr == currLastExpr;     //false
                //prevLastExpr.toString().equals(currLastExpr.toString()); //true
                
                //new String[] {"β", "α", "η", "→" }
                switch(curr.getReduction()){
                    case 'a':
                    case 'α':
                        /*** ALPHA REDUCTION ***/
                        Debug("ALPHA");
                        prevAlphaStr = prevAlphaExpr.toString();
                        trackAlphaChange=prevAlphaStr;
                        currAlphaStr = currAlphaExpr.toString();
                        Debug("previous alpha string: " + prevAlphaStr);
                        Debug("current alpha string:  " + currAlphaStr);

                        numA = countAlpha(prevAlphaStr);
                        numAExpr = countAlpha(currAlphaStr);
                        Debug("# alpha's in prev: " + (numA+1) + "\n# alpha's in curr: " + (numAExpr+1));
                        if(numAExpr<numA){ //there are less alpha reductions performed by calculator
                            //can award marks proportional to number of alpha reductions
                            if(numAExpr>-1){ //default is -1
                                //calculator changes ambiguous symbols automatically, so if there is still ambiguity, wrong
                                break;
                            }
                            int indexI = -1;
                            int iOffset=0;  //if there are multiple 'i's, indices can shift between comparison lines
                            int alphaNum = -1;
                            boolean bound=false;
                            int numIs = 0;
                            int numIsCurrent = 0;
                            int numIsTrack = 0;
                            
                            for(int i = 0;i<prevAlphaStr.length();i++){
                                char c = prevAlphaStr.charAt(i);
                                if(c=='i'){ 
                                    if(prevAlphaStr.charAt(i-1)=='\\' || prevAlphaStr.charAt(i-1)=='λ'){    //check affected a lambda symbol
                                        indexI = i;                                                         //get index of alpha-converted character
                                        alphaNum = Character.getNumericValue(prevAlphaStr.charAt(i+1));
                                        Debug("alphaNum: i" + alphaNum);
                                        Debug("i: " + i);
                                        Debug("iOffset: " + iOffset);
                                        numIs = countI(prevAlphaStr.substring(0,i));
                                        char c2 = currAlphaStr.charAt(i-numIs);                           //retrieve character of line at same index
                                        Debug("c2: " + c2);
                                        Debug("numIs: " + numIs);
                                        Debug("charAt(" + (i-numIs) + ")");
                                        if(c != c2){                                                        //if character changed, good!
                                            numIsTrack = countI(prevAlphaStr.substring(0,i-numIs)) - countI(trackAlphaChange.substring(0,i-numIs));
                                            Debug("numIsTrack: " + numIsTrack);
                                            trackAlphaChange = trackAlphaChange.substring(0,i-numIsTrack) + c2 + trackAlphaChange.substring(i-numIsTrack+2);
                                            Debug("prevAlphaStr: " + prevAlphaStr);
                                            Debug("trackAlphaChange: " + trackAlphaChange);
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
                                                        Debug("iOffset: " + iOffset);
                                                        numIs = countI(prevAlphaStr.substring(0,boundVarI));
                                                        Debug("numIs: " + numIs);
                                                        //numIsCurrent = countI(currAlphaStr.substring(i-numIs,boundVarI-numIs-iOffset-1));
                                                        //Debug("numIsCurrent: " + numIsCurrent);
                                                        Debug("charAt(" + (boundVarI-numIs) + ")");
                                                        
                                                        char c3 = currAlphaStr.charAt(boundVarI-numIs);
                                                        
                                                        Debug("c3: " + c3);
                                                        if(c3==c2){                                             //bound var also changed
                                                            Debug("bound var changed");
                                                            bound = true;
                                                            numIsTrack = countI(prevAlphaStr.substring(0,boundVarI)) - countI(trackAlphaChange.substring(0,boundVarI-numIs));
                                                            Debug(prevAlphaStr.substring(0,boundVarI));
                                                            Debug(trackAlphaChange.substring(0,boundVarI-numIs));
                                                            Debug(countI(prevAlphaStr.substring(0,boundVarI-numIs)) + " - " +  countI(trackAlphaChange.substring(0,boundVarI-numIs)));
                                                            Debug("numIsTrack: " + numIsTrack);
                                                            Debug("prevAlphaStr: " + prevAlphaStr);
                                                            Debug(trackAlphaChange.substring(0,boundVarI-numIsTrack) + "\t[" + c2 + "]\t"+trackAlphaChange.substring(boundVarI-numIsTrack+2));
                                                            trackAlphaChange = trackAlphaChange.substring(0,boundVarI-numIsTrack) + c2 + trackAlphaChange.substring(boundVarI-numIsTrack+2);
                                                            Debug("trackAlphaChange: " + trackAlphaChange);
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
                            }
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
                                Debug("prevResult: " + prevResult);
                                Debug("prevResultTrack: " + prevResultTrack);
                                Debug("currResult: " + currResult);
                                Debug("numAlphasPrevResult: " + numAlphasPrevResult);
                                Debug("numAlphasTrack: " + numAlphasTrack);
                                Debug("numAlphasCurrResult: " + numAlphasCurrResult);

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
                            Debug("prevResult: " + prevResult);
                            Debug("currResult: " + currResult);
                            Debug("numAlphasPrevResult: " + numAlphasPrevResult);
                            Debug("numAlphasCurrResult: " + numAlphasCurrResult);
                            
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
                        Debug("BETA");
                        markAwarded = doBetaReduction(prevLastExpr, currLastExpr, prevReducedExpr, currReducedExpr, curr);
                        if(markAwarded){
                            Debug("markAwarded, prevAlphaStr before: " + prevAlphaStr);
                            prevAlphaStr = prevReducedExpr.toString();
                            Debug("markAwarded, prevAlphaStr after: " + prevAlphaStr);
                        }else{ //may be alpha's still required in future
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
                                   //\v.\i0.(\x.x y z) v
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
                                        System.out.println(pe.getMessage());
                                        break;
                                        //jTextArea.setText(pe.getMessage());
                                    }
                                   
                               }
                               Debug("prevReducedExpr: " + prevReducedExpr + "\ncurrReducedExpr: " + currReducedExpr);
                               Debug("prevLastExpr: " + prevLastExpr + "\ncurrLastExpr" + currLastExpr); 
                               
                               markAwarded = doBetaReduction(prevLastExpr, currLastExpr, prevReducedExpr, currReducedExpr, curr);
                               
                            }
                        }
                        
                        if(!markAwarded && require.equals("")){
                            //try applicative (default normal)
                            Debug("mark not awarded, require empty");
                            Options.getEvaluationOrderOption().setValue(0);
                            currResult = stream.runExpr(curr.getExpression());  //get new reductions based on current line
                            Debug("currResult: " + currResult);
                            try {
                                currReducedExpr = Parser.parse(curr.getExpression());
                            } catch (Parser.ParseException pe) {
                                System.out.println(pe.getMessage());
                                break;
                                //jTextArea.setText(pe.getMessage());
                            }
                            prevReducedExpr = currResult.get(0);
                            currLastExpr = currResult.get(currResult.size()-1);
                            markAwarded = doBetaReduction(prevLastExpr, currLastExpr, prevReducedExpr, currReducedExpr, curr);
                            Options.getEvaluationOrderOption().setValue(2);
                        }
                        
                        
                        break;    
                    case 'n':
                    case 'η':
                        Debug("ETA");
                        Options.getEtaReductionsOption().setValue(true);
                        Debug(Options.getEtaReductionsOption().getValue());

                        if(prev == null)
                            continue;

                        currResult = stream.runExpr(prev.getExpression()); 
                        Options.getEtaReductionsOption().setValue(false);
                        Debug("currResult: " + currResult);

                        prevReducedExpr = currResult.get(1);
                        prevLastExpr = currResult.get(currResult.size()-1);
                        Debug("prev last reduced form: " + prevLastExpr);
                        Debug("current last reduced form: " + currLastExpr);

                        markAwarded = doBetaReduction(prevLastExpr, currLastExpr, prevReducedExpr, currReducedExpr, curr);

                        if(currResult.size()>2)
                            currResult.set(1,currResult.get(2)); //move an element up to compensate for the prev expression used to recalculate currResult
                        
                        break;
                    case '>':
                    case '→':
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
                        //could be changed so that penalties only occur for beta reductions
                        //user may have changed brackets, as long as still same expression, fine
                        else if(prevReducedExpr.toString().equals(currReducedExpr.toString()) ){ 
                            markAwarded=true;
                            Debug("no mark penalty");
                        }
                        else{
                            markAwarded = true;
                        }
                        
                        break;
                    default:
                        break;
                }
                
                /*** reasoning **/
                Debug("-------REASONING-------");
                //check change of symbols correct
                String reasoning = curr.getReasoning();
                int splitIndex = reasoning.indexOf("/");
                if(splitIndex<0){
                    System.out.println("no split in reasoning");
                    if(curr.getReduction()=='n'||curr.getReduction()=='η'){
                        Debug("award mark for reasoning");
                        curr.addMark(1);
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
                        //jTextArea.setText(pe.getMessage());
                    }
                    String currStr = currReducedExpr.toString();
                    Debug("prevStr: " + prevStr);
                    Debug("currStr: " + currStr);
                    String newStr = "";
                    Debug("target: " + target);
                    Debug("replacement: " + replacement);
                    switch(curr.getReduction()){
                        case 'a':
                        case 'α':
                        case '>':
                        case '→':
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
                            
                            /*String [] checkMultiI;
                            StringBuilder prevStrBuilder = new StringBuilder();
                            for(int i=0;i<countUniqueAlphas;i++){
                                checkMultiI = prevStr.split("\\\\i"+i);
                                for(int j=1;j<checkMultiI.length;j++){
                                    checkMultiI[j] = "\\i"+i+checkMultiI[j];
                                }
                                
                                //if there are multiple expressions alpha converted with same iX
                                //there will be 3 Strings or more
                                Debug("i"+i+" checkMultiI.length: " + checkMultiI.length);
                                if(checkMultiI.length>2){
                                    //multiple \iX's
                                    for(int j=2;j<checkMultiI.length;j++){
                                        countUniqueAlphas+=1;
                                        checkMultiI[j] = checkMultiI[j].replace("i"+i,"i"+countUniqueAlphas);
                                    }
                                }
                                prevStrBuilder = new StringBuilder();
                                //once duplicat/ambiguous iX's have been correctd, remake prevStr
                                for(String cMI:checkMultiI){
                                    prevStrBuilder.append(cMI); 
                                }
                                prevStr = prevStrBuilder.toString();
                            }
                            */
                            //all iX's should now be unique
                            //Debug("prevStr after checkMultiI: " + prevStr);
                            //Debug("target: " + target);
                            
                            //String currStrReplace = currStr;
                            
                            
                            //map iX to target
                            Map<String,String> iToTarget = new HashMap<>();
                            String compareStr = currStr;
                            boolean foundRelation = false;
                            Debug("countUniqueAlphas+1: " + (countUniqueAlphas+1) + " targets.length: " + targets.length);
                            Debug("currStr: " + currStr);
                            
                            for(int i=0;i<alphaChanges.length;i++){
                                //for each unique iX
                                compareStr = compareStr.replace(replacements[i],targets[i]);
                                Debug("replacements[i]: " + replacements[i] + " targets[i]: " + targets[i]);
                                
                            }
                            
                            //count # in prev.getExpression and compareStr
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
                                    replacementCorrect[i] = true;
                                    Debug("replacementCorrect: " + replacementCorrect[i]);
                                    Debug("award half mark for reasoning (correctly replaced)");
                                    if(curr.getReduction()=='>'||curr.getReduction()=='→'){
                                        Debug("CONVERSION corret");//no marks for conversion
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
                            
                            //parsing automatically does alpha conversion where necessary
                            Debug("compareStr: " + compareStr);
                            
                            if(compareStr.equals(prevStr)){
                                Debug("award half mark(s) for reasoning (compareStr.equals(prevStr)): ");
                                Debug("alphaChanges.length/2: " + alphaChanges.length/2.0);
                                if(curr.getReduction()=='>'||curr.getReduction()=='→'){
                                    Debug("CONVERSION corret");//no marks for conversion
                                }else
                                    curr.addMark(alphaChanges.length/2.0);
                                
                                /*for (int i=0;i<alphaChanges.length;i++){
                                    if(replacementCorrect[i] == true)
                                        curr.addMark(1);
                                }*/
                            }else if(curr.getReduction()=='>'||curr.getReduction()=='→'){
                                    //could be converting, in which case should equal currStr
                                    if(compareStr.equals(currStr)){
                                        Debug("CONVERSION correct");//no marks for conversion
                                    }
                            }else{
                                /*for (int i=0;i<alphaChanges.length;i++){
                                    compareStr = compareStr.replace(targets[i],replacements[i]);
                                }
                                try {
                                    compareStr = Parser.parse(compareStr).toString();
                                } catch (Parser.ParseException pe) {
                                    System.out.println(pe.getMessage());
                                }
                                Debug("compareStr (2nd try): " + compareStr);
                                if(compareStr.equals(prevStr)){
                                    Debug("compareStr.equals(prevStr)");
                                    Debug("award mark for reasoning: ");
                                    Debug("alphaChanges.length/2: " + alphaChanges.length/2.0);
                                    curr.addMark(alphaChanges.length/2.0);
                                }
                                */
                            }
                            
                                /*
                                foundRelation = false;
                                for(int j=0;j<targets.length;j++){
                                    //check if target replaced by iX is same as before after parsing 
                                    //parsing auto alpha converts where necessary
                                    
                                    compareStr = prevStr.replace("i"+i,targets[j]);
                                    Debug("compareStr before parse: " + compareStr);
                                    try {
                                        compareStr = Parser.parse(compareStr).toString();
                                    } catch (Parser.ParseException pe) {
                                        System.out.println(pe.getMessage());
                                    }
                                    Debug("compareStr: " + compareStr);
                                    if(compareStr.equals(prevStr)){
                                        Debug("compareStr.equals(prevStr) i" + i + "\t" + targets[j]);
                                        
                                        iToTarget.put(targets[j], "i"+i);
                                        foundRelation = true;
                                        //break;
                                    }
                                }
                                */
                            
                            
                            //could replace all iX's with targets in diff combinations to determine correctness
                            /*
                            for(int i=0;i<alphaChanges.length;i++){
                                
                                target = iToTarget.get(targets[i]); //get iX which corresponds to target (as determined above)
                                replacement = replacements[i];


                                Debug("target: " + target);
                                Debug("replacement: " + replacement);

                                int countTargetBefore = 0;
                                int countTargetAfter = 0;
                                int countReplacementBefore = 0;
                                int countReplacementAfter = 0;
                                int targetIndex = 0;
                                int indexReplace = 0;

                                while(targetIndex>-1&&indexReplace>-1){
                                    targetIndex = prevStr.indexOf(target,targetIndex);
                                    if(targetIndex>-1)
                                        countTargetBefore += 1;
                                    indexReplace = prevStr.indexOf(replacement,indexReplace);
                                    if(indexReplace>-1)
                                        countReplacementBefore += 1;
                                }

                                targetIndex = 0;
                                indexReplace = 0;
                                while(targetIndex>-1&&indexReplace>-1){
                                    targetIndex = currStr.indexOf(target,targetIndex);
                                    if(targetIndex>-1)
                                        countTargetAfter += 1;
                                    indexReplace = currStr.indexOf(replacement,indexReplace);
                                    if(indexReplace>-1)
                                        countReplacementAfter += 1;
                                }
                                Debug("countTargetBefore: " + countTargetBefore);
                                Debug("countTargetAfter: " + countTargetAfter);
                                Debug("countReplacementBefore: " + countReplacementBefore);
                                Debug("countReplacementAfter: " + countReplacementAfter);

                                if((countTargetBefore-countTargetAfter)==(countReplacementAfter-countReplacementBefore)){
                                    Debug("award mark for reasoning");
                                    curr.addMark(1);
                                }
                            }
                            */
                           /* int replacementOffset = 0;
                            boolean goodLine = true;
                            for (int i=0;i<prevStr.length();i++){
                                if(prevStr.charAt(i)!=currStr.charAt(i)){
                                    Debug(prevStr.substring(i+replacementOffset,i+replacementOffset+target.length()) + " vs " + target);
                                    if(prevStr.substring(i,i+target.length()).equals(target)){
                                        Debug(currStr.substring(i,i+replacement.length()) + " vs " + replacement);
                                        if(currStr.substring(i+replacementOffset,i+replacementOffset+replacement.length()).equals(replacement)){
                                            replacementOffset+= replacement.length()-target.length();
                                        }else{
                                            goodLine = false;
                                            break;
                                        }
                                    }else{
                                        goodLine = false;
                                        break;
                                    }
                                }
                            }
                            Debug("goodLine: " + goodLine);
                            */
                            break;
                        case 'b':
                        case 'B':
                        case 'β':
                            Debug("BETA");
                            if(replacement.contains("\\")||replacement.contains("λ")){
                                if(replacement.charAt(0)!='('){
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
                            if(indexTarget>0){
                                int p=indexTarget;
                                char cP = prevStr.charAt(p-1);
                                if(cP=='('){
                                    newStr = prevStr.substring(0,indexTarget-1);
                                }else{
                                    newStr = prevStr.substring(0,indexTarget);
                                }
                                p=p+target.length()+2; //move to past '.' of lambda expression
                                Debug("newStr from prevStr: " + newStr);
                                while(pNum>0){
                                    cP = prevStr.charAt(p);
                                    if(cP == '('){
                                        pNum+=1;
                                        newStr=newStr+cP;
                                    }else if(cP == ')'){
                                        pNum-=1;
                                        if(pNum<1)
                                            break;
                                        newStr=newStr+cP;
                                    }else if(prevStr.substring(p,p+target.length()).equals(target)){
                                        newStr=newStr+replacement;    
                                    }
                                    else{
                                        newStr=newStr+cP;
                                    }
                                    p+=1;
                                }
                                Debug("newStr: " + newStr);
                                
                                if(currStr.charAt(0)=='(')
                                    newStr = "(" + newStr + ")";//add parentheses for separation and maintenance of logic
                                
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
                                        newStr = newStr.substring(0,initialIndex-1) + newStr.substring(initialIndex);
                                        Debug("newStr: " + newStr);
                                        initialIndex-=1;//for further right parenthesis missing errors
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
            
            Debug("------------------------------\n" + sol);
            Debug("Mark penalty: " + markPenalty + "\n\n---------------------------------------------------------------------------");
        }
    }
    
    public int countAlpha(String alpha){
        //count number of unique 'i's - indicative of alpha conversion by calculator
        int countI = -1;
        int iNum=countI;

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
    
    public int countI(String alpha){
        //count number of 'i's including bound vars
        int counter = 0;
        for( int i=0; i<alpha.length(); i++ ) {
            if(alpha.charAt(i) == 'i' ) {
                counter++;
            } 
        }
        return counter;
    }
    
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
            //finalResultsList contain the final exprs derived from the original question at each step the its reduction
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Automarker();
    }
    
}
