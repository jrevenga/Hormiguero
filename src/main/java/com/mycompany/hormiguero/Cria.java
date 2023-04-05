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
public class Cria extends Hormiga {

    public Cria(Integer id, Colonia colonia) {
        super("HC", id, colonia);
    }

    @Override
    public void run() {
        
        entrarColonia();
        while(true){
            int tiempo = new Random().nextInt(2000) + 3000;
            comer(tiempo);
            descansar(4000);
        }
    }
}
