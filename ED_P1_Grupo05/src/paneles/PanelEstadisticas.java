/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author ED_P1_Grupo05
 */
public class PanelEstadisticas extends JPanel {
    private GestorDatos gestor;
    private JTextArea areaEstadisticas;
    private JButton btnGenerarReporte, btnExportarCSV, btnActualizar;
    private JComboBox<String> comboReporte;
    private JCheckBox checkIncluirGraficos;
    
    public PanelEstadisticas(GestorDatos gestor) {
        this.gestor = gestor;
        inicializarComponentes();
        configurarEventos();
        generarEstadisticas();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelControles.add(new JLabel("Tipo de Reporte:"));
        
        comboReporte = new JComboBox<>(new String[]{
            "General", "Por Tipo de Residuo", "Por Zona", "Prioridad Ambiental", "Eficiencia de Recolección"
        });
        panelControles.add(comboReporte);
        
        btnGenerarReporte = new JButton("Generar Reporte");
        panelControles.add(btnGenerarReporte);
        
        btnActualizar = new JButton("Actualizar");
        panelControles.add(btnActualizar);
        
        checkIncluirGraficos = new JCheckBox("Incluir gráficos ASCII");
        panelControles.add(checkIncluirGraficos);
        
        btnExportarCSV = new JButton("Exportar a CSV");
        panelControles.add(btnExportarCSV);
        
        areaEstadisticas = new JTextArea(25, 60);
        areaEstadisticas.setEditable(false);
        areaEstadisticas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaEstadisticas.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollEstadisticas = new JScrollPane(areaEstadisticas);
        
        add(panelControles, BorderLayout.NORTH);
        add(scrollEstadisticas, BorderLayout.CENTER);
    }
    
    private void configurarEventos() {
        btnGenerarReporte.addActionListener(e -> generarEstadisticas());
        btnActualizar.addActionListener(e -> generarEstadisticas());
        btnExportarCSV.addActionListener(this::exportarACSV);
        comboReporte.addActionListener(e -> generarEstadisticas());
    }
   
    public void actualizarEstadisticas() {
        generarEstadisticas();
    }
    
    public void generarEstadisticas() {
        String tipoReporte = comboReporte.getSelectedItem().toString();  
        StringBuilder stats = new StringBuilder();
        
        stats.append("=== ECOTRACK - REPORTE ESTADÍSTICO ===\n");
        stats.append("Tipo: ").append(tipoReporte).append("\n");
        stats.append("Generado: ").append(java.time.LocalDateTime.now()).append("\n\n");
        stats.append("Total residuos: ").append(gestor.getResiduos().size()).append("\n\n");
        
        switch (tipoReporte) {
            case "General":
                generarEstadisticasGenerales(stats);
                break;
            case "Por Tipo de Residuo":
                generarEstadisticasPorTipo(stats);
                break;
            case "Por Zona":
                generarEstadisticasPorZona(stats);
                break;
            case "Prioridad Ambiental":
                generarEstadisticasPrioridad(stats);
                break;
            case "Eficiencia de Recolección":
                generarEstadisticasEficiencia(stats);
                break;
        }
        
        areaEstadisticas.setText(stats.toString());
    }
    
