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
public class Zona implements Serializable, Comparable<Zona>{
    private static final long serialVersionUID = 1L;
    private final String nombre;
    private double recolectado;
    private double pendiente;

    public Zona(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }
    public double getRecolectado() { return recolectado; }
    public double getPendiente() { return pendiente; }

    public void setRecolectado(double recolectado) {
        this.recolectado = recolectado;
    }

    public void setPendiente(double pendiente) {
        this.pendiente = pendiente;
    }
    

    public void addRecolectado(double v) { this.recolectado += v; }
    public void addPendiente(double v) { this.pendiente += v; }

    public double utilidad() { return recolectado - pendiente; }

    @Override
    public String toString() {
        return nombre + " (u=" + utilidad() + ")";
    }
    
    @Override
    public int compareTo(Zona otra) {
        return Double.compare(otra.utilidad(), this.utilidad());
    }
}
