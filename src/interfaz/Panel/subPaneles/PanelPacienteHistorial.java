package interfaz.Panel.subPaneles;

import Main.Log;
import backend.Citas.Cita;
import backend.GestionHistorial.Consulta;
import backend.Medicacion.Medicamento;
import backend.Medicacion.Vacuna;
import backend.Pruebas.*;
import backend.Usuarios.Paciente;
import Controlador.*;
import interfaz.Dialog.DialogAnularCita;
import interfaz.Dialog.DialogDetalles;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class PanelPacienteHistorial extends JPanel implements OyenteCitas {

    private final Paciente paciente;
    private final Controlador controlador;

    private JTabbedPane tabs;

    private final int IDXCITAS = 0;

    // ===== CITAS =====
    private JTable tablaCitas;
    private DefaultTableModel modeloCitas;
    private ArrayList<Cita> citasVisibles = new ArrayList<>();

    // === CONSULTAS ===
    private JTable tablaConsultas;

    // === P. LAb ====
    private JTable tablaPLab;

    public PanelPacienteHistorial(Paciente paciente, Controlador controlador) {
        controlador.addOyenteCitas(this);
        this.controlador = controlador;
        this.paciente = paciente;
        inicializar();
    }

    private void inicializar() {
        setLayout(new BorderLayout());

        tabs = new JTabbedPane();

        tabs.add("Citas", panelCitas());
        tabs.add("Consultas", panelConsultas());
        tabs.add("Pruebas", panelPruebas());
        tabs.add("Medicamentos", panelMedicacion());

        add(tabs, BorderLayout.CENTER);
    }

    // =================== CITAS ===================
    private JScrollPane panelCitas() {
        String[] cols = {"Fecha", "Hora", "Médico", "Estado"};

        modeloCitas = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCitas = new JTable(modeloCitas);
        tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cargarCitas();

        JPopupMenu menu = crearMenuCitas();

        tablaCitas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostrarMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mostrarMenu(e);
            }

            private void mostrarMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int fila = tablaCitas.rowAtPoint(e.getPoint());
                    if (fila >= 0) {
                        tablaCitas.setRowSelectionInterval(fila, fila);
                        menu.show(tablaCitas, e.getX(), e.getY());
                    }
                }
            }
        });

        return new JScrollPane(tablaCitas);
    }

    private void cargarCitas() {
        modeloCitas.setRowCount(0);
        citasVisibles.clear();

        for (Cita c : controlador.getCitas().getCitas()) {
            if (c.getPaciente().equals(paciente)) {
                citasVisibles.add(c);
                modeloCitas.addRow(new Object[]{
                        c.getFechaHora().toLocalDate(),
                        c.getFechaHora().toLocalTime(),
                        c.getMedico().getCIPA() + " (" + c.getMedico().getEspecialidad() + ")",
                        c.isAnulada() ? "Anulada" : "Vigente"
                });
            }
        }
    }

    private JPopupMenu crearMenuConsultas() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem itemDetalles = new JMenuItem("Detalles");
        itemDetalles.addActionListener(e -> {
            int fila = tablaConsultas.getSelectedRow();
            if (fila >= 0) {
                verDetalles(
                        paciente.getHistorial().getConsultas().get(fila).toString(),
                        "Detalles de la consulta");
            }
        });

        menu.add(itemDetalles);
        return menu;
    }

    private JPopupMenu crearMenuPLab() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem itemDetalles = new JMenuItem("Detalles");
        itemDetalles.addActionListener(e -> {
            int fila = tablaPLab.getSelectedRow();
            if (fila >= 0) {
                verDetalles(
                        paciente.getHistorial().getLaboratorio().get(fila).toString(),
                        "Detalles de la prueba");
            }
        });

        menu.add(itemDetalles);
        return menu;
    }

    private JPopupMenu crearMenuCitas() {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem itemDetalles = new JMenuItem("Detalles");
        JMenuItem itemAnular = new JMenuItem("Anular");
        itemAnular.setForeground(Color.RED);

        itemDetalles.addActionListener(e -> {
            int fila = tablaCitas.getSelectedRow();
            if (fila >= 0) {
                verDetalles(citasVisibles.get(fila).toString(), "Detalles de la cita.");
            }
        });

        itemAnular.addActionListener(e -> {
            int fila = tablaCitas.getSelectedRow();
            if (fila >= 0) {
                anularCita(citasVisibles.get(fila));
            }
        });

        menu.add(itemDetalles);
        menu.add(itemAnular);
        return menu;
    }

    private void verDetalles(String txt, String titulo) {
        // Abrir ventana de detalles
        DialogDetalles dc = new DialogDetalles(
                SwingUtilities.getWindowAncestor(this),
                txt,
                titulo
        );
        dc.setVisible(true);
    }

    private void anularCita(Cita c) {
        DialogAnularCita da = new DialogAnularCita(
                SwingUtilities.getWindowAncestor(this),
                controlador,
                TipoUsuario.PACIENTE,
                c
        );

        da.setVisible(true);
    }

    // =================== CONSULTAS ===================
    private JScrollPane panelConsultas() {
        String[] cols = {"Fecha", "Motivo", "Tipo", "Centro", "Médico"};

        DefaultTableModel m = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        for (Consulta c : paciente.getHistorial().getConsultas()) {
            m.addRow(new Object[]{
                    c.getFecha(),
                    c.getMotivo(),
                    c.getTipoConsulta(),
                    c.getCentro(),
                    c.getMedico().getCIPA()
            });
        }

        tablaConsultas = new JTable(m);

        JPopupMenu menu = crearMenuConsultas();

        tablaConsultas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostrarMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mostrarMenu(e);
            }

            private void mostrarMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int fila = tablaConsultas.rowAtPoint(e.getPoint());
                    if (fila >= 0) {
                        tablaConsultas.setRowSelectionInterval(fila, fila);
                        menu.show(tablaConsultas, e.getX(), e.getY());
                    }
                }
            }
        });


        return new JScrollPane(tablaConsultas);
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

        DefaultTableModel m = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        for (Imagen img : paciente.getHistorial().getImagen()) {
            m.addRow(new Object[]{
                    img.getFecha(),
                    img.getCentro(),
                    img.getRutaInforme(),
                    img.getRutaImagen()
            });
        }

        JTable tabla = new JTable(m);

        // Renderer para columnas de rutas
        tabla.getColumnModel().getColumn(2).setCellRenderer(new linkArchivos());
        tabla.getColumnModel().getColumn(3).setCellRenderer(new linkArchivos());

        // Click para abrir archivo
        activarClickAbrirArchivo(tabla, 2, 3);

        return new JScrollPane(tabla);
    }

    private JScrollPane panelPruebasLaboratorio() {
        String[] cols = {"Fecha", "Centro", "Ruta informe", "Descripcion"};

        DefaultTableModel m = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        for (Laboratorio lab : paciente.getHistorial().getLaboratorio()) {
            m.addRow(new Object[]{
                    lab.getFecha(),
                    lab.getCentro(),
                    lab.getRutaInforme(),
                    lab.getInforme()
            });
        }

        tablaPLab = new JTable(m);

        tablaPLab.getColumnModel().getColumn(2).setCellRenderer(new linkArchivos());
        activarClickAbrirArchivo(tablaPLab, 2);

        JPopupMenu menu = crearMenuPLab();

        tablaPLab.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostrarMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mostrarMenu(e);
            }

            private void mostrarMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int fila = tablaPLab.rowAtPoint(e.getPoint());
                    if (fila >= 0) {
                        tablaPLab.setRowSelectionInterval(fila, fila);
                        menu.show(tablaPLab, e.getX(), e.getY());
                    }
                }
            }
        });

        return new JScrollPane(tablaPLab);
    }


    // =================== MEDICACIÓN ===================
    private JScrollPane panelMedicacion() {
        String[] cols = {"Tipo", "Nombre", "Momentaniedad","Dosis", "Fecha inicio", "Fecha fin", "Fecha Siguiente"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);

        for (Medicamento med : paciente.getHistorial().getMedicamentos()) {
            m.addRow(new Object[]{
                    (med instanceof Vacuna)? "Vacuna" : "Medicamento",
                    med.getNombre(),
                    med.getTipoPreescripcion(),
                    med.getDosis() + " mg / " + med.getFrecuencia() + " veces día",
                    med.getFechaInicio(),
                    med.getFechaFin(),
                    (med instanceof Vacuna)? ((Vacuna)med).getFechaSgteDosis() : "--"
            });
        }

        return new JScrollPane(new JTable(m));
    }

    private void activarClickAbrirArchivo(JTable tabla, int... columnas) {
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {
                    int fila = tabla.rowAtPoint(e.getPoint());
                    int col = tabla.columnAtPoint(e.getPoint());

                    for (int c : columnas) {
                        if (col == c) {
                            Object val = tabla.getValueAt(fila, col);
                            if (val instanceof String ruta) {
                                File f = new File(ruta);
                                if (f.exists()) {
                                    try {
                                        Desktop.getDesktop().open(f);
                                    } catch (Exception ex) {
                                        JOptionPane.showMessageDialog(
                                                tabla,
                                                "No se pudo abrir el archivo",
                                                "Error",
                                                JOptionPane.ERROR_MESSAGE
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    // =================== OYENTE ===================
    @Override
    public void onCitaUpdate(Cita c) {
        Log.INFO("Cita Update -> " + c);
        SwingUtilities.invokeLater(this::cargarCitas);
    }
}
