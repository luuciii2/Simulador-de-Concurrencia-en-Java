/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.List;
import java.util.Random;

/**
 *
 * @author Lucía
 */
public class Humano extends Thread{
    
    //log compartido
    private LogApocalipsis log;
    //espacios compartidos con los demas hilos
    private Refugio refugio;
    private Riesgo riesgo;
    private List<Tunel> tuneles;
    private AreaInsegura areaInseguraActual;
    //clase pausa
    private Pausa pausa;
    //variables 
    private final String idHumano;
    private boolean marcado;
    private volatile  boolean vivo;
    private int comidaRecolectada; //numero de unidades de comida que ha recolectado en su ultima visita a ZonaRiesgo
    private boolean enCombate; //T si está siendo atacado
    private Random random;
    private boolean esperandoComer;
    

    public Humano(LogApocalipsis log, Refugio refugio, Riesgo riesgo, List<Tunel> tuneles, String idHumano, Pausa pausa) {
        this.log = log;
        this.refugio = refugio;
        this.riesgo = riesgo;
        this.tuneles = tuneles;
        this.idHumano = idHumano;
        this.marcado = false;
        this.vivo = true;
        this.comidaRecolectada = 0;
        this.enCombate = false;
        this.random = new Random();
        this.pausa = pausa;
        this.esperandoComer = false;
    }

    public boolean isEsperandoComer() {
        return esperandoComer;
    }

    public void setEsperandoComer(boolean esperandoComer) {
        this.esperandoComer = esperandoComer;
    }
    

    public LogApocalipsis getLog() {
        return log;
    }

    public Refugio getRefugio() {
        return refugio;
    }

    public Riesgo getRiesgo() {
        return riesgo;
    }

    public List<Tunel> getTuneles() {
        return tuneles;
    }

    public String getIdHumano() {
        return idHumano;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    public int getComidaRecolectada() {
        return comidaRecolectada;
    }

    public void setComidaRecolectada(int comidaRecolectada) {
        this.comidaRecolectada = comidaRecolectada;
    }

    public boolean isEnCombate() {
        return enCombate;
    }

    public void setEnCombate(boolean enCombate) {
        this.enCombate = enCombate;
    }


    
    public void run(){
        while (vivo){
            try{
                

                log.escribir("soy "+idHumano + " y estoy empezando el ciclo");
                pausa.esperarSiPausado();
            
                //lo primero que hace el humano es acceder a la zona comun
                refugio.getZonaComun().accederZonaComun(this); 
                pausa.esperarSiPausado();
                Thread.sleep(random.nextInt(1000, 2001));
                pausa.esperarSiPausado();
                int tunelElegido = refugio.getZonaComun().seleccionarTunel();
                refugio.getZonaComun().salirZonaComun(this);
                
                
                Tunel t = this.tuneles.get(tunelElegido - 1);
                t.esperarGrupo(this);
                pausa.esperarSiPausado();
                t.entrarColaIda(this);   
                pausa.esperarSiPausado();
                t.esperarTurnoCruce(this, true);  
                pausa.esperarSiPausado();
                t.cruzar(this, true); 
                
                this.areaInseguraActual = riesgo.getAreasInseguras().get(tunelElegido-1);
                areaInseguraActual.accederAreaInseguraH(this);
                pausa.esperarSiPausado();
                try{
                    Thread.sleep(random.nextInt(3000,5001));
                }catch(InterruptedException ie){
                    //en este caso no hacemos nada porque lo que ha ocurrido es que el zombi ha atacado al humano mientras estaba en AreaInsegura
                }

                pausa.esperarSiPausado();
                areaInseguraActual.recolectarComida(2, this);
                
                pausa.esperarSiPausado();
                areaInseguraActual.salirAreaInseguraH(this);
                pausa.esperarSiPausado();
                
                t.entrarColaVuelta(this);
                pausa.esperarSiPausado();
                t.esperarTurnoCruce(this, false);  
                pausa.esperarSiPausado();
                t.cruzar(this, false);
                
                pausa.esperarSiPausado();
                
                //acceder al comedor para depositar la comida, se ha puesto un sleep para que sea mas realista
                refugio.getZonaComedor().accederZonaComedor(this);
                Thread.sleep(random.nextInt(2000,4001));
                pausa.esperarSiPausado();
                refugio.getZonaComedor().depositarComida(this);
                pausa.esperarSiPausado();
                refugio.getZonaComedor().salirZonaComedor(this);
                
                refugio.getZonaDescanso().accederZonaDescanso(this);
                pausa.esperarSiPausado();
                Thread.sleep(random.nextInt(2000,4001));
                pausa.esperarSiPausado();
                refugio.getZonaDescanso().salirZonaDescanso(this);
                
                refugio.getZonaComedor().accederZonaComedor(this);
                pausa.esperarSiPausado();
                refugio.getZonaComedor().comer(this);
                Thread.sleep(random.nextInt(3000,5001));
                pausa.esperarSiPausado();
                refugio.getZonaComedor().salirZonaComedor(this);
                
                if(marcado){
                
                    refugio.getZonaDescanso().accederZonaDescanso(this);
                    pausa.esperarSiPausado();
                    Thread.sleep(random.nextInt(3000,5001));
                    pausa.esperarSiPausado();
                    refugio.getZonaDescanso().salirZonaDescanso(this);
                    
                    this.marcado = false; //se ha recuperado del combate
                }
           
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }
            catch(HumanoMuertoException e){
                
                return; //salir del run 
            }
        }    
    }



    
    public boolean recibirAtaque(Zombi z, long duracion) {
    try {
        pausa.esperarSiPausado();
        z.getPausa().esperarSiPausado();
        Thread.sleep(duracion);
        Random r = new Random();
        boolean exito = r.nextInt(3) < 2; // 0,1 → éxito; 2 → muere

        pausa.esperarSiPausado();
        z.getPausa().esperarSiPausado();        
        if (exito){
            this.marcado = true;
            pausa.esperarSiPausado();
            z.getPausa().esperarSiPausado();            
            return true;
            
        }
        else{
            this.vivo = false;
            pausa.esperarSiPausado();
            z.getPausa().esperarSiPausado();                      
            //morir y crear un zombi
            
            String nuevoId = "Z" + this.idHumano.substring(1);
            Zombi nuevoZombi = new Zombi(this.log, this.riesgo,nuevoId,this.areaInseguraActual.getId(), this.pausa);
            nuevoZombi.start();
            
            return false;
            
        }

    } catch (InterruptedException e) {
        return true; //el ataque no se ha completado (en principio esta excepcion no salta)
    } finally {
        synchronized(this){
            enCombate = false;
            pausa.esperarSiPausado();
            z.getPausa().esperarSiPausado();              
            this.notifyAll(); //notificar a los humanos esperando para salir de area insegura (ya no esta en combate y puede)

            this.areaInseguraActual.finAtaque(z, this); //sacarlos del HashMap "combates"
        }
    }
        
}

}
