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
        if (!colonia.getListaSoldados().contains(this) && !colonia.getListaSoldadosCombate().contains(this)) {
            if (colonia.verificarInsecto()) {
                colonia.getListaSoldados().add(this);
            } else {
                colonia.getListaSoldadosCombate().add(this);
            }
        }
        try {
            colonia.entrarColonia(this);
            while (true) {
                if (iteraciones == 6) {
                    iteraciones = 0;
                    int tiempo = new Random().nextInt(2000) + 3000;
                    comer(tiempo);
                } else {
                    hacerInstruccion();
                    descansar(2000);
                }
            }
        } catch (InterruptedException ex) {
            try {
                colonia.combatirInsecto(this);
                sleep(1000);
            } catch (InterruptedException | BrokenBarrierException ex1) {}
        }
    }

    private void hacerInstruccion() throws InterruptedException {
        //verificarInsecto();
        //Entrar ZONA DE INSTRUCCIÓN
        colonia.entrarInstruccion(this);
        colonia.escribirEnLog("La hormiga soldado " + tipo + id + " comienza a hacer instrucción");
        Thread.sleep(new Random().nextInt(6000) + 2000);
        //Salir ZONA DE INSTRUCCIÓN
        colonia.salirInstruccion(this);
    }
}
