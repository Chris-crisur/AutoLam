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
    
    public double getMark()
    {
<<<<<<< HEAD
=======
        if(mark==0)
            report.getTotalMark();
>>>>>>> ac2c3d9245ec1ccb95c1490fe769d24cb9a3d88e
        return mark;
    }
    
    public void setSolutions(Solution [] solutions){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
	String date = sdf.format(new Date()); 
        report = new SummaryReport(new Date(),solutions);
    }
 
    public SummaryReport getSummaryReport(){
        return report;
    }
    
    @Override
    public String toString(){
<<<<<<< HEAD
        return studentNum + ": " + getName() + "\n" + getSummaryReport();
=======
        return studentNum + ": " + getName() + "\n" + getSummaryReport().getReport();
>>>>>>> ac2c3d9245ec1ccb95c1490fe769d24cb9a3d88e
    }
    
}
