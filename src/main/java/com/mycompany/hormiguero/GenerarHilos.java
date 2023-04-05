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
public class GenerarHilos extends Thread{
    
    private Colonia colonia;
    private int obrerasCreadas;
    private int soldadosCreados;
    
    public GenerarHilos(Colonia colonia) {
        this.colonia = colonia;
        obrerasCreadas = 1;
        soldadosCreados = 1;
    }
    
    public void run() {
        
        while (obrerasCreadas < 6001) {
            // Calcular el tiempo de espera aleatorio
            int tiempoEspera = (new Random().nextInt(2700) + 800);
            
            // Crear una obrera
                Obrera obrera = new Obrera(obrerasCreadas, colonia);
                obrera.start();
                obrerasCreadas++;
            
            try {
                Thread.sleep(tiempoEspera);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (obrerasCreadas % 3 == 0) {
                // Crear una soldado y una cría
                Soldado soldado = new Soldado(soldadosCreados, colonia);
                soldado.start();
                
                try {
                    Thread.sleep(tiempoEspera);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Cria cria = new Cria(soldadosCreados, colonia);
                cria.start();
                soldadosCreados++;
            }
        }
    }
    
}
