/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Distribuido;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.rmi.Naming;

/**
 *
 * @author Lucía
 */
public class ClienteApocalipsis {
    
    private static ClienteApocalipsisFrame clienteApocalipsis = new ClienteApocalipsisFrame();

    public static void main(String[] args) {
        try {
            
            javax.swing.SwingUtilities.invokeLater(() -> {
                //clienteApocalipsis.setSize(new Dimension(865, 530));
                clienteApocalipsis.setSize(670, 570); // ajusta según tu layout

                // Calcula la resolución de pantalla
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

                // Coloca la ventana a la derecha
                int x = screenSize.width - clienteApocalipsis.getWidth();  // completamente a la derecha
                int y = 0; // parte superior de la pantalla

                clienteApocalipsis.setLocation(x, y);
                clienteApocalipsis.setVisible(true);
            });

            
        } catch (Exception e) {
                e.printStackTrace();
        }
        
        
        new Thread(() -> {
            try {
                InterfazRemota objeto = (InterfazRemota) Naming.lookup("//localhost/ObjetoApocalipsis"); 
                while (true) {
                    // Obtener datos del sistema
                    int[] humanosRefugio = objeto.getHumanosRefugio();
                    int[] humanosBarrera = objeto.getHumanosBarrera();
                    int[] humanosEsperandoIda = objeto.getHumanosEsperandoIda();
                    int[] humanosEsperandoVuelta = objeto.getHumanosEsperandoVuelta();
                    int[] humanosAreasInseguras = objeto.getHumanosAreasInseguras();
                    int[] zombisAreasInseguras = objeto.getZombisAreasInseguras();
                    String[] topZombis = objeto.getTopZombisLetales(3);
                    // Actualizar la vista
                    clienteApocalipsis.actualizarFrame(humanosRefugio, humanosBarrera, humanosEsperandoIda, humanosEsperandoVuelta, humanosAreasInseguras,
                                                       zombisAreasInseguras, topZombis);
                    if (clienteApocalipsis.isPausa()){objeto.pausar();}
                    else{objeto.reanudar();}

                    // Esperar 2 segundos antes de volver a consultar
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start(); 
       
       
    }
}
