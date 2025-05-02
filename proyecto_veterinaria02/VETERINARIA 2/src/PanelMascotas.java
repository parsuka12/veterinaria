import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Panel para gestionar mascotas.
 */
public class PanelMascotas extends JPanel {
    private JTextField nombreField, especieField, razaField, edadField, pesoField, propietarioIdField;
    private JButton registrarButton, consultarButton;

    /**
     * Constructor que configura la UI del panel de mascotas.
     */
    public PanelMascotas() {
        setLayout(new GridLayout(8, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Componentes para ingresar datos
        add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        add(nombreField);

        add(new JLabel("Especie:"));
        especieField = new JTextField();
        add(especieField);

        add(new JLabel("Raza:"));
        razaField = new JTextField();
        add(razaField);

        add(new JLabel("Edad (años):"));
        edadField = new JTextField();
        add(edadField);

        add(new JLabel("Peso (kg):"));
        pesoField = new JTextField();
        add(pesoField);

        add(new JLabel("ID del Propietario:"));
        propietarioIdField = new JTextField();
        add(propietarioIdField);

        // Botones
        registrarButton = new JButton("Registrar Mascota");
        registrarButton.addActionListener(_ -> registrarMascota());
        add(registrarButton);

        consultarButton = new JButton("Consultar Mascotas");
        consultarButton.addActionListener(_ -> consultarMascotas());
        add(consultarButton);
    }

    /**
     * Método para registrar una nueva mascota en la base de datos.
     */
    private void registrarMascota() {
        try {
            String nombre = nombreField.getText();
            String especie = especieField.getText();
            String raza = razaField.getText();
            int edad = Integer.parseInt(edadField.getText());
            double peso = Double.parseDouble(pesoField.getText());
            int propietarioId = Integer.parseInt(propietarioIdField.getText());

            // Validaciones
            if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos de texto deben estar completos.");
            }
            if (edad < 0) {
                throw new IllegalArgumentException("La edad debe ser un número positivo.");
            }
            if (peso <= 0) {
                throw new IllegalArgumentException("El peso debe ser mayor que cero.");
            }

            Connection conexion = ConexionBD.conectar();
            if (conexion != null) {
                // Verificar si el propietario existe
                String checkPropietario = "SELECT id_Propietario FROM Propietarios WHERE id_Propietario = ?";
                PreparedStatement checkStmt = conexion.prepareStatement(checkPropietario);
                checkStmt.setInt(1, propietarioId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "El propietario con ID " + propietarioId + " no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Insertar la mascota
                String sql = "INSERT INTO Mascotas (nombre_Mascota, especie, raza, edad, peso, fk_Propietario) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setString(1, nombre);
                stmt.setString(2, especie);
                stmt.setString(3, raza);
                stmt.setInt(4, edad);
                stmt.setDouble(5, peso);
                stmt.setInt(6, propietarioId);
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Mascota registrada con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                conexion.close();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos para edad, peso y ID del propietario.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Datos Inválidos", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar la mascota en la base de datos.", "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para consultar todas las mascotas registradas.
     */
    private void consultarMascotas() {
        Connection conexion = ConexionBD.conectar();
        if (conexion != null) {
            try {
                String sql = "SELECT m.*, p.nombre_Propietario FROM Mascotas m LEFT JOIN Propietarios p ON m.fk_Propietario = p.id_Propietario";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                StringBuilder resultado = new StringBuilder("Mascotas Registradas:\n");
                while (rs.next()) {
                    resultado.append("ID: ").append(rs.getInt("id_Mascota"))
                             .append(" | Nombre: ").append(rs.getString("nombre_Mascota"))
                             .append(" | Especie: ").append(rs.getString("especie"))
                             .append(" | Raza: ").append(rs.getString("raza"))
                             .append(" | Edad: ").append(rs.getInt("edad"))
                             .append(" | Peso: ").append(rs.getDouble("peso"))
                             .append(" | Propietario: ").append(rs.getString("nombre_Propietario"))
                             .append("\n");
                }
                
                JOptionPane.showMessageDialog(this, resultado.toString(), "Consulta de Mascotas", JOptionPane.INFORMATION_MESSAGE);
                conexion.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al consultar las mascotas.", "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarCampos() {
        nombreField.setText("");
        especieField.setText("");
        razaField.setText("");
        edadField.setText("");
        pesoField.setText("");
        propietarioIdField.setText("");
    }
}
