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
public class ResultText {
    private String palabra;
    private String pathDocumento;
    private int linea;
    private String tiempo;
    private Button action;

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public String getPathDocumento() {
        return pathDocumento;
    }

    public void setPathDocumento(String pathDocumento) {
        this.pathDocumento = pathDocumento;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public Button getAction() {
        return action;
    }

    public void setAction(Button action) {
        this.action = action;
    }
    
    

    public ResultText() {
    }

    public ResultText(String palabra, String pathDocumento, int linea, String tiempo, Button action) {
        this.palabra = palabra;
        this.pathDocumento = pathDocumento;
        this.linea = linea;
        this.tiempo = tiempo;
        this.action = action;
    }
    
    
    
}
