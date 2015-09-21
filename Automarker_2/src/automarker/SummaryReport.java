/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automarker;

import java.util.*;
/**
 *
 * @author Annie
 */
public class SummaryReport extends Report{
    private double totalMark;
    private ArrayList<Question> questions;

    public SummaryReport(Date dateCreated) {
        super(dateCreated);
    }

    public double getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(double totalMark) {
        this.totalMark = totalMark;
    }

    public ArrayList<Question> getQuestion() {
        return questions;
    }

    public void setQuestion(ArrayList<Question> questions) {
        this.questions = questions;
    }
    
    
}
