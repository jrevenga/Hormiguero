/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jorge
 */
public class Comedor {
    
    private Integer comida, criasComiendo;
    private final List<Hormiga> comedor;
    private Colonia colonia;

    public Comedor(Colonia colonia) {
        this.comida = 0;
        this.criasComiendo = 0;
        this.comedor = new ArrayList<>();
        this.colonia = colonia;
    }

    public Integer getComida() {
        return comida;
    }

    public Integer getCriasComiendo() {
        return criasComiendo;
    }

    public List<Hormiga> getLista() {
        return comedor;
    }

    
    public synchronized void entrar(Hormiga h) throws InterruptedException {
        colonia.getPausa().verificarPausa();
        if(h instanceof Cria){ 
            criasComiendo++;
        }
        comedor.add(h);
        colonia.getInterfaz().mostrarComedor(lista(comedor));
    }
    
    public synchronized void salir(Hormiga h) throws InterruptedException {
        colonia.getPausa().verificarPausa();
        boolean b = comedor.remove(h);
        if(b && h instanceof Cria){
            criasComiendo--;
        }
        colonia.getInterfaz().mostrarComedor(lista(comedor));
    }
    
    public synchronized void depositar() throws InterruptedException {
        colonia.getPausa().verificarPausa();
        comida += 5;
        colonia.getInterfaz().unidadesComedor(comida.toString());
        notifyAll();    //Liberar bloqueos para los que esperan para comer
     }
    
    public synchronized void comer() throws InterruptedException {
        colonia.getPausa().verificarPausa();
        while (comida < 1){
            wait();     //Se bloquea hasta que traigan comida para comer
        }
        comida--;    //Consume 1 unidad de alimento
        colonia.getInterfaz().unidadesComedor(comida.toString());
    }
    
    private synchronized String lista(List<Hormiga> lista) {
        String contenido = "";
        for (int i = 0; i < lista.size(); i++) {
            Hormiga h = lista.get(i);
            contenido += h.tipo + h.id + ", ";
        }
        return contenido;
    }
}
