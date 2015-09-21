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
    private int id;
    private Date dateCreated;
    //private Solution sol;

    public Report(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getId() {
        return id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
  
    
}
