import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase VeterinarioDAO
 * Gestiona la interacción con la base de datos de veterinarios.
 */
public class VeterinarioDAO {

    /**
     * Método para registrar un nuevo veterinario.
     */
    public static boolean registrarVeterinario(String nombre, String contacto) {
        try (Connection conexion = ConexionBD.conectar()) {
            String sql = "INSERT INTO Veterinario (nombre_Vet, Contacto) VALUES (?, ?)";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, contacto);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar el veterinario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para consultar veterinarios.
     */
    public static void consultarVeterinarios() {
        try (Connection conexion = ConexionBD.conectar()) {
            String sql = "SELECT * FROM Veterinario";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id_Veterinario") + " | Nombre: " + rs.getString("nombre_Vet") + " | Contacto: " + rs.getString("Contacto"));
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar los veterinarios: " + e.getMessage());
        }
    }
}