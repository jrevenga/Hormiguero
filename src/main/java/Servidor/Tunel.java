/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Jorge
 */
public class Tunel {

    private Semaphore semaforo;

    public Tunel() {
        semaforo = new Semaphore(1, true);
    }
    
    public void entrarTunel() throws InterruptedException {
        semaforo.acquire();
   }
    public void salirTunel() {
        semaforo.release();
   }
}

