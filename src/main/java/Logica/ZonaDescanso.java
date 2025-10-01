/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Interfaz.VistaZonaDescanso;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lucía
 */
public class ZonaDescanso  {
    private final LogApocalipsis log;

    private Refugio refugio;
    private VistaZonaDescanso vistaZonaDescanso;
    private List<Humano> humanos;

    public ZonaDescanso(Refugio refugio, LogApocalipsis log) {
        this.refugio = refugio;
        this.humanos = new ArrayList<>();
        this.log = log;
    }
    public void añadirObservador(VistaZonaDescanso vistaZonaDescanso){
        this.vistaZonaDescanso = vistaZonaDescanso;
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
    public void accederZonaDescanso(Humano h) {
        synchronized (humanos) {
            humanos.add(h);
            this.vistaZonaDescanso.actualizarListaHumanos(h.getIdHumano(), true);

        }
        
        log.escribir(h.getIdHumano()+" descansando");
     }
    public void salirZonaDescanso(Humano h) {
        synchronized (humanos) {
            humanos.remove(h);
            this.vistaZonaDescanso.actualizarListaHumanos(h.getIdHumano(), false);       
        }

    }
    
}
