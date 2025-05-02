import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel para gestionar citas veterinarias.
 */
public class PanelCitas extends JPanel {
    private JTextField fechaField, motivoField;
    private JComboBox<Integer> mascotaBox, veterinarioBox;
    private JButton registrarButton, consultarButton;

    /**
     * Constructor que configura la UI del panel de citas.
     */
    public PanelCitas() {
        setLayout(new GridLayout(6, 2, 10, 10));
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

        add(new JLabel("Motivo:"));
        motivoField = new JTextField();
        add(motivoField);

        // Botones
        registrarButton = new JButton("Registrar Cita");
        registrarButton.addActionListener(_ -> registrarCita());
        add(registrarButton);

        consultarButton = new JButton("Consultar Citas");
        consultarButton.addActionListener(_ -> consultarCitas());
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
     * Método para registrar una nueva cita en la base de datos.
     */
    private void registrarCita() {
        try {
            String fecha = fechaField.getText();
            String motivo = motivoField.getText();
            int idMascota = (int) mascotaBox.getSelectedItem();
            int idVeterinario = (int) veterinarioBox.getSelectedItem();

            if (fecha.isEmpty() || motivo.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos deben estar completos.");
            }

            boolean exito = CitasDAO.registrarCita(fecha, idMascota, idVeterinario, motivo);
            
            if (exito) {
                JOptionPane.showMessageDialog(this, "Cita registrada con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la cita.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Datos Inválidos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para consultar todas las citas programadas en la base de datos.
     */
    private void consultarCitas() {
        Connection conexion = ConexionBD.conectar();
        if (conexion != null) {
            try {
                String sql = "SELECT * FROM Citas";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                StringBuilder resultado = new StringBuilder("Citas Programadas:\n");
                while (rs.next()) {
                    resultado.append("ID Cita: ").append(rs.getInt("id_cita"))
                             .append(" | Fecha: ").append(rs.getString("fecha"))
                             .append(" | Mascota ID: ").append(rs.getInt("fk_Mascota"))
                             .append(" | Veterinario ID: ").append(rs.getInt("fk_Veterinario"))
                             .append(" | Motivo: ").append(rs.getString("motivo"))
                             .append("\n");
                }
                JOptionPane.showMessageDialog(this, resultado.toString(), "Consulta de Citas", JOptionPane.INFORMATION_MESSAGE);
                conexion.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al consultar las citas.", "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarCampos() {
        fechaField.setText("");
        motivoField.setText("");
    }
}
