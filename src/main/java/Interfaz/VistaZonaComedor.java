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
public class VistaZonaComedor extends JPanel{
    private final DefaultListModel<String> listaModel;
    private final JList<String> listaVisual;
    private final String id;

    private final JLabel etiquetaComida;
    
    public VistaZonaComedor() {
        this.id = "ZonaComedor";
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(224,130));
        
        JLabel titulo = new JLabel("Zona Comedor", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        add(titulo, BorderLayout.NORTH);
        // Panel izquierdo: lista de humanos comiendo
        listaModel = new DefaultListModel<>();
        listaVisual = new JList<>(listaModel);
        listaVisual.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(listaVisual);
        

        // Panel inferior: comida disponible
        etiquetaComida = new JLabel();
        etiquetaComida.setFont(new Font("SansSerif", Font.BOLD, 14));
        etiquetaComida.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(etiquetaComida, BorderLayout.CENTER);

        // Añadir al panel principal
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
    public String getId(){
        return this.id;
    }

    public void actualizarComida(int valor) {
        SwingUtilities.invokeLater(() -> {
            etiquetaComida.setText("Comida: "+valor);
        });
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
