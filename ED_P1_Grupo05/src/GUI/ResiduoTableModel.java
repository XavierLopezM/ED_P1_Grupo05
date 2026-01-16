/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Listas.CircularLinkedList;
import Programa.Residuo;
import Listas.List;
import java.time.format.DateTimeFormatter;
import Listas.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ED_P1_Grupo05
 */
public class ResiduoTableModel extends AbstractTableModel {
    private final String[] columnNames = {
        "ID", "Nombre", "Tipo", "Peso (kg)", "Zona", "Prioridad", "Fecha Recolección"
    };
    
    private List<Residuo> datos;
    private DateTimeFormatter formatter;
    
    public ResiduoTableModel(CircularLinkedList<Residuo> residuos) {
        this.datos = new ArrayList<>();
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        actualizarDatos(residuos);
    }
    
    public void actualizarDatos(CircularLinkedList<Residuo> residuos) {
        datos.clear();
        if (residuos != null) {
            for (Residuo residuo : residuos) {
                datos.addLast(residuo);
            }
        }
        fireTableDataChanged();
    }
    
    public void setDatos(List<Residuo> nuevosDatos) {
        this.datos = nuevosDatos != null ? nuevosDatos : new ArrayList<>();
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return datos.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= datos.size()) {
            return null;
        }
        
        Residuo residuo = datos.get(rowIndex);
        switch (columnIndex) {
            case 0: return residuo.getId();
            case 1: return residuo.getNombre();
            case 2: return residuo.getTipo();
            case 3: return residuo.getPesoKg(); 
            case 4: return residuo.getZona();
            case 5: return residuo.getPrioridadAmbiental();
            case 6: return residuo.getFechaRecoleccion().format(formatter);
            default: return null;
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 3: return Double.class;    
            case 5: return Integer.class;   
            default: return String.class;
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; 
    }
    
    public Residuo getResiduoAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < datos.size()) {
            return datos.get(rowIndex);
        }
        return null;
    }
}