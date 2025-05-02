import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase PanelNotificaciones
 * Representa la interfaz para gestionar notificaciones y recordatorios en la base de datos.
 */
public class PanelNotificaciones extends JPanel {
    private JTextField mensajeField;
    private JComboBox<Integer> propietarioBox, veterinarioBox;
    private JButton enviarNotificacionButton, consultarNotificacionesButton;

    /**
     * Constructor de la interfaz de notificaciones.
     * Se crean los campos de texto y los botones.
     */
    public PanelNotificaciones() {
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Mensaje de Notificación:"));
        mensajeField = new JTextField();
        add(mensajeField);

        add(new JLabel("ID del Propietario:"));
        propietarioBox = new JComboBox<>(obtenerPropietarios());
        add(propietarioBox);

        add(new JLabel("ID del Veterinario:"));
        veterinarioBox = new JComboBox<>(obtenerVeterinarios());
        add(veterinarioBox);

        // Botón para enviar una nueva notificación
        enviarNotificacionButton = new JButton("Enviar Notificación");
        enviarNotificacionButton.addActionListener(_ -> enviarNotificacion());
        add(enviarNotificacionButton);

        // Botón para consultar notificaciones enviadas
        consultarNotificacionesButton = new JButton("Consultar Notificaciones");
        consultarNotificacionesButton.addActionListener(_ -> consultarNotificaciones());
        add(consultarNotificacionesButton);
    }

    /**
     * Método para obtener la lista de IDs de propietarios.
     * Idealmente, se consultaría la base de datos.
     */
    private Integer[] obtenerPropietarios() {
        return new Integer[]{1, 2, 3, 4, 5}; // Se debería consultar la base de datos
    }

    /**
     * Método para obtener la lista de IDs de veterinarios.
     * Idealmente, se consultaría la base de datos.
     */
    private Integer[] obtenerVeterinarios() {
        return new Integer[]{1, 2, 3}; // Se debería consultar la base de datos
    }

    /**
     * Método para enviar una nueva notificación a la base de datos.
     * Captura posibles errores de entrada y conexión.
     */
    private void enviarNotificacion() {
        try {
            String mensaje = mensajeField.getText();
            int idPropietario = (int) propietarioBox.getSelectedItem();
            int idVeterinario = (int) veterinarioBox.getSelectedItem();

            if (mensaje.isEmpty()) {
                throw new IllegalArgumentException("El mensaje no puede estar vacío.");
            }

            Connection conexion = ConexionBD.conectar();
            if (conexion != null) {
                String sql = "INSERT INTO Notificaciones (mensaje, fk_Propietario, fk_Veterinario) VALUES (?, ?, ?)";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setString(1, mensaje);
                stmt.setInt(2, idPropietario);
                stmt.setInt(3, idVeterinario);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Notificación enviada con éxito!");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar la notificación en la base de datos.", "Error SQL", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para consultar todas las notificaciones enviadas en la base de datos.
     * Muestra los resultados en una ventana emergente.
     */
    private void consultarNotificaciones() {
        Connection conexion = ConexionBD.conectar();
        if (conexion != null) {
            try {
                String sql = "SELECT * FROM Notificaciones";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                StringBuilder resultado = new StringBuilder("Notificaciones Enviadas:\n");
                while (rs.next()) {
                    resultado.append("ID Notificación: ").append(rs.getInt("id_Notificacion"))
                             .append(" | Mensaje: ").append(rs.getString("mensaje"))
                             .append(" | Propietario ID: ").append(rs.getInt("fk_Propietario"))
                             .append(" | Veterinario ID: ").append(rs.getInt("fk_Veterinario"))
                             .append("\n-----------------------------------------\n");
                }
                JOptionPane.showMessageDialog(this, resultado.toString(), "Consulta de Notificaciones", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al consultar las notificaciones.", "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}