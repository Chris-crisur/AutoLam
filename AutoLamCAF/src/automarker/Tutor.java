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
public class Tutor extends Person{
    private String studentNum;

    public Tutor(String studentNum, String name) {
        super(name, "Tutor");
        this.studentNum = studentNum;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }
    
    
    
    
}
