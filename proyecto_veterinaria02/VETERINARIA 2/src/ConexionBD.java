import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase ConexionBD
 * Gestiona la conexión con la base de datos SQLite.
 */
public class ConexionBD {

    // Cambiar la ruta al archivo de base de datos para que sea absoluta.
    private static final String DB_PATH = System.getProperty("user.dir") + "/veterinaria.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    /**
     * Método para establecer la conexión con la base de datos.
     * 
     * @return Objeto Connection si la conexión es exitosa, null en caso de error.
     */
    public static Connection conectar() {
        Connection conexion = null;
        try {
            // Cargar el driver de SQLite
            Class.forName("org.sqlite.JDBC");

            // Establecer la conexión
            conexion = DriverManager.getConnection(URL);
            System.out.println("✅ Conexión establecida con la base de datos SQLite en " + DB_PATH);

            // Crear tablas si no existen
            crearTablasSiNoExisten(conexion);

            return conexion;
        } catch (SQLException e) {
            System.err.println("⚠️ Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("⚠️ No se encontró el driver de SQLite: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Crea las tablas en la base de datos si no existen.
     * 
     * @param conexion Conexión activa a la base de datos
     */
    private static void crearTablasSiNoExisten(Connection conexion) {
        try {
            Statement stmt = conexion.createStatement();

            // Tabla Propietarios
            stmt.execute("CREATE TABLE IF NOT EXISTS Propietarios (" +
                    "id_Propietario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre_Propietario TEXT NOT NULL, " +
                    "Contacto TEXT, " +
                    "Direccion TEXT)");

            // Tabla Mascotas
            stmt.execute("CREATE TABLE IF NOT EXISTS Mascotas (" +
                    "id_Mascota INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre_Mascota TEXT NOT NULL, " +
                    "especie TEXT, " +
                    "raza TEXT, " +
                    "edad INTEGER, " +
                    "peso REAL, " +
                    "fk_Propietario INTEGER, " +
                    "FOREIGN KEY(fk_Propietario) REFERENCES Propietarios(id_Propietario))");

            // Tabla Veterinarios
            stmt.execute("CREATE TABLE IF NOT EXISTS Veterinarios (" +
                    "id_Veterinario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre_Veterinario TEXT NOT NULL, " +
                    "especialidad TEXT, " +
                    "Contacto TEXT)");

            // Agregar tabla de Servicios
            stmt.execute("CREATE TABLE IF NOT EXISTS Servicios (" +
                    "id_Servicios INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre_Servicios TEXT NOT NULL, " +
                    "Costo REAL NOT NULL, " +
                    "Descripcion TEXT NOT NULL)");

            // Tabla Historial_Medico
            stmt.execute("CREATE TABLE IF NOT EXISTS Historial_Medico (" +
                    "id_Historial INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Fecha_Consulta TEXT, " +
                    "Diagnostico TEXT, " +
                    "Tratamiento TEXT, " +
                    "fk_Mascota INTEGER, " +
                    "fk_Veterinario INTEGER, " +
                    "FOREIGN KEY(fk_Mascota) REFERENCES Mascotas(id_Mascota), " +
                    "FOREIGN KEY(fk_Veterinario) REFERENCES Veterinarios(id_Veterinario))");

            // Tabla Citas
            stmt.execute("CREATE TABLE IF NOT EXISTS Citas (" +
                    "id_cita INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "fecha TEXT, " +
                    "fk_Mascota INTEGER, " +
                    "fk_Veterinario INTEGER, " +
                    "motivo TEXT, " +
                    "FOREIGN KEY(fk_Mascota) REFERENCES Mascotas(id_Mascota), " +
                    "FOREIGN KEY(fk_Veterinario) REFERENCES Veterinarios(id_Veterinario))");

            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error al crear las tablas: " + e.getMessage());
        }
    }

    /**
     * Cierra la conexión de manera segura.
     * 
     * @param conexion Objeto Connection a cerrar.
     */
    public static void cerrarConexion(Connection conexion) {
        try {
            if (conexion != null) {
                conexion.close();
                System.out.println("🔄 Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection pruebaConexion = conectar();
        if (pruebaConexion != null) {
            cerrarConexion(pruebaConexion);
        }
    }
}
