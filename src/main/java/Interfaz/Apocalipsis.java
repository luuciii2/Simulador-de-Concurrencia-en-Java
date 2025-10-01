/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Interfaz;

import Logica.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author Lucía
 */
public class Apocalipsis extends javax.swing.JPanel {
    private Refugio refugio;
    private Riesgo riesgo;
    private List<Tunel> tuneles;
    private Pausa pausa;
    /**
     * Creates new form Apocalipsis
     */
    public Apocalipsis() {
        initComponents();
    }
    
    
    public void conectarPausa(Pausa pausa) {
        this.pausa = pausa;
    }
    
    
    public void conectarRefugio(Refugio refugio){
        this.refugio = refugio;
        
        JLabel tituloRefugio = new JLabel("REFUGIO", SwingConstants.CENTER);
        tituloRefugio.setFont(new Font("SansSerif", Font.BOLD, 14));
        jPanelRefugio.setMinimumSize(new Dimension(159, 488));
        jPanelRefugio.setMaximumSize(new Dimension(159, 488));

        jPanelRefugio.setLayout(new BorderLayout());
        jPanelRefugio.setPreferredSize(new Dimension(159, 488));
        jPanelRefugio.add(tituloRefugio, BorderLayout.NORTH);

        VistaZonaDescanso vistaZonaDescanso = new VistaZonaDescanso();
        refugio.getZonaDescanso().añadirObservador(vistaZonaDescanso);
        jPanelZonaDescanso.setLayout(new BorderLayout());
        jPanelZonaDescanso.add(vistaZonaDescanso, BorderLayout.CENTER);
        jPanelZonaDescanso.revalidate();
        jPanelZonaDescanso.repaint();

        VistaZonaComedor vistaZonaComedor = new VistaZonaComedor();
        refugio.getZonaComedor().añadirObservador(vistaZonaComedor);
        jPanelZonaComedor.setLayout(new BorderLayout());
        jPanelZonaComedor.add(vistaZonaComedor, BorderLayout.CENTER);
        jPanelZonaComedor.revalidate();
        jPanelZonaComedor.repaint();

        VistaZonaComun vistaZonaComun = new VistaZonaComun();
        refugio.getZonaComun().añadirObservador(vistaZonaComun);
        jPanelZonaComun.setLayout(new BorderLayout());
        jPanelZonaComun.add(vistaZonaComun, BorderLayout.CENTER);
        jPanelZonaComun.revalidate();
        jPanelZonaComun.repaint();
        

    }
    
    public void conectarRiesgo(Riesgo riesgo){
        this.riesgo = riesgo;
        
        JLabel tituloRiesgo = new JLabel("RIESGO", SwingConstants.CENTER);
        tituloRiesgo.setFont(new Font("SansSerif", Font.BOLD, 14));
       
        jPanelRiesgo.setLayout(new BorderLayout());
        jPanelRiesgo.setPreferredSize(new Dimension(275, 488));
        jPanelRiesgo.add(tituloRiesgo, BorderLayout.NORTH);
        JPanel[] panelesAreas = {
            jPanelAreaInsegura1,
            jPanelAreaInsegura2,
            jPanelAreaInsegura3,
            jPanelAreaInsegura4
        };
        JPanel[] panelesCombates = {
            jPanelCombates1,
            jPanelCombates2,
            jPanelCombates3,
            jPanelCombates4
        };
        for (int i = 0; i < riesgo.getAreasInseguras().size(); i++) {
            AreaInsegura areaInsegura = riesgo.getAreasInseguras().get(i);
            VistaAreaInsegura vistaAreaInsegura = new VistaAreaInsegura(i+1, panelesCombates[i], jPanelHumanoMuerto);

            areaInsegura.añadirObservador(vistaAreaInsegura);

            JPanel panel = panelesAreas[i];
            panel.setLayout(new BorderLayout());
            panel.add(vistaAreaInsegura, BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
            
            
        }
    }
    
    public void conectarTuneles(List<Tunel> tuneles) {
        this.tuneles = tuneles;
        
        JPanel[] panelesIda = {
            jPanelColaIda1,
            jPanelColaIda2,
            jPanelColaIda3,
            jPanelColaIda4
        };

        JPanel[] panelesVuelta = {
            jPanelColaVuelta1,
            jPanelColaVuelta2,
            jPanelColaVuelta3,
            jPanelColaVuelta4
        };

        JPanel[] panelesBarrera = {
            jPanelBarrera1,
            jPanelBarrera2,
            jPanelBarrera3,
            jPanelBarrera4
        };

        JPanel[] panelesTunel = {
            jPanelTunel1,
            jPanelTunel2,
            jPanelTunel3,
            jPanelTunel4
        };

        for (int i = 0; i < tuneles.size(); i++) {
            Tunel tunel = tuneles.get(i);

            VistaTunel vistaTunel = new VistaTunel(
                panelesIda[i],
                panelesVuelta[i],
                panelesBarrera[i],
                panelesTunel[i]
            );

            tunel.añadirObservador(vistaTunel); 
        }
    }
    
    public void comprobarHumanidadTerminada(){
        new Thread(() -> {
            while (true){
                try{
                    Thread.sleep(60000); //cada minuto comprobamos si la humanidad ha terminado
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                boolean todosEsperando = refugio.todosEsperandoComer(); //ver si todos los que hay en el refugio estan en el comedor esperando para comer
                boolean todosEnBarrera = true;
                for (int i = 0; i<4; i++){
                    if (!tuneles.get(i).todosEsperandoBarrera()){todosEnBarrera = false;} //con que haya un tunel con humanos vale
                }
                boolean nadieEnAreasInseguras = true;
                int[] humanosAreas = riesgo.humanosAreasInseguras();
                for (int j = 0; j<4; j++){
                    if (humanosAreas[j] != 0){nadieEnAreasInseguras = false;} //con que haya un area con humanos vale
                }

                if (todosEsperando && todosEnBarrera && nadieEnAreasInseguras){
                            jPanelHumanidadAcabada.setLayout(new BorderLayout());
                            jPanelHumanidadAcabada.setPreferredSize(new Dimension(200, 72));
                            JLabel label = new JLabel("LOS HUMANOS SE HAN EXTINGUIDO");
                            label.setFont(new Font("Arial", Font.BOLD, 18));
                            jPanelHumanidadAcabada.add(label);
                            jPanelHumanidadAcabada.revalidate();
                            jPanelHumanidadAcabada.repaint();
                }
            }
        }).start(); 
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelRefugio = new javax.swing.JPanel();
        jPanelZonaComun = new javax.swing.JPanel();
        jPanelZonaDescanso = new javax.swing.JPanel();
        jPanelZonaComedor = new javax.swing.JPanel();
        jPanelTunel2 = new javax.swing.JPanel();
        jPanelTunel1 = new javax.swing.JPanel();
        jPanelTunel3 = new javax.swing.JPanel();
        jPanelTunel4 = new javax.swing.JPanel();
        jPanelBarrera2 = new javax.swing.JPanel();
        jPanelBarrera4 = new javax.swing.JPanel();
        jPanelBarrera3 = new javax.swing.JPanel();
        jPanelBarrera1 = new javax.swing.JPanel();
        jPanelColaIda2 = new javax.swing.JPanel();
        jPanelColaIda1 = new javax.swing.JPanel();
        jPanelColaIda3 = new javax.swing.JPanel();
        jPanelColaIda4 = new javax.swing.JPanel();
        jPanelColaVuelta1 = new javax.swing.JPanel();
        jPanelColaVuelta2 = new javax.swing.JPanel();
        jPanelColaVuelta3 = new javax.swing.JPanel();
        jPanelColaVuelta4 = new javax.swing.JPanel();
        jPanelRiesgo = new javax.swing.JPanel();
        jPanelAreaInsegura1 = new javax.swing.JPanel();
        jPanelAreaInsegura2 = new javax.swing.JPanel();
        jPanelAreaInsegura3 = new javax.swing.JPanel();
        jPanelAreaInsegura4 = new javax.swing.JPanel();
        jPanelCombates1 = new javax.swing.JPanel();
        jPanelCombates2 = new javax.swing.JPanel();
        jPanelCombates3 = new javax.swing.JPanel();
        jPanelCombates4 = new javax.swing.JPanel();
        jToggleButtonPausa = new javax.swing.JToggleButton();
        jPanelHumanoMuerto = new javax.swing.JPanel();
        jPanelHumanidadAcabada = new javax.swing.JPanel();

        jPanelRefugio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanelZonaComun.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelZonaComunLayout = new javax.swing.GroupLayout(jPanelZonaComun);
        jPanelZonaComun.setLayout(jPanelZonaComunLayout);
        jPanelZonaComunLayout.setHorizontalGroup(
            jPanelZonaComunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 143, Short.MAX_VALUE)
        );
        jPanelZonaComunLayout.setVerticalGroup(
            jPanelZonaComunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 97, Short.MAX_VALUE)
        );

        jPanelZonaDescanso.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelZonaDescansoLayout = new javax.swing.GroupLayout(jPanelZonaDescanso);
        jPanelZonaDescanso.setLayout(jPanelZonaDescansoLayout);
        jPanelZonaDescansoLayout.setHorizontalGroup(
            jPanelZonaDescansoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 143, Short.MAX_VALUE)
        );
        jPanelZonaDescansoLayout.setVerticalGroup(
            jPanelZonaDescansoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 99, Short.MAX_VALUE)
        );

        jPanelZonaComedor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelZonaComedorLayout = new javax.swing.GroupLayout(jPanelZonaComedor);
        jPanelZonaComedor.setLayout(jPanelZonaComedorLayout);
        jPanelZonaComedorLayout.setHorizontalGroup(
            jPanelZonaComedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 143, Short.MAX_VALUE)
        );
        jPanelZonaComedorLayout.setVerticalGroup(
            jPanelZonaComedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelRefugioLayout = new javax.swing.GroupLayout(jPanelRefugio);
        jPanelRefugio.setLayout(jPanelRefugioLayout);
        jPanelRefugioLayout.setHorizontalGroup(
            jPanelRefugioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRefugioLayout.createSequentialGroup()
                .addGroup(jPanelRefugioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelZonaComun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelZonaDescanso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelZonaComedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        jPanelRefugioLayout.setVerticalGroup(
            jPanelRefugioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRefugioLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jPanelZonaComun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jPanelZonaDescanso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jPanelZonaComedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );

        jPanelTunel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelTunel2Layout = new javax.swing.GroupLayout(jPanelTunel2);
        jPanelTunel2.setLayout(jPanelTunel2Layout);
        jPanelTunel2Layout.setHorizontalGroup(
            jPanelTunel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 134, Short.MAX_VALUE)
        );
        jPanelTunel2Layout.setVerticalGroup(
            jPanelTunel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        jPanelTunel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelTunel1Layout = new javax.swing.GroupLayout(jPanelTunel1);
        jPanelTunel1.setLayout(jPanelTunel1Layout);
        jPanelTunel1Layout.setHorizontalGroup(
            jPanelTunel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 134, Short.MAX_VALUE)
        );
        jPanelTunel1Layout.setVerticalGroup(
            jPanelTunel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanelTunel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelTunel3Layout = new javax.swing.GroupLayout(jPanelTunel3);
        jPanelTunel3.setLayout(jPanelTunel3Layout);
        jPanelTunel3Layout.setHorizontalGroup(
            jPanelTunel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 136, Short.MAX_VALUE)
        );
        jPanelTunel3Layout.setVerticalGroup(
            jPanelTunel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanelTunel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelTunel4Layout = new javax.swing.GroupLayout(jPanelTunel4);
        jPanelTunel4.setLayout(jPanelTunel4Layout);
        jPanelTunel4Layout.setHorizontalGroup(
            jPanelTunel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 136, Short.MAX_VALUE)
        );
        jPanelTunel4Layout.setVerticalGroup(
            jPanelTunel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanelBarrera2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelBarrera2Layout = new javax.swing.GroupLayout(jPanelBarrera2);
        jPanelBarrera2.setLayout(jPanelBarrera2Layout);
        jPanelBarrera2Layout.setHorizontalGroup(
            jPanelBarrera2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );
        jPanelBarrera2Layout.setVerticalGroup(
            jPanelBarrera2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );

        jPanelBarrera4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelBarrera4Layout = new javax.swing.GroupLayout(jPanelBarrera4);
        jPanelBarrera4.setLayout(jPanelBarrera4Layout);
        jPanelBarrera4Layout.setHorizontalGroup(
            jPanelBarrera4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );
        jPanelBarrera4Layout.setVerticalGroup(
            jPanelBarrera4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 71, Short.MAX_VALUE)
        );

        jPanelBarrera3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelBarrera3Layout = new javax.swing.GroupLayout(jPanelBarrera3);
        jPanelBarrera3.setLayout(jPanelBarrera3Layout);
        jPanelBarrera3Layout.setHorizontalGroup(
            jPanelBarrera3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );
        jPanelBarrera3Layout.setVerticalGroup(
            jPanelBarrera3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanelBarrera1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelBarrera1Layout = new javax.swing.GroupLayout(jPanelBarrera1);
        jPanelBarrera1.setLayout(jPanelBarrera1Layout);
        jPanelBarrera1Layout.setHorizontalGroup(
            jPanelBarrera1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );
        jPanelBarrera1Layout.setVerticalGroup(
            jPanelBarrera1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanelColaIda2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelColaIda2Layout = new javax.swing.GroupLayout(jPanelColaIda2);
        jPanelColaIda2.setLayout(jPanelColaIda2Layout);
        jPanelColaIda2Layout.setHorizontalGroup(
            jPanelColaIda2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );
        jPanelColaIda2Layout.setVerticalGroup(
            jPanelColaIda2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );

        jPanelColaIda1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelColaIda1Layout = new javax.swing.GroupLayout(jPanelColaIda1);
        jPanelColaIda1.setLayout(jPanelColaIda1Layout);
        jPanelColaIda1Layout.setHorizontalGroup(
            jPanelColaIda1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );
        jPanelColaIda1Layout.setVerticalGroup(
            jPanelColaIda1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );

        jPanelColaIda3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelColaIda3Layout = new javax.swing.GroupLayout(jPanelColaIda3);
        jPanelColaIda3.setLayout(jPanelColaIda3Layout);
        jPanelColaIda3Layout.setHorizontalGroup(
            jPanelColaIda3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );
        jPanelColaIda3Layout.setVerticalGroup(
            jPanelColaIda3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );

        jPanelColaIda4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelColaIda4Layout = new javax.swing.GroupLayout(jPanelColaIda4);
        jPanelColaIda4.setLayout(jPanelColaIda4Layout);
        jPanelColaIda4Layout.setHorizontalGroup(
            jPanelColaIda4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );
        jPanelColaIda4Layout.setVerticalGroup(
            jPanelColaIda4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );

        jPanelColaVuelta1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelColaVuelta1Layout = new javax.swing.GroupLayout(jPanelColaVuelta1);
        jPanelColaVuelta1.setLayout(jPanelColaVuelta1Layout);
        jPanelColaVuelta1Layout.setHorizontalGroup(
            jPanelColaVuelta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );
        jPanelColaVuelta1Layout.setVerticalGroup(
            jPanelColaVuelta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );

        jPanelColaVuelta2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelColaVuelta2Layout = new javax.swing.GroupLayout(jPanelColaVuelta2);
        jPanelColaVuelta2.setLayout(jPanelColaVuelta2Layout);
        jPanelColaVuelta2Layout.setHorizontalGroup(
            jPanelColaVuelta2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );
        jPanelColaVuelta2Layout.setVerticalGroup(
            jPanelColaVuelta2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );

        jPanelColaVuelta3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelColaVuelta3Layout = new javax.swing.GroupLayout(jPanelColaVuelta3);
        jPanelColaVuelta3.setLayout(jPanelColaVuelta3Layout);
        jPanelColaVuelta3Layout.setHorizontalGroup(
            jPanelColaVuelta3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );
        jPanelColaVuelta3Layout.setVerticalGroup(
            jPanelColaVuelta3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );

        jPanelColaVuelta4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelColaVuelta4Layout = new javax.swing.GroupLayout(jPanelColaVuelta4);
        jPanelColaVuelta4.setLayout(jPanelColaVuelta4Layout);
        jPanelColaVuelta4Layout.setHorizontalGroup(
            jPanelColaVuelta4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );
        jPanelColaVuelta4Layout.setVerticalGroup(
            jPanelColaVuelta4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );

        jPanelRiesgo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanelAreaInsegura1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelAreaInsegura1Layout = new javax.swing.GroupLayout(jPanelAreaInsegura1);
        jPanelAreaInsegura1.setLayout(jPanelAreaInsegura1Layout);
        jPanelAreaInsegura1Layout.setHorizontalGroup(
            jPanelAreaInsegura1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 143, Short.MAX_VALUE)
        );
        jPanelAreaInsegura1Layout.setVerticalGroup(
            jPanelAreaInsegura1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );

        jPanelAreaInsegura2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelAreaInsegura2Layout = new javax.swing.GroupLayout(jPanelAreaInsegura2);
        jPanelAreaInsegura2.setLayout(jPanelAreaInsegura2Layout);
        jPanelAreaInsegura2Layout.setHorizontalGroup(
            jPanelAreaInsegura2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 143, Short.MAX_VALUE)
        );
        jPanelAreaInsegura2Layout.setVerticalGroup(
            jPanelAreaInsegura2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );

        jPanelAreaInsegura3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelAreaInsegura3Layout = new javax.swing.GroupLayout(jPanelAreaInsegura3);
        jPanelAreaInsegura3.setLayout(jPanelAreaInsegura3Layout);
        jPanelAreaInsegura3Layout.setHorizontalGroup(
            jPanelAreaInsegura3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 143, Short.MAX_VALUE)
        );
        jPanelAreaInsegura3Layout.setVerticalGroup(
            jPanelAreaInsegura3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );

        jPanelAreaInsegura4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelAreaInsegura4Layout = new javax.swing.GroupLayout(jPanelAreaInsegura4);
        jPanelAreaInsegura4.setLayout(jPanelAreaInsegura4Layout);
        jPanelAreaInsegura4Layout.setHorizontalGroup(
            jPanelAreaInsegura4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 143, Short.MAX_VALUE)
        );
        jPanelAreaInsegura4Layout.setVerticalGroup(
            jPanelAreaInsegura4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );

        jPanelCombates1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelCombates1Layout = new javax.swing.GroupLayout(jPanelCombates1);
        jPanelCombates1.setLayout(jPanelCombates1Layout);
        jPanelCombates1Layout.setHorizontalGroup(
            jPanelCombates1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 108, Short.MAX_VALUE)
        );
        jPanelCombates1Layout.setVerticalGroup(
            jPanelCombates1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );

        jPanelCombates2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelCombates2Layout = new javax.swing.GroupLayout(jPanelCombates2);
        jPanelCombates2.setLayout(jPanelCombates2Layout);
        jPanelCombates2Layout.setHorizontalGroup(
            jPanelCombates2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 108, Short.MAX_VALUE)
        );
        jPanelCombates2Layout.setVerticalGroup(
            jPanelCombates2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );

        jPanelCombates3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelCombates3Layout = new javax.swing.GroupLayout(jPanelCombates3);
        jPanelCombates3.setLayout(jPanelCombates3Layout);
        jPanelCombates3Layout.setHorizontalGroup(
            jPanelCombates3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 108, Short.MAX_VALUE)
        );
        jPanelCombates3Layout.setVerticalGroup(
            jPanelCombates3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );

        jPanelCombates4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelCombates4Layout = new javax.swing.GroupLayout(jPanelCombates4);
        jPanelCombates4.setLayout(jPanelCombates4Layout);
        jPanelCombates4Layout.setHorizontalGroup(
            jPanelCombates4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 108, Short.MAX_VALUE)
        );
        jPanelCombates4Layout.setVerticalGroup(
            jPanelCombates4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelRiesgoLayout = new javax.swing.GroupLayout(jPanelRiesgo);
        jPanelRiesgo.setLayout(jPanelRiesgoLayout);
        jPanelRiesgoLayout.setHorizontalGroup(
            jPanelRiesgoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRiesgoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelRiesgoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRiesgoLayout.createSequentialGroup()
                        .addGroup(jPanelRiesgoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelRiesgoLayout.createSequentialGroup()
                                .addComponent(jPanelAreaInsegura1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelCombates1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelRiesgoLayout.createSequentialGroup()
                                .addComponent(jPanelAreaInsegura3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelCombates3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanelRiesgoLayout.createSequentialGroup()
                        .addGroup(jPanelRiesgoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelRiesgoLayout.createSequentialGroup()
                                .addComponent(jPanelAreaInsegura2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelCombates2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelRiesgoLayout.createSequentialGroup()
                                .addComponent(jPanelAreaInsegura4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelCombates4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanelRiesgoLayout.setVerticalGroup(
            jPanelRiesgoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRiesgoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelRiesgoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelCombates1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelAreaInsegura1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanelRiesgoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelAreaInsegura2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelCombates2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanelRiesgoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelAreaInsegura3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelCombates3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanelRiesgoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelAreaInsegura4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelCombates4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jToggleButtonPausa.setText("Pausar");
        jToggleButtonPausa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonPausaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelHumanoMuertoLayout = new javax.swing.GroupLayout(jPanelHumanoMuerto);
        jPanelHumanoMuerto.setLayout(jPanelHumanoMuertoLayout);
        jPanelHumanoMuertoLayout.setHorizontalGroup(
            jPanelHumanoMuertoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 275, Short.MAX_VALUE)
        );
        jPanelHumanoMuertoLayout.setVerticalGroup(
            jPanelHumanoMuertoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelHumanidadAcabadaLayout = new javax.swing.GroupLayout(jPanelHumanidadAcabada);
        jPanelHumanidadAcabada.setLayout(jPanelHumanidadAcabadaLayout);
        jPanelHumanidadAcabadaLayout.setHorizontalGroup(
            jPanelHumanidadAcabadaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelHumanidadAcabadaLayout.setVerticalGroup(
            jPanelHumanidadAcabadaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 72, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jPanelHumanidadAcabada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelRefugio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanelBarrera4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelColaIda4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanelBarrera3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelColaIda3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanelBarrera1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelColaIda1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanelBarrera2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelColaIda2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelTunel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelTunel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelTunel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelTunel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButtonPausa, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelColaVuelta4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelColaVuelta3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelColaVuelta1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelColaVuelta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelRiesgo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(204, 204, 204)
                        .addComponent(jPanelHumanoMuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanelColaVuelta1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jToggleButtonPausa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanelTunel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanelBarrera1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanelColaIda1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jPanelBarrera2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanelColaVuelta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jPanelColaIda2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanelTunel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanelColaIda3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelBarrera3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelTunel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelColaVuelta3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelBarrera4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jPanelTunel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanelColaIda4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanelColaVuelta4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanelRefugio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanelRiesgo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelHumanidadAcabada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelHumanoMuerto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(38, 38, 38))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButtonPausaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonPausaActionPerformed
    if (jToggleButtonPausa.isSelected()) {
        pausa.pausar(false); 
        jToggleButtonPausa.setText("Reanudar");
    } else {
        pausa.reanudar(false); 
        jToggleButtonPausa.setText("Pausar");
    }
    }//GEN-LAST:event_jToggleButtonPausaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelAreaInsegura1;
    private javax.swing.JPanel jPanelAreaInsegura2;
    private javax.swing.JPanel jPanelAreaInsegura3;
    private javax.swing.JPanel jPanelAreaInsegura4;
    private javax.swing.JPanel jPanelBarrera1;
    private javax.swing.JPanel jPanelBarrera2;
    private javax.swing.JPanel jPanelBarrera3;
    private javax.swing.JPanel jPanelBarrera4;
    private javax.swing.JPanel jPanelColaIda1;
    private javax.swing.JPanel jPanelColaIda2;
    private javax.swing.JPanel jPanelColaIda3;
    private javax.swing.JPanel jPanelColaIda4;
    private javax.swing.JPanel jPanelColaVuelta1;
    private javax.swing.JPanel jPanelColaVuelta2;
    private javax.swing.JPanel jPanelColaVuelta3;
    private javax.swing.JPanel jPanelColaVuelta4;
    private javax.swing.JPanel jPanelCombates1;
    private javax.swing.JPanel jPanelCombates2;
    private javax.swing.JPanel jPanelCombates3;
    private javax.swing.JPanel jPanelCombates4;
    private javax.swing.JPanel jPanelHumanidadAcabada;
    private javax.swing.JPanel jPanelHumanoMuerto;
    private javax.swing.JPanel jPanelRefugio;
    private javax.swing.JPanel jPanelRiesgo;
    private javax.swing.JPanel jPanelTunel1;
    private javax.swing.JPanel jPanelTunel2;
    private javax.swing.JPanel jPanelTunel3;
    private javax.swing.JPanel jPanelTunel4;
    private javax.swing.JPanel jPanelZonaComedor;
    private javax.swing.JPanel jPanelZonaComun;
    private javax.swing.JPanel jPanelZonaDescanso;
    private javax.swing.JToggleButton jToggleButtonPausa;
    // End of variables declaration//GEN-END:variables
}
