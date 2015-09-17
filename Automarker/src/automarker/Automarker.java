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
import java.util.List;

/**
 *
 * @author Chris
 */
public class Automarker {
    private boolean debug = true;
    private Solution [] solutions;
    private List<String> finalResultsList;
    
    Automarker(){
        //TODO: sign in
        loadFiles();
        //solutions = TestCases.loadTest("all");
        
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
        Options.getEvaluationOrderOption().setValue(3); //default evaluation is normal (without thunks)
        
        for (Solution sol: solutions){
            String qStart = sol.getQuestion().getStart();
            String qStartChanged = qStart;      //used for alpha conversion changes
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
                  
                if(currResult.size()==1){
                    //done
                    break;
                }
                
                prevAlphaExpr = currAlphaExpr;    //should be used to keep track of alpha reductions
                prevReducedExpr = currResult.get(1);    //first line of reduction
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
                                            trackAlphaChange = trackAlphaChange.substring(0,i-numIsTrack) + c2 + trackAlphaChange.substring(i-numIsTrack+2);
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
                                                            numIsTrack = countI(prevAlphaStr.substring(0,boundVarI-numIs)) - countI(trackAlphaChange.substring(0,boundVarI-numIs));;
                                                            Debug("numIsTrack: " + numIsTrack);
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

                        
                        break;
                    case '>':
                    case '→':
                        Debug("CONVERSION");
                        
                        break;
                    default:
                        break;
                }
                
                if(!markAwarded){
                    markPenalty+=1;
                }
                
                prev = curr;
                
            }
            sol.setMark(markPenalty);
            
            Debug(sol);
            Debug("Mark penalty: " + markPenalty + "\n\n-------------------------");
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
