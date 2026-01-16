/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paneles;

import GUI.DialogoAgregarResiduo;
import GUI.ResiduoTableModel;
import Programa.GestorDatos;
import Programa.Residuo;
import Programa.TipoResiduo;
import Listas.List;
import Listas.ArrayList;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;


/**
 *
 * @author ED_P1_Grupo05
 */
public class PanelGestionResiduos extends JPanel {
    private GestorDatos gestor;
    private java.util.Iterator<Residuo> itAdelante;
    private java.util.Iterator<Residuo> itAtras;
    private int indiceActual = 0;
    private Residuo residuoActual;
    private PanelEstadisticas panelEstadisticas;
    private JTable tablaResiduos;
    private ResiduoTableModel tableModel;
    private JButton btnAgregar, btnEliminar, btnOrdenar, btnBuscar, btnLimpiarBusqueda;
    private JComboBox<String> comboOrdenamiento;
    private JTextField txtBuscar;
    private JLabel lblTotalResiduos;
    private JButton btnAnterior, btnSiguiente;
    private JLabel lblNavegacion;
    private Random random;
    
    public PanelGestionResiduos(GestorDatos gestor, PanelEstadisticas panelEstadisticas) {
        this.gestor = gestor;
        this.panelEstadisticas = panelEstadisticas; 
        this.random = new Random();
        inicializarComponentes();
        configurarEventos();
        actualizarTabla();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelSuperior = crearPanelControles();
        
        JPanel panelCentral = crearPanelTabla();
        
        JPanel panelInferior = crearPanelNavegacion();
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelControles() {
        JPanel panelControles = new JPanel(new BorderLayout(10, 10));
        
        JPanel panelFila1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFila1.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(20);
        panelFila1.add(txtBuscar);
        btnBuscar = new JButton("Buscar");
        panelFila1.add(btnBuscar);
        btnLimpiarBusqueda = new JButton("Limpiar");
        panelFila1.add(btnLimpiarBusqueda);
        
        JPanel panelFila2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFila2.add(new JLabel("Ordenar por:"));
        comboOrdenamiento = new JComboBox<>(new String[]{
            "ID", "Peso", "Tipo", "Prioridad Ambiental", "Zona"
        });
        panelFila2.add(comboOrdenamiento);
        btnOrdenar = new JButton("Aplicar Orden");
        panelFila2.add(btnOrdenar);
        
        lblTotalResiduos = new JLabel("Total residuos: 0");
        panelFila2.add(Box.createHorizontalStrut(50));
        panelFila2.add(lblTotalResiduos);
        
        panelControles.add(panelFila1, BorderLayout.NORTH);
        panelControles.add(panelFila2, BorderLayout.SOUTH);
        
        return panelControles;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("📋 Lista de Residuos Registrados"));

        tableModel = new ResiduoTableModel(gestor.getResiduos());
        tablaResiduos = new JTable(tableModel);

        tablaResiduos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaResiduos.setAutoCreateRowSorter(true);
        tablaResiduos.setFillsViewportHeight(true);


        JScrollPane scrollPane = new JScrollPane(tablaResiduos);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        panelTabla.add(scrollPane, BorderLayout.CENTER);
        return panelTabla;
    }
    
    private JPanel crearPanelNavegacion() {
        JPanel panelNavegacion = new JPanel(new BorderLayout(10, 10));
        
        JPanel panelNavIteradores = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAnterior = new JButton("← Anterior");
        lblNavegacion = new JLabel("1 / 0");
        btnSiguiente = new JButton("Siguiente →");
        
        panelNavIteradores.add(btnAnterior);
        panelNavIteradores.add(lblNavegacion);
        panelNavIteradores.add(btnSiguiente);
        
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAgregar = new JButton("➕ Agregar Residuo");
        btnEliminar = new JButton("🗑️ Eliminar Seleccionado");
        
        panelAcciones.add(btnAgregar);
        panelAcciones.add(btnEliminar);
        
        panelNavegacion.add(panelNavIteradores, BorderLayout.CENTER);
        panelNavegacion.add(panelAcciones, BorderLayout.EAST);
        
        return panelNavegacion;
    }
    
