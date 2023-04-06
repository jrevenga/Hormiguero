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
public class Soldado extends Hormiga {

    public Soldado(Integer id, Colonia colonia) {
        super("HS", id, colonia);
    }

    @Override
    public void run() {
        
        colonia.entrarColonia(this);
        while(true){
            if(iteraciones == 6){
                iteraciones = 0;
                int tiempo = new Random().nextInt(2000) + 3000;
                comer(tiempo);
            }
            else{
                hacerInstruccion();
                descansar(2000);
            }
        }
    }
    
    private void hacerInstruccion(){
        //verificarInsecto();
        //Entrar ZONA DE INSTRUCCIÓN
        colonia.entrarInstruccion(this);
        colonia.escribirEnLog("La hormiga soldado " + tipo + id + " comienza a hacer instrucción");
        try {
            Thread.sleep(new Random().nextInt(6000) + 2000);
        } catch (InterruptedException ex) {}
        //Salir ZONA DE INSTRUCCIÓN
        colonia.salirInstruccion(this);
    }
}

