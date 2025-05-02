import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase MascotaDAO
 * Gestiona la interacción con la base de datos de mascotas.
 */
public class MascotaDAO {
    
    /**
     * Método para registrar una nueva mascota en la base de datos.
     */
    public static boolean registrarMascota(String nombre, String raza, int edad) {
        try (Connection conexion = ConexionBD.conectar()) {
            String sql = "INSERT INTO Mascotas (nombre, raza, edad) VALUES (?, ?, ?)";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, raza);
            stmt.setInt(3, edad);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar la mascota: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para consultar todas las mascotas registradas.
     */
    public static void consultarMascotas() {
        try (Connection conexion = ConexionBD.conectar()) {
            String sql = "SELECT * FROM Mascotas";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Nombre: " + rs.getString("nombre") + " | Raza: " + rs.getString("raza") + " | Edad: " + rs.getInt("edad"));
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar las mascotas: " + e.getMessage());
        }
    }
}