/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import javafx.scene.control.Button;

/**
 *
 * @author Lathy
 */
public class QueryText {
    
    private int ranking;
    private String path;
    private int reference;
    private Button action;

    public QueryText() {
    }
    
    

    public QueryText(int ranking, String path, int reference, Button action) {
        this.ranking = ranking;
        this.path = path;
        this.reference = reference;
        this.action = action;
    }

    
    
    
    
    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getReference() {
        return reference;
    }

    public void setReference(int reference) {
        this.reference = reference;
    }

    public Button getAction() {
        return action;
    }

    public void setAction(Button action) {
        this.action = action;
    }
    
    
    
    
}
