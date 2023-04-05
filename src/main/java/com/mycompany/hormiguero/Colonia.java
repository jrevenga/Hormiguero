/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hormiguero;

/**
 *
 * @author Jorge
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JOptionPane;

public class Colonia {
    private Tunel entrada;
    private Tunel salida1;
    private Tunel salida2;
    private List<Hormiga> obrerasExterior;
    private List<Hormiga> soldadosInvasion;
    private List<Hormiga> almacen;
    private List<Hormiga> transporte;
    private List<Hormiga> soldadosIntruccion;
    private List<Hormiga> zonaDescanso;
    private List<Hormiga> comedor;
    private List<Hormiga> criasRefugio;
    private Interfaz interfaz;
    private Semaphore semaforoAlmacen, cogerAlmacen, comer;
    private Integer criasComiendo, obrerasInterior, comidaAlmacen, comidaComedor;
    private FileWriter logWriter;
    private PrintWriter pw;
    private DateTimeFormatter dtf;
    private Lock cerrojo;
    private Condition detener;
    private boolean detenido, insecto;

    public Colonia(Interfaz interfaz) {
        entrada = new Tunel();
        salida1 = new Tunel();
        salida2 = new Tunel();
        almacen = new ArrayList<>();
        comedor = new ArrayList<>();
        zonaDescanso = new ArrayList<>();
        obrerasExterior = new ArrayList<>();
        soldadosIntruccion = new ArrayList<>();
        soldadosInvasion = new ArrayList<>();
        criasRefugio = new ArrayList<>();
        transporte = new ArrayList<>();
        obrerasInterior = 0;
        criasComiendo = 0;
        try {
            logWriter = new FileWriter("evolucionHormiguero.txt");
            pw = new PrintWriter(logWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.interfaz = interfaz;
        semaforoAlmacen = new Semaphore(10, true);
        cogerAlmacen = new Semaphore(0, true);
        comer = new Semaphore(0, true);
        detenido = false;
        cerrojo = new ReentrantLock();
        detener = cerrojo.newCondition();
        insecto = true;
        comidaAlmacen = 0;
        comidaComedor = 0;
    }
    
    
    public synchronized void entrarColonia(Hormiga h) {
        verificarPausa();
        entrada.cruzarTunel();
        if(h.tipo == "HO"){
            obrerasExterior.remove(h);
            obrerasInterior++;
        }
        interfaz.mostrarObrerasExterior(lista(obrerasExterior));
    }

    public synchronized void salirColonia(Hormiga h) {
        verificarPausa();
        int salida = new Random().nextInt(2);
        if(salida == 0){
            salida1.cruzarTunel();
        }
        else{
            salida2.cruzarTunel();
        }
        if(h.tipo == "HO"){
            obrerasInterior--;
            obrerasExterior.add(h);
        }
        interfaz.mostrarObrerasExterior(lista(obrerasExterior));
    }
    
    
    public synchronized void entrarAlmacen(Hormiga h) {
        verificarPausa();
        try {
            semaforoAlmacen.acquire();
        } catch (InterruptedException ex) {}
        almacen.add(h);
        interfaz.mostrarAlmacen(lista(almacen));
    }
    
     public synchronized void depositarAlmacen(Hormiga h) {
         verificarPausa();
         comidaAlmacen +=5;
         //cogerAlmacen.release();
         interfaz.unidadesAlmacen(comidaAlmacen.toString());
     }
     
     public synchronized void cogerAlmacen(Hormiga h) throws InterruptedException {
         verificarPausa();
         if (comidaAlmacen < 5){
             //Bloqueas hasta que traigan comida
             //cogerAlmacen.acquire();
         }
         comidaAlmacen -=5;
         interfaz.unidadesAlmacen(comidaAlmacen.toString());
     }
    
    public synchronized void salirAlmacen(Hormiga h) {
        verificarPausa();
        semaforoAlmacen.release();
        almacen.remove(h);
        interfaz.mostrarAlmacen(lista(almacen));
    }
    
    public synchronized void empezarTransporte(Hormiga h) {
        verificarPausa();
        transporte.add(h);
        interfaz.mostrarllevandoComida(lista(transporte));
    }

    public synchronized void acabarTransporte(Hormiga h) {
        verificarPausa();
        transporte.remove(h);
        interfaz.mostrarllevandoComida(lista(transporte));
    }
    
    
    public synchronized void entrarComedor(Hormiga h) {
        verificarPausa();
        if(h.tipo == "HC"){ 
            criasComiendo++;
        }
        comedor.add(h);
        interfaz.mostrarComedor(lista(comedor));
    }
    
    public synchronized void depositarComedor(Hormiga h) {
        verificarPausa();
         comidaComedor +=5;
         //comer.release();
         interfaz.unidadesComedor(comidaComedor.toString());
     }
    
    public synchronized void comerComedor(Hormiga h) throws InterruptedException {
        verificarPausa();
        if(h.tipo != "HC"){ // Las hormigas crias no consumen unidades   
            if(comidaComedor < 1){
                //Se bloquea hasta que traigan comida
                //comer.acquire();
            }else{
                comidaComedor--;    //Consume 1 unidad de alimento
            }
        }
        interfaz.unidadesComedor(comidaComedor.toString());
    }

    public synchronized void salirComedor(Hormiga h) {
        verificarPausa();
        comedor.remove(h);
        interfaz.mostrarComedor(lista(comedor));
    }
    
    
    public synchronized void entrarZonaDescanso(Hormiga h) {
        verificarPausa();
        zonaDescanso.add(h);
        interfaz.mostrarDescanso(lista(zonaDescanso));
    }

    public synchronized void salirZonaDescanso(Hormiga h) {
        verificarPausa();
        zonaDescanso.remove(h);
        interfaz.mostrarDescanso(lista(zonaDescanso));
    }
    
    
    public synchronized void entrarInstruccion(Hormiga h) {
        verificarPausa();
        soldadosIntruccion.add(h);
        interfaz.mostrarInstruccion(lista(soldadosIntruccion));
    }

    public synchronized void salirInstruccion(Hormiga h) {
        verificarPausa();
        soldadosIntruccion.remove(h);
        interfaz.mostrarInstruccion(lista(soldadosIntruccion));
    }
    
    
    public synchronized void entrarRefugio(Hormiga h) {
        verificarPausa();
        criasRefugio.add(h);
        interfaz.mostarRefugio(lista(criasRefugio));
    }

    public synchronized void salirRefugio(Hormiga h) {
        verificarPausa();
        criasRefugio.remove(h);
        interfaz.mostarRefugio(lista(criasRefugio));
    }
    
    
    private String lista(List<Hormiga> lista) {
        String contenido = "";
        for (int i = 0; i < lista.size(); i++) {
            Hormiga h = lista.get(i);
            contenido += h.tipo + h.id + ", ";
        }
        return contenido;
    }
    
    public void escribirEnLog(String evento) {
        try {
            pw.println(dtf.format(LocalDateTime.now()) +" - "+ evento);
            logWriter.flush();
        } catch (IOException e) {} 
    }
    
    public void verificarInsecto() { 
        
    }
    
    public void insecto() {
        if(insecto == true){
            JOptionPane.showMessageDialog(null, "Ya hay un insecto atacando la colonia");
        }
        else{
            
        }
    }
    
    public void verificarPausa() {
        try {
            cerrojo.lock();
            while (detenido) {
                try {
                    detener.await();
                } catch (InterruptedException e) {}
            }
        } finally {
            cerrojo.unlock();
        }
    }

    public void pausar() {
        try {
            cerrojo.lock();
            detenido = true;
        } finally {
            cerrojo.unlock();
        }
    }

    public void reanudar() {
        try {
            cerrojo.lock();
            detenido = false;
            detener.signalAll();
        } finally {
            cerrojo.unlock();
        }
    }
}


