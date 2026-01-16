/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paneles;

import Programa.CentroReciclaje;
import Programa.GestorDatos;
import Programa.Residuo;
import Listas.ArrayList;
import Listas.List;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Deque;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

/**
 *
 * @author ED_P1_Grupo05
 */
public class PanelCentroReciclaje extends JPanel {
    private GestorDatos gestor;
    private JList<String> listaPila;
    private DefaultListModel<String> modeloPila;
    private JButton btnProcesar, btnAgregarPila, btnLimpiar, btnEstadisticas;
    private JLabel lblTamanioPila, lblEstadisticas,lblTipoMasComun, lblEficiencia;
    private JTextArea areaProcesados;
    private CentroReciclaje centroReciclaje;
    
    public PanelCentroReciclaje(GestorDatos gestor) {
        this.gestor = gestor;
        this.centroReciclaje = gestor.getCentroReciclaje();
        inicializarComponentes();
        configurarEventos();
        actualizarPila();
        actualizarEstadisticas();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        
        JPanel panelPila = crearPanelPila();
        
        JPanel panelDerecho = crearPanelDerecho();
        
        splitPane.setLeftComponent(panelPila);
        splitPane.setRightComponent(panelDerecho);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelPila() {
        JPanel panelPila = new JPanel(new BorderLayout(10, 10));
        panelPila.setBorder(BorderFactory.createTitledBorder("♻️ Pila de Reciclaje "));
        
        modeloPila = new DefaultListModel<>();
        listaPila = new JList<>(modeloPila);
        listaPila.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPila.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPila = new JScrollPane(listaPila);
        scrollPila.setPreferredSize(new Dimension(380, 300));
        
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTamanioPila = new JLabel("Elementos en pila: 0");
        panelInfo.add(lblTamanioPila);
        
        JPanel panelControles = new JPanel(new GridLayout(2, 2, 5, 5));
        btnProcesar = new JButton("Procesar Cima");
        btnAgregarPila = new JButton("Agregar Residuo");
        btnLimpiar = new JButton("Limpiar Pila");
        btnEstadisticas = new JButton("Ver Estadísticas");
        
        panelControles.add(btnProcesar);
        panelControles.add(btnAgregarPila);
        panelControles.add(btnLimpiar);
        panelControles.add(btnEstadisticas);
        
        panelPila.add(scrollPila, BorderLayout.CENTER);
        panelPila.add(panelInfo, BorderLayout.NORTH);
        panelPila.add(panelControles, BorderLayout.SOUTH);
        
        return panelPila;
    }
    
    private JPanel crearPanelDerecho() {
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("📊 Procesamiento y Estadísticas"));
        
        areaProcesados = new JTextArea(20, 40);
        areaProcesados.setEditable(false);
        areaProcesados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaProcesados.setText("Residuos procesados aparecerán aquí...\n\n");
        
        JScrollPane scrollProcesados = new JScrollPane(areaProcesados);
        
        JPanel panelStats = new JPanel(new GridLayout(4, 1, 5, 5));
        lblEstadisticas = new JLabel("Total procesado: 0 kg | Residuos: 0");
        lblTipoMasComun = new JLabel("Tipo más común: Ninguno");
        lblEficiencia = new JLabel("Eficiencia: "+calcularEficiencia()+" %");
        JButton btnExportar = new JButton("Exportar Reporte");
        
        panelStats.add(lblEstadisticas);
        panelStats.add(lblTipoMasComun);
        panelStats.add(lblEficiencia);
        panelStats.add(btnExportar);
        
        panelDerecho.add(scrollProcesados, BorderLayout.CENTER);
        panelDerecho.add(panelStats, BorderLayout.SOUTH);
        
        return panelDerecho;
    }
    
    private void configurarEventos() {
        btnProcesar.addActionListener(this::procesarCimaPila);
        btnAgregarPila.addActionListener(this::agregarResiduoAPila);
        btnLimpiar.addActionListener(e -> limpiarPila());
        btnEstadisticas.addActionListener(e -> mostrarEstadisticasDetalladas());
    }
    
