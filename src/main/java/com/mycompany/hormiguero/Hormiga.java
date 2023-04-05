/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hormiguero;


/**
 *
 * @author Jorge
 */
public abstract class Hormiga extends Thread {
    protected String tipo;
    protected Integer id;
    protected Colonia colonia;
    protected Integer iteraciones;

    // constructor
    public Hormiga(String tipo, Integer id, Colonia colonia) {
        this.tipo = tipo;
        this.id = id;
        this.colonia = colonia;
        iteraciones = 0;
    }
    
    public void comer(int tiempo){
        //Entrar ZONA PARA COMER
        colonia.entrarComedor(this);
        colonia.comerComedor(this);
        colonia.escribirEnLog("La hormiga " + tipo + id + " come un alimento");
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException ex) {}
        //Salir ZONA PARA COMER
        colonia.salirComedor(this);
    }
    
    public void descansar(int tiempo){
        //Entrar ZONA DE DESCANSO
        colonia.entrarZonaDescanso(this);
        colonia.escribirEnLog("La hormiga " + tipo + id + " descansa");
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException ex) {}
        //Salir ZONA DE DESCANSO
        colonia.salirZonaDescanso(this);
    }
}