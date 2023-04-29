/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            if (colonia.verificarInsecto()) {
                this.interrupt();
            } else {
                if (!colonia.getListaCrias().contains(this)) {
                    colonia.getListaCrias().add(this);
                }
                colonia.entrarColonia(this);
                while (true) {
                    int tiempo = new Random().nextInt(2000) + 3000;
                    comer(tiempo);
                    descansar(4000);
                }
            }
        } catch (InterruptedException ex) {
            try {
                colonia.entrarRefugio(this);
                System.out.println("Entrar Refugio");
                while (colonia.verificarInsecto()){
                    //Mientras haya insecto no puede salir CREAR MONITOR O LO QUE SEA
                }
                System.out.println("Salir Refugio");
                colonia.salirRefugio(this);
            } catch (InterruptedException ex1) {}
        }
    }
}
