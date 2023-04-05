/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hormiguero;

import java.util.Random;

/**
 *
 * @author Jorge
 */
public abstract class Hormiga extends Thread {
    protected Integer id;
    protected String tipo;
    protected Colonia colonia;
    protected Integer iteraciones;

    // constructor
    public Hormiga(String tipo, Integer id, Colonia colonia) {
        this.tipo = tipo;
        this.id = id;
        this.colonia = colonia;
        iteraciones = 0;
    }
    public void run() {
        
    }
    
    public void entrarColonia(){
        colonia.getEntrada().cruzarTunel();
    }
    
    public void salirColonia(){
        int salida = new Random().nextInt(2);
        if(salida == 0){
            colonia.getSalida1().cruzarTunel();
        }
        else{
            colonia.getSalida2().cruzarTunel();
        }
    }
    
    public void comer(int tiempo){
        if(tipo != "HO"){       // Las hormigas obreras no se preocupan por los insectos
            //verificarInsecto();
        }
        //Entrar ZONA PARA COMER
        if(tipo != "HC"){       // Las hormigas crias no consumen unidades
            //Consume 1 unidad de alimento
        }
        colonia.escribirEnLog("La hormiga " + tipo + id + " come un alimento");
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException ex) {}
        //Salir ZONA PARA COMER
    }
    
    public void descansar(int tiempo){
        if(tipo != "HO"){
            //verificarInsecto();
        }
        //Entrar ZONA DE DESCANSO
        colonia.escribirEnLog("La hormiga " + tipo + id + " descansa");
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException ex) {}
        //Salir ZONA DE DESCANSO
    }
}