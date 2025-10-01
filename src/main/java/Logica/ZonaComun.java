/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Interfaz.VistaZonaComun;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Lucía
 */
public class ZonaComun  {
    
    private final LogApocalipsis log;
 
    private Refugio refugio;
    private VistaZonaComun vistaZonaComun;
    private List<Humano> humanos;
    
    private Random random;

    public ZonaComun(Refugio refugio, LogApocalipsis log) {
        this.refugio = refugio;
        this.humanos = new ArrayList<>();
        this.log = log;
        this.random = new Random();
    }
    public void añadirObservador(VistaZonaComun vistaZonaComun){
        this.vistaZonaComun = vistaZonaComun;
    }

    public Refugio getRefugio() {
        return refugio;
    }


    public void setRefugio(Refugio refugio) {
        this.refugio = refugio;
    }

    public List<Humano> getHumanos() {
        synchronized (humanos){
            return humanos;
        }
    }

    public void setHumanos(List<Humano> humanos) {
        this.humanos = humanos;
    }
    
    public void accederZonaComun(Humano h) {
        synchronized (humanos) {
            humanos.add(h);
            this.vistaZonaComun.actualizarListaHumanos(h.getIdHumano(), true);
        }
        
        log.escribir(h.getIdHumano()+" en zona comun");
    }
    
    public int seleccionarTunel(){
        return random.nextInt(1, 5);
    }

    public void salirZonaComun(Humano h) {
     
        synchronized (humanos) {
            humanos.remove(h);
            this.vistaZonaComun.actualizarListaHumanos(h.getIdHumano(), false);

    }
    }
}
