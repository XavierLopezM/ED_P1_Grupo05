/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Serializable;

import Listas.CircularLinkedList;
import Programa.Residuo;
import Programa.Zona;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 *
 * @author ED_P1_Grupo05
 */
public class Archivo {
    private static final String DIRECTORIO_DATA = "data";
    private static final String ARCHIVO_RESIDUOS = DIRECTORIO_DATA + "/residuos.dat";
    private static final String ARCHIVO_ZONAS = DIRECTORIO_DATA + "/zonas.dat";
    
    public Archivo() {
        crearDirectorioData();
    }
    
    private void crearDirectorioData() {
        File directorio = new File(DIRECTORIO_DATA);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println(" Directorio 'data' creado exitosamente");
            } else {
                System.err.println("❌ Error creando directorio 'data'");
            }
        }
    }
    
    public void guardarResiduos(CircularLinkedList<Residuo> residuos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_RESIDUOS))) {
            oos.writeObject(residuos);
            System.out.println("✅ Residuos guardados exitosamente: " + residuos.size() + " residuos");
        } catch (IOException e) {
            System.err.println("❌ Error guardando residuos: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public CircularLinkedList<Residuo> cargarResiduos() {
        File archivo = new File(ARCHIVO_RESIDUOS);
        if (!archivo.exists()) {
            System.out.println(" Archivo de residuos no encontrado, creando nueva lista");
            return new CircularLinkedList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_RESIDUOS))) {
            CircularLinkedList<Residuo> residuosCargados = (CircularLinkedList<Residuo>) ois.readObject();
            System.out.println("✅ Residuos cargados exitosamente: " + residuosCargados.size() + " residuos");
            return residuosCargados;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Error cargando residuos: " + e.getMessage());
            System.out.println(" Creando nueva lista debido a error...");
            return new CircularLinkedList<>();
        }
    }
    
    public void guardarZonas(Map<String, Zona> zonas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_ZONAS))) {
            oos.writeObject(zonas);
            System.out.println("✅ Zonas guardadas exitosamente: " + zonas.size() + " zonas");
        } catch (IOException e) {
            System.err.println("❌ Error guardando zonas: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Zona> cargarZonas() {
        File archivo = new File(ARCHIVO_ZONAS);
        if (!archivo.exists()) {
            System.out.println(" Archivo de zonas no encontrado, se crearán zonas por defecto");
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_ZONAS))) {
            Map<String, Zona> zonasCargadas = (Map<String, Zona>) ois.readObject();
            System.out.println("✅ Zonas cargadas exitosamente: " + zonasCargadas.size() + " zonas");
            return zonasCargadas;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Error cargando zonas: " + e.getMessage());
            return null;
        }
    }
    
    public void verificarEstadoArchivos() {
        File archivoResiduos = new File(ARCHIVO_RESIDUOS);
        File archivoZonas = new File(ARCHIVO_ZONAS);
        
        System.out.println("\n=== ESTADO DE ARCHIVOS ===");
        System.out.println(" Residuos: " + (archivoResiduos.exists() ? "✅ Existe" : "❌ No existe"));
        System.out.println(" Zonas: " + (archivoZonas.exists() ? "✅ Existe" : "❌ No existe"));
        System.out.println("==========================\n");
    }

}
