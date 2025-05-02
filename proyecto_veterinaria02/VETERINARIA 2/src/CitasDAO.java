import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase CitasDAO
 * Gestiona la interacción con la base de datos de citas veterinarias.
 */
public class CitasDAO {

    /**
     * Método para registrar una nueva cita.
     * @param fecha Fecha de la cita
     * @param idMascota ID de la mascota
     * @param idVeterinario ID del veterinario
     * @param motivo Motivo de la cita
     * @return true si se registró correctamente, false en caso contrario
     */
    public static boolean registrarCita(String fecha, int idMascota, int idVeterinario, String motivo) {
        try (Connection conexion = ConexionBD.conectar()) {
            String sql = "INSERT INTO Citas (fecha, fk_Mascota, fk_Veterinario, motivo) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, fecha);
            stmt.setInt(2, idMascota);
            stmt.setInt(3, idVeterinario);
            stmt.setString(4, motivo);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar la cita: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para consultar todas las citas programadas.
     */
    public static void consultarCitas() {
        try (Connection conexion = ConexionBD.conectar()) {
            String sql = "SELECT * FROM Citas";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID Cita: " + rs.getInt("id_cita") + 
                                   " | Fecha: " + rs.getString("fecha") + 
                                   " | Mascota ID: " + rs.getInt("fk_Mascota") + 
                                   " | Veterinario ID: " + rs.getInt("fk_Veterinario") + 
                                   " | Motivo: " + rs.getString("motivo"));
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar las citas: " + e.getMessage());
        }
    }
}
