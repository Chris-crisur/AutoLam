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
    private Solution [] solutions;
    private double totalMark;

    public SummaryReport(Date dateCreated, Solution [] solutions) {
        super(dateCreated);
        this.solutions = solutions;
        for(Solution sol: solutions){
            totalMark += sol.getMark();
        }
    }
<<<<<<< HEAD
    
    public void setTotalMark(double totalMark) {
        this.totalMark = totalMark;
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
=======

    public double getTotalMark() {
        return totalMark;
    }
    
    public String getReport(){
        StringBuilder sb = new StringBuilder();
        double maxMark = 0;
        for(Solution sol: solutions){
            sb.append(sol.toString()).append("\n");
            maxMark += sol.getQuestion().getMaxMark();
        }
        
        sb.append("Total mark: ").append(totalMark).append(" out of ").append(maxMark)
                .append("\nPercentage: ").append(totalMark*100.00/maxMark);
        
        return sb.toString();
>>>>>>> ac2c3d9245ec1ccb95c1490fe769d24cb9a3d88e
    }
    
    @Override
    public String toString(){
        return getReport();
    }
    
}
