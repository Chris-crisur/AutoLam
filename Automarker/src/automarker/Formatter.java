/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automarker;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author Chris
 */
public class Formatter {
    static class LambdaFilter extends DocumentFilter
    {
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int length, String text, AttributeSet attr) throws BadLocationException
        {
            text=text.replace('\\', 'λ');
            //text=text.replace("(", "()");
            //text=text.replace("[", "[]");
            super.insertString(fb, length, text, attr);
        }
        
        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attr) throws BadLocationException
        {
            text=text.replace('\\', 'λ');
            //text=text.replace("(", "()");
            //text=text.replace("[", "[]");
            super.replace(fb, offset, length, text, attr);
        }
    }
}