    private void generarEstadisticasGenerales(StringBuilder stats) {
        int totalResiduos =  gestor.getResiduos().size();
        double pesoTotal = 0;
        Map<String, Integer> conteoTipos = new java.util.HashMap<>();
        Map<String, Double> pesoPorZona = new java.util.HashMap<>();
        
        for (Residuo residuo : gestor.getResiduos()) {
            pesoTotal += residuo.getPesoKg();
            conteoTipos.merge(residuo.getTipo().toString(), 1, Integer::sum);
            pesoPorZona.merge(residuo.getZona(), residuo.getPesoKg(), Double::sum);
        }
        
        stats.append("📊 ESTADÍSTICAS GENERALES\n\n");
        stats.append(String.format("Total de residuos registrados: %d\n", totalResiduos));
        stats.append(String.format("Peso total de residuos: %.2f kg\n\n", pesoTotal));
        
        if (totalResiduos == 0) {
            stats.append("ℹ️ No hay residuos registrados en el sistema.\n");
            return;
        }
        
        stats.append(" DISTRIBUCIÓN POR TIPO\n");
        stats.append("Tipo           | Cantidad | Porcentaje\n");
        stats.append("---------------|----------|-----------\n");
        
        for (Map.Entry<String, Integer> entry : conteoTipos.entrySet()) {
            double porcentaje = (entry.getValue() * 100.0) / totalResiduos;
            stats.append(String.format("%-14s | %8d | %9.1f%%\n", entry.getKey(), entry.getValue(), porcentaje));
        }
        
        stats.append("\n🗺️ DISTRIBUCIÓN POR ZONA\n");
        stats.append("Zona           | Peso (kg) | Porcentaje\n");
        stats.append("---------------|-----------|-----------\n");
        
        for (Map.Entry<String, Double> entry : pesoPorZona.entrySet()) {
            double porcentaje = (entry.getValue() * 100.0) / pesoTotal;
            stats.append(String.format("%-14s | %9.2f | %9.1f%%\n", entry.getKey(), entry.getValue(), porcentaje));
        }
        
        if (checkIncluirGraficos.isSelected()) {
            stats.append("\n");
            stats.append("📊 GRÁFICO DE DISTRIBUCIÓN POR TIPO (ASCII)\n");
            for (Map.Entry<String, Integer> entry : conteoTipos.entrySet()) {
                int barras = (entry.getValue() * 20) / totalResiduos;
                stats.append(String.format("%-12s: %s\n", entry.getKey(), "█".repeat(Math.max(1, barras))));
            }
            
            stats.append("\n📊 GRÁFICO DE DISTRIBUCIÓN POR ZONA (ASCII)\n");
            for (Map.Entry<String, Double> entry : pesoPorZona.entrySet()) {
                int barras = (int)((entry.getValue() * 20) / pesoTotal);;
                stats.append(String.format("%-12s: %s\n", entry.getKey(), "█".repeat(Math.max(1, barras))));
            }
        }
    }
    
    private void generarEstadisticasPorTipo(StringBuilder stats) {
        Map<String, Double> statsTipo = gestor.obtenerEstadisticasPorTipo();
        double pesoTotal = statsTipo.values().stream().mapToDouble(Double::doubleValue).sum();
        
        stats.append("️ ESTADÍSTICAS POR TIPO DE RESIDUO\n\n");
         if (statsTipo.isEmpty()) {
            stats.append("ℹ️ No hay residuos registrados en el sistema.\n");
            return;
        }
         
        stats.append("Tipo           | Peso (kg) | Porcentaje\n");
        stats.append("---------------|-----------|-----------\n");
        
        for (Map.Entry<String, Double> entry : statsTipo.entrySet()) {
            double porcentaje = (entry.getValue() * 100.0) / pesoTotal;
            stats.append(String.format("%-14s | %9.2f | %9.1f%%\n", entry.getKey(), entry.getValue(), porcentaje));
        }
        
        if (checkIncluirGraficos.isSelected()) {
            stats.append("\n📊 GRÁFICO DE PESO POR TIPO\n");
            for (Map.Entry<String, Double> entry : statsTipo.entrySet()) {
                int barras = (int) ((entry.getValue() * 20) / pesoTotal);
                double porcentaje = (entry.getValue() * 100.0) / pesoTotal;
                stats.append(String.format("%-12s: %s %.1f%%\n", entry.getKey(), "█".repeat(Math.max(1, barras)), porcentaje));
            }
        }
    }
    
    private void generarEstadisticasPorZona(StringBuilder stats) {
        Map<String, Double> statsZona = gestor.obtenerEstadisticasPorZona();
        double pesoTotal = statsZona.values().stream().mapToDouble(Double::doubleValue).sum();
        
        stats.append("🗺️ ESTADÍSTICAS POR ZONA\n\n");
        if (statsZona.isEmpty()) {
            stats.append("ℹ️ No hay residuos registrados en el sistema.\n");
            return;
        }
        stats.append("Zona           | Peso (kg) | Porcentaje | Utilidad\n");
        stats.append("---------------|-----------|------------|----------\n");
        
        for (Map.Entry<String, Double> entry : statsZona.entrySet()) {
            double porcentaje = (entry.getValue() * 100.0) / pesoTotal;
            Zona zona = gestor.obtenerZona(entry.getKey());
            double utilidad = zona != null ? zona.utilidad() : 0;
            
            stats.append(String.format("%-14s | %9.2f | %10.1f%% | %8.2f\n", entry.getKey(), entry.getValue(), porcentaje, utilidad));
        }
        
        if (checkIncluirGraficos.isSelected()) {
            stats.append("\n📊 GRÁFICO DE DISTRIBUCIÓN POR ZONA\n");
            for (Map.Entry<String, Double> entry : statsZona.entrySet()) {
                int barras = (int) ((entry.getValue() * 20) / pesoTotal);
                stats.append(String.format("%-12s: %s\n", entry.getKey(), "█".repeat(Math.max(1, barras))));
            }
        }
    }
    
    private void generarEstadisticasPrioridad(StringBuilder stats) {
        Map<Integer, Integer> conteoPrioridad = new java.util.HashMap<>();
        Map<Integer, Double> pesoPorPrioridad = new java.util.HashMap<>();
        
        for (Residuo residuo : gestor.getResiduos()) {
            int prioridad = residuo.getPrioridadAmbiental();
            conteoPrioridad.merge(prioridad, 1, Integer::sum);
            pesoPorPrioridad.merge(prioridad, residuo.getPesoKg(), Double::sum);
        }
        
        stats.append("⚠️ ESTADÍSTICAS POR PRIORIDAD AMBIENTAL\n\n");
        
        if (conteoPrioridad.isEmpty()) {
            stats.append("ℹ️ No hay residuos registrados en el sistema.\n");
            return;
        }
        
        stats.append("Prioridad | Cantidad | Peso (kg) | Nivel de Riesgo\n");
        stats.append("----------|----------|-----------|-----------------\n");
        
        for (int i = 1; i <= 10; i++) {
            int cantidad = conteoPrioridad.getOrDefault(i, 0);
            double peso = pesoPorPrioridad.getOrDefault(i, 0.0);
            String riesgo = obtenerNivelRiesgo(i);
            
            if (cantidad > 0) {
                stats.append(String.format("%9d | %8d | %9.2f | %s\n", i, cantidad, peso, riesgo));
            }
        }
        
        int totalResiduos = conteoPrioridad.values().stream().mapToInt(Integer::intValue).sum();
        int altoRiesgo = 0, medioRiesgo = 0, bajoRiesgo = 0;
        
        for (Map.Entry<Integer, Integer> entry : conteoPrioridad.entrySet()) {
            if (entry.getKey() >= 8) altoRiesgo += entry.getValue();
            else if (entry.getKey() >= 5) medioRiesgo += entry.getValue();
            else bajoRiesgo += entry.getValue();
        }
        
        stats.append("\n📋 RESUMEN DE RIESGO AMBIENTAL\n");
        stats.append(String.format("Alto riesgo (8-10): %d residuos (%.1f%%)\n", 
            altoRiesgo, totalResiduos > 0 ? (altoRiesgo * 100.0) / totalResiduos : 0));
        stats.append(String.format("Medio riesgo (5-7): %d residuos (%.1f%%)\n", 
            medioRiesgo, totalResiduos > 0 ? (medioRiesgo * 100.0) / totalResiduos : 0));
        stats.append(String.format("Bajo riesgo (1-4): %d residuos (%.1f%%)\n", 
            bajoRiesgo, totalResiduos > 0 ? (bajoRiesgo * 100.0) / totalResiduos : 0));
    }
    
