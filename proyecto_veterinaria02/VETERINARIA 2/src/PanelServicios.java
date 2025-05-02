import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase PanelServicios
 * Representa la interfaz para gestionar servicios veterinarios en la base de datos.
 */
public class PanelServicios extends JPanel {
    private JTextField nombreField, costoField;
    private JTextArea descripcionArea;
    private JButton registrarButton, consultarButton, eliminarButton;

    /**
     * Constructor de la interfaz de servicios.
     * Se crean los campos de texto y los botones.
     */
    public PanelServicios() {
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Nombre del Servicio:"));
        nombreField = new JTextField();
        add(nombreField);

        add(new JLabel("Costo:"));
        costoField = new JTextField();
        add(costoField);

        add(new JLabel("Descripción:"));
        descripcionArea = new JTextArea(3, 20);
        add(new JScrollPane(descripcionArea)); // Permite desplazar el texto

        // Botón para registrar un nuevo servicio
        registrarButton = new JButton("Registrar Servicio");
        registrarButton.addActionListener(_ -> registrarServicio());
        add(registrarButton);

        // Botón para consultar servicios disponibles
        consultarButton = new JButton("Consultar Servicios");
        consultarButton.addActionListener(_ -> consultarServicios());
        add(consultarButton);

        // Botón para eliminar servicios
        eliminarButton = new JButton("Eliminar Servicio");
        eliminarButton.addActionListener(_ -> eliminarServicio());
        add(eliminarButton);
    }

    /**
     * Método para registrar un nuevo servicio en la base de datos.
     * Captura posibles errores de entrada y conexión.
     */
    private void registrarServicio() {
        try {
            String nombre = nombreField.getText();
            double costo = Double.parseDouble(costoField.getText());
            String descripcion = descripcionArea.getText();

            if (nombre.isEmpty() || descripcion.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos deben estar completos.");
            }

            Connection conexion = ConexionBD.conectar();
            if (conexion != null) {
                String sql = "INSERT INTO Servicios (nombre_Servicios, Costo, Descripcion) VALUES (?, ?, ?)";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setString(1, nombre);
                stmt.setDouble(2, costo);
                stmt.setString(3, descripcion);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Servicio registrado con éxito!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: El costo debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el servicio en la base de datos.", "Error SQL", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para consultar todos los servicios registrados en la base de datos.
     * Muestra los resultados en una ventana emergente.
     */
    private void consultarServicios() {
        Connection conexion = ConexionBD.conectar();
        if (conexion != null) {
            try {
                String sql = "SELECT * FROM Servicios";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                StringBuilder resultado = new StringBuilder("Servicios Disponibles:\n");
                while (rs.next()) {
                    resultado.append("ID: ").append(rs.getInt("id_Servicios"))
                             .append(" | Nombre: ").append(rs.getString("nombre_Servicios"))
                             .append(" | Costo: ").append(rs.getDouble("Costo"))
                             .append(" | Descripción: ").append(rs.getString("Descripcion"))
                             .append("\n");
                }
                JOptionPane.showMessageDialog(this, resultado.toString(), "Consulta de Servicios", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al consultar los servicios.", "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método para eliminar un servicio de la base de datos.
     * Pide al usuario el ID del servicio a eliminar.
     */
    private void eliminarServicio() {
        try {
            String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del servicio a eliminar:");
            if (idStr == null || idStr.isEmpty()) return; // Si el usuario cancela, no hace nada

            int id = Integer.parseInt(idStr);

            Connection conexion = ConexionBD.conectar();
            if (conexion != null) {
                String sql = "DELETE FROM Servicios WHERE id_Servicios = ?";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setInt(1, id);
                int filasAfectadas = stmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(this, "Servicio eliminado correctamente!");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el servicio con el ID especificado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: El ID debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar el servicio en la base de datos.", "Error SQL", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
        }
    }
}