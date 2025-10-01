/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Distribuido;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Lucía
 */
public interface InterfazRemota extends Remote {
    int[] getHumanosRefugio()                      throws RemoteException;
    int[] getHumanosBarrera()                    throws RemoteException;   
    int[] getHumanosEsperandoIda()               throws RemoteException;
    int[] getHumanosEsperandoVuelta()               throws RemoteException;
    int[] getHumanosAreasInseguras()             throws RemoteException;  
    int[] getZombisAreasInseguras()              throws RemoteException;   
    String[] getTopZombisLetales(int top)    throws RemoteException;   

    
    void pausar()                                throws RemoteException;
    void reanudar()                              throws RemoteException;
}
