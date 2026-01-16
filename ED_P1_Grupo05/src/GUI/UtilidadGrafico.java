/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Programa.Zona;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ED_P1_Grupo05
 */
public class UtilidadGrafico {
    
    public static String generarGraficoBarras(List<Zona> zonas) {
        StringBuilder grafico = new StringBuilder();
        grafico.append("📊 GRÁFICO DE UTILIDAD AMBIENTAL POR ZONA\n\n");
        
        int maxBarras = 20;
        double maxUtilidad = zonas.stream().mapToDouble(Zona::utilidad).map(Math::abs).max().orElse(1.0);
        
        for (Zona zona : zonas) {
            double utilidad = zona.utilidad();
            int barras = (int) ((Math.abs(utilidad) * maxBarras) / maxUtilidad);
            String barra = "█".repeat(Math.max(1, barras));
            
            if (utilidad >= 0) {
                grafico.append(String.format("%-12s: %s %.2f\n", zona.getNombre(), barra, utilidad));
            } else {
                grafico.append(String.format("%-12s: %s %.2f\n", zona.getNombre(), barra, utilidad));
            }
        }
        
        return grafico.toString();
    }
    
    public static String generarGraficoTorta(Map<String, Double> datos, String titulo) {
        StringBuilder grafico = new StringBuilder();
        grafico.append(titulo).append("\n\n");
        
        double total = datos.values().stream().mapToDouble(Double::doubleValue).sum();
        String[] simbolos = {"🟥", "🟦", "🟩", "🟨", "🟪", "🟧"};
        int simboloIndex = 0;
        
        for (Map.Entry<String, Double> entry : datos.entrySet()) {
            double porcentaje = (entry.getValue() * 100.0) / total;
            String simbolo = simbolos[simboloIndex % simbolos.length];
            grafico.append(String.format("%s %-12s: %.1f%%\n", simbolo, entry.getKey(), porcentaje));
            simboloIndex++;
        }
        
        return grafico.toString();
    }
}
