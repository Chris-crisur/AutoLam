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
public class Line {
    private static int idCount = 0;
    private int id;
    private String expression;
    private char reduction;
    private String reasoning;
    private double mark;

    public Line(String expression, char reduction, String reasoning) {
        idCount += 1;
        id = idCount;
        this.expression = expression;
        this.reduction = reduction;
        this.reasoning = reasoning;
    }

    public int getId() {
        return id;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public char getReduction() {
        return reduction;
    }

    public void setReduction(char reduction) {
        this.reduction = reduction;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "Line{" + "id=" + id + ", expression=" + expression + ", reduction=" + reduction + ", reasoning=" + reasoning + ", mark=" + mark + '}';
    }
    
    
    
}
