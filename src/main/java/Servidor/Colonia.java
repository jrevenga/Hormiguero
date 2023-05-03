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
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import javax.swing.JOptionPane;

public class Colonia {

    private final Tunel entrada, salida1, salida2;
    private final Comedor comedor;
    private final Almacen almacen;
    private final Pausa pausa;
    private final InsectoInvasor insecto;
    private final List<Hormiga> obrerasExterior, soldadosInvasion, transporte, soldadosIntruccion, zonaDescanso,
            soldadosColonia, soldadosColoniaAux, criasColonia, criasRefugio;
    private Integer obrerasInterior, comidaAlmacen;
    private final Interfaz interfaz;
    private FileWriter logWriter;
    private final PrintWriter pw;
    private final DateTimeFormatter dtf;
    private CyclicBarrier invasion;

    public Colonia(Interfaz interfaz) {
        this.entrada = new Tunel();
        this.salida1 = new Tunel();
        this.salida2 = new Tunel();
        this.pausa = new Pausa();
        this.comedor = new Comedor(this);
        this.almacen = new Almacen(this);
        this.insecto = new InsectoInvasor();
        this.obrerasExterior = new ArrayList<>();
        this.soldadosInvasion = new ArrayList<>();
        this.transporte = new ArrayList<>();
        this.soldadosIntruccion = new ArrayList<>();
        this.zonaDescanso = new ArrayList<>();
        this.soldadosColonia = new ArrayList<>();
        this.soldadosColoniaAux = new ArrayList<>();
        this.criasColonia = new ArrayList<>();
        this.criasRefugio = new ArrayList<>();
        this.obrerasInterior = 0;
        this.comidaAlmacen = 0;
        this.interfaz = interfaz;
        this.dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        try {
            this.logWriter = new FileWriter("evolucionHormiguero.txt");
        } catch (IOException ex) {
        }
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

    public InsectoInvasor getInsecto() {
        return insecto;
    }

    public synchronized Integer getObrerasExterior() {
        return obrerasExterior.size();
    }

    public synchronized Integer getObrerasInterior() {
        return obrerasInterior;
    }

    public synchronized Integer getSoldadosIntruccion() {
        return soldadosIntruccion.size();
    }

    public synchronized Integer getSoldadosInvasion() {
        return soldadosInvasion.size();
    }

    public synchronized Integer getCriasComiendo() {
        return comedor.getCriasComiendo();
    }

    public synchronized Integer getCriasRefugio() {
        return criasRefugio.size();
    }
    
    public void empezarEntrar(Hormiga h) throws InterruptedException {
        pausa.verificarPausa();
        entrada.entrarTunel();
    }

    public synchronized void entrarColonia(Hormiga h) throws InterruptedException {
        pausa.verificarPausa();
        entrada.salirTunel();
        if (h instanceof Obrera) {
            obrerasExterior.remove(h);
            obrerasInterior++;
        } else if (h instanceof Soldado) {
            if (insecto.isAmenazaActiva()) {
                soldadosColoniaAux.add(h);
            } else {
                soldadosColonia.add(h);
            }
        } else {
            criasColonia.add(h);
        }
        interfaz.mostrarObrerasExterior(lista(obrerasExterior));
    }
    
    public void empezarSalir(Hormiga h, Integer n) throws InterruptedException {
        pausa.verificarPausa();
        if (n == 0) {
            salida1.entrarTunel();
        } else {
            salida2.entrarTunel();
        }
    }

    public synchronized void salirColonia(Hormiga h, Integer n) throws InterruptedException {
        pausa.verificarPausa();
        if (n == 0) {
            salida1.salirTunel();
        } else {
            salida2.salirTunel();
        }
        if (h instanceof Obrera) {
            obrerasInterior--;
            obrerasExterior.add(h);
            interfaz.mostrarObrerasExterior(lista(obrerasExterior));
        } else {
            soldadosColonia.remove(h);
        }
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
        if(zonaDescanso.remove(h)){
            interfaz.mostrarDescanso(lista(zonaDescanso));
        }
        
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
        h.isInterrupted();
    }

    // INSECTO INVASOR
    public synchronized void insecto() throws InterruptedException {
        pausa.verificarPausa();
        if (insecto.isAmenazaActiva() == true) {
            JOptionPane.showMessageDialog(null, "Ya hay un insecto atacando la colonia");
        } else {
            invasion = new CyclicBarrier(soldadosColonia.size());
            insecto.generarAmenaza();
            insecto.interrumpirHormigas(criasColonia);
            insecto.interrumpirHormigas(soldadosColonia);
        }
    }

    public void esperarSoldados(Hormiga h) throws InterruptedException, BrokenBarrierException {
        pausa.verificarPausa();
        synchronized (soldadosInvasion){
            soldadosInvasion.add(h);
            interfaz.mostrarSoldadosInsecto(lista(soldadosInvasion));
        }
        invasion.await();
    }

    public synchronized void exito(Hormiga h) throws InterruptedException {
        pausa.verificarPausa();
        soldadosInvasion.remove(h);
        interfaz.mostrarSoldadosInsecto(lista(soldadosInvasion));
        insecto.eliminarAmenaza();
        soldadosColonia.addAll(soldadosColoniaAux);
        soldadosColoniaAux.clear();
    }

    // OTROS
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
            pw.println(dtf.format(LocalDateTime.now()) + " - " + evento);
            logWriter.flush();
        } catch (IOException e) {
        }
    }

}
