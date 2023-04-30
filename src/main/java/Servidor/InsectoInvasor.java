/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.util.List;

/**
 *
 * @author Jorge
 */
public class InsectoInvasor {
    
    private boolean amenazaActiva;

    public InsectoInvasor() {
        this.amenazaActiva = false;
    }

    public synchronized boolean isAmenazaActiva() {
        return amenazaActiva;
    }

    public synchronized void generarAmenaza() {
        amenazaActiva = true;
    }
    
    public synchronized void interrumpirHormigas(List<Hormiga> h) {
        for (Hormiga hormiga : h) {
            hormiga.interrupt();
        }
    }

    public synchronized void eliminarAmenaza() {
        amenazaActiva = false;
        notifyAll();    // Notificar a todas las hormigas que la amenaza ha desaparecido
    }

    public synchronized void esperarAmenazaDesaparezca() throws InterruptedException {
        while (amenazaActiva) {
            wait();
        }
    }
}


