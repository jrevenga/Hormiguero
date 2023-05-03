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
public class Cria extends Hormiga {

    public Cria(Integer id, Colonia colonia) {
        super("HC", id, colonia);
    }

    @Override
    public void run() {
        try {
            colonia.empezarEntrar(this);
            Thread.sleep(100); // tiempo para cruzar
            colonia.entrarColonia(this);
        } catch (InterruptedException ex) {}

        if (colonia.getInsecto().isAmenazaActiva()) {
            this.interrupt();
        }

        while (true) {
            try {
                int tiempo = new Random().nextInt(2000) + 3000;
                comer(tiempo);
                descansar(4000);
            } catch (InterruptedException ex) {
                try {
                    refugiarse();   // Redirigir al refugio si se recibe una interrupción
                    this.isInterrupted(); // Volver a runnable
                } catch (InterruptedException ex1) {}
            }
        }
    }

    private void refugiarse() throws InterruptedException {
        colonia.getComedor().salir(this);
        colonia.salirZonaDescanso(this);
        // Acudir a la zona de refugio
        colonia.entrarRefugio(this);
        colonia.escribirEnLog("La hormiga cria " + tipo + id + " entra al refugio");
        colonia.getInsecto().esperarAmenazaDesaparezca();
        colonia.salirRefugio(this);
        colonia.escribirEnLog("La hormiga cria " + tipo + id + " deja el refugio");
    }
}
