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
public class Report {
    private static int id = 0;
    private final String dateCreated;

    public Report(String dateCreated) {
        this.dateCreated = dateCreated;
        id+=1;
    }

    public int getId() {
        return id;
    }

    public String getDateCreated() {
        return dateCreated;
    }
}
