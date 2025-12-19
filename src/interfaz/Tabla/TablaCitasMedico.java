package interfaz.Tabla;

import Controlador.*;
import Main.Log;
import backend.Citas.Cita;
import interfaz.Dialog.DialogAnularCita;
import interfaz.Dialog.DialogDetalles;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class TablaCitasMedico extends JTable implements OyenteCitas {

    private Controlador controlador;
    private JPopupMenu popup;
    private DefaultTableModel modelo;

    private final String[] headerNames = {"Fecha", "Nombre Pac.", "Estado"};

    public TablaCitasMedico(Controlador controlador) {
        super();

        // Creamos y guardamos el modelo
        modelo = new DefaultTableModel(headerNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Asignamos el modelo a la JTable
        setModel(modelo);

        this.controlador = controlador;
        this.controlador.addOyenteCitas(this);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPopupMenu popup = crearMenuCitas();

        TablaCitasMedico t = this;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { mostrarMenu(e); }
            @Override
            public void mouseReleased(MouseEvent e) { mostrarMenu(e); }

            private void mostrarMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int fila = t.rowAtPoint(e.getPoint());
                    if (fila >= 0) {
                        t.setRowSelectionInterval(fila, fila);
                        popup.show(t, e.getX(), e.getY());
                    }
                }
            }
        });

        mostrarCitas();
    }

    private void mostrarCitas() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Limpiamos el modelo por si ya había datos
        modelo.setRowCount(0);

        // Obtenemos las citas del controlador
        List<Cita> citas = controlador.getCitas().obtenerCitasMedico(controlador.getLoginMedico());

        // Añadimos cada cita como fila
        for (Cita c : citas) {
            modelo.addRow(new Object[]{
                    dtf.format(c.getFechaHora()),
                    c.getPaciente().getNombreCompleto() + " (" + c.getPaciente().getCIPA() + ")",
                    (c.isAnulada()) ? "Anulada" : "Vigente",
            });
        }
    }

    private Cita getCitaSeleccionada() {
        List<Cita> citasMed = controlador.getCitas().obtenerCitasMedico(controlador.getLoginMedico());


        int i = 0;
        i = this.getSelectedRow();
        if (i >= 0) {
            return citasMed.get(i);
        }

        return null;
    }


    private JPopupMenu crearMenuCitas() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem fin = new JMenuItem("Finalizar y añadir al historial");
        JMenuItem det = new JMenuItem("Detalles");
        JMenuItem anular = new JMenuItem("Anular");
        anular.setForeground(Color.RED);

        fin.addActionListener(e -> {
            finalizar(getCitaSeleccionada());
        });

        det.addActionListener(e -> {
            verDetalles(getCitaSeleccionada().toString());
        });

        anular.addActionListener(e -> {
            anular(getCitaSeleccionada());
        });

        popupMenu.add(fin);
        popupMenu.addSeparator();
        popupMenu.add(det);
        popupMenu.add(anular);

        return(popupMenu);
    }

    private void finalizar(Cita c) {
        Log.INFO("fin: " + c);
    }

    private void anular(Cita c) {
        DialogAnularCita da = new DialogAnularCita(
                SwingUtilities.getWindowAncestor(this),
                controlador,
                TipoUsuario.MEDICO,
                c
        );
        da.setVisible(true);
    }

    private void verDetalles(String txt) {
        DialogDetalles dd = new DialogDetalles(
                SwingUtilities.getWindowAncestor(this),
                txt,
                "Detalles de la cita."
        );
        dd.setVisible(true);
    }

    @Override
    public void onCitaUpdate(Cita c) {
        Log.INFO("Cita Update -> " + c);

        SwingUtilities.invokeLater(this::mostrarCitas);
    }
}
