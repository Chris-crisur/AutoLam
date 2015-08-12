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
 * @author Annie
 */
public class Solution {
    private static int idCount = 0;
    
    private int id;
    private Question question;
    private List<Line> lines;
    private String dateCreated;     //TODO: change to formatted date
    private String dateSubmitted;   //TODO: change to formatted date

    public Solution(Question question){
        idCount += 1;
        id = idCount;
        this.question = question;
        lines = new ArrayList<>();
        dateCreated = "date"; //System.getTimeInMillis();
    }
    
    public int getID(){
        return id;
    }
    
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    @Override
    public String toString() {
        return "Solution{" + "id=" + id + ", question=" + question + ", lines=" + lines + ", dateCreated=" + dateCreated + ", dateSubmitted=" + dateSubmitted + '}';
    }
    
}
