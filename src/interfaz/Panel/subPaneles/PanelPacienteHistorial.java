package interfaz.Panel.subPaneles;

import Controlador.Controlador;
import backend.Citas.Cita;
import backend.GestionHistorial.Consulta;
import backend.Medicacion.Medicamento;
import backend.Pruebas.Imagen;
import backend.Pruebas.Laboratorio;
import backend.Usuarios.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelPacienteHistorial extends JPanel {

    private final Paciente paciente;


    public PanelPacienteHistorial(Paciente paciente) {
        this.paciente = paciente;
        inicializar();
    }

    private void inicializar() {
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Citas", panelCitas());
        tabs.add("Consultas", panelConsultas());
        tabs.add("Pruebas", panelPruebas());
        tabs.add("Medicamentos", panelMedicacion());

        add(tabs, BorderLayout.CENTER);
    }

    // =================== CITAS ===================
    private JScrollPane panelCitas() {
        String[] cols = {"Fecha", "Hora", "Médico", "Estado"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);

        for (Cita c : paciente.getArrayCitas()) {
            m.addRow(new Object[]{
                    c.getFechaHora().toLocalDate(),
                    c.getFechaHora().toLocalTime(),
                    c.getMedico().getCIPA(),
                    c.isAnulada() ? "Anulada" : "Vigente"
            });
        }

        return new JScrollPane(new JTable(m));
    }

    // =================== CONSULTAS ===================
    private JScrollPane panelConsultas() {
        String[] cols = {"Fecha", "Motivo", "Tipo", "Centro", "Médico"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);

        for (Consulta c : paciente.getHistorial().getConsultas()) {
            m.addRow(new Object[]{
                    c.getFecha(),
                    c.getMotivo(),
                    c.getTipoConsulta(),
                    c.getCentro(),
                    c.getMedico().getCIPA()
            });
        }

        return new JScrollPane(new JTable(m));
    }

    // =================== PRUEBAS ===================
    private JComponent panelPruebas() {
        JTabbedPane subTabs = new JTabbedPane();

        subTabs.add("Imagen", panelPruebasImagen());
        subTabs.add("Laboratorio", panelPruebasLaboratorio());

        return subTabs;
    }

    private JScrollPane panelPruebasImagen() {
        String[] cols = {"Fecha", "Centro", "Ruta informe", "Ruta imagen"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);

        for (Imagen img : paciente.getHistorial().getImagen()) {
            m.addRow(new Object[]{
                    img.getFecha(),
                    img.getCentro(),
                    img.getRutaInforme(),
                    img.getRutaImagen()
            });
        }

        return new JScrollPane(new JTable(m));
    }

    private JScrollPane panelPruebasLaboratorio() {
        String[] cols = {"Fecha", "Centro", "Ruta informe", "Informe"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);

        for (Laboratorio lab : paciente.getHistorial().getLaboratorio()) {
            m.addRow(new Object[]{
                    lab.getFecha(),
                    lab.getCentro(),
                    lab.getRutaInforme(),
                    lab.toString()
            });
        }

        return new JScrollPane(new JTable(m));
    }

    // =================== MEDICACIÓN ===================
    private JScrollPane panelMedicacion() {
        String[] cols = {"Medicamento"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);

        for (Medicamento med : paciente.getHistorial().getMedicamentosActivos()) {
            m.addRow(new Object[]{med.toString()});
        }

        return new JScrollPane(new JTable(m));
    }
}

