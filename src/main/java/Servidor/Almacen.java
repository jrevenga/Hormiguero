/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Jorge
 */
public class Almacen {
    
    private Integer comida, criasComiendo;
    private final List<Hormiga> lista;
    private final Semaphore semaforoAlmacen;
    private Colonia colonia;

    public Almacen(Colonia colonia) {
        this.comida = 0;
        this.criasComiendo = 0;
        this.lista = new ArrayList<>();
        this.semaforoAlmacen = new Semaphore(10, false);
        this.colonia = colonia;
    }
    
    
    public synchronized void entrar(Hormiga h) throws InterruptedException {
        colonia.getPausa().verificarPausa();
        try {
            semaforoAlmacen.acquire();
        } catch (InterruptedException ex) {}
        lista.add(h);
        colonia.getInterfaz().mostrarAlmacen(lista(lista));
    }
    
    public synchronized void salir(Hormiga h) throws InterruptedException {
        colonia.getPausa().verificarPausa();
        lista.remove(h);
        colonia.getInterfaz().mostrarAlmacen(lista(lista));
        semaforoAlmacen.release();
    }
    
    public synchronized void depositar() throws InterruptedException {
        colonia.getPausa().verificarPausa();
        comida += 5;
        colonia.getInterfaz().unidadesAlmacen(comida.toString());
        //Liberar bloqueos para los que esperan la comida en el almacen
        notifyAll();
    }
    
    public synchronized void coger() throws InterruptedException {
        colonia.getPausa().verificarPausa();
        while (comida < 5){
            wait();
        }
        comida -= 5;
        colonia.getInterfaz().unidadesAlmacen(comida.toString());
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