    private void generarEstadisticasEficiencia(StringBuilder stats) {
        stats.append(" EFICIENCIA DE RECOLECCIÓN POR ZONA\n\n");
        
        if (gestor.getZonas().isEmpty()) {
            stats.append("ℹ️ No hay zonas registradas en el sistema.\n");
            return;
        }
        
        stats.append("Zona           | Pendiente (kg) | Recolectado (kg) | Utilidad  | Eficiencia\n");
        stats.append("---------------|----------------|------------------|-----------|-----------\n");
        
        for (Zona zona : gestor.obtenerZonasOrdenadasPorUtilidad()) {
            double pendiente = zona.getPendiente();
            double recolectado = zona.getRecolectado();
            double utilidad = zona.utilidad();
            double eficiencia = (pendiente + recolectado) > 0 ? (recolectado / (pendiente + recolectado)) * 100 : 0;
            
            stats.append(String.format("%-14s | %14.2f | %16.2f | %9.2f | %9.1f%%\n", zona.getNombre(), pendiente, recolectado, utilidad, eficiencia));
        }
        
        double totalPendiente = 0;
        double totalRecolectado = 0;
        
        for (Zona zona : gestor.getZonas().values()) {
            totalPendiente += zona.getPendiente();
            totalRecolectado += zona.getRecolectado();
        }
        
        double eficienciaGeneral = (totalPendiente + totalRecolectado) > 0 ?
            (totalRecolectado / (totalPendiente + totalRecolectado)) * 100 : 0;
        
        stats.append("\n📊 EFICIENCIA GENERAL DEL SISTEMA\n");
        stats.append(String.format("Total pendiente: %.2f kg\n", totalPendiente));
        stats.append(String.format("Total recolectado: %.2f kg\n", totalRecolectado));
        stats.append(String.format("Eficiencia general: %.1f%%\n", eficienciaGeneral));
        
        stats.append("\n RECOMENDACIONES:\n");
        if (eficienciaGeneral < 50) {
            stats.append("• Se requiere optimización urgente de rutas\n");
            stats.append("• Incrementar frecuencia de recolección\n");
            stats.append("• Revisar asignación de vehículos por zona\n");
        } else if (eficienciaGeneral < 75) {
            stats.append("• Buen desempeño, oportunidades de mejora\n");
            stats.append("• Optimizar rutas en zonas de baja eficiencia\n");
        } else {
            stats.append("• Excelente eficiencia del sistema\n");
            stats.append("• Mantener estrategias actuales\n");
        }
    }
    
    private String obtenerNivelRiesgo(int prioridad) {
        if (prioridad >= 8) return "🚨 ALTO";
        if (prioridad >= 5) return "⚠️  MEDIO";
        return "✅ BAJO";
    }
    
