/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Interfaz.VistaAreaInsegura;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Lucía
 */
public class AreaInsegura {
    private LogApocalipsis log;
    private Riesgo riesgo;
    private VistaAreaInsegura vistaAreaInsegura;
    private final int id; //para saber cual es
    private List<Humano> humanos;
    private List<Zombi> zombies;
    private HashMap<Zombi,Humano> combates; //hash map para guardar los combates que se estan llevando a cabo

    public AreaInsegura(Riesgo riesgo, LogApocalipsis log, int id) {
        this.riesgo = riesgo;
        this.humanos = new ArrayList<>();
        this.zombies = new ArrayList<>();
        this.combates = new HashMap<>();
        this.log = log;
        this.id = id;
    }
    
    public void añadirObservador (VistaAreaInsegura vistaAreaInsegura){
        this.vistaAreaInsegura = vistaAreaInsegura;
    }

    public Riesgo getRiesgo() {
        return riesgo;
    }

    public void setRiesgo(Riesgo riesgo) {
        this.riesgo = riesgo;
    }

    public List<Humano> getHumanos() {
        return humanos;
    }

    public void setHumanos(List<Humano> humanos) {
        this.humanos = humanos;
    }

    public List<Zombi> getZombies() {
        return zombies;
    }

    public void setZombies(List<Zombi> zombies) {
        this.zombies = zombies;
    }

    public int getId() {
        return id;
    }

    public HashMap<Zombi, Humano> getCombates() {
        return combates;
    }
    
    

    public synchronized void accederAreaInseguraH(Humano h) {
            humanos.add(h);

        
            log.escribir(h.getIdHumano()+" acaba de entrar a AreaInsegura "+this.id);
            this.vistaAreaInsegura.actualizarListaHumanos(h.getIdHumano(), true);
        
    }

    public void recolectarComida(int cantidadComida, Humano h){
        if (!h.isMarcado()){h.setComidaRecolectada(cantidadComida);}
         
    }
    
    public synchronized void salirAreaInseguraH(Humano h) throws HumanoMuertoException{
        

        synchronized(h){//usamos monitor para proteger enCombate ya que esta variable es accedida desde distintas clases
            while(h.isEnCombate()){
                try{
                    h.wait(); //esperar hasta que acabe el combate para poder salir de AreaInsegura
                }catch(InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        }
        
        if (!h.isVivo()){

            throw new HumanoMuertoException(h.getIdHumano());
        }
        
        if(h.isMarcado()){
            h.setComidaRecolectada(0);
            log.escribir(h.getIdHumano()+" acaba de salir de AreaInsegura "+this.id);
            return;
        }
        
        humanos.remove(h);
            this.vistaAreaInsegura.actualizarListaHumanos(h.getIdHumano(), false);

        
        log.escribir(h.getIdHumano()+" acaba de salir de AreaInsegura "+this.id);
        }
    public void accederAreaInseguraZ(Zombi z) {
        synchronized (zombies) {
            zombies.add(z);
        }

        this.vistaAreaInsegura.actualizarListaZombis(z.getIdZombi(), true);

    }
    public void salirAreaInseguraZ(Zombi z) {
        synchronized (zombies) {
            zombies.remove(z);
        }
        this.vistaAreaInsegura.actualizarListaZombis(z.getIdZombi(), false);
    }
    
    public synchronized Humano seleccionarHumanoAleatorio(Zombi z) { //si no hay humanos o el seleccionado aleatoriamente esta en combate --> Se retorna null
            
            
            if (humanos.isEmpty()) return null;
            int i = new Random().nextInt(humanos.size());
            Humano hRandom = humanos.get(i);
            
            synchronized (hRandom){ //usamos monitor para proteger enCombate ya que esta variable es accedida desde distintas clases

                    hRandom.setEnCombate(true); 
                    humanos.remove(hRandom);
                    this.vistaAreaInsegura.actualizarListaHumanos(hRandom.getIdHumano(), false);
                    synchronized(combates){
                        combates.put(z, hRandom);
                        String combate = z.getIdZombi() + " vs "+ combates.get(z).getIdHumano();
                        this.vistaAreaInsegura.actualizarCombates(combate, true);
                    }
                    
                    
                    return hRandom;
                
            }
            
    }
    
    public  void finAtaque(Zombi z,Humano h){
        synchronized (combates){
            combates.remove(z,h);
            String combate = z.getIdZombi() + " vs "+ h.getIdHumano();
            
            this.vistaAreaInsegura.actualizarCombates(combate, false);
            if (!h.isVivo()){this.vistaAreaInsegura.notificarMuerte(h.getIdHumano());}
        }
        
    }
    
    
}
