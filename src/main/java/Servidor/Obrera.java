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
    
    private Random rand;

    public Obrera(Integer id, Colonia colonia) {
        super("HO", id, colonia);
        rand = new Random();
    }
    
    @Override
    public void run() {
        try {
            colonia.empezarEntrar(this);
            Thread.sleep(100); // tiempo para cruzar
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
        } catch (InterruptedException ex) {}
    }
    
    private void mueveComida() throws InterruptedException{
        //Entrar ALMACÉN DE COMIDA
        colonia.getAlmacen().entrar(this);
        Thread.sleep(rand.nextInt(1000) + 1000);
        colonia.getAlmacen().coger();
        colonia.escribirEnLog("La hormiga obrera " + tipo + id + " coge comida del ALMACEN DE COMIDA");
        //Salir ALMACÉN DE COMIDA
        colonia.getAlmacen().salir(this);
        colonia.empezarTransporte(this);
        Thread.sleep(rand.nextInt(2000) + 1000);    //Se mueve hasta la ZONA PARA COMER
        colonia.acabarTransporte(this);
        //Entrar ZONA PARA COMER
        colonia.getComedor().entrar(this);
        //Se cargan los 5 alimentos
        Thread.sleep(rand.nextInt(1000) + 1000);
        colonia.getComedor().depositar();
        colonia.escribirEnLog("La hormiga obrera " + tipo + id + " deposita comida en la ZONA PARA COMER");
        //Salir ZONA PARA COMER
        colonia.getComedor().salir(this);
        iteraciones++;
    }
    
    private void recoleztarComida() throws InterruptedException{
        int salida = new Random().nextInt(2); // elegir salida
        colonia.empezarSalir(this, salida);
        Thread.sleep(100); // tiempo para cruzar
        colonia.salirColonia(this, salida);
        colonia.escribirEnLog("La hormiga obrera " + tipo + id + " sale a buscar comida");
        Thread.sleep(4000);
        colonia.empezarEntrar(this);
        Thread.sleep(100); // tiempo para cruzar
        colonia.entrarColonia(this);
        colonia.escribirEnLog("La hormiga obrera " + tipo + id + " vuelve de recoleztar comida");
    }
    
    private void depositarComida() throws InterruptedException{
        //Entrar ALMACÉN DE COMIDA, sólo pueden acceder simultáneamente 10 hormigas.
        colonia.getAlmacen().entrar(this);
        Thread.sleep(rand.nextInt(2000) + 2000);
        colonia.getAlmacen().depositar();
        colonia.escribirEnLog("La hormiga obrera " + tipo + id + " deposita comida en EL AlMACEN DE COMIDA");
        //Salir ALMACÉN DE COMIDA
        colonia.getAlmacen().salir(this);
        iteraciones++;
    }
}

