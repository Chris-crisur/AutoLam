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
public class Person {
    private static int id = 0;
    private String name;
    private String type;
    
    public Person(String name, String type) {
        this.name = name;
        this.type = type;
        id+=1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
