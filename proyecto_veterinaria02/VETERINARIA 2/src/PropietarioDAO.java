import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase PropietarioDAO
 * Gestiona la interacción con la base de datos de propietarios.
 */
public class PropietarioDAO {

    /**
     * Método para registrar un nuevo propietario.
     */
    public static boolean registrarPropietario(String nombre, String contacto, String direccion) {
        try (Connection conexion = ConexionBD.conectar()) {
            String sql = "INSERT INTO Propietario (nombre_Propietario, Contacto, Direccion) VALUES (?, ?, ?)";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, contacto);
            stmt.setString(3, direccion);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar el propietario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para consultar propietarios.
     */
    public static void consultarPropietarios() {
        try (Connection conexion = ConexionBD.conectar()) {
            String sql = "SELECT * FROM Propietario";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id_Propietario") + " | Nombre: " + rs.getString("nombre_Propietario") + " | Contacto: " + rs.getString("Contacto") + " | Dirección: " + rs.getString("Direccion"));
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar los propietarios: " + e.getMessage());
        }
    }
}