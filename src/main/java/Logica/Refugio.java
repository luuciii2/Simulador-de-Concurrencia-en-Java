/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

/**
 *
 * @author Lucía
 */
public class Refugio {
    
    //log compartido
    private LogApocalipsis log;
    //zonas dentro del refugio
    private ZonaComedor zonaComedor;
    private ZonaComun zonaComun;
    private ZonaDescanso zonaDescanso;
    //variables

    public Refugio(LogApocalipsis log) {
        this.log = log;
        this.zonaComedor = new ZonaComedor(this, log);
        this.zonaComun = new ZonaComun(this, log);
        this.zonaDescanso = new ZonaDescanso(this, log);
    }

    public LogApocalipsis getLog() {
        return log;
    }

    public void setLog(LogApocalipsis log) {
        this.log = log;
    }

    public ZonaComedor getZonaComedor() {
        return zonaComedor;
    }

    public void setZonaComedor(ZonaComedor zonaComedor) {
        this.zonaComedor = zonaComedor;
    }

    public ZonaComun getZonaComun() {
        return zonaComun;
    }

    public void setZonaComun(ZonaComun zonaComun) {
        this.zonaComun = zonaComun;
    }

    public ZonaDescanso getZonaDescanso() {
        return zonaDescanso;
    }

    public void setZonaDescanso(ZonaDescanso zonaDescanso) {
        this.zonaDescanso = zonaDescanso;
    }
    
    public int[] getNumHumanos(){ //sumamos los humanos de todas las zonas. Metodo utilizado para el Cliente RMI.
        int[] humanos = new int[4];
        humanos[0] = this.zonaComedor.getHumanos().size();
        humanos[1] = this.zonaComun.getHumanos().size();
        humanos[2] = this.zonaDescanso.getHumanos().size();
        humanos[3] = this.zonaComedor.getHumanos().size() + this.zonaComun.getHumanos().size() + this.zonaDescanso.getHumanos().size();
        return  humanos;
    }

    public boolean todosEsperandoComer (){ //este metodo se usa para comprobar si la humanidad ha muerto
        boolean todosEsperando = true;
        if (this.zonaComun.getHumanos().isEmpty() && this.zonaDescanso.getHumanos().isEmpty()){
            int i=0;
            while (todosEsperando && i<this.zonaComedor.getHumanos().size()){
                if (!this.zonaComedor.getHumanos().get(i).isEsperandoComer()){todosEsperando = false;}
                i++;
            }
        }
        return todosEsperando;
    }
    
}
