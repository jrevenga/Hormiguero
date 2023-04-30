/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jorge
 */
public class Soldado extends Hormiga {

    public Soldado(Integer id, Colonia colonia) {
        super("HS", id, colonia);
    }

    @Override
    public void run() {
        try {
            colonia.entrarColonia(this);
        } catch (InterruptedException ex) {}
        
        
        while (true) {
            try{
                if(iteraciones == 6){
                    iteraciones = 0;
                    int tiempo = new Random().nextInt(2000) + 3000;
                    comer(tiempo);
                }
                else{
                    hacerInstruccion();
                    descansar(2000);
                }
            } catch (InterruptedException ex){
                try {
                    luchar();
                    this.isInterrupted(); // Volver a runnable
                } catch (InterruptedException | BrokenBarrierException ex1) {}
            }
        }
    }
    
    private void luchar() throws InterruptedException, BrokenBarrierException {
        colonia.getComedor().salir(this);
        colonia.salirInstruccion(this);
        colonia.salirZonaDescanso(this);
        // Salir a repeler insecto
        colonia.salirColonia(this);
        colonia.escribirEnLog("La hormiga soldado " + tipo + id + " sale de la colonia");
        colonia.esperarSoldados(this);
        colonia.escribirEnLog("La hormiga soldado " + tipo + id + " lucha contra el INSECTO INVASOR");
        Thread.sleep(20000); // tiempo de lucha
        colonia.exito(this);
        colonia.entrarColonia(this);
        colonia.escribirEnLog("La hormiga soldado " + tipo + id + " vuelve a la colonia");
    }
    
    private void hacerInstruccion() throws InterruptedException{
        //Entrar ZONA DE INSTRUCCIÓN
        colonia.entrarInstruccion(this);
        colonia.escribirEnLog("La hormiga soldado " + tipo + id + " comienza a hacer instrucción");
        Thread.sleep(new Random().nextInt(6000) + 2000);
        //Salir ZONA DE INSTRUCCIÓN
        colonia.salirInstruccion(this);
    }
}

