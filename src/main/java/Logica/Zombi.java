/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.Random;

/**
 *
 * @author Lucía
 */
public class Zombi extends Thread{
    //log compartido
    private LogApocalipsis log;
    //espacios compartidos con los demas hilos
    private Riesgo riesgo;
    //variables
    private final String idZombi;
    private int muertesCausadas;
    private int actual;//refleja el area inicial, que sera -1 para el Z000 y para los demas el area en el que murio su humano
    private boolean inicio; //relfeja si es el inicio de su creacion 
    private Random random;
    private Pausa pausa;

    public Zombi(LogApocalipsis log, Riesgo riesgo, String idZombi, int actual, Pausa pausa) {
        this.log = log;
        this.riesgo = riesgo;
        this.idZombi = idZombi;
        this.muertesCausadas = 0;
        this.random = new Random();
        this.actual = actual;
        this.inicio = true;
        this.pausa = pausa;
    }

    public Pausa getPausa() {
        return pausa;
    }
    

    public LogApocalipsis getLog() {
        return log;
    }

    public Riesgo getRiesgo() {
        return riesgo;
    }

    public String getIdZombi() {
        return idZombi;
    }

    public int getMuertesCausadas() {
        return muertesCausadas;
    }

    public void setMuertesCausadas(int muertesCausadas) {
        this.muertesCausadas = muertesCausadas;
    }
    
    public void run(){
        riesgo.annadirZombi(this);
        log.escribir("Nuevo zombi: "+idZombi);
        while(true){
            try{
                pausa.esperarSiPausado();
                AreaInsegura areaActual;
                if(inicio && actual != -1){ //si es un humano que se acaba de comvertir en zombi, para que nazca donde murio el humano
                    areaActual = riesgo.getAreasInseguras().get(actual-1);
                    this.inicio = false;
                    
                }
                else{ //si es el Z000 o si no es la primera iteracion del while
                    areaActual = riesgo.seleccionarAreaInsegura(actual);
                    actual = areaActual.getId();
                    
                }

                areaActual.accederAreaInseguraZ(this);
                pausa.esperarSiPausado();
                Humano humanoObjetivo = areaActual.seleccionarHumanoAleatorio(this);
                if (humanoObjetivo != null){ 
                    pausa.esperarSiPausado();
                    atacarHumano(humanoObjetivo);
                    Thread.sleep(random.nextInt(2000, 3001));
                }
                else{
                    pausa.esperarSiPausado();
                    Thread.sleep(random.nextInt(2000, 3001));
                }
                pausa.esperarSiPausado();
                areaActual.salirAreaInseguraZ(this);
            
            
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }
        }
    }
    
    public void atacarHumano(Humano h){
        long duracion = random.nextInt(500,1501);
        log.escribir("COMBATE: "+this.idZombi+"-"+h.getIdHumano());
        
        pausa.esperarSiPausado();

        boolean sobrevive = h.recibirAtaque(this, duracion); // método en Humano
        
        if (!sobrevive) {
            this.muertesCausadas++;
            log.escribir("RESULTADO COMBATE "+this.idZombi+"-"+h.getIdHumano()+": HUMANO MUERTO\nNumero de muertes de "+this.idZombi+": "+this.muertesCausadas);
        }
        else{
            log.escribir("RESULTADO COMBATE "+this.idZombi+"-"+h.getIdHumano()+": HUMANO VIVO");
        }
    
    }
    
    
}
