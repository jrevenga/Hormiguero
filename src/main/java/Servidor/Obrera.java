/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.util.Random;

/**
 *
 * @author Jorge
 */
public class Obrera extends Hormiga {
    
    Random rand;

    public Obrera(Integer id, Colonia colonia) {
        super("HO", id, colonia);
        rand = new Random();
    }
    
    @Override
    public void run() {
        
        colonia.entrarColonia(this);
        while(true){
            if(iteraciones == 10){
                comer(3000);
                descansar(2000);
                iteraciones = 0;
            }
            else{
                if(id % 2 == 0){
                    try {
                        mueveComida();
                    } catch (InterruptedException ex) {}
                }
                else{
                    recoleztarComida();
                    depositarComida();
                }
            }
        }
    }
    
    private void mueveComida() throws InterruptedException{
        //Entrar ALMACÉN DE COMIDA
        colonia.entrarAlmacen(this);
        try {
            Thread.sleep(rand.nextInt(1000) + 1000);
        } catch (InterruptedException ex) {}
        colonia.cogerAlmacen(this);
        colonia.escribirEnLog("La hormiga obrera " + tipo + id + " coge comida del ALMACÉN DE COMIDA");
        //Salir ALMACÉN DE COMIDA
        colonia.salirAlmacen(this);
        colonia.empezarTransporte(this);
        try {
            Thread.sleep(rand.nextInt(2000) + 1000);    //Se mueve hasta la ZONA PARA COMER
        } catch (InterruptedException ex) {}
        colonia.acabarTransporte(this);
        //Entrar ZONA PARA COMER
        colonia.entrarComedor(this);
        //Se cargan los 5 alimentos
        try {
            Thread.sleep(rand.nextInt(1000) + 1000);
        } catch (InterruptedException ex) {}
        colonia.depositarComedor(this);
        colonia.escribirEnLog("La hormiga obrera " + tipo + id + " deposita comida en la ZONA PARA COMER");
        //Salir ZONA PARA COMER
        colonia.salirComedor(this);
        iteraciones++;
    }
    
    private void recoleztarComida(){
        colonia.salirColonia(this);
        colonia.escribirEnLog("La hormiga obrera " + tipo + id + " sale a buscar comida");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {}
        colonia.entrarColonia(this);
        colonia.escribirEnLog("La hormiga obrera " + tipo + id + " vuelve de recoleztar comida");
    }
    
    private void depositarComida(){
        //Entrar ALMACÉN DE COMIDA, sólo pueden acceder simultáneamente 10 hormigas.
        colonia.entrarAlmacen(this);
        try {
            Thread.sleep(rand.nextInt(2000) + 2000);
        } catch (InterruptedException ex) {}
        colonia.depositarAlmacen(this);
        colonia.escribirEnLog("La hormiga obrera " + tipo + id + " deposita comida en EL AlMACEN DE COMIDA");
        //Salir ALMACÉN DE COMIDA
        colonia.salirAlmacen(this);
        iteraciones++;
    }
}

