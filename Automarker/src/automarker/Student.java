/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automarker;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Chris
 */
public class Student extends Person{

    private SimpleStringProperty studentNum;
    private double mark;
    private SummaryReport report;
    
    public Student(String studentNum, String name) {
        super(name,"Student");
        this.studentNum = new SimpleStringProperty(studentNum);
        mark = 0;
        report=null;
    }

    public String getStudentNum() {
        return studentNum.get();
    }
    
    public double getMark(){
        if(mark==0)
            mark=report.getTotalMark();
        return mark;
    }
    
    public void setSolutions(Solution [] solutions){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
	String date = sdf.format(new Date()); 
        report = new SummaryReport(date,solutions);
    }
 
    public SummaryReport getSummaryReport(){
        return report;
    }
    
    @Override
    public String toString(){
        return studentNum + ": " + getName() + "\n" + report.getReport();
    }
    
}
