/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Programa;

import java.io.Serializable;

/**
 *
 * @author ED_P1_Grupo05
 */
public class Vehiculo implements Serializable, Comparable<Vehiculo> {
    private static final long serialVersionUID = 1L;
    private String id;
    private String tipo;
    private String zonaAsignada;
    private double capacidad;
    private double prioridad;
    
    public Vehiculo(String id, String tipo, String zonaAsignada, double capacidad, GestorDatos gestor) {
        this.id = id;
        this.tipo = tipo;
        this.zonaAsignada = zonaAsignada;
        this.capacidad = capacidad;
        this.prioridad = calcularPrioridad(gestor);
    }
    
    private double calcularPrioridad(GestorDatos gestor) {
        Zona zona = gestor.obtenerZona(this.zonaAsignada);
        if (zona == null) return 3.0;
        
        double utilidad = zona.utilidad();
        double pendiente = zona.getPendiente();
        
        // Fórmula de prioridad según requisitos del PDF
        if (utilidad < -20) return 10.0;
        if (utilidad < -10) return 8.5;
        if (utilidad < 0) return 7.0;
        if (pendiente > 50) return 6.0;
        if (pendiente > 20) return 4.5;
        return 2.0;
    }
    
    public String getId() { return id; }
    public String getTipo() { return tipo; }
    public String getZonaAsignada() { return zonaAsignada; }
    public double getCapacidad() { return capacidad; }
    public double getPrioridad() { return prioridad; }
    
    @Override
    public String toString() {
        return String.format("Vehículo %s - %s (%s) - Prioridad: %.2f", id, tipo, zonaAsignada, prioridad);
    }
    
    @Override
    public int compareTo(Vehiculo otro) {
        return Double.compare(otro.prioridad, this.prioridad); // Orden descendente
    }
}
