/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paneles;

import Programa.GestorDatos;
import Programa.Residuo;
import Programa.Zona;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 *
 * @author ED_P1_Grupo05
 */
public class PanelZonas extends JPanel{
    private GestorDatos gestor;
    private JList<String> listaZonas;
    private DefaultListModel<String> modeloZonas;
    private JTextArea areaDetallesZona;
    private JButton btnActualizar, btnSimularRecoleccion;
    private JLabel lblTotalZonas;
    
    public PanelZonas(GestorDatos gestor) {
        this.gestor = gestor;
        inicializarComponentes();
        configurarEventos();
        actualizarListaZonas();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        
        JPanel panelIzquierdo = crearPanelIzquierdo();
        
        JPanel panelDerecho = crearPanelDerecho();
        
        splitPane.setLeftComponent(panelIzquierdo);
        splitPane.setRightComponent(panelDerecho);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelIzquierdo() {
        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 10));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("🗺️ Zonas Urbanas"));
        
        modeloZonas = new DefaultListModel<>();
        listaZonas = new JList<>(modeloZonas);
        listaZonas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaZonas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollZonas = new JScrollPane(listaZonas);
        
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTotalZonas = new JLabel("Total zonas: 0");
        panelInfo.add(lblTotalZonas);
        
        JPanel panelControles = new JPanel(new GridLayout(2, 1, 5, 5));
        btnActualizar = new JButton("Actualizar Zonas");
        btnSimularRecoleccion = new JButton("Simular Recolección");
        
        panelControles.add(btnActualizar);
        panelControles.add(btnSimularRecoleccion);
        
        panelIzquierdo.add(scrollZonas, BorderLayout.CENTER);
        panelIzquierdo.add(panelInfo, BorderLayout.NORTH);
        panelIzquierdo.add(panelControles, BorderLayout.SOUTH);
        
        return panelIzquierdo;
    }
    
    private JPanel crearPanelDerecho() {
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("📋 Detalles de Zona"));
        
        areaDetallesZona = new JTextArea(20, 50);
        areaDetallesZona.setEditable(false);
        areaDetallesZona.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollDetalles = new JScrollPane(areaDetallesZona);
        
        JPanel panelUtilidad = crearPanelUtilidad();
        
        panelDerecho.add(scrollDetalles, BorderLayout.CENTER);
        panelDerecho.add(panelUtilidad, BorderLayout.SOUTH);
        
        return panelDerecho;
    }
    
    private JPanel crearPanelUtilidad() {
        JPanel panelUtilidad = new JPanel(new BorderLayout());
        panelUtilidad.setBorder(BorderFactory.createTitledBorder("📊 Calculadora de Utilidad Ambiental"));
        
        JPanel panelCalculadora = new JPanel(new GridLayout(3, 2, 5, 5));
        
        JTextField txtRecolectado = new JTextField("0");
        JTextField txtPendiente = new JTextField("0");
        JButton btnCalcular = new JButton("Calcular Utilidad");
        JLabel lblResultado = new JLabel("Utilidad: 0.00");
        
        panelCalculadora.add(new JLabel("Residuos Recolectados (kg):"));
        panelCalculadora.add(txtRecolectado);
        panelCalculadora.add(new JLabel("Residuos Pendientes (kg):"));
        panelCalculadora.add(txtPendiente);
        panelCalculadora.add(btnCalcular);
        panelCalculadora.add(lblResultado);
        
        btnCalcular.addActionListener(e -> {
            try {
                double recolectado = Double.parseDouble(txtRecolectado.getText());
                double pendiente = Double.parseDouble(txtPendiente.getText());
                double utilidad = recolectado - pendiente;
                
                lblResultado.setText(String.format("Utilidad: %.2f", utilidad));
                lblResultado.setForeground(utilidad >= 0 ? Color.GREEN.darker() : Color.RED);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese valores numéricos válidos.","Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panelUtilidad.add(panelCalculadora, BorderLayout.CENTER);
        return panelUtilidad;
    }
    
    private void configurarEventos() {
        btnActualizar.addActionListener(e -> actualizarListaZonas());
        btnSimularRecoleccion.addActionListener(this::simularRecoleccion);
        listaZonas.addListSelectionListener(e -> mostrarDetallesZona());
    }
    
    private void actualizarListaZonas() {
        modeloZonas.clear();
    
        for (Zona zona : gestor.obtenerZonasOrdenadasPorUtilidad()) {
            double utilidad = zona.utilidad();
            String icono = "✅"; 

            if (utilidad < -10) {
                icono = "🚨"; 
            } else if (utilidad < 0) {
                icono = "⚠️"; 
            } else if (utilidad >= 5) {
                icono = "🌟"; 
            }

            modeloZonas.addElement(String.format("%s %s (Util: %.2f)", icono, zona.getNombre(), utilidad));
        }

        lblTotalZonas.setText("Total zonas: " + modeloZonas.size());

        if (modeloZonas.size() > 0) {
            listaZonas.setSelectedIndex(0);
            mostrarDetallesZona();
        } else {
            areaDetallesZona.setText("No hay zonas disponibles para mostrar.");
        }
    }
    
    private void mostrarDetallesZona() {
        int indice = listaZonas.getSelectedIndex();
        if (indice != -1) {
            String elementoLista = modeloZonas.get(indice);

            String nombreZona = extraerNombreZona(elementoLista);
            Zona zona = gestor.obtenerZona(nombreZona);

            if (zona != null) {
                StringBuilder detalles = new StringBuilder();
                detalles.append("=== INFORMACIÓN DETALLADA DE LA ZONA ===\n\n");
                detalles.append(String.format("Nombre: %s\n", zona.getNombre()));
                detalles.append(String.format("Residuos Recolectados: %.2f kg\n", zona.getRecolectado()));
                detalles.append(String.format("Residuos Pendientes: %.2f kg\n", zona.getPendiente()));
                detalles.append(String.format("Utilidad Ambiental: %.2f\n\n", zona.utilidad()));

                double utilidad = zona.utilidad();
                detalles.append("=== ANÁLISIS DE UTILIDAD ===\n\n");
                if (utilidad < -10) {
                    detalles.append("🚨 ESTADO: CRÍTICO\n");
                    detalles.append("• Alta acumulación de residuos\n");
                    detalles.append("• Prioridad máxima de recolección\n");
                    detalles.append("• Riesgo ambiental elevado\n");
                    detalles.append("• Se recomienda acción inmediata\n");
                } else if (utilidad < 0) {
                    detalles.append("⚠️  ESTADO: ALERTA\n");
                    detalles.append("• Acumulación moderada de residuos\n");
                    detalles.append("• Requiere atención prioritaria\n");
                    detalles.append("• Riesgo ambiental medio\n");
                    detalles.append("• Planificar recolección urgente\n");
                } else if (utilidad < 5) {
                    detalles.append("✅ ESTADO: ESTABLE\n");
                    detalles.append("• Gestión adecuada de residuos\n");
                    detalles.append("• Prioridad normal de recolección\n");
                    detalles.append("• Riesgo ambiental bajo\n");
                    detalles.append("• Mantener monitoreo regular\n");
                } else {
                    detalles.append("🌟 ESTADO: ÓPTIMO\n");
                    detalles.append("• Excelente gestión de residuos\n");
                    detalles.append("• Zona modelo en eficiencia\n");
                    detalles.append("• Riesgo ambiental mínimo\n");
                    detalles.append("• Replicar estrategias en otras zonas\n");
                }

                if (utilidad < -10) {
                    detalles.append("\n📋 PLAN DE ACCIÓN RECOMENDADO:\n");
                    detalles.append("• Despachar vehículo de alta prioridad\n");
                    detalles.append("• Duplicar frecuencia de recolección\n");
                    detalles.append("• Monitorear diariamente\n");
                    detalles.append("• Asignar recursos adicionales\n");
                }

                areaDetallesZona.setText(detalles.toString());
            } else {
                areaDetallesZona.setText("Error: No se pudo cargar la información de la zona.");
            }
        } else {
            areaDetallesZona.setText("Seleccione una zona para ver sus detalles.");
        }
    }
    
    private void simularRecoleccion(ActionEvent e) {
        if (listaZonas.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione una zona primero.","Zona no Seleccionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String elementoLista = modeloZonas.get(listaZonas.getSelectedIndex());
        String nombreZona = extraerNombreZona(elementoLista);
        Zona zona = gestor.obtenerZona(nombreZona);

        if (zona != null) {
            if (zona.getPendiente() > 0) {
                double cantidadRecolectada = Math.min(zona.getPendiente(), 50.0);
                double nuevoPendiente = zona.getPendiente() - cantidadRecolectada;

                zona.setPendiente(nuevoPendiente);
                zona.setRecolectado(zona.getRecolectado() + cantidadRecolectada);

                actualizarListaZonas();

                JOptionPane.showMessageDialog(this, 
                    String.format("✅ Simulación exitosa!\n\n" +"Zona: %s\n" +"Recolectado: %.2f kg\n" +"Nuevo pendiente: %.2f kg\n" +"Nueva utilidad: %.2f", zona.getNombre(), cantidadRecolectada, zona.getPendiente(), zona.utilidad()),"Recolección Simulada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "✅ Esta zona ya está limpia.\nNo hay residuos pendientes para recolectar.","Zona Limpia", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se pudo encontrar la zona seleccionada.","Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String extraerNombreZona(String elementoLista) {
        if (elementoLista == null || elementoLista.isEmpty()) {
            return "";
        }

        String s = elementoLista.replaceAll("[🚨⚠️✅🌟]", "").trim();

        int inicioParentesis = s.indexOf('(');
        if (inicioParentesis != -1) {
            return s.substring(0, inicioParentesis).trim();
        }

        return s;
    }
    
    
}
