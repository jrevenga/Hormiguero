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
    
    private boolean detenido;

    public Pausa() {
        this.detenido = false;
    }
    
    public synchronized void verificarPausa() throws InterruptedException {
        while (detenido) {
            wait();
        }
    }

    public synchronized void pausar() {
        detenido = true;
    }

    public synchronized void reanudar() {
        detenido = false;
        notifyAll();
    }
    
}
