/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Compartido.InterfazRemota;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Jorge
 */
public class DatosColonia extends UnicastRemoteObject implements InterfazRemota {
    
    Colonia colonia;

    public DatosColonia(Colonia colonia) throws RemoteException {
        this.colonia = colonia;
    }

    @Override
    public Integer getExterior() throws RemoteException {
        return colonia.getObrerasExterior();
    }

    @Override
    public Integer getInterior() throws RemoteException {
        return colonia.getObrerasInterior();
    }

    @Override
    public Integer getInstruccion() throws RemoteException {
        return colonia.getSoldadosIntruccion();
    }

    @Override
    public Integer getInvasion() throws RemoteException {
        return colonia.getSoldadosInvasion();
    }

    @Override
    public Integer getComedor() throws RemoteException {
        return colonia.getCriasComiendo();
    }

    @Override
    public Integer getRefugio() throws RemoteException {
        return colonia.getCriasRefugio();
    }

    @Override
    public void generarInsecto() throws RemoteException {
        try {
            colonia.insecto();
        } catch (InterruptedException ex) {}
    }

    
}
