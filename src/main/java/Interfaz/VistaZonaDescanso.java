/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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
public class VistaZonaDescanso extends JPanel{
    private final DefaultListModel<String> listaModel;
    private final JList<String> listaVisual;
    private final String id;
    
    public VistaZonaDescanso() {
        this.id = "ZonaDescanso";
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(224,130));

        JLabel titulo = new JLabel("Zona Descanso", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        add(titulo, BorderLayout.NORTH);
        
        listaModel = new DefaultListModel<>();
        listaVisual = new JList<>(listaModel);
        listaVisual.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(listaVisual);
        scrollPane.setPreferredSize(new Dimension(224,135));
        add(scrollPane, BorderLayout.CENTER);
    }
    public String getId(){
        return this.id;
    }
    public void actualizarListaHumanos(String idHumano, boolean annadir) {

        SwingUtilities.invokeLater(() -> {
            if (annadir){
                listaModel.addElement(idHumano);
            }
            else{
                listaModel.removeElement(idHumano);
            }
        });
    }

    
}
