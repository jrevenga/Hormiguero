/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hormiguero;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Jorge
 */
public class Tunel {

    private Semaphore semaforo;
    private static final int TIEMPO_CRUZAR = 100;

    public Tunel() {
        semaforo = new Semaphore(0, true);
    }
    
    public void cruzarTunel() {
        try {
            semaforo.acquire();
            Thread.sleep(TIEMPO_CRUZAR);
            semaforo.release();
        } catch (InterruptedException e) {}
   }
}

