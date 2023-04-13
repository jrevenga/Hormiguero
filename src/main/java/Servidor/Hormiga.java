/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;


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
        this.iteraciones = 0;
    }
    
    public void comer(int tiempo) throws InterruptedException {
        //Entrar ZONA PARA COMER
        colonia.getComedor().entrar(this);
        colonia.getComedor().comer();
        colonia.escribirEnLog("La hormiga " + tipo + id + " come un alimento");
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException ex) {}
        //Salir ZONA PARA COMER
        colonia.getComedor().salir(this);
    }
    
    public void descansar(int tiempo) throws InterruptedException{
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