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
public class AnalyticsReport extends Report{
    private double avgMark;
    private ArrayList<String> mistakes;
    
    public AnalyticsReport(Date dateCreated) {
        super(dateCreated);
    }

    public double getAvgMark() {
        return avgMark;
    }

    public void setAvgMark(double avgMark) {
        this.avgMark = avgMark;
    }

    public ArrayList<String> getMistakes() {
        return mistakes;
    }

    public void setMistakes(ArrayList<String> mistakes) {
        this.mistakes = mistakes;
    }
    
    
}
