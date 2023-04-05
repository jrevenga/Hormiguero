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
    private Semaphore semaforoAlmacen;
    private Integer criasComiendo, obrerasInterior, comidaAlmacen, comidaComedor = 0;
    private FileWriter logWriter;
    private PrintWriter pw;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

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
        try {
            logWriter = new FileWriter("evolucionHormiguero.txt");
            pw = new PrintWriter(logWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.interfaz = interfaz;
        semaforoAlmacen = new Semaphore(10, true);
    }
    
    
    public synchronized void entrarColonia(Hormiga h) {
        entrada.cruzarTunel();
        if(h.tipo == "HO"){
            obrerasExterior.remove(h);
            obrerasInterior++;
        }
    }

    public synchronized void salirColonia(Hormiga h) {
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
    }
    
    
    public synchronized void entrarAlmacen(Hormiga h) {
        try {
            semaforoAlmacen.acquire();
        } catch (InterruptedException ex) {}
        almacen.add(h);
        interfaz.mostrarAlmacen(lista(almacen));
    }
    
    public synchronized void salirAlmacen(Hormiga h) {
        semaforoAlmacen.release();
        almacen.remove(h);
        interfaz.mostrarAlmacen(lista(almacen));
    }
    
    
    public synchronized void empezarTransporte(Hormiga h) {
        transporte.add(h);
        interfaz.mostrarllevandoComida(lista(transporte));
    }

    public synchronized void acabarTransporte(Hormiga h) {
        transporte.remove(h);
        interfaz.mostrarllevandoComida(lista(transporte));
    }
    
    
    public synchronized void entrarComedor(Hormiga h) {
        if(h.tipo == "HC"){
            criasComiendo++;
        }
        comedor.add(h);
        interfaz.mostrarComedor(lista(comedor));
    }

    public synchronized void salirComedor(Hormiga h) {
        comedor.remove(h);
        interfaz.mostrarComedor(lista(comedor));
    }
    
    
    public synchronized void entrarZonaDescanso(Hormiga h) {
        zonaDescanso.add(h);
        interfaz.mostrarDescanso(lista(zonaDescanso));
    }

    public synchronized void salirZonaDescanso(Hormiga h) {
        zonaDescanso.remove(h);
        interfaz.mostrarDescanso(lista(zonaDescanso));
    }
    
    
    public synchronized void entrarInstruccion(Hormiga h) {
        soldadosIntruccion.add(h);
        interfaz.mostrarInstruccion(lista(soldadosIntruccion));
    }

    public synchronized void salirInstruccion(Hormiga h) {
        soldadosIntruccion.remove(h);
        interfaz.mostrarInstruccion(lista(soldadosIntruccion));
    }
    
    
    public synchronized void entrarRefugio(Hormiga h) {
        criasRefugio.add(h);
        interfaz.mostarRefugio(lista(criasRefugio));
    }

    public synchronized void salirRefugio(Hormiga h) {
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
    
    public synchronized void escribirEnLog(String evento) {
        try {
            pw.println(evento +" "+ dtf.format(LocalDateTime.now()));
            logWriter.flush();
            logWriter.close();
        } catch (IOException e) {} 
        
        finally {
            try {
                logWriter.close();
            } catch (IOException e) {}
        }
    }
}


