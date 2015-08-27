/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automarker;

import java.util.List;

/**
 *
 * @author Chris
 */
public class Stream {
    
    private Engine engine = new Engine();
    
    Stream(){
        
    }
    
    public List<Expr> runExpr(String str){
        List<Expr> exprList = engine.addDefinition(str);
        
        return exprList;
    }
    
}
