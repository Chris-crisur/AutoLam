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
    private ArrayList<Solution> solutions;

    public SummaryReport(Date dateCreated, ArrayList<Solution> solutions) {
        super(dateCreated);
        this.solutions = solutions;
    }

    public double getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(double totalMark) {
        this.totalMark = totalMark;
    }

    public ArrayList<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(ArrayList<Solution> solutions) {
        this.solutions = solutions;
    }

    public ArrayList<String> displayReport(){
        ArrayList<String> report = new ArrayList<String>();
        int count = 1;
        double totalMark = 0;
        
        report.add("Question #:\tMark:");
        for (Solution sol : solutions) {
            report.add("Question " + count + ": \t" + sol.getMark());
            totalMark += sol.getMark();
            
            count++;
        }
        
        report.add("\nTotal mark: \t " + totalMark);
        
        return report;
    }
    
    
}
