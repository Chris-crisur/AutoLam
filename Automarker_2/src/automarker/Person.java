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
public class Person {
    private static int id = 0;
    private SimpleStringProperty name;
    private String type;
    
    public Person(String name, String type) {
        this.name = new SimpleStringProperty(name);
        this.type = type;
        id+=1;
    }

    public String getName() {
        return name.get();
    }


    public String getType() {
        return type;
    }

    
}
