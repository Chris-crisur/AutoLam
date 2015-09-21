/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automarker;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Chris
 */
public class Student extends Person{

    private SimpleStringProperty studentNum;
    private int mark;
    
    public Student(String studentNum, String name) {
        super(name,"Student");
        this.studentNum = new SimpleStringProperty(studentNum);
        mark = 0;
    }

    public String getStudentNum() {
        return studentNum.get();
    }
    public void setMark(int i){mark+=i;}
    
    public int getMark()
    {
        return mark;
    }
    
    
    
}
