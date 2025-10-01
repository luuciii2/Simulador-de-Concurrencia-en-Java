/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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
public class VistaTunel extends JPanel {
    private final DefaultListModel<String> modeloColaIda = new DefaultListModel<>();
    private final DefaultListModel<String> modeloColaVuelta = new DefaultListModel<>();
    private final DefaultListModel<String> modeloBarrera = new DefaultListModel<>();
    private final JLabel etiquetaTunel = new JLabel();

    public VistaTunel(JPanel panelColaIda, JPanel panelColaVuelta, JPanel panelBarrera, JPanel panelTunel) {
        // Configurar listas visuales
        JList<String> listaIda = new JList<>(modeloColaIda);
        JList<String> listaVuelta = new JList<>(modeloColaVuelta);
        JList<String> listaBarrera = new JList<>(modeloBarrera);
        
        listaIda.setFont(new Font("Monospaced", Font.PLAIN, 12));
        listaVuelta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        listaBarrera.setFont(new Font("Monospaced", Font.PLAIN, 12));
        etiquetaTunel.setFont(new Font("Monospaced", Font.BOLD, 14));
        etiquetaTunel.setHorizontalAlignment(SwingConstants.CENTER);

        
        JScrollPane scrollIda = new JScrollPane(listaIda);
        JScrollPane scrollVuelta = new JScrollPane(listaVuelta);
        JScrollPane scrollBarrera = new JScrollPane(listaBarrera);

        scrollIda.setBorder(BorderFactory.createTitledBorder("Esperando Ida"));
        scrollVuelta.setBorder(BorderFactory.createTitledBorder("Esperando Vuelta"));
        scrollBarrera.setBorder(BorderFactory.createTitledBorder("Barrera"));
        etiquetaTunel.setBorder(BorderFactory.createTitledBorder("En túnel"));
            
        Dimension tamanioBarrera = new Dimension(81, 71);
        Dimension tamanioCola = new Dimension(81, 71);
        Dimension tamanioTunel = new Dimension(81,71);
        
        panelColaIda.setLayout(new BorderLayout());
        panelColaIda.add(scrollIda, BorderLayout.CENTER);
        panelColaIda.setPreferredSize(tamanioCola);
        
        panelColaVuelta.setLayout(new BorderLayout());
        panelColaVuelta.add(scrollVuelta, BorderLayout.CENTER);
        panelColaVuelta.setPreferredSize(tamanioCola);
        
        panelBarrera.setLayout(new BorderLayout());
        panelBarrera.add(scrollBarrera, BorderLayout.CENTER);
        panelBarrera.setPreferredSize(tamanioBarrera);
        
        panelTunel.setLayout(new BorderLayout());
        panelTunel.add(etiquetaTunel, BorderLayout.CENTER);
        panelTunel.setPreferredSize(tamanioTunel);

    }

    public void actualizarLista(String idHumano, String tipo, boolean añadir) {
        SwingUtilities.invokeLater(() -> {
            DefaultListModel<String> modelo = switch (tipo.toLowerCase()) {
                case "ida" -> modeloColaIda;
                case "vuelta" -> modeloColaVuelta;
                default -> null;
            };
            if (modelo == null) return;

            if (añadir && !modelo.contains(idHumano)) modelo.addElement(idHumano);
            else if (!añadir) modelo.removeElement(idHumano);
        });
    }
    public void actualizarBarrera(String idHumano, boolean annadir) {
        SwingUtilities.invokeLater(() -> {
            if (annadir) {
                modeloBarrera.addElement(idHumano);

            } else{
                modeloBarrera.clear(); // limpiar toda la lista porque se ha levantado la barrera
            }
        });
    }
    public void actualizarTunel(String idHumano) {
        SwingUtilities.invokeLater(() -> {
            etiquetaTunel.setText(idHumano == null ? "" : idHumano);
        });
    }
}
