/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ecotrackproy;

import GUI.EcoTrackGUI;
import java.io.File;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author ED_P1_Grupo05
 */
public class EcoTrackProy {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //metodo para borrar anteriores archivos
        //eliminarArchivosCorruptos();
    
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            EcoTrackGUI gui = new EcoTrackGUI();
            gui.setVisible(true);
        });
    }

    private static void eliminarArchivosCorruptos() {
        try {
            File directorioData = new File("data");
            if (directorioData.exists()) {
                File[] archivos = directorioData.listFiles();
                if (archivos != null) {
                    for (File archivo : archivos) {
                        if (archivo.getName().endsWith(".dat")) {
                            archivo.delete();
                            System.out.println("Eliminado: " + archivo.getName());
                        }
                    }
                }
                System.out.println("Archivos corruptos eliminados. Sistema listo para nuevo inicio.");
            }
        } catch (Exception e) {
            System.err.println("Error eliminando archivos: " + e.getMessage());
        }
    }
    
}
