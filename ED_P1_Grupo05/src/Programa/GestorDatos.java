/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Programa;

import Comparadores.ComparadorPorPeso;
import Listas.ArrayList;
import Listas.List;
import Comparadores.ComparadorPorPrioridad;
import Comparadores.ComparadorPorTipo;
import Listas.CircularLinkedList;
import Serializable.Archivo;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author ED_P1_Grupo05
 */
public class GestorDatos {
    private CircularLinkedList<Residuo> residuos;
    private Map<String, Zona> zonas;
    private Deque<Residuo> pilaReciclaje;
    private Queue<Vehiculo> vehiculos;
    private Archivo archivo;
     private CentroReciclaje centroReciclaje;
    
    public GestorDatos() {
        this.residuos = new CircularLinkedList<>();
        this.zonas = new HashMap<>();
        this.pilaReciclaje = new LinkedList<>();
        this.vehiculos = new PriorityQueue<>(new Comparator<Vehiculo>() {
            @Override
            public int compare(Vehiculo v1, Vehiculo v2) {
                return Double.compare(v2.getPrioridad(), v1.getPrioridad());
            }
        });
        this.archivo = new Archivo();
        this.centroReciclaje = new CentroReciclaje();
   
        archivo.verificarEstadoArchivos();
        
        cargarDatos();
        
        if (zonas.isEmpty()) {
            inicializarZonas();
        }
        
        System.out.println(" Sistema inicializado con " + residuos.size() + " residuos y " + zonas.size() + " zonas");
    }
    
    private void inicializarZonas() {
        String[] nombresZonas = {"Zona Norte", "Zona Sur", "Zona Este", "Zona Oeste", "Centro"};
        for (String nombre : nombresZonas) {
            zonas.put(nombre, new Zona(nombre));
        }
        System.out.println("️ Zonas inicializadas: " + nombresZonas.length + " zonas creadas");
    }
    
    public void agregarResiduo(Residuo residuo) {
        residuos.addLast(residuo);
        Zona zona = zonas.get(residuo.getZona());
        if (zona != null) {
            zona.addPendiente(residuo.getPesoKg());
        }
        
        archivo.guardarResiduos(residuos);
        archivo.guardarZonas(zonas);
        
        System.out.println("➕ Residuo agregado: " + residuo.getNombre() + " | Total: " + residuos.size());
    }
    
    public boolean eliminarResiduo(Residuo residuo) {
        if (residuo == null) return false;
        
        int index = residuos.indexOf(residuo);
        if (index == -1) return false;
        
        Zona zona = zonas.get(residuo.getZona());
        if (zona != null) {
            zona.setPendiente(zona.getPendiente() - residuo.getPesoKg());
        }
        
        Residuo eliminado = residuos.remove(index);
        
        archivo.guardarResiduos(residuos);
        archivo.guardarZonas(zonas);
        
        System.out.println("➖ Residuo eliminado: " + residuo.getNombre() + " | Total: " + residuos.size());
        
        return eliminado != null;
    }
    
    public List<Residuo> obtenerResiduosOrdenados(String criterio) {
        List<Residuo> lista = new ArrayList<>();
        for (Residuo residuo : residuos) {
            lista.addLast(residuo);
        }
        
        switch (criterio.toLowerCase()) {
            case "peso":
                lista.sort(new ComparadorPorPeso());
                break;
            case "tipo":
                lista.sort(new ComparadorPorTipo());
                break;
            case "Prioridad Ambiental":
                residuos.sort(new ComparadorPorPrioridad());
                break;
            default:
                lista.sort(null);
        }
        
        return lista;
    }
    
    public List<Zona> obtenerZonasOrdenadasPorUtilidad() {
        List<Zona> listaZonas = new ArrayList<>();
        for (Zona zona : zonas.values()) {
            listaZonas.addLast(zona);
        }

        listaZonas.sort(new Comparator<Zona>() {
            @Override
            public int compare(Zona z1, Zona z2) {
                return Double.compare(z2.utilidad(), z1.utilidad()); 
            }
        });

        return listaZonas;
    }
    
    public Zona obtenerZona(String nombre) {
        return zonas.get(nombre);
    }
    
    public void agregarAReciclaje(Residuo residuo) {
        pilaReciclaje.push(residuo);
        eliminarResiduo(residuo); 
    }
    
    public Residuo procesarReciclaje() {
         if (!pilaReciclaje.isEmpty()) {
            Residuo residuo = pilaReciclaje.pop(); 
            centroReciclaje.procesarResiduo(residuo); 
            return residuo;
        }
        return null;
    }
    
    public void agregarVehiculo(Vehiculo vehiculo) {
        vehiculos.offer(vehiculo);
        System.out.println("🚚 Vehículo agregado: " + vehiculo.getId());
    }
    
    public Vehiculo despacharVehiculo() {
        Vehiculo vehiculo = vehiculos.poll();
        if (vehiculo != null) {
            Zona zona = zonas.get(vehiculo.getZonaAsignada());
            if (zona != null && zona.getPendiente() > 0) {
                double cantidadRecolectar = Math.min(zona.getPendiente(), 50.0);
                zona.addRecolectado(cantidadRecolectar);
                zona.setPendiente(zona.getPendiente() - cantidadRecolectar);
                
                archivo.guardarZonas(zonas);
                
                System.out.println("📦 Recolectados " + cantidadRecolectar + " kg en " + zona.getNombre());
            }
        }
        return vehiculo;
    }
    
    public Map<String, Double> obtenerEstadisticasPorTipo() {
        Map<String, Double> stats = new HashMap<>();
        for (Residuo residuo : residuos) {
            stats.merge(residuo.getTipo().toString(), residuo.getPesoKg(), Double::sum);
        }
        return stats;
    }
    
    public Map<String, Double> obtenerEstadisticasPorZona() {
        Map<String, Double> stats = new HashMap<>();
        for (Residuo residuo : residuos) {
            stats.merge(residuo.getZona(), residuo.getPesoKg(), Double::sum);
        }
        return stats;
    }
    
    public void guardarDatos() {
        archivo.guardarResiduos(residuos);
        archivo.guardarZonas(zonas);
        System.out.println("💾 Datos guardados: " + residuos.size() + " residuos, " + zonas.size() + " zonas");
    }
    
    public void cargarDatos() {
        CircularLinkedList<Residuo> residuosCargados = archivo.cargarResiduos();
        if (residuosCargados != null) {
            this.residuos = residuosCargados;
        }

        Map<String, Zona> zonasCargadas = archivo.cargarZonas();
        if (zonasCargadas != null && !zonasCargadas.isEmpty()) {
            this.zonas = zonasCargadas;
        }
    }
    
    
    public Residuo buscarResiduoPorId(String id) {
        for (Residuo r : residuos) {
            if (r.getId().equalsIgnoreCase(id)) return r;
        }
        return null;
    }

    public int indexOfResiduo(Residuo objetivo) {
        return residuos.indexOf(objetivo);
    }
    
    public CircularLinkedList<Residuo> getResiduos() { return residuos; }
    public Map<String, Zona> getZonas() { return zonas; }
    public Deque<Residuo> getPilaReciclaje() { return pilaReciclaje; }
    public Queue<Vehiculo> getVehiculos() { return vehiculos; }

    
    public CentroReciclaje getCentroReciclaje() {
        return centroReciclaje;
    }
    
    
}
