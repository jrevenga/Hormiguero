/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

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
import javax.swing.JOptionPane;

public class Colonia {
    private final Tunel entrada, salida1, salida2;
    private final Comedor comedor;
    private final Almacen almacen;
    private final Pausa pausa;
    private final List<Hormiga> obrerasExterior, soldadosInvasion, transporte, soldadosIntruccion, zonaDescanso, criasRefugio;
    private Integer obrerasInterior, comidaAlmacen;
    private boolean insecto;
    private final Interfaz interfaz;
    private FileWriter logWriter;
    private final PrintWriter pw;
    private final DateTimeFormatter dtf;

    public Colonia(Interfaz interfaz) {
        this.entrada = new Tunel();
        this.salida1 = new Tunel();
        this.salida2 = new Tunel();
        this.pausa = new Pausa();
        this.comedor = new Comedor(this);
        this.almacen = new Almacen(this);
        this.obrerasExterior = new ArrayList<>();
        this.soldadosInvasion = new ArrayList<>();
        this.transporte = new ArrayList<>();
        this.soldadosIntruccion = new ArrayList<>();
        this.zonaDescanso = new ArrayList<>();
        this.criasRefugio = new ArrayList<>();
        this.obrerasInterior = 0;
        this.comidaAlmacen = 0;
        this.insecto = false;
        this.interfaz = interfaz;
        this.dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        try {
            this.logWriter = new FileWriter("evolucionHormiguero.txt");
        } catch (IOException ex) {}
        this.pw = new PrintWriter(logWriter);
        interfaz.unidadesAlmacen(comidaAlmacen.toString());
        interfaz.unidadesComedor(comedor.getComida().toString());
    }

    public Pausa getPausa() {
        return pausa;
    }

    public Comedor getComedor() {
        return comedor;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public Interfaz getInterfaz() {
        return interfaz;
    }

    public Integer getObrerasExterior() {
        return obrerasExterior.size();
    }
    
    public Integer getObrerasInterior() {
        return obrerasInterior;
    }
    
    public Integer getSoldadosIntruccion() {
        return soldadosIntruccion.size();
    }

    public Integer getSoldadosInvasion() {
        return soldadosInvasion.size();
    }

    public Integer getCriasComiendo() {
        return comedor.getCriasComiendo();
    }

    public Integer getCriasRefugio() {
        return criasRefugio.size();
    }
    
    
    public synchronized void entrarColonia(Hormiga h) throws InterruptedException {
        pausa.verificarPausa();
        entrada.cruzarTunel();
        if(h instanceof Obrera){
            obrerasExterior.remove(h);
            obrerasInterior++;
        }
        interfaz.mostrarObrerasExterior(lista(obrerasExterior));
    }

    public synchronized void salirColonia(Hormiga h) throws InterruptedException {
        pausa.verificarPausa();
        int salida = new Random().nextInt(2);
        if(salida == 0){
            salida1.cruzarTunel();
        }
        else{
            salida2.cruzarTunel();
        }
        if(h instanceof Obrera){
            obrerasInterior--;
            obrerasExterior.add(h);
        }
        interfaz.mostrarObrerasExterior(lista(obrerasExterior));
    }
    
    // Transporte entre ALMACEN y COMEDOR
    public synchronized void empezarTransporte(Hormiga h) throws InterruptedException {
        pausa.verificarPausa();
        transporte.add(h);
        interfaz.mostrarllevandoComida(lista(transporte));
    }

    public synchronized void acabarTransporte(Hormiga h) throws InterruptedException {
        pausa.verificarPausa();
        transporte.remove(h);
        interfaz.mostrarllevandoComida(lista(transporte));
    }
    
    // ZONA DE DESCANSO
    public synchronized void entrarZonaDescanso(Hormiga h) throws InterruptedException {
        pausa.verificarPausa();
        zonaDescanso.add(h);
        interfaz.mostrarDescanso(lista(zonaDescanso));
    }

    public synchronized void salirZonaDescanso(Hormiga h) throws InterruptedException {
        pausa.verificarPausa();
        zonaDescanso.remove(h);
        interfaz.mostrarDescanso(lista(zonaDescanso));
    }
    
    // ZONA DE INSTRUCCION
    public synchronized void entrarInstruccion(Hormiga h) throws InterruptedException {
        pausa.verificarPausa();
        soldadosIntruccion.add(h);
        interfaz.mostrarInstruccion(lista(soldadosIntruccion));
    }

    public synchronized void salirInstruccion(Hormiga h) throws InterruptedException {
        pausa.verificarPausa();
        soldadosIntruccion.remove(h);
        interfaz.mostrarInstruccion(lista(soldadosIntruccion));
    }
    
    // REFUGIO
    public synchronized void entrarRefugio(Hormiga h) throws InterruptedException {
        pausa.verificarPausa();
        criasRefugio.add(h);
        interfaz.mostarRefugio(lista(criasRefugio));
    }

    public synchronized void salirRefugio(Hormiga h) throws InterruptedException {
        pausa.verificarPausa();
        criasRefugio.remove(h);
        interfaz.mostarRefugio(lista(criasRefugio));
    }
    
    
    private synchronized String lista(List<Hormiga> lista) {
        String contenido = "";
        for (int i = 0; i < lista.size(); i++) {
            Hormiga h = lista.get(i);
            contenido += h.tipo + h.id + ", ";
        }
        return contenido;
    }
    
    public synchronized void escribirEnLog(String evento) {
        try {
            pw.println(dtf.format(LocalDateTime.now()) +" - "+ evento);
            logWriter.flush();
        } catch (IOException e) {} 
    }
    
    
    public synchronized void verificarInsecto() { 
        
    }
    
    public synchronized void insecto() {
        if(insecto == true){
            JOptionPane.showMessageDialog(null, "Ya hay un insecto atacando la colonia");
        }
        else{
            insecto = true;
        }
    }
}


