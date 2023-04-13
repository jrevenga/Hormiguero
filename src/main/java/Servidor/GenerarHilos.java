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
public class GenerarHilos extends Thread{
    
    private Colonia colonia;
    private int obrerasCreadas;
    private int soldadosCreados;
    
    public GenerarHilos(Colonia colonia) {
        this.colonia = colonia;
        obrerasCreadas = 1;
        soldadosCreados = 1;
    }
    
    @Override
    public void run() {  
        while (obrerasCreadas < 6001) {
            try {
                // Calcular el tiempo de espera aleatorio
                int tiempoEspera = (new Random().nextInt(2700) + 800);
                colonia.getPausa().verificarPausa();
                // Crear una obrera
                Obrera obrera = new Obrera(obrerasCreadas, colonia);
                obrera.start();
                obrerasCreadas++;
                
                Thread.sleep(tiempoEspera);
                
                if (obrerasCreadas % 3 == 0) {
                    colonia.getPausa().verificarPausa();
                    // Crear una soldado y una cría
                    Soldado soldado = new Soldado(soldadosCreados, colonia);
                    soldado.start();
                    
                    Thread.sleep(tiempoEspera);
                    colonia.getPausa().verificarPausa();
                    Cria cria = new Cria(soldadosCreados, colonia);
                    cria.start();
                    soldadosCreados++;
                }
            } catch (InterruptedException ex) {}
        }
    }
    
}
