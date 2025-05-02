import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel para gestionar el historial médico de las mascotas.
 */
public class PanelHistorialMedico extends JPanel {
    private JTextField fechaField;
    private JTextArea diagnosticoArea, tratamientoArea;
    private JComboBox<Integer> mascotaBox, veterinarioBox;
    private JButton registrarButton, consultarButton;

    /**
     * Constructor que configura la UI del panel de historial médico.
     */
    public PanelHistorialMedico() {
        setLayout(new GridLayout(7, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Componentes para ingresar datos
        add(new JLabel("Fecha (YYYY-MM-DD):"));
        fechaField = new JTextField();
        add(fechaField);

        add(new JLabel("ID Mascota:"));
        mascotaBox = new JComboBox<>(obtenerMascotas());
        add(mascotaBox);

        add(new JLabel("ID Veterinario:"));
        veterinarioBox = new JComboBox<>(obtenerVeterinarios());
        add(veterinarioBox);

        add(new JLabel("Diagnóstico:"));
        diagnosticoArea = new JTextArea();
        JScrollPane diagnosticoScroll = new JScrollPane(diagnosticoArea);
        add(diagnosticoScroll);

        add(new JLabel("Tratamiento:"));
        tratamientoArea = new JTextArea();
        JScrollPane tratamientoScroll = new JScrollPane(tratamientoArea);
        add(tratamientoScroll);

        // Botones
        registrarButton = new JButton("Registrar Historial");
        registrarButton.addActionListener(_ -> registrarHistorialMedico());
        add(registrarButton);

        consultarButton = new JButton("Consultar Historial");
        consultarButton.addActionListener(_ -> consultarHistorialMedico());
        add(consultarButton);
    }

    /**
     * Método para obtener la lista de IDs de mascotas desde la base de datos.
     * @return Arreglo de IDs de mascotas
     */
    private Integer[] obtenerMascotas() {
        List<Integer> ids = new ArrayList<>();
        Connection conexion = ConexionBD.conectar();
        if (conexion != null) {
            try {
                String sql = "SELECT id_Mascota FROM Mascotas";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    ids.add(rs.getInt("id_Mascota"));
                }
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al obtener las mascotas: " + e.getMessage());
            }
        }
        
        // Si no hay mascotas registradas, devolvemos un valor por defecto
        if (ids.isEmpty()) {
            return new Integer[]{1, 2, 3, 4, 5}; // Valores por defecto
        }
        
        return ids.toArray(new Integer[0]);
    }

    /**
     * Método para obtener la lista de IDs de veterinarios desde la base de datos.
     * @return Arreglo de IDs de veterinarios
     */
    private Integer[] obtenerVeterinarios() {
        List<Integer> ids = new ArrayList<>();
        Connection conexion = ConexionBD.conectar();
        if (conexion != null) {
            try {
                String sql = "SELECT id_Veterinario FROM Veterinarios";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    ids.add(rs.getInt("id_Veterinario"));
                }
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al obtener los veterinarios: " + e.getMessage());
            }
        }
        
        // Si no hay veterinarios registrados, devolvemos un valor por defecto
        if (ids.isEmpty()) {
            return new Integer[]{1, 2, 3}; // Valores por defecto
        }
        
        return ids.toArray(new Integer[0]);
    }

    /**
     * Método para registrar un nuevo historial médico en la base de datos.
     */
    private void registrarHistorialMedico() {
        try {
            String fechaConsulta = fechaField.getText();
            String diagnostico = diagnosticoArea.getText();
            String tratamiento = tratamientoArea.getText();
            int idMascota = (int) mascotaBox.getSelectedItem();
            int idVeterinario = (int) veterinarioBox.getSelectedItem();

            if (fechaConsulta.isEmpty() || diagnostico.isEmpty() || tratamiento.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos deben estar completos.");
            }

            Connection conexion = ConexionBD.conectar();
            if (conexion != null) {
                String sql = "INSERT INTO Historial_Medico (Fecha_Consulta, Diagnostico, Tratamiento, fk_Mascota, fk_Veterinario) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setString(1, fechaConsulta);
                stmt.setString(2, diagnostico);
                stmt.setString(3, tratamiento);
                stmt.setInt(4, idMascota);
                stmt.setInt(5, idVeterinario);
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Historial médico registrado con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                conexion.close();
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Datos Inválidos", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el historial médico.", "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para consultar el historial médico de una mascota específica.
     */
    private void consultarHistorialMedico() {
        int idMascota = (int) mascotaBox.getSelectedItem();
        Connection conexion = ConexionBD.conectar();
        if (conexion != null) {
            try {
                String sql = "SELECT * FROM Historial_Medico WHERE fk_Mascota = ?";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setInt(1, idMascota);
                ResultSet rs = stmt.executeQuery();

                StringBuilder reporte = new StringBuilder("Reporte de Historial Médico:\n");
                while (rs.next()) {
                    reporte.append("Fecha Consulta: ").append(rs.getString("Fecha_Consulta"))
                           .append("\nDiagnóstico: ").append(rs.getString("Diagnostico"))
                           .append("\nTratamiento: ").append(rs.getString("Tratamiento"))
                           .append("\n-------------------------------------------------\n");
                }
                
                if (reporte.toString().equals("Reporte de Historial Médico:\n")) {
                    JOptionPane.showMessageDialog(this, "No hay registros médicos para esta mascota.", "Reporte Vacío", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, reporte.toString(), "Reporte de Mascota", JOptionPane.INFORMATION_MESSAGE);
                }
                conexion.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al generar el reporte.", "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarCampos() {
        fechaField.setText("");
        diagnosticoArea.setText("");
        tratamientoArea.setText("");
    }
}