    private void exportarACSV(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar estadísticas a CSV");
        fileChooser.setSelectedFile(new java.io.File("estadisticas_ecotrack.csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (java.io.PrintWriter writer = new java.io.PrintWriter(fileChooser.getSelectedFile())) {
                String tipoReporte = comboReporte.getSelectedItem().toString();
                
                switch (tipoReporte) {
                    case "General":
                        exportarEstadisticasGeneralesCSV(writer);
                        break;
                    case "Por Tipo de Residuo":
                        exportarEstadisticasPorTipoCSV(writer);
                        break;
                    case "Por Zona":
                        exportarEstadisticasPorZonaCSV(writer);
                        break;
                    case "Prioridad Ambiental":
                        exportarEstadisticasPrioridadCSV(writer);
                        break;
                    case "Eficiencia de Recolección":
                        exportarEstadisticasEficienciaCSV(writer);
                        break;
                }
                
                JOptionPane.showMessageDialog(this, "Estadísticas exportadas exitosamente a CSV.","Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(),"Error de Exportación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportarEstadisticasGeneralesCSV(java.io.PrintWriter writer) {
        writer.println("Tipo_Estadistica,Valor");
        writer.println("Total_Residuos," + gestor.getResiduos().size());
        
        double pesoTotal = 0;
        for (Residuo r : gestor.getResiduos()) {
            pesoTotal += r.getPesoKg();
        }
        writer.println("Peso_Total_kg," + String.format("%.2f", pesoTotal));
        
        writer.println("\nDistribucion_Por_Tipo");
        writer.println("Tipo,Cantidad,Porcentaje");
        Map<String, Integer> conteoTipos = new java.util.HashMap<>();
        for (Residuo r : gestor.getResiduos()) {
            conteoTipos.merge(r.getTipo().toString(), 1, Integer::sum);
        }
        
        int total = gestor.getResiduos().size();
        for (Map.Entry<String, Integer> entry : conteoTipos.entrySet()) {
            double porcentaje = total > 0 ? (entry.getValue() * 100.0) / total : 0;
            writer.printf("%s,%d,%.1f%%\n", entry.getKey(), entry.getValue(), porcentaje);
        }
    }
    
    private void exportarEstadisticasPorTipoCSV(java.io.PrintWriter writer) {
        writer.println("Tipo,Peso_kg,Porcentaje");
        Map<String, Double> statsTipo = gestor.obtenerEstadisticasPorTipo();
        double pesoTotal = statsTipo.values().stream().mapToDouble(Double::doubleValue).sum();
        
        for (Map.Entry<String, Double> entry : statsTipo.entrySet()) {
            double porcentaje = pesoTotal > 0 ? (entry.getValue() * 100.0) / pesoTotal : 0;
            writer.printf("%s,%.2f,%.1f%%\n", entry.getKey(), entry.getValue(), porcentaje);
        }
    }
    
    private void exportarEstadisticasPorZonaCSV(java.io.PrintWriter writer) {
        writer.println("Zona,Peso_kg,Porcentaje,Utilidad");
        Map<String, Double> statsZona = gestor.obtenerEstadisticasPorZona();
        double pesoTotal = statsZona.values().stream().mapToDouble(Double::doubleValue).sum();
        
        for (Map.Entry<String, Double> entry : statsZona.entrySet()) {
            double porcentaje = pesoTotal > 0 ? (entry.getValue() * 100.0) / pesoTotal : 0;
            Zona zona = gestor.obtenerZona(entry.getKey());
            double utilidad = zona != null ? zona.utilidad() : 0;
            writer.printf("%s,%.2f,%.1f%%,%.2f\n", entry.getKey(), entry.getValue(), porcentaje, utilidad);
        }
    }
    
    private void exportarEstadisticasPrioridadCSV(java.io.PrintWriter writer) {
        writer.println("Prioridad,Cantidad,Peso_kg,Nivel_Riesgo");
        Map<Integer, Integer> conteoPrioridad = new java.util.HashMap<>();
        Map<Integer, Double> pesoPorPrioridad = new java.util.HashMap<>();
        
        for (Residuo residuo : gestor.getResiduos()) {
            int prioridad = residuo.getPrioridadAmbiental();
            conteoPrioridad.merge(prioridad, 1, Integer::sum);
            pesoPorPrioridad.merge(prioridad, residuo.getPesoKg(), Double::sum);
        }
        
        for (int i = 1; i <= 10; i++) {
            int cantidad = conteoPrioridad.getOrDefault(i, 0);
            double peso = pesoPorPrioridad.getOrDefault(i, 0.0);
            String riesgo = obtenerNivelRiesgo(i).replace("🚨", "ALTO").replace("⚠️", "MEDIO").replace("✅", "BAJO").trim();
            writer.printf("%d,%d,%.2f,%s\n", i, cantidad, peso, riesgo);
        }
    }
    
    private void exportarEstadisticasEficienciaCSV(java.io.PrintWriter writer) {
        writer.println("Zona,Pendiente_kg,Recolectado_kg,Utilidad,Eficiencia_%");
        
        for (Zona zona : gestor.obtenerZonasOrdenadasPorUtilidad()) {
            double pendiente = zona.getPendiente();
            double recolectado = zona.getRecolectado();
            double utilidad = zona.utilidad();
            double eficiencia = (pendiente + recolectado) > 0 ? (recolectado / (pendiente + recolectado)) * 100 : 0;
            
            writer.printf("%s,%.2f,%.2f,%.2f,%.1f\n", zona.getNombre(), pendiente, recolectado, utilidad, eficiencia);
        }
    }
}
