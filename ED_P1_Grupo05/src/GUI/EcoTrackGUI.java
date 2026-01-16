/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Programa.GestorDatos;
import Programa.Residuo;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import paneles.PanelCentroReciclaje;
import paneles.PanelEstadisticas;
import paneles.PanelGestionResiduos;
import paneles.PanelRutasRecoleccion;
import paneles.PanelZonas;

/**
 *
 * @author ED_P1_Grupo05
 */
public class EcoTrackGUI extends JFrame{
    private GestorDatos gestor;
    private JTabbedPane tabbedPane;
    
    private PanelGestionResiduos panelResiduos;
    private PanelRutasRecoleccion panelRutas;
    private PanelCentroReciclaje panelReciclaje;
    private PanelEstadisticas panelEstadisticas;
    private PanelZonas panelZonas;
    
    public EcoTrackGUI() {
        inicializarComponentes();
        configurarVentana();
    }
    
    private void inicializarComponentes() {
        setTitle("EcoTrack - Sistema Inteligente de Gestión de Residuos Urbanos");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        setSize(1200, 800);
        setLocationRelativeTo(null);

        gestor = new GestorDatos();
        
        panelEstadisticas = new PanelEstadisticas(gestor);
    
        panelResiduos = new PanelGestionResiduos(gestor, panelEstadisticas);

        panelRutas = new PanelRutasRecoleccion(gestor);
        panelReciclaje = new PanelCentroReciclaje(gestor);
        panelZonas = new PanelZonas(gestor);
        
        mostrarResumenInicial();

        tabbedPane = new JTabbedPane();

        panelRutas = new PanelRutasRecoleccion(gestor);
        panelReciclaje = new PanelCentroReciclaje(gestor);
        panelEstadisticas = new PanelEstadisticas(gestor);
        panelZonas = new PanelZonas(gestor);

        tabbedPane.addTab("🏠 Gestión de Residuos", panelResiduos);
        tabbedPane.addTab("🚚 Rutas de Recolección", panelRutas);
        tabbedPane.addTab("♻️ Centro de Reciclaje", panelReciclaje);
        tabbedPane.addTab("📊 Estadísticas", panelEstadisticas);
        tabbedPane.addTab("🗺️ Gestión de Zonas", panelZonas);

        add(tabbedPane, BorderLayout.CENTER);


        configurarToolBar();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarAplicacion();
            }
        });
    }
    
    private void configurarToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
    }
    
    private void configurarVentana() {
        ImageIcon icono = new ImageIcon("data/icono.png"); 
        if (icono.getImage() != null) {
            setIconImage(icono.getImage());
        }

        setVisible(true);
    }
    
    
    
    
    private void cerrarAplicacion() {
        int confirm = JOptionPane.showConfirmDialog(this,"¿Está seguro de que desea salir?\n\n" +"Se guardarán automáticamente:\n" +"• " + gestor.getResiduos().size() + " residuos\n" + "• " + gestor.getZonas().size() + " zonas","Confirmar Salida",JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            gestor.guardarDatos();
            System.exit(0);
        }
    }
    
    private void mostrarResumenInicial() {
        int totalResiduos = gestor.getResiduos().size();

        if (totalResiduos > 0) {
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("✅ Sistema cargado exitosamente\n\n");
            mensaje.append("📊 Resumen inicial:\n");
            mensaje.append("• Residuos cargados: ").append(totalResiduos).append("\n");
            mensaje.append("• Zonas activas: ").append(gestor.getZonas().size()).append("\n");

            mensaje.append("\n📦 Últimos residuos registrados:\n");
            int contador = 0;
            for (Residuo residuo : gestor.getResiduos()) {
                if (contador < 3) { 
                    mensaje.append("  - ").append(residuo.getNombre()).append(" (").append(residuo.getPesoKg()).append(" kg)\n");
                    contador++;
                } else {
                    break;
                }
            }
            if (totalResiduos > 3) {
                mensaje.append("  ... y ").append(totalResiduos - 3).append(" más\n");
            }

            JOptionPane.showMessageDialog(this, mensaje.toString(),"Bienvenido a EcoTrack", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "🆕 Sistema iniciado sin residuos\n\n" +"Puede comenzar agregando nuevos residuos\n" +"en la pestaña 'Gestión de Residuos'","Bienvenido a EcoTrack", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
