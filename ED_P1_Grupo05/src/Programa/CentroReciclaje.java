/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Programa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ED_P1_Grupo05
 */
public class CentroReciclaje implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Double> residuosProcesados;
    private double totalProcesado;
    private int totalResiduosProcesados;
    
    public CentroReciclaje() {
        this.residuosProcesados = new HashMap<>();
        this.totalProcesado = 0;
        this.totalResiduosProcesados = 0;
    }
    
    public void procesarResiduo(Residuo residuo) {
        String tipo = residuo.getTipo().toString();
        double peso = residuo.getPesoKg();
        
        residuosProcesados.merge(tipo, peso, Double::sum);
        totalProcesado += peso;
        totalResiduosProcesados++;
    }
    
    public Map<String, Double> getEstadisticasProcesamiento() {
        return new HashMap<>(residuosProcesados);
    }
    
    public double getTotalProcesado() {
        return totalProcesado;
    }
    
    public int getTotalResiduosProcesados() {
        return totalResiduosProcesados;
    }
    
    public String getTipoMasProcesado() {
        if (residuosProcesados.isEmpty()) {
            return "Ninguno";
        }
        
        return residuosProcesados.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }
    
    public double getPorcentajeTipo(String tipo) {
        if (totalProcesado == 0) return 0;
        Double peso = residuosProcesados.get(tipo);
        return peso != null ? (peso / totalProcesado) * 100 : 0;
    }
    
    public void reiniciarEstadisticas() {
        residuosProcesados.clear();
        totalProcesado = 0;
        totalResiduosProcesados = 0;
    }
    
    @Override
    public String toString() {
        return String.format("CentroReciclaje{totalProcesado=%.2f kg, totalResiduos=%d}", totalProcesado, totalResiduosProcesados);
    }
}
