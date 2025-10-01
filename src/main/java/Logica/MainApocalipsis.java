/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Distribuido.ObjetoRemoto;
import Interfaz.ApocalipsisFrame;
import java.awt.Dimension;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Lucía
 */
public class MainApocalipsis {
    public static void main(String[] args) {
       
        Random random = new Random();
        LogApocalipsis log = new LogApocalipsis();

        Refugio refugio = new Refugio(log);
        Riesgo riesgo = new Riesgo(log);

        Tunel tunel1 = new Tunel(1, log);
        Tunel tunel2 = new Tunel(2, log);
        Tunel tunel3 = new Tunel(3, log);
        Tunel tunel4 = new Tunel(4, log);

        List<Tunel> tuneles = new ArrayList<>();
        tuneles.add(tunel1);
        tuneles.add(tunel2);
        tuneles.add(tunel3);
        tuneles.add(tunel4);
        
        Pausa pausa = new Pausa();
        try{
            ObjetoRemoto objeto = new ObjetoRemoto(refugio, tuneles, riesgo, pausa);
            LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/ObjetoApocalipsis", objeto);        
        } catch(Exception e){
            e.printStackTrace();
        }



        // Crear y mostrar la interfaz
        javax.swing.SwingUtilities.invokeLater(() -> {
            ApocalipsisFrame apocalipsis = new ApocalipsisFrame(refugio, riesgo, tuneles, pausa);
            apocalipsis.setSize(new Dimension(865, 630));
            apocalipsis.setVisible(true);
        
            
            // Generador escalonado de humanos (en hilo independiente)
            new Thread(() -> {
                long maxHumanos = 10000;
                long contadorHumanos = 1;

                // Paciente cero
                Zombi zombi = new Zombi(log, riesgo, "Z0000", -1, pausa);
                zombi.start();

                for (long i = 0; i < maxHumanos; i++) {
                    String id = String.format("H%04d", contadorHumanos++);
                    Humano humano = new Humano(log, refugio, riesgo, tuneles, id, pausa);
                    humano.start();

                    try {
                        Thread.sleep(random.nextInt(500, 2000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });
    }
}
