/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Interfaz.VistaTunel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Lucía
 */
public class Tunel {
    
    private LogApocalipsis log;
    private VistaTunel vistaTunel;
    private final int id; //1,2,3 y 4 porque cada uno corresponde con un area
    private List<Humano> humanosEnBarrera;
    private List<Humano> humanosEnTunel;
    private final Queue<Humano> colaIda;      //refugio --> riesgo
    private final Queue<Humano> colaVuelta;   // riesgo --> refugio
    private CyclicBarrier barrera;
            

    public Tunel(int id, LogApocalipsis log) {
        this.id = id;
        this.log = log;
        this.humanosEnBarrera = new ArrayList<>();
        this.humanosEnTunel = new ArrayList<>();
        this.colaIda = new ArrayDeque<>();  
        this.colaVuelta  = new ArrayDeque<>(); 
        this.barrera = new CyclicBarrier(3 ,() -> {  // acción cuando llega el 3º
                            synchronized (humanosEnBarrera) {
                                String ids = humanosEnBarrera.stream().map(Humano::getIdHumano).toList().toString(); // "[H0003, H0005, H0007]"
                                log.escribir("Túnel " + id + " --> se abre la barrera, cruzan " + ids);
                                humanosEnBarrera.clear();             // vaciamos para el siguiente grupo
                                this.vistaTunel.actualizarBarrera(null, false);

                            }
                            });
    }
    
    public void añadirObservador(VistaTunel vistaTunel){
        this.vistaTunel = vistaTunel;
    }
    
    public int getId() {
        return id;
    }




    public void esperarGrupo(Humano h) {
        try {
            synchronized (humanosEnBarrera) {
                humanosEnBarrera.add(h);
                this.vistaTunel.actualizarBarrera(h.getIdHumano(), true);
                log.escribir(h.getIdHumano() + " esperando en la barrera del túnel " + id);
                //notificar interfaz de que el humano esta aqui
            }
            barrera.await();

        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public synchronized void entrarColaIda(Humano h) {
        //notificar interfaz
        log.escribir(h.getIdHumano()+" esperando para ir a Riesgo por tunel "+this.id);
        colaIda.add(h);
        this.vistaTunel.actualizarLista(h.getIdHumano(), "ida", true);

    }
    
    public synchronized void entrarColaVuelta(Humano h) {
        log.escribir(h.getIdHumano()+" esperando para ir al Refugio por tunel "+this.id);
        colaVuelta.add(h);
        this.vistaTunel.actualizarLista(h.getIdHumano(), "vuelta", true);


    }
    
    public synchronized void esperarTurnoCruce(Humano h, boolean haciaRiesgo)
            throws InterruptedException {

        while (true) {
            boolean tunelLibre = humanosEnTunel.isEmpty();
            boolean hayHumanosVuelta = !colaVuelta.isEmpty();

            if (tunelLibre) {
                if (hayHumanosVuelta) {
                    // Hay prioridad para los que vuelven al refugio
                    if (!haciaRiesgo && colaVuelta.peek() == h) {
                        return; // Soy de vuelta y estoy el primero: cruzo
                    }
                } else {
                    // No hay humanos volviendo: pueden cruzar los de ida
                    if (haciaRiesgo && colaIda.peek() == h) {
                        return; // Soy de ida y estoy el primero: cruzo
                    }
                }
            }

            wait();
        }
    }

    public synchronized void cruzar(Humano h, boolean haciaRiesgo)  {

        try {
            if (haciaRiesgo) {
                colaIda.remove();
                this.vistaTunel.actualizarLista(h.getIdHumano(), "ida", false);

            } else {
                colaVuelta.remove();
                this.vistaTunel.actualizarLista(h.getIdHumano(), "vuelta", false);

            }
            humanosEnTunel.add(h);
            this.vistaTunel.actualizarTunel(h.getIdHumano());
            if (haciaRiesgo){log.escribir(h.getIdHumano()+" cruzando hacia Riesgo por tunel "+this.id);}
            else{log.escribir(h.getIdHumano()+" cruzando hacia el Refugio por tunel "+this.id);}

            Thread.sleep(1000); //lo que tarda en cruzar el tunel
            
            
           
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }finally{
            humanosEnTunel.remove(h);
            this.vistaTunel.actualizarTunel(null);
            notifyAll();
        }
    }
    

    public synchronized int getNumHumanosBarrera(){
 
         return this.humanosEnBarrera.size();

        
    }


    public synchronized int getNumHumanosEsperandoIda(){

        return this.colaIda.size();
       
        
    }
    
    public synchronized int getNumHumanosEsperandoVuelta(){

            return this.colaVuelta.size();
      
        
    }
    
    public synchronized boolean todosEsperandoBarrera(){ //metodo para comprobar si la humanidad ha terminado
        return this.colaIda.isEmpty() && this.colaVuelta.isEmpty() && this.humanosEnTunel.isEmpty();
    }
    
}
