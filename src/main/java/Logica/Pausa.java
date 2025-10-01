/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

/**
 *
 * @author Lucía
 */
public class Pausa {
    private boolean enPausa = false;
    private boolean enPausaRemoto = false;
    


    public synchronized void pausar(boolean remoto) {
        if (remoto){enPausaRemoto = true;}
        else{enPausa = true;}
        
        
    }

    public synchronized void reanudar(boolean remoto) {
        if (remoto){enPausaRemoto = false;}
        else{enPausa = false;}
        notifyAll();  // Despierta a todos los hilos en espera
    }
    

    public synchronized void esperarSiPausado() {
        while (enPausa || enPausaRemoto) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
