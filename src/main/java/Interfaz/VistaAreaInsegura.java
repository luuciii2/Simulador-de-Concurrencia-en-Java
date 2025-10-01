/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author Lucía
 */
public class VistaAreaInsegura extends JPanel  {
    
    private final DefaultListModel<String> modeloHumanos;
    private final JList<String> listaHumanos;
    private final DefaultListModel<String> modeloZombis;
    private final JList<String> listaZombis;    
    private final DefaultListModel<String> modeloCombates;
    private final JList<String> listaCombates;
    private final String id;
    private final JPanel panelCombates;
    private JPanel humanoMuerto;
    
    public VistaAreaInsegura(int id, JPanel panelCombates, JPanel humanoMuerto) {
        this.humanoMuerto = humanoMuerto;
        
        this.id = "AreaInsegura" + id;
        this.panelCombates = panelCombates;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 110));

        // TÍTULO SUPERIOR 
        JLabel titulo = new JLabel(this.id, SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        add(titulo, BorderLayout.NORTH);

        //  MODELOS Y LISTAS 
        modeloHumanos = new DefaultListModel<>();
        modeloZombis = new DefaultListModel<>();
        modeloCombates = new DefaultListModel<>();

        this.listaHumanos = new JList<>(modeloHumanos);
        this.listaZombis = new JList<>(modeloZombis);
        listaHumanos.setFont(new Font("Monospaced", Font.PLAIN, 11));
        listaZombis.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollHumanos = new JScrollPane(listaHumanos);
        JScrollPane scrollZombis = new JScrollPane(listaZombis);
        scrollHumanos.setBorder(BorderFactory.createTitledBorder("Humanos"));
        scrollZombis.setBorder(BorderFactory.createTitledBorder("Zombis"));

        scrollHumanos.setPreferredSize(new Dimension(140, 80));
        scrollZombis.setPreferredSize(new Dimension(140, 80));

        //  PANEL CENTRAL CON LAS DOS LISTAS
        JPanel panelListas = new JPanel(new GridLayout(1, 2));
        panelListas.add(scrollHumanos);
        panelListas.add(scrollZombis);
        add(panelListas, BorderLayout.CENTER);

        //  COMBATES 
        this.listaCombates = new JList<>(modeloCombates);
        listaCombates.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollCombates = new JScrollPane(listaCombates);
        scrollCombates.setPreferredSize(new Dimension(140, 100));
        scrollCombates.setBorder(BorderFactory.createTitledBorder("Combates"));

        panelCombates.setLayout(new BorderLayout());
        panelCombates.add(scrollCombates, BorderLayout.CENTER);
    }

    public String getId() {
            return this.id;
    }

    public void notificarMuerte(String idHumano){
        humanoMuerto.removeAll();
        humanoMuerto.setLayout(new BorderLayout());
        humanoMuerto.setPreferredSize(new Dimension(275, 72));
        JLabel label = new JLabel(idHumano + " ha muerto");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        humanoMuerto.add(label);
        humanoMuerto.revalidate();
        humanoMuerto.repaint();

        // Crear un hilo que elimine el label después de 3 segundos
        new Thread(() -> {
            try {
                Thread.sleep(3000); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            SwingUtilities.invokeLater(() -> {  
                humanoMuerto.remove(label);
                humanoMuerto.revalidate();
                humanoMuerto.repaint();
            });
        }).start();
    }
    public void actualizarListaHumanos(String idHumano, boolean annadir) {
            SwingUtilities.invokeLater(() -> {
                if (annadir){
                    modeloHumanos.addElement(idHumano);
                }
                else{
                    modeloHumanos.removeElement(idHumano);
                }
            });   
    }

    
    public void actualizarListaZombis(String idZombi, boolean annadir) {
        SwingUtilities.invokeLater(() -> {
            if (annadir){
                modeloZombis.addElement(idZombi);
            }
            else{
                modeloZombis.removeElement(idZombi);
            }
        });       
    }

    
    public void actualizarCombates(String combate, boolean annadir) {
        SwingUtilities.invokeLater(() -> {
            if (annadir){
                modeloCombates.addElement(combate);
            }
            else{
                modeloCombates.removeElement(combate);
            }
         });         
    }
    
}
