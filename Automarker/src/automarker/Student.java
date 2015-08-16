/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automarker;

/**
 *
 * @author Chris
 */
public class Student extends Person{

    private String studentNum;
    
    public Student(String name, String studentNum) {
        super(name,"Student");
        this.studentNum = studentNum;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }
    
    
    
}