    private void configurarEventos() {
        btnAgregar.addActionListener(this::mostrarDialogoAgregarResiduo);
        btnEliminar.addActionListener(this::eliminarResiduoSeleccionado);
        btnOrdenar.addActionListener(this::aplicarOrdenamiento);
        btnBuscar.addActionListener(this::buscarResiduos);
        btnLimpiarBusqueda.addActionListener(e -> limpiarBusqueda());
        btnAnterior.addActionListener(this::navegarAnterior);
        btnSiguiente.addActionListener(this::navegarSiguiente);
        
        tablaResiduos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editarResiduoSeleccionado();
                }
            }
        });
    }
    
    private void mostrarDialogoAgregarResiduo(ActionEvent e) {
        DialogoAgregarResiduo dialogo = new DialogoAgregarResiduo((JFrame) SwingUtilities.getWindowAncestor(this));
        dialogo.setVisible(true);
        
        if (dialogo.isGuardado()) {
            Residuo nuevoResiduo = dialogo.getResiduo();
            gestor.agregarResiduo(nuevoResiduo);
            actualizarTabla();
            if (panelEstadisticas != null) {
                panelEstadisticas.actualizarEstadisticas();
            }
            JOptionPane.showMessageDialog(this, "Residuo agregado exitosamente:\n" + nuevoResiduo.getNombre(),"Residuo Agregado", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void eliminarResiduoSeleccionado(ActionEvent e) {
        int filaSeleccionada = tablaResiduos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un residuo para eliminar.","Sin Selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
       
        int modelRow = tablaResiduos.convertRowIndexToModel(filaSeleccionada);
        Residuo residuo = tableModel.getResiduoAt(modelRow);
        
        if (residuo != null) {
            int confirm = JOptionPane.showConfirmDialog(this,"¿Está seguro de que desea eliminar el residuo?\n" +"ID: " + residuo.getId() + "\n" +"Nombre: " + residuo.getNombre(),"Confirmar Eliminación",JOptionPane.YES_NO_OPTION);
            
            
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean eliminado = gestor.eliminarResiduo(residuo);
                if (eliminado) {
                    actualizarTabla();

                    if (panelEstadisticas != null) {
                        panelEstadisticas.actualizarEstadisticas();
                    }

                    JOptionPane.showMessageDialog(this, "Residuo eliminado exitosamente.","Eliminación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    
    private void editarResiduoSeleccionado() {
        int filaSeleccionada = tablaResiduos.getSelectedRow();
        if (filaSeleccionada == -1) return;
        
        int modelRow = tablaResiduos.convertRowIndexToModel(filaSeleccionada);
        Residuo residuo = tableModel.getResiduoAt(modelRow);
        
        if (residuo != null) {
            JTextField txtNombre = new JTextField(residuo.getNombre());
            JTextField txtPeso = new JTextField(String.valueOf(residuo.getPesoKg()));
            JComboBox<String> comboTipo = new JComboBox<>(new String[]{
                "ORGANICO", "PLASTICO", "VIDRIO", "ELECTRONICO", "METAL", "PAPEL", "OTRO"
            });
            comboTipo.setSelectedItem(residuo.getTipo());
            
            JComboBox<String> comboZona = new JComboBox<>(new String[]{
                "Zona Norte", "Zona Sur", "Zona Este", "Zona Oeste", "Centro"
            });
            comboZona.setSelectedItem(residuo.getZona());
            
            JSpinner spinnerPrioridad = new JSpinner(new SpinnerNumberModel(
                residuo.getPrioridadAmbiental(), 1, 10, 1));
            
            JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
            panel.add(new JLabel("Nombre:"));
            panel.add(txtNombre);
            panel.add(new JLabel("Tipo:"));
            panel.add(comboTipo);
            panel.add(new JLabel("Peso (kg):"));
            panel.add(txtPeso);
            panel.add(new JLabel("Zona:"));
            panel.add(comboZona);
            panel.add(new JLabel("Prioridad:"));
            panel.add(spinnerPrioridad);
            
            int result = JOptionPane.showConfirmDialog(this, panel, 
                "Editar Residuo - " + residuo.getId(), JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                try {
                    residuo.setNombre(txtNombre.getText());
                    
                    String tipo = (String) comboTipo.getSelectedItem();
                    TipoResiduo tipoR = TipoResiduo.OTRO;

                    if(tipo.equals("ORGANICO")){
                        tipoR = TipoResiduo.ORGANICO;
                    }else if(tipo.equals("PLASTICO")){
                        tipoR = TipoResiduo.PLASTICO;
                    }else if(tipo.equals("VIDRIO")){
                        tipoR = TipoResiduo.VIDRIO;
                    }else if(tipo.equals("ELECTRONICO")){
                        tipoR = TipoResiduo.ELECTRONICO;
                    }else if(tipo.equals("METAL")){
                        tipoR = TipoResiduo.METAL;
                    }else if(tipo.equals("PAPEL")){
                        tipoR = TipoResiduo.PAPEL;
                    }
                    residuo.setTipo(tipoR);
                    residuo.setPesoKg(Double.parseDouble(txtPeso.getText()));
                    residuo.setZona((String) comboZona.getSelectedItem());
                    residuo.setPrioridadAmbiental((Integer) spinnerPrioridad.getValue());
                    
                    actualizarTabla();
                    JOptionPane.showMessageDialog(this, "Residuo actualizado exitosamente.","Actualización Exitosa", JOptionPane.INFORMATION_MESSAGE);
                        
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El peso debe ser un número válido.","Error de Formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void aplicarOrdenamiento(ActionEvent e) {
        String criterio = (String) comboOrdenamiento.getSelectedItem();
        List<Residuo> residuosOrdenados = gestor.obtenerResiduosOrdenados(criterio);
        tableModel.setDatos(residuosOrdenados);
        actualizarEstadisticas();
    }
    
    private void buscarResiduos(ActionEvent e) {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();
        if (textoBusqueda.isEmpty()) {
            limpiarBusqueda();
            return;
        }
        
        List<Residuo> resultados = new ArrayList<>();
        for (Residuo residuo : gestor.getResiduos()) {
            if (residuo.getNombre().toLowerCase().contains(textoBusqueda) ||residuo.getTipo().toString().toLowerCase().contains(textoBusqueda) ||residuo.getZona().toLowerCase().contains(textoBusqueda) ||residuo.getId().toLowerCase().contains(textoBusqueda)) {
                resultados.addLast(residuo);
            }
        }
        
        tableModel.setDatos(resultados);
        lblTotalResiduos.setText(String.format("Resultados: %d de %d", resultados.size(), gestor.getResiduos().size()));
    }
    
    private void limpiarBusqueda() {
        txtBuscar.setText("");
        actualizarTabla();
    }
    
    private void navegarAnterior(ActionEvent e) {
        if (indiceActual > 0) {
            indiceActual--;
            tablaResiduos.setRowSelectionInterval(indiceActual, indiceActual);
            actualizarNavegacion();
        }
    }


    
    private void navegarSiguiente(ActionEvent e) {
    int total = gestor.getResiduos().size();
    if (indiceActual < total - 1) {
        indiceActual++;
        tablaResiduos.setRowSelectionInterval(indiceActual, indiceActual);
        actualizarNavegacion();
    }
}


    
    private void actualizarTabla() {
        tableModel.actualizarDatos(gestor.getResiduos());

        // Inicializar iteradores
        itAdelante = gestor.getResiduos().iterator();
        itAtras = gestor.getResiduos().IteratorHaciaAtras();

        if (itAdelante.hasNext()) {
            residuoActual = itAdelante.next();
            seleccionarResiduoEnTabla(residuoActual);
        }

        actualizarEstadisticas();
        actualizarNavegacion();
    }

    
    private void actualizarEstadisticas() {
        int total = gestor.getResiduos().size();
        lblTotalResiduos.setText(String.format("Total residuos: %d", total));
    }
    
    private void actualizarNavegacion() {
        int total = gestor.getResiduos().size();

        if (total == 0) {
            lblNavegacion.setText("0 / 0");
            btnAnterior.setEnabled(false);
            btnSiguiente.setEnabled(false);
            return;
        }

        lblNavegacion.setText(String.format("%d / %d", indiceActual + 1, total));

        btnAnterior.setEnabled(indiceActual > 0);
        btnSiguiente.setEnabled(indiceActual < total - 1);
    }

    
    public void mostrarDialogoAgregarResiduo() {
        mostrarDialogoAgregarResiduo(new ActionEvent(btnAgregar, ActionEvent.ACTION_PERFORMED, ""));
    }
    
    private void seleccionarResiduoEnTabla(Residuo r) {
    if (r == null) return;

    for (int i = 0; i < tableModel.getRowCount(); i++) {
        Residuo res = tableModel.getResiduoAt(i);
        if (res.equals(r)) {
            tablaResiduos.setRowSelectionInterval(i, i);
            tablaResiduos.scrollRectToVisible(
                tablaResiduos.getCellRect(i, 0, true)
            );
            lblNavegacion.setText((i + 1) + " / " + tableModel.getRowCount());
            break;
        }
    }
}

}

