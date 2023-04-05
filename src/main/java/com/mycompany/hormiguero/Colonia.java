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
import java.util.ArrayList;
import java.util.List;

public class Colonia {
    private Tunel entrada;
    private Tunel salida1;
    private Tunel salida2;
    private List<Hormiga> obrerasInterior;
    private List<Hormiga> obrerasExterior;
    private List<Hormiga> soldadosIntruccion;
    private List<Hormiga> soldadosInvasion;
    private List<Hormiga> criasComiendo;
    private List<Hormiga> criasRefugio;
    private boolean invasorActivo;
    private FileWriter logWriter;

    public Colonia() {
        entrada = new Tunel();
        salida1 = new Tunel();
        salida2 = new Tunel();
        obrerasInterior = new ArrayList<>();
        obrerasExterior = new ArrayList<>();
        soldadosIntruccion = new ArrayList<>();
        soldadosInvasion = new ArrayList<>();
        criasComiendo = new ArrayList<>();
        criasRefugio = new ArrayList<>();
        invasorActivo = false;
        try {
            logWriter = new FileWriter("evolucionHormiguero.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tunel getEntrada() {
        return entrada;
    }

    public void setEntrada(Tunel entrada) {
        this.entrada = entrada;
    }

    public Tunel getSalida1() {
        return salida1;
    }

    public void setSalida1(Tunel salida1) {
        this.salida1 = salida1;
    }

    public Tunel getSalida2() {
        return salida2;
    }

    public void setSalida2(Tunel salida2) {
        this.salida2 = salida2;
    }

    public List<Hormiga> getObrerasInterior() {
        return obrerasInterior;
    }

    public void setObrerasInterior(List<Hormiga> obrerasInterior) {
        this.obrerasInterior = obrerasInterior;
    }

    public List<Hormiga> getObrerasExterior() {
        return obrerasExterior;
    }

    public void setObrerasExterior(List<Hormiga> obrerasExterior) {
        this.obrerasExterior = obrerasExterior;
    }

    public List<Hormiga> getSoldadosIntruccion() {
        return soldadosIntruccion;
    }

    public void setSoldadosIntruccion(List<Hormiga> soldadosIntruccion) {
        this.soldadosIntruccion = soldadosIntruccion;
    }

    public List<Hormiga> getSoldadosInvasion() {
        return soldadosInvasion;
    }

    public void setSoldadosInvasion(List<Hormiga> soldadosInvasion) {
        this.soldadosInvasion = soldadosInvasion;
    }

    public List<Hormiga> getCriasComiendo() {
        return criasComiendo;
    }

    public void setCriasComiendo(List<Hormiga> criasComiendo) {
        this.criasComiendo = criasComiendo;
    }

    public List<Hormiga> getCriasRefugio() {
        return criasRefugio;
    }

    public void setCriasRefugio(List<Hormiga> criasRefugio) {
        this.criasRefugio = criasRefugio;
    }

    public boolean isInvasorActivo() {
        return invasorActivo;
    }

    public void setInvasorActivo(boolean invasorActivo) {
        this.invasorActivo = invasorActivo;
    }

    public FileWriter getLogWriter() {
        return logWriter;
    }

    public void setLogWriter(FileWriter logWriter) {
        this.logWriter = logWriter;
    }
    
    
    
    
    
    
    public synchronized void escribirEnLog(String evento) {
        try {
            logWriter.write(evento + "\n");
            logWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void finalizar() {
        try {
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


