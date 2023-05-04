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
public class Pausa {
    
    private boolean pausa;

    public Pausa() {
        this.pausa = false;
    }
    
    public synchronized void verificarPausa() throws InterruptedException {
        while (pausa) {
            wait();
        }
    }

    public synchronized void pausar() {
        pausa = true;
    }

    public synchronized void reanudar() {
        pausa = false;
        notifyAll();
    }
    
}