    private void procesarCimaPila(ActionEvent e) {
        try {
            Residuo residuo = gestor.procesarReciclaje();
            if (residuo != null) {
                areaProcesados.append(String.format("✅ PROCESADO: %s - %s (%.2f kg) - %s\n",residuo.getId(), residuo.getNombre(), residuo.getPesoKg(), residuo.getTipo()));
                
                actualizarEstadisticas();
                actualizarPila();
                
                JOptionPane.showMessageDialog(this, "Residuo procesado exitosamente:\n" + residuo.getNombre(),"Procesamiento Exitoso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No hay residuos en la pila para procesar.","Pila Vacía", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar residuo: " + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void agregarResiduoAPila(ActionEvent e) {
        List<Residuo> residuosDisponibles = new ArrayList<>();
        for (Residuo r : gestor.getResiduos()) {
            residuosDisponibles.addLast(r);
        }

        if (residuosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay residuos disponibles para agregar a la pila.","Sin Residuos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Residuo[] arrayResiduos = new Residuo[residuosDisponibles.size()];
        for (int i = 0; i < residuosDisponibles.size(); i++) {
            arrayResiduos[i] = residuosDisponibles.get(i);
        }

        Residuo seleccionado = (Residuo) JOptionPane.showInputDialog(this,"Seleccione un residuo para agregar a la pila de reciclaje:","Agregar a Pila de Reciclaje",JOptionPane.QUESTION_MESSAGE,null,arrayResiduos,arrayResiduos[0]);

        if (seleccionado != null) {
            gestor.agregarAReciclaje(seleccionado);
            actualizarPila();
            JOptionPane.showMessageDialog(this, "Residuo agregado a la pila de reciclaje: " + seleccionado.getNombre(),"Agregado Exitosamente", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void limpiarPila() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea limpiar toda la pila de reciclaje?",
            "Confirmar Limpieza",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            while (!gestor.getPilaReciclaje().isEmpty()) {
                gestor.getPilaReciclaje().pollLast();
            }
            actualizarPila();
            areaProcesados.setText("Pila limpiada. No hay residuos procesados recientemente.\n\n");
        }
    }
    
    private void mostrarEstadisticasDetalladas() {
        actualizarEstadisticas();

        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DETALLADAS DEL CENTRO DE RECICLAJE ===\n\n");
        stats.append(String.format("Total de residuos procesados: %d\n", centroReciclaje.getTotalResiduosProcesados()));
        stats.append(String.format("Peso total procesado: %.2f kg\n", centroReciclaje.getTotalProcesado()));
        stats.append(String.format("Tipo más procesado: %s\n\n", centroReciclaje.getTipoMasProcesado()));
        
        stats.append("Distribución por tipo:\n");
        
        Map<String, Double> estadisticas = centroReciclaje.getEstadisticasProcesamiento();
        if (estadisticas.isEmpty()) {
            stats.append("  No hay datos de procesamiento\n");
        } else {
            for (Map.Entry<String, Double> entry : estadisticas.entrySet()) {
                double porcentaje = centroReciclaje.getPorcentajeTipo(entry.getKey());
                stats.append(String.format("  %s: %.1f%% (%.2f kg)\n", entry.getKey(), porcentaje, entry.getValue()));
            }
        }
        
        JOptionPane.showMessageDialog(this, stats.toString(),"Estadísticas Detalladas", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void actualizarPila() {
        modeloPila.clear();
    
        Deque<Residuo> pila = gestor.getPilaReciclaje();

        if (!pila.isEmpty()) {
            
            ArrayList<Residuo> elementos = new ArrayList<>();
            for(Residuo r1 : pila){
                elementos.addLast(r1);
            }
            elementos.reverse(); 

            for (Residuo r : elementos) {
                modeloPila.addElement(String.format("%s - %s (%.1f kg) - %s", r.getId(), r.getNombre(), r.getPesoKg(), r.getTipo()));
            }
        }

        lblTamanioPila.setText("Elementos en pila: " + modeloPila.size());
    }
    
    private void actualizarEstadisticas() {
        lblEstadisticas.setText(String.format("Total procesado: %.2f kg | Residuos: %d", centroReciclaje.getTotalProcesado(), centroReciclaje.getTotalResiduosProcesados()));
    
        lblTipoMasComun.setText(String.format("Tipo más común: %s", centroReciclaje.getTipoMasProcesado()));

        lblEficiencia.setText(String.format("Eficiencia: %.1f%%", calcularEficiencia()));
    }
    
    private double calcularEficiencia() {
        int totalResiduos = centroReciclaje.getTotalResiduosProcesados();
        double totalPeso = centroReciclaje.getTotalProcesado();

        if (totalResiduos == 0) return 0;

        
        double eficienciaPorCantidad = (totalResiduos * 10.0); 
        double eficienciaPorPeso = totalPeso * 2.0; 

        double eficienciaTotal = Math.min(100, (eficienciaPorCantidad + eficienciaPorPeso) / (Math.max(1, totalResiduos) * 15.0) * 100);

        return Math.round(eficienciaTotal * 10.0) / 10.0;
    }
}
