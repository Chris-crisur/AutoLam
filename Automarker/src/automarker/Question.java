/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package automarker;

/**
 *
 * @author Annie
 */
public class Question {
    private int id;
    private String description;
    private double maxMark;
    private String requirements; //maybe ENUM or int?

    public Question(int id, String description, double maxMark, String requirements) {
        this.id = id;
        this.description = description;
        this.maxMark = maxMark;
        this.requirements = requirements;
    }

    public int getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMaxMark() {
        return maxMark;
    }

    public void setMaxMark(double maxMark) {
        this.maxMark = maxMark;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    @Override
    public String toString() {
        return "Question{" + "id=" + id + ", description=" + description + ", maxMark=" + maxMark + ", requirements=" + requirements + '}';
    }
    
    
    
}
