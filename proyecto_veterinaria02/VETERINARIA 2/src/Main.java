import javax.swing.SwingUtilities;
import java.sql.Connection;

/**
 * Clase Main
 * Inicia la aplicación del sistema de gestión veterinaria y verifica la conexión con la base de datos.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Intentamos conectarnos a la base de datos antes de iniciar la aplicación
                Connection conexion = ConexionBD.conectar();
                if (conexion == null) {
                    System.err.println("❌ Error: No se pudo conectar con la base de datos.");
                    return; // No iniciar la aplicación si hay problemas de conexión
                }

                System.out.println("✅ Conexión a la base de datos exitosa. Iniciando la aplicación...");
                ConexionBD.cerrarConexion(conexion); // Cerramos la conexión inicial

                // Creación de la ventana principal de la aplicación
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true);

            } catch (Exception e) {
                System.err.println("⚠️ Error inesperado al iniciar la aplicación.");
                e.printStackTrace();
            }
        });
    }
}
