package Compartido;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Jorge
 */
public interface InterfazRemota extends Remote {

    Integer getExterior() throws RemoteException;
    Integer getInterior() throws RemoteException;
    Integer getInstruccion() throws RemoteException;
    Integer getInvasion() throws RemoteException;
    Integer getComedor() throws RemoteException;
    Integer getRefugio() throws RemoteException;
    void generarInsecto() throws RemoteException;
}