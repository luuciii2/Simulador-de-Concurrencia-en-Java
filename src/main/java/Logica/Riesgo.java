/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Lucía
 */
public class Riesgo {
    
    //log compartido
    private LogApocalipsis log;
    
    //areas dentro de la zona de riesgo
    private List<AreaInsegura> areasInseguras;

    private List<Zombi> zombis; 
            
    public Riesgo(LogApocalipsis log) {
        this.log = log;
        this.areasInseguras = new ArrayList<>();
        for (int i=0; i<4;i++){
            areasInseguras.add(new AreaInsegura(this,log,i+1));
        }
        this.zombis = new ArrayList<>();
    }
    public void annadirZombi(Zombi z){ //aqui se guardan todos los zombis existentes
        synchronized (zombis){
            zombis.add(z);
            zombis.sort(Comparator.comparingInt(Zombi::getMuertesCausadas).reversed());
        }
    }
    public LogApocalipsis getLog() {
        return log;
    }

    public void setLog(LogApocalipsis log) {
        this.log = log;
    }

    public List<AreaInsegura> getAreasInseguras() {
        return areasInseguras;
    }
    
    //metodo para que los zombies vayan cambiando de area insegura
    public AreaInsegura seleccionarAreaInsegura(int actual) {
        if (actual == -1) {
            // Primera vez (Z000), aún no tiene area asignada
            return this.areasInseguras.get(new Random().nextInt(4));
        }

        AreaInsegura areaActual = this.areasInseguras.get(actual-1);

        List<AreaInsegura> candidatas = new ArrayList<>(this.areasInseguras);
        candidatas.remove(areaActual); // elimina la actual
        
        //se devuelve una random que no sea la actual, para que el zombi se vaya cambiando de area
        int idx = new Random().nextInt(candidatas.size());
        return candidatas.get(idx);
    }
    
    public int [] humanosAreasInseguras(){ 
        int[] humanos = new int[4];
        for (int i = 0; i<4; i++){
            AreaInsegura area = this.areasInseguras.get(i);
            humanos[i] = area.getHumanos().size() + area.getCombates().size();
        }
        return humanos;
    }
    public int [] zombisAreasInseguras(){
        int[] zombis = new int[4];
        for (int i = 0; i<4; i++){
            AreaInsegura area = this.areasInseguras.get(i);
            zombis[i] = area.getZombies().size();
        }
        return zombis;
    }   
    
    public String[] rankingZombis(int top) {
        int limite = Math.min(top, this.zombis.size());
        String[] ranking = new String[limite];

        for (int i = 0; i < limite; i++) {
            Zombi z = this.zombis.get(i);
            ranking[i] = z.getIdZombi() + " - " + z.getMuertesCausadas() + " muertes";
        }

        return ranking;
    }
}
