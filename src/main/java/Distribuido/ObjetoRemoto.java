/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Distribuido;

import Logica.Pausa;
import Logica.Refugio;
import Logica.Riesgo;
import Logica.Tunel;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author Lucía
 */
public class ObjetoRemoto extends UnicastRemoteObject implements InterfazRemota{
    private final Refugio refugio;
    private final List<Tunel> tuneles;
    private final Riesgo riesgo;
    private final Pausa pausa;

    public ObjetoRemoto(Refugio refugio, List<Tunel> tuneles,Riesgo riesgo,Pausa pausa) throws RemoteException {
        this.refugio = refugio;
        this.tuneles = tuneles;
        this.riesgo = riesgo;
        this.pausa = pausa;
    }


    public int[] getHumanosRefugio() throws RemoteException {
        return refugio.getNumHumanos();         
    }

    public int[] getHumanosBarrera() throws RemoteException {
        int[] h = new int[tuneles.size()];
        for (int i = 0; i < tuneles.size(); i++) {
            h[i] = tuneles.get(i).getNumHumanosBarrera();   
        }
        return h;
    }
    public int[] getHumanosEsperandoIda() throws RemoteException {
        int[] h = new int[tuneles.size()];
        for (int i = 0; i < tuneles.size(); i++) {
            h[i] = tuneles.get(i).getNumHumanosEsperandoIda();   
        }
        return h;
    }
    public int[] getHumanosEsperandoVuelta() throws RemoteException{
        int[] h = new int[tuneles.size()];
        for (int i = 0; i < tuneles.size(); i++) {
            h[i] = tuneles.get(i).getNumHumanosEsperandoVuelta();   
        }
        return h;
    }
    
    public int[] getHumanosAreasInseguras() throws RemoteException{
        return riesgo.humanosAreasInseguras();   
    }
    
    public int[] getZombisAreasInseguras()throws RemoteException {
        return riesgo.zombisAreasInseguras();   
    }
    public String[] getTopZombisLetales(int top)throws RemoteException {
        return riesgo.rankingZombis(top);     
    }


    public void pausar()   throws RemoteException { 


        pausa.pausar(true);    }

    public void reanudar()throws RemoteException  { pausa.reanudar(true);  }


}
