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
import java.util.concurrent.Semaphore;
import javax.swing.JOptionPane;

public class Colonia {
    private final Tunel entrada, salida1, salida2;
    private final List<Hormiga> obrerasExterior, soldadosInvasion, almacen, transporte, soldadosIntruccion, zonaDescanso, comedor, criasRefugio;
    private Integer criasComiendo, obrerasInterior, comidaAlmacen, comidaComedor;
    private boolean detenido, insecto;
    private final Semaphore semaforoAlmacen;
    private final Interfaz interfaz;
    private FileWriter logWriter;
    private final PrintWriter pw;
    private final DateTimeFormatter dtf;

    public Colonia(Interfaz interfaz) {
        this.entrada = new Tunel();
        this.salida1 = new Tunel();
        this.salida2 = new Tunel();
        this.obrerasExterior = new ArrayList<>();
        this.soldadosInvasion = new ArrayList<>();
        this.transporte = new ArrayList<>();
        this.almacen = new ArrayList<>();
        this.soldadosIntruccion = new ArrayList<>();
        this.zonaDescanso = new ArrayList<>();
        this.comedor = new ArrayList<>();
        this.criasRefugio = new ArrayList<>();
        this.criasComiendo = 0;
        this.obrerasInterior = 0;
        this.comidaAlmacen = 0;
        this.comidaComedor = 0;
        this.detenido = false;
        this.insecto = false;
        this.semaforoAlmacen = new Semaphore(10, true);
        this.interfaz = interfaz;
        this.dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        try {
            this.logWriter = new FileWriter("evolucionHormiguero.txt");
        } catch (IOException ex) {}
        this.pw = new PrintWriter(logWriter);
        interfaz.unidadesAlmacen(comidaAlmacen.toString());
        interfaz.unidadesComedor(comidaComedor.toString());
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
        return criasComiendo;
    }

    public Integer getCriasRefugio() {
        return criasRefugio.size();
    }
    
    
    public synchronized void entrarColonia(Hormiga h) throws InterruptedException {
        verificarPausa();
        entrada.cruzarTunel();
        if(h instanceof Obrera){
            obrerasExterior.remove(h);
            obrerasInterior++;
        }
        interfaz.mostrarObrerasExterior(lista(obrerasExterior));
    }

    public synchronized void salirColonia(Hormiga h) throws InterruptedException {
        verificarPausa();
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
    
    
    public synchronized void entrarAlmacen(Hormiga h) throws InterruptedException {
        verificarPausa();
        try {
            semaforoAlmacen.acquire();
        } catch (InterruptedException ex) {}
        almacen.add(h);
        interfaz.mostrarAlmacen(lista(almacen));
    }
    
     public synchronized void depositarAlmacen(Hormiga h) throws InterruptedException {
        verificarPausa();
        comidaAlmacen += 5;
        interfaz.unidadesAlmacen(comidaAlmacen.toString());
        //Liberar bloqueos para los que esperan la comida en el almacen
        notifyAll();
     }
     
     public synchronized void cogerAlmacen(Hormiga h) throws InterruptedException {
        verificarPausa();
        while (comidaAlmacen < 5){
            wait();
        }
        comidaAlmacen -= 5;
        interfaz.unidadesAlmacen(comidaAlmacen.toString());
     }
    
    public synchronized void salirAlmacen(Hormiga h) throws InterruptedException {
        verificarPausa();
        semaforoAlmacen.release();
        almacen.remove(h);
        interfaz.mostrarAlmacen(lista(almacen));
    }
    
    public synchronized void empezarTransporte(Hormiga h) throws InterruptedException {
        verificarPausa();
        transporte.add(h);
        interfaz.mostrarllevandoComida(lista(transporte));
    }

    public synchronized void acabarTransporte(Hormiga h) throws InterruptedException {
        verificarPausa();
        transporte.remove(h);
        interfaz.mostrarllevandoComida(lista(transporte));
    }
    
    
    public synchronized void entrarComedor(Hormiga h) throws InterruptedException {
        verificarPausa();
        if(h instanceof Cria){ 
            criasComiendo++;
        }
        comedor.add(h);
        interfaz.mostrarComedor(lista(comedor));
    }
    
    public synchronized void depositarComedor(Hormiga h) throws InterruptedException {
        verificarPausa();
        comidaComedor += 5;
        interfaz.unidadesComedor(comidaComedor.toString());
        //Liberar bloqueos para los que esperan para comer
        notifyAll();
     }
    
    public synchronized void comerComedor(Hormiga h) throws InterruptedException {
        verificarPausa();
        while (comidaComedor < 1){
            //Se bloquea hasta que traigan comida para comer
            wait();
        }
        comidaComedor--;    //Consume 1 unidad de alimento
        interfaz.unidadesComedor(comidaComedor.toString());
    }

    public synchronized void salirComedor(Hormiga h) throws InterruptedException {
        verificarPausa();
        if(h instanceof Cria){ 
            criasComiendo--;
        }
        comedor.remove(h);
        interfaz.mostrarComedor(lista(comedor));
    }
    
    
    public synchronized void entrarZonaDescanso(Hormiga h) throws InterruptedException {
        verificarPausa();
        zonaDescanso.add(h);
        interfaz.mostrarDescanso(lista(zonaDescanso));
    }

    public synchronized void salirZonaDescanso(Hormiga h) throws InterruptedException {
        verificarPausa();
        zonaDescanso.remove(h);
        interfaz.mostrarDescanso(lista(zonaDescanso));
    }
    
    
    public synchronized void entrarInstruccion(Hormiga h) throws InterruptedException {
        verificarPausa();
        soldadosIntruccion.add(h);
        interfaz.mostrarInstruccion(lista(soldadosIntruccion));
    }

    public synchronized void salirInstruccion(Hormiga h) throws InterruptedException {
        verificarPausa();
        soldadosIntruccion.remove(h);
        interfaz.mostrarInstruccion(lista(soldadosIntruccion));
    }
    
    
    public synchronized void entrarRefugio(Hormiga h) throws InterruptedException {
        verificarPausa();
        criasRefugio.add(h);
        interfaz.mostarRefugio(lista(criasRefugio));
    }

    public synchronized void salirRefugio(Hormiga h) throws InterruptedException {
        verificarPausa();
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
    
    public synchronized void verificarPausa() throws InterruptedException {
        while (detenido) {
            wait();
        }
    }

    public synchronized void pausar() {
        detenido = true;
    }

    public synchronized void reanudar() {
        detenido = false;
        notifyAll();
    }
}


