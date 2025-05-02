import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Panel para gestionar veterinarios.
 */
public class PanelVeterinarios extends JPanel {
    private JTextField nombreField, especialidadField, contactoField;
    private JButton registrarButton, consultarButton;

    /**
     * Constructor que configura la UI del panel de veterinarios.
     */
    public PanelVeterinarios() {
        setLayout(new GridLayout(5, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Componentes para ingresar datos
        add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        add(nombreField);

        add(new JLabel("Especialidad:"));
        especialidadField = new JTextField();
        add(especialidadField);

        add(new JLabel("Contacto:"));
        contactoField = new JTextField();
        add(contactoField);

        // Botones
        registrarButton = new JButton("Registrar Veterinario");
        registrarButton.addActionListener(_ -> registrarVeterinario());
        add(registrarButton);

        consultarButton = new JButton("Consultar Veterinarios");
        consultarButton.addActionListener(_ -> consultarVeterinarios());
        add(consultarButton);
    }

    /**
     * Método para registrar un nuevo veterinario en la base de datos.
     */
    private void registrarVeterinario() {
        try {
            String nombre = nombreField.getText();
            String especialidad = especialidadField.getText();
            String contacto = contactoField.getText();

            // Validaciones
            if (nombre.isEmpty() || especialidad.isEmpty() || contacto.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos deben estar completos.");
            }

            Connection conexion = ConexionBD.conectar();
            if (conexion != null) {
                String sql = "INSERT INTO Veterinarios (nombre_Veterinario, especialidad, Contacto) VALUES (?, ?, ?)";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setString(1, nombre);
                stmt.setString(2, especialidad);
                stmt.setString(3, contacto);
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Veterinario registrado con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                conexion.close();
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Datos Inválidos", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el veterinario en la base de datos.", "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para consultar todos los veterinarios registrados.
     */
    private void consultarVeterinarios() {
        Connection conexion = ConexionBD.conectar();
        if (conexion != null) {
            try {
                String sql = "SELECT * FROM Veterinarios";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                StringBuilder resultado = new StringBuilder("Veterinarios Registrados:\n");
                while (rs.next()) {
                    resultado.append("ID: ").append(rs.getInt("id_Veterinario"))
                             .append(" | Nombre: ").append(rs.getString("nombre_Veterinario"))
                             .append(" | Especialidad: ").append(rs.getString("especialidad"))
                             .append(" | Contacto: ").append(rs.getString("Contacto"))
                             .append("\n");
                }
                
                JOptionPane.showMessageDialog(this, resultado.toString(), "Consulta de Veterinarios", JOptionPane.INFORMATION_MESSAGE);
                conexion.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al consultar los veterinarios.", "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarCampos() {
        nombreField.setText("");
        especialidadField.setText("");
        contactoField.setText("");
    }
}
