import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Panel para gestionar propietarios.
 */
public class PanelPropietarios extends JPanel {
    private JTextField nombreField, contactoField, direccionField;
    private JButton registrarButton, consultarButton;

    /**
     * Constructor que configura la UI del panel de propietarios.
     */
    public PanelPropietarios() {
        setLayout(new GridLayout(5, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Componentes para ingresar datos
        add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        add(nombreField);

        add(new JLabel("Contacto:"));
        contactoField = new JTextField();
        add(contactoField);

        add(new JLabel("Dirección:"));
        direccionField = new JTextField();
        add(direccionField);

        // Botones
        registrarButton = new JButton("Registrar Propietario");
        registrarButton.addActionListener(_ -> registrarPropietario());
        add(registrarButton);

        consultarButton = new JButton("Consultar Propietarios");
        consultarButton.addActionListener(_ -> consultarPropietarios());
        add(consultarButton);
    }

    /**
     * Método para registrar un nuevo propietario en la base de datos.
     */
    private void registrarPropietario() {
        try {
            String nombre = nombreField.getText();
            String contacto = contactoField.getText();
            String direccion = direccionField.getText();

            // Validaciones
            if (nombre.isEmpty() || contacto.isEmpty() || direccion.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos deben estar completos.");
            }

            Connection conexion = ConexionBD.conectar();
            if (conexion != null) {
                String sql = "INSERT INTO Propietarios (nombre_Propietario, Contacto, Direccion) VALUES (?, ?, ?)";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setString(1, nombre);
                stmt.setString(2, contacto);
                stmt.setString(3, direccion);
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Propietario registrado con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                conexion.close();
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Datos Inválidos", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el propietario en la base de datos.", "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para consultar todos los propietarios registrados.
     */
    private void consultarPropietarios() {
        Connection conexion = ConexionBD.conectar();
        if (conexion != null) {
            try {
                String sql = "SELECT * FROM Propietarios";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                StringBuilder resultado = new StringBuilder("Propietarios Registrados:\n");
                while (rs.next()) {
                    resultado.append("ID: ").append(rs.getInt("id_Propietario"))
                             .append(" | Nombre: ").append(rs.getString("nombre_Propietario"))
                             .append(" | Contacto: ").append(rs.getString("Contacto"))
                             .append(" | Dirección: ").append(rs.getString("Direccion"))
                             .append("\n");
                }
                JOptionPane.showMessageDialog(this, resultado.toString(), "Consulta de Propietarios", JOptionPane.INFORMATION_MESSAGE);
                conexion.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al consultar los propietarios.", "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarCampos() {
        nombreField.setText("");
        contactoField.setText("");
        direccionField.setText("");
    }
}
