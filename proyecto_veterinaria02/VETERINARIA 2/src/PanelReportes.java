import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel para generar reportes.
 */
public class PanelReportes extends JPanel {
    private JComboBox<Integer> mascotaBox;
    private JButton generarReporteButton;

    /**
     * Constructor que configura la UI del panel de reportes.
     */
    public PanelReportes() {
        setLayout(new GridLayout(3, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Componentes para seleccionar mascota
        add(new JLabel("Seleccione Mascota (ID):"));
        mascotaBox = new JComboBox<>(obtenerMascotas());
        add(mascotaBox);

        // Botón para generar reporte
        generarReporteButton = new JButton("Generar Reporte Médico");
        generarReporteButton.addActionListener(_ -> generarReporteMascota());
        add(generarReporteButton);
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
     * Método para generar y mostrar un reporte del historial médico de una mascota.
     */
    private void generarReporteMascota() {
        int idMascota = (int) mascotaBox.getSelectedItem();
        Connection conexion = ConexionBD.conectar();
        if (conexion != null) {
            try {
                // Consulta para obtener información de la mascota
                String sqlMascota = "SELECT * FROM Mascotas WHERE id_Mascota = ?";
                PreparedStatement stmtMascota = conexion.prepareStatement(sqlMascota);
                stmtMascota.setInt(1, idMascota);
                ResultSet rsMascota = stmtMascota.executeQuery();
                
                if (!rsMascota.next()) {
                    JOptionPane.showMessageDialog(this, "No se encontró la mascota con ID " + idMascota, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String nombreMascota = rsMascota.getString("nombre_Mascota");
                String especie = rsMascota.getString("especie");
                String raza = rsMascota.getString("raza");
                
                // Consulta para obtener el historial médico
                String sqlHistorial = "SELECT * FROM Historial_Medico WHERE fk_Mascota = ?";
                PreparedStatement stmtHistorial = conexion.prepareStatement(sqlHistorial);
                stmtHistorial.setInt(1, idMascota);
                ResultSet rs = stmtHistorial.executeQuery();

                StringBuilder reporte = new StringBuilder("Reporte de Historial Médico:\n");
                reporte.append("Mascota: ").append(nombreMascota)
                       .append(" (").append(especie).append(" - ").append(raza).append(")\n")
                       .append("-------------------------------------------------\n");
                
                boolean tieneHistorial = false;
                while (rs.next()) {
                    tieneHistorial = true;
                    reporte.append("Fecha Consulta: ").append(rs.getString("Fecha_Consulta"))
                           .append("\nDiagnóstico: ").append(rs.getString("Diagnostico"))
                           .append("\nTratamiento: ").append(rs.getString("Tratamiento"))
                           .append("\n-------------------------------------------------\n");
                }
                
                if (!tieneHistorial) {
                    reporte.append("No hay registros médicos para esta mascota.");
                }
                
                JOptionPane.showMessageDialog(this, reporte.toString(), "Reporte de Mascota", JOptionPane.INFORMATION_MESSAGE);
                conexion.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al generar el reporte.", "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
