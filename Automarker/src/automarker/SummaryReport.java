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
    }
    
    @Override
    public String toString(){
        return getReport();
    }
    
}
