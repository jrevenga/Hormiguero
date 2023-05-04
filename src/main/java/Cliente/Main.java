/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Compartido.InterfazRemota;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Jorge
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException, NotBoundException, InterruptedException {
        
        Registry registro = LocateRegistry.getRegistry("127.0.0.1", 1099);
        InterfazRemota obj = (InterfazRemota) registro.lookup("//127.0.0.1/Colonia");
        Interfaz cliente = new Interfaz(obj);
        cliente.setVisible(true);
        while(true){
            cliente.mostarExterior(obj.getExterior().toString());
            cliente.mostarInterior(obj.getInterior().toString());
            cliente.mostarInstruccion(obj.getInstruccion().toString());
            cliente.mostarInvasion(obj.getInvasion().toString());
            cliente.mostarComedor(obj.getComedor().toString());
            cliente.mostarRefugio(obj.getRefugio().toString());
            Thread.sleep(500);
        }
        
    }
    
}
