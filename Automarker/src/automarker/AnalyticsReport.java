/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automarker;

import java.util.*;
/**
 *  Class has potential to collectively analyse data from students.
 *  In this implementation, it can store
 * @author Annie
 */
public class AnalyticsReport extends Report{
    
    private double marks;
    private ArrayList<String> studNumbers;
    
    public AnalyticsReport(String dateCreated) {
        super(dateCreated);
        studNumbers = new ArrayList<>();
    }

    public double getAvgMark() {
        return marks/studNumbers.size();
    }

    public void addStudent(String studNumber, double mark) {
        this.marks += mark;
        studNumbers.add(studNumber);
    }

    public ArrayList<String> getStudentNumbers() {
        return studNumbers;
    }

    
    
    
}
