package interfaz.Panel.subPaneles;

import Controlador.Controlador;
import Main.Log;
import backend.Citas.Cita;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;

public class PanelListaCitasMedico extends JTable {

    private Controlador controlador;
    private DefaultTableModel model;

    private final String[] headerNames = {"Fecha", "Nombre Pac.", "Estado"};

    private JPopupMenu popup;

    public PanelListaCitasMedico(Controlador controlador) {
        super();

        this.model = new DefaultTableModel(headerNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        setModel(model);

        this.controlador = controlador;
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        crearMenuCitas();
        mostrarCitas();
    }

    private Cita getCitaSeleccionada() {
        int fila = this.getSelectedRow();
        if (fila >= 0) {
            return controlador.getCitas().obtenerCitasMedico(
                    controlador.getLoginMedico()).get(fila);
        }
        return null;
    }

    private JPopupMenu inicializarPopup() {
        JPopupMenu ret = new JPopupMenu();

        JMenuItem fin = new JMenuItem("Finalizar y añadir al historial.");
        JMenuItem detalles = new JMenuItem("Detalles");
        JMenuItem anular = new JMenuItem("Anular");
        anular.setForeground(Color.RED);

        fin.addActionListener(e -> {
            finalizarCita(getCitaSeleccionada());
        });

        detalles.addActionListener(e -> {
            verDetalles(getCitaSeleccionada());
        });

        anular.addActionListener(e -> {
            anular(getCitaSeleccionada());
        });

        ret.add(fin);
        ret.addSeparator();
        ret.add(detalles);
        ret.add(anular);
        return ret;
    }

    private void crearMenuCitas() {
        popup = inicializarPopup();
        PanelListaCitasMedico p = this;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostrarMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {mostrarMenu(e);}

            private void mostrarMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int fila = rowAtPoint(e.getPoint());
                    if (fila >= 0) {
                        setRowSelectionInterval(fila, fila);
                        popup.show(p, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void mostrarCitas() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        for (Cita c : controlador.getCitas().obtenerCitasMedico(controlador.getLoginMedico())) {
            model.addRow(new Object[]{
                    dtf.format(c.getFechaHora()),
                    c.getPaciente().getNombreCompleto() + " (" + c.getPaciente().getCIPA() + ")",
                    (c.isAnulada()) ? "Anulada" : "Vigente",
            });
        }
    }

    private void finalizarCita(Cita c) {
        Log.INFO("Marcar completada");
    }

    private void verDetalles(Cita c) {
        if (c == null) return;
        Log.INFO("ver Detalles: " + c.toString());
    }

    private void anular(Cita c) {
        Log.INFO("Anular");
    }


}
