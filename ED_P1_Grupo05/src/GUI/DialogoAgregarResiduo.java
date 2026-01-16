/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Programa.Residuo;
import Programa.TipoResiduo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author ED_P1_Grupo05
 */
public class DialogoAgregarResiduo extends JDialog {
    private JTextField txtId, txtNombre, txtPeso;
    private JComboBox<String> comboTipo, comboZona;
    private JSpinner spinnerPrioridad;
    private JButton btnGuardar, btnCancelar;
    private boolean guardado = false;
    private Residuo residuo;
    private Random random;
    
    public DialogoAgregarResiduo(JFrame parent) {
        super(parent, "Agregar Nuevo Residuo", true);
        this.random = new Random();
        inicializarComponentes();
        configurarVentana();
    }
    
    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        
        panelFormulario.add(new JLabel("ID:"));
        txtId = new JTextField(generarIdAutomatico());
        txtId.setEditable(false);
        txtId.setBackground(Color.LIGHT_GRAY);
        panelFormulario.add(txtId);
        
        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);
        
        panelFormulario.add(new JLabel("Tipo:"));
        comboTipo = new JComboBox<>(new String[]{"ORGANICO", "PLASTICO", "VIDRIO", "ELECTRONICO", "METAL", "PAPEL", "OTRO"});
        panelFormulario.add(comboTipo);
        
        panelFormulario.add(new JLabel("Peso (kg):"));
        txtPeso = new JTextField();
        panelFormulario.add(txtPeso);
        
        panelFormulario.add(new JLabel("Zona:"));
        comboZona = new JComboBox<>(new String[]{"Zona Norte", "Zona Sur", "Zona Este", "Zona Oeste", "Centro"});
        panelFormulario.add(comboZona);
        
        panelFormulario.add(new JLabel("Prioridad Ambiental:"));
        spinnerPrioridad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        panelFormulario.add(spinnerPrioridad);
        
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        
        btnGuardar.setBackground(new Color(70, 130, 180));
        btnGuardar.setForeground(Color.BLACK);
        btnCancelar.setBackground(new Color(220, 80, 60));
        btnCancelar.setForeground(Color.BLACK);
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        configurarEventos();
    }
    
    private void configurarVentana() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    private void configurarEventos() {
        btnGuardar.addActionListener(this::guardarResiduo);
        btnCancelar.addActionListener(e -> dispose());
        
        txtNombre.addActionListener(e -> txtPeso.requestFocus());
        txtPeso.addActionListener(e -> guardarResiduo(e));
    }
    
    private void guardarResiduo(ActionEvent e) {
        if (validarDatos()) {
            String id = txtId.getText();
            String nombre = txtNombre.getText();
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
            double peso = Double.parseDouble(txtPeso.getText());
            String zona = (String) comboZona.getSelectedItem();
            int prioridad = (Integer) spinnerPrioridad.getValue();
            LocalDate fechaRecoleccion = LocalDate.now();
            
            this.residuo = new Residuo(id, nombre, tipoR, peso,fechaRecoleccion ,zona, prioridad);
            this.guardado = true;
            dispose();
        }
    }
    
    private boolean validarDatos() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El nombre del residuo es obligatorio.",
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus();
            return false;
        }
        
        try {
            double peso = Double.parseDouble(txtPeso.getText());
            if (peso <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "El peso debe ser mayor a cero.",
                    "Error de Validación", 
                    JOptionPane.ERROR_MESSAGE);
                txtPeso.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "El peso debe ser un número válido.",
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            txtPeso.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private String generarIdAutomatico() {
        return "R" + String.format("%04d", random.nextInt(10000));
    }
    
    public boolean isGuardado() {
        return guardado;
    }
    
    public Residuo getResiduo() {
        return residuo;
    }
}