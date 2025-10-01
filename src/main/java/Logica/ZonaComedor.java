/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Interfaz.VistaZonaComedor;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Lucía
 */
public class ZonaComedor {
    private final LogApocalipsis log;

    private Refugio refugio;
    private VistaZonaComedor vistaZonaComedor;
    private List<Humano> humanos;
    private int comidaRecolectada; //en el refugio se guarda la comida que van recolectando los humanos
    private Semaphore semaforoComida;

    public ZonaComedor(Refugio refugio, LogApocalipsis log) {
        this.refugio = refugio;
        this.humanos = new ArrayList<>();
        this.log = log;
        this.comidaRecolectada = 0;
        this.semaforoComida  = new Semaphore(0, true);

    }
    public void añadirObservador(VistaZonaComedor vistaZonaComedor){
        this.vistaZonaComedor = vistaZonaComedor;
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
    public int getComidaRecolectada() {
        return comidaRecolectada;
    }

    public void setComidaRecolectada(int comidaRecolectada) {
        this.comidaRecolectada = comidaRecolectada;
    }
    public synchronized void depositarComida(Humano h){
        int cantidad = h.getComidaRecolectada();
        this.comidaRecolectada += cantidad;
        log.escribir(h.getIdHumano() + " acaba de depositar " +cantidad+" uds de comida \nComida total: "+this.comidaRecolectada+ " uds");
        this.vistaZonaComedor.actualizarComida(this.comidaRecolectada);
        h.setComidaRecolectada(0); //ha dejado la comida en el deposito
        semaforoComida.release(cantidad); // Añadir tantos permisos como unidades de comida se añadan
        
    }
    public void accederZonaComedor(Humano h) {
        synchronized (humanos) {
            humanos.add(h);
            this.vistaZonaComedor.actualizarListaHumanos(h.getIdHumano(), true);
        }

        log.escribir(h.getIdHumano()+" en el comedor");
    }
    public void comer(Humano h) {
        try {
            log.escribir(h.getIdHumano() + " esperando para comer");
            h.setEsperandoComer(true);
            semaforoComida.acquire(); // Espera turno 
            h.setEsperandoComer(false);
            synchronized (this) {
                log.escribir(h.getIdHumano() + " comiendo");
                comidaRecolectada--; // Actualiza el contador visual
                this.vistaZonaComedor.actualizarComida(comidaRecolectada);
            }

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    public void salirZonaComedor(Humano h) {
        synchronized (humanos) {
            humanos.remove(h);
            this.vistaZonaComedor.actualizarListaHumanos(h.getIdHumano(), false);
        }

        
    }
    
}
