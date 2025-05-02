import javax.swing.*;
import java.awt.*;

/**
 * Clase VentanaPrincipal
 * Representa la ventana principal del sistema de gestión veterinaria.
 */
public class VentanaPrincipal extends JFrame {
    
    /**
     * Constructor de la ventana principal.
     * Define la estructura de la interfaz gráfica y agrega pestañas.
     */
    public VentanaPrincipal() {
        try {
            setTitle("Sistema de Gestión Veterinaria"); // Título de la ventana
            setSize(800, 600); // Tamaño de la ventana
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana
            setLocationRelativeTo(null); // Centra la ventana en la pantalla
            setLayout(new BorderLayout()); // Establece el diseño de la ventana

            // Creación del menú superior
            JMenuBar menuBar = new JMenuBar();
            JMenu menuArchivo = new JMenu("Archivo");
            JMenuItem salirItem = new JMenuItem("Salir");
            salirItem.addActionListener(_ -> System.exit(0)); // Cierra la aplicación cuando se presiona
            menuArchivo.add(salirItem);
            menuBar.add(menuArchivo);
            setJMenuBar(menuBar); // Agrega el menú a la ventana

            // Creación de pestañas para cada módulo
            JTabbedPane pestañas = new JTabbedPane();
            pestañas.addTab("Mascotas", new PanelMascotas()); // Pestaña de mascotas
            pestañas.addTab("Propietarios", new PanelPropietarios()); // Pestaña de propietarios
            pestañas.addTab("Veterinarios", new PanelVeterinarios()); // Pestaña de veterinarios
            pestañas.addTab("Citas", new PanelCitas()); // Pestaña de citas veterinarias
            pestañas.addTab("Servicios", new PanelServicios()); // Pestaña de servicios

            add(pestañas, BorderLayout.CENTER); // Agrega las pestañas a la ventana
        } catch (Exception e) {
            System.err.println("Error al cargar la interfaz gráfica: " + e.getMessage());
        }
    }

    /**
     * Método principal para iniciar la aplicación.
     * La UI se ejecuta en el hilo de eventos de Swing para evitar bloqueos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true); // Hace visible la ventana
            } catch (Exception e) {
                System.err.println("Error inesperado al iniciar la aplicación: " + e.getMessage());
            }
        });
    }
}