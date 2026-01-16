/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Programa;

import Programa.TipoResiduo;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author ED_P1_Grupo05
 */
public class Residuo implements Serializable, Comparable<Residuo>{
    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private TipoResiduo tipo;
    private double pesoKg;
    private LocalDate fechaRecoleccion;
    private String zona;
    private int prioridadAmbiental;

    public Residuo(String id, String nombre, TipoResiduo tipo, double pesoKg, LocalDate fechaRecoleccion, String zona, int prioridadAmbiental) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.pesoKg = pesoKg;
        this.fechaRecoleccion = fechaRecoleccion;
        this.zona = zona;
        this.prioridadAmbiental = prioridadAmbiental;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public TipoResiduo getTipo() { return tipo; }
    public double getPesoKg() { return pesoKg; }
    public LocalDate getFechaRecoleccion() { return fechaRecoleccion; }
    public String getZona() { return zona; }
    public int getPrioridadAmbiental() { return prioridadAmbiental; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTipo(TipoResiduo tipo) { this.tipo = tipo; }
    public void setPesoKg(double pesoKg) { this.pesoKg = pesoKg; }
    public void setFechaRecoleccion(LocalDate fechaRecoleccion) { this.fechaRecoleccion = fechaRecoleccion; }
    public void setZona(String zona) { this.zona = zona; }
    public void setPrioridadAmbiental(int prioridadAmbiental) { this.prioridadAmbiental = prioridadAmbiental; }
    

    @Override
    public String toString() {
        return String.format("%s(%s) %.2fkg, zona=%s, prio=%d", nombre, tipo, pesoKg, zona, prioridadAmbiental);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Residuo)) return false;
        Residuo residuo = (Residuo) o;
        return Objects.equals(id, residuo.id);
    }
    
    @Override
    public int compareTo(Residuo o) {
        return Double.compare(
            this.getPrioridadAmbiental(),
            o.getPrioridadAmbiental()
        );
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
