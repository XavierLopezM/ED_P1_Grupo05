/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comparadores;

import Programa.Residuo;
import java.util.Comparator;

/**
 *
 * @author ED_P1_Grupo05
 */
public class ComparadorPorPeso implements Comparator<Residuo> {
    @Override
    public int compare(Residuo r1, Residuo r2) {
        return Double.compare(r1.getPesoKg(), r2.getPesoKg());
    }
}
