/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paneles;

import Programa.GestorDatos;
import Programa.Vehiculo;
import Programa.Zona;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
public class PanelRutasRecoleccion extends JPanel {
    private GestorDatos gestor;
    private JList<String> listaVehiculos;
    private DefaultListModel<String> modeloVehiculos;
    private JTextArea areaDetalles;
    private JButton btnDespachar, btnAgregarVehiculo, btnActualizar;
    private JLabel lblEstadoCola;
    private Random random;
    
    public PanelRutasRecoleccion(GestorDatos gestor) {
        this.gestor = gestor;
        this.random = new Random();
        inicializarComponentes();
        configurarEventos();
        actualizarListaVehiculos();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        
        JPanel panelIzquierdo = crearPanelIzquierdo();
        
        JPanel panelDerecho = crearPanelDerecho();
        
        splitPane.setLeftComponent(panelIzquierdo);
        splitPane.setRightComponent(panelDerecho);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelIzquierdo() {
        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 10));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("🚚 Vehículos en Cola de Prioridad"));
        
        modeloVehiculos = new DefaultListModel<>();
        listaVehiculos = new JList<>(modeloVehiculos);
        listaVehiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaVehiculos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollVehiculos = new JScrollPane(listaVehiculos);
        
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblEstadoCola = new JLabel("Vehículos en cola: 0");
        panelInfo.add(lblEstadoCola);
        
        JPanel panelControles = new JPanel(new GridLayout(1, 3, 5, 5));
        btnDespachar = new JButton("Despachar");
        btnAgregarVehiculo = new JButton("Nuevo Vehículo");
        btnActualizar = new JButton("Actualizar");
        
        
        panelControles.add(btnDespachar);
        panelControles.add(btnAgregarVehiculo);
        panelControles.add(btnActualizar);
        
        
        panelIzquierdo.add(scrollVehiculos, BorderLayout.CENTER);
        panelIzquierdo.add(panelInfo, BorderLayout.NORTH);
        panelIzquierdo.add(panelControles, BorderLayout.SOUTH);
        
        return panelIzquierdo;
    }
    
    private JPanel crearPanelDerecho() {
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("📊 Detalles y Utilidad de Zonas"));
        
        areaDetalles = new JTextArea(20, 40);
        areaDetalles.setEditable(false);
        areaDetalles.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollDetalles = new JScrollPane(areaDetalles);
        
        JPanel panelZonas = crearPanelUtilidadZonas();
        
        panelDerecho.add(scrollDetalles, BorderLayout.CENTER);
        panelDerecho.add(panelZonas, BorderLayout.SOUTH);
        
        return panelDerecho;
    }
    
    private JPanel crearPanelUtilidadZonas() {
        JPanel panelZonas = new JPanel(new BorderLayout());
        panelZonas.setBorder(BorderFactory.createTitledBorder(" Utilidad Ambiental por Zona"));
        
        JTextArea areaUtilidad = new JTextArea(8, 40);
        areaUtilidad.setEditable(false);
        areaUtilidad.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        actualizarUtilidadZonas(areaUtilidad);
        
        JScrollPane scrollUtilidad = new JScrollPane(areaUtilidad);
        panelZonas.add(scrollUtilidad, BorderLayout.CENTER);
        
        return panelZonas;
    }
    
    private void configurarEventos() {
        btnDespachar.addActionListener(this::despacharVehiculo);
        btnAgregarVehiculo.addActionListener(this::agregarNuevoVehiculo);
        btnActualizar.addActionListener(e -> actualizarListaVehiculos());
        listaVehiculos.addListSelectionListener(e -> mostrarDetallesVehiculo());
    }
    
    private void despacharVehiculo(ActionEvent e) {
        if (gestor.getVehiculos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay vehículos en la cola para despachar.","Cola Vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Vehiculo vehiculo = gestor.despacharVehiculo();
        if (vehiculo != null) {
            actualizarListaVehiculos();
            actualizarUtilidadZonas();
            
            JOptionPane.showMessageDialog(this, "Vehículo despachado exitosamente:\n" + "ID: " + vehiculo.getId() + "\n" +"Zona: " + vehiculo.getZonaAsignada() + "\n" +"Prioridad: " + String.format("%.2f", vehiculo.getPrioridad()),"Despacho Exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void agregarNuevoVehiculo(ActionEvent e) {
        String[] tipos = {"Camión Compactador", "Camión Recolector", "Furgoneta Ecológica", "Camión de Carga"};
        String[] zonas = {"Zona Norte", "Zona Sur", "Zona Este", "Zona Oeste", "Centro"};
        
        JComboBox<String> comboTipo = new JComboBox<>(tipos);
        JComboBox<String> comboZona = new JComboBox<>(zonas);
        JTextField txtCapacidad = new JTextField("1000");
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Tipo de Vehículo:"));
        panel.add(comboTipo);
        panel.add(new JLabel("Zona Asignada:"));
        panel.add(comboZona);
        panel.add(new JLabel("Capacidad (kg):"));
        panel.add(txtCapacidad);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Nuevo Vehículo", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String id = "V" + String.format("%03d", random.nextInt(1000));
                String tipo = (String) comboTipo.getSelectedItem();
                String zona = (String) comboZona.getSelectedItem();
                double capacidad = Double.parseDouble(txtCapacidad.getText());
                
                Vehiculo nuevoVehiculo = new Vehiculo(id, tipo, zona, capacidad,gestor);
                gestor.agregarVehiculo(nuevoVehiculo);
                actualizarListaVehiculos();
                
                JOptionPane.showMessageDialog(this, "Vehículo agregado exitosamente:\nID: " + id,"Vehículo Agregado", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La capacidad debe ser un número válido.","Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void mostrarDetallesVehiculo() {
        int indice = listaVehiculos.getSelectedIndex();
        if (indice != -1 && !gestor.getVehiculos().isEmpty()) {
            Vehiculo vehiculo = gestor.getVehiculos().peek();
            
            StringBuilder detalles = new StringBuilder();
            detalles.append("=== DETALLES DEL VEHÍCULO ===\n\n");
            detalles.append(String.format("ID: %s\n", vehiculo.getId()));
            detalles.append(String.format("Tipo: %s\n", vehiculo.getTipo()));
            detalles.append(String.format("Zona Asignada: %s\n", vehiculo.getZonaAsignada()));
            detalles.append(String.format("Capacidad: %.2f kg\n", vehiculo.getCapacidad()));
            detalles.append(String.format("Prioridad: %.2f\n\n", vehiculo.getPrioridad()));
            
            Zona zona = gestor.obtenerZona(vehiculo.getZonaAsignada());
            if (zona != null) {
                detalles.append("=== INFORMACIÓN DE LA ZONA ===\n\n");
                detalles.append(String.format("Residuos Pendientes: %.2f kg\n", zona.getPendiente()));
                detalles.append(String.format("Residuos Recolectados: %.2f kg\n", zona.getRecolectado()));
                detalles.append(String.format("Utilidad Ambiental: %.2f\n", zona.utilidad()));
                
                double utilidad = zona.utilidad();
                if (utilidad < -10) {
                    detalles.append("\n🚨 ALTA PRIORIDAD: Zona crítica con acumulación severa");
                } else if (utilidad < 0) {
                    detalles.append("\n⚠️  PRIORIDAD MEDIA: Zona requiere atención");
                } else {
                    detalles.append("\n✅ PRIORIDAD BAJA: Zona en buen estado");
                }
            }
            
            areaDetalles.setText(detalles.toString());
        }
    }
    
    private void actualizarListaVehiculos() {
        modeloVehiculos.clear();
        
        if (!gestor.getVehiculos().isEmpty()) {
            Queue<Vehiculo> copia = new PriorityQueue<>(new Comparator<Vehiculo>() {
            @Override
            public int compare(Vehiculo v1, Vehiculo v2) {
                return Double.compare(v2.getPrioridad(), v1.getPrioridad());
            }
        });
            
            int contador = 1;
            while (!gestor.getVehiculos().isEmpty()) {
                Vehiculo v = gestor.getVehiculos().poll();
                modeloVehiculos.addElement(String.format("%d. %s - %s (Pri: %.2f)", contador++, v.getId(), v.getZonaAsignada(), v.getPrioridad()));
                copia.offer(v);
            }
            
            while (!copia.isEmpty()) {
                gestor.getVehiculos().offer(copia.poll());
            }
        }
        
        lblEstadoCola.setText("Vehículos en cola: " + modeloVehiculos.size());
        actualizarUtilidadZonas();
    }
    
    private void actualizarUtilidadZonas() {
        actualizarUtilidadZonas(null);
    }
    
    private void actualizarUtilidadZonas(JTextArea areaUtilidad) {
        StringBuilder utilidad = new StringBuilder();
        utilidad.append("Zona           | Utilidad | Estado\n");
        utilidad.append("---------------|----------|--------\n");
        
        for (Zona zona : gestor.obtenerZonasOrdenadasPorUtilidad()) {
            double u = zona.utilidad();
            String estado;
            if (u < -10) {
                estado = "🚨 CRÍTICO";
            }
            else if (u < 0) {
                estado = "⚠️  ALERTA";
            }
            else if (u < 5) {
                estado = "✅ ESTABLE";
            }
            else {
                estado = "🌟 ÓPTIMO";
            }
            
            utilidad.append(String.format("%-14s | %7.2f | %s\n", zona.getNombre(), u, estado));
        }
        
        if (areaUtilidad != null) {
            areaUtilidad.setText(utilidad.toString());
        }
    }
    
    public void despacharVehiculo() {
        despacharVehiculo(new ActionEvent(btnDespachar, ActionEvent.ACTION_PERFORMED, ""));
    }
    
    
  
}