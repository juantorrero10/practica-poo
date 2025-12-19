package interfaz.Panel.subPaneles;

import Controlador.Controlador;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PanelContenidoMedico extends JPanel {

    private Controlador controlador;

    // Sección Citas
    private JTable tablaCitas;
    private JButton btnHoy;
    private JButton btnSemana;
    private JButton btnTodas;
    private JButton btnCrear;

    // Sección Historial
    private JTextField campoCipa;
    private JButton btnBuscar;
    private JPanel panelResultado;
    private JLabel labelError;

    public PanelContenidoMedico(Controlador c) {

        this.controlador = c;
        inicializarComponentes();
        layoutComponentes();
        estilizarComponentes();
    }

    private void inicializarComponentes() {
        // Citas
        tablaCitas = new PanelListaCitasMedico(controlador);
        btnHoy = new JButton("Hoy");
        btnSemana = new JButton("Semana");
        btnTodas = new JButton("Todas");
        btnCrear = new JButton("Crear");

        // Historial
        campoCipa = new JTextField(10);
        btnBuscar = new JButton("Buscar");
        labelError = new JLabel("Cualquier error se muestra aqui");
        labelError.setForeground(Color.RED);
        labelError.setFont(new Font("Arial", Font.PLAIN, 11));
        labelError.setPreferredSize(new Dimension(300, 15));

        panelResultado = new JPanel();
        panelResultado.setPreferredSize(new Dimension(400, 200));
        panelResultado.setBackground(new Color(255, 220, 230)); // rosa claro placeholder
    }

    private void layoutComponentes() {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(12, 12, 12, 12));

        GridBagConstraints gbc = baseGbc();

        // Panel Citas (columna izquierda)
        JPanel panelCitas = crearSeccionCitas();
        gbc.gridx = 0;
        gbc.weightx = 0.09;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(panelCitas, gbc);


        JPanel separador = new JPanel();
        separador.setPreferredSize(new Dimension(4, 1));
        separador.setOpaque(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(separador, gbc);

        // Panel Historial (columna derecha)
        JPanel panelHistorial = crearSeccionHistorial();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(panelHistorial, gbc);
    }

    private JPanel crearSeccionCitas() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(crearSeccionBorder("Citas"));

        GridBagConstraints gbc = baseGbc();

        // Filtros
        JPanel filtros = new JPanel(new GridBagLayout());
        filtros.setOpaque(false);
        GridBagConstraints gf = baseGbc();
        gf.insets = new Insets(0, 0, 0, 8);
        gf.fill = GridBagConstraints.NONE;

        gf.gridx = 0; gf.gridy = 0; filtros.add(btnHoy, gf);
        gf.gridx = 1; filtros.add(btnSemana, gf);
        gf.gridx = 2; filtros.add(btnTodas, gf);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addWithSeparator(panel, filtros, gbc, new Insets(0, 0, 8, 0));

        // Tabla de citas con scroll
        JScrollPane scrollTabla = new JScrollPane(tablaCitas);
        scrollTabla.setBorder(new LineBorder(new Color(210, 210, 210)));
        scrollTabla.getViewport().setBackground(Color.WHITE);
        tablaCitas.setFillsViewportHeight(true);

        gbc.gridy = 1;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollTabla, gbc);

        // Botón Crear (alineado a la izquierda)
        JPanel pie = new JPanel(new GridBagLayout());
        pie.setOpaque(false);
        GridBagConstraints gp = baseGbc();
        gp.fill = GridBagConstraints.NONE;
        gp.anchor = GridBagConstraints.WEST;
        pie.add(btnCrear, gp);

        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addWithSeparator(panel, pie, gbc, new Insets(8, 0, 0, 0));

        return panel;
    }

    private JPanel crearSeccionHistorial() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(crearSeccionBorder("Historial Pacientes"));

        GridBagConstraints gbc = baseGbc();

        // Bloque buscador horizontal
        JPanel buscador = new JPanel(new GridBagLayout());
        buscador.setOpaque(false);
        GridBagConstraints gb = baseGbc();

        JLabel labelCipa = new JLabel("CIPA:");
        labelCipa.setFont(new Font("Arial", Font.BOLD, 13));
        labelCipa.setForeground(new Color(80, 80, 80)); // gris claro

        gb.gridx = 0; gb.gridy = 0;
        buscador.add(labelCipa, gb);

        gb.gridx = 1;
        buscador.add(campoCipa, gb);

        gb.gridx = 2;
        buscador.add(btnBuscar, gb);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addWithSeparator(panel, buscador, gbc, new Insets(0, 0, 10, 0));

        gbc.gridy = 1;
        panel.add(labelError, gbc);

        // Resultado
        gbc.gridy = 2;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(panelResultado, gbc);

        return panel;
    }

    private TitledBorder crearSeccionBorder(String titulo) {
        return new TitledBorder(
                new CompoundBorder(
                        new LineBorder(new Color(160, 160, 160), 1, true),
                        new EmptyBorder(8, 8, 8, 8)
                ),
                titulo,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.PLAIN, 14),
                new Color(51, 51, 51) // gris oscuro
        );
    }


    private void estilizarComponentes() {
        // Botones de filtros estilo ligero
        Font filtroFont = new Font("Arial", Font.BOLD, 12);
        for (JButton b : new JButton[]{btnHoy, btnSemana, btnTodas}) {
            b.setFont(filtroFont);
            b.setFocusPainted(false);
            b.setBackground(new Color(245, 245, 245));
            b.setBorder(new CompoundBorder(
                    new LineBorder(new Color(210, 210, 210)),
                    new EmptyBorder(4, 10, 4, 10)
            ));
        }

        // Botones principales
        estilizarBotonPrimario(btnCrear);
        estilizarBotonPrimario(btnBuscar);

        // Tabla
        tablaCitas.setRowHeight(22);
        tablaCitas.setGridColor(new Color(230, 230, 230));
        tablaCitas.setShowHorizontalLines(true);
        tablaCitas.setShowVerticalLines(false);
        tablaCitas.setForeground(new Color(40, 40, 40));
        tablaCitas.setBackground(Color.WHITE);
        tablaCitas.getTableHeader().setReorderingAllowed(false);
        tablaCitas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaCitas.getTableHeader().setBackground(new Color(240, 240, 240));
        tablaCitas.getTableHeader().setBorder(new LineBorder(new Color(210, 210, 210)));
    }

    private void estilizarBotonPrimario(JButton b) {
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBackground(new Color(66, 133, 244)); // azul
        b.setForeground(Color.WHITE);
        b.setBorder(new CompoundBorder(
                new LineBorder(new Color(66, 133, 244)),
                new EmptyBorder(3, 8, 3, 8)
        ));
    }

    private TitledBorder crearTitledBorder(String titulo) {
        return new TitledBorder(
                new CompoundBorder(
                        new LineBorder(new Color(210, 210, 210), 1, true),
                        new EmptyBorder(3, 8, 8, 8)
                ),
                titulo,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 15),
                new Color(50, 50, 50)
        );
    }

    private GridBagConstraints baseGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        return gbc;
    }

    private void addWithSeparator(JPanel panel, JComponent comp, GridBagConstraints gbc, Insets extraInsets) {
        gbc.insets = extraInsets;
        panel.add(comp, gbc);
    }

    // Getters si necesitas acceder desde fuera
    public JTable getTablaCitas() { return tablaCitas; }
    public JTextField getCampoCipa() { return campoCipa; }
    public JPanel getPanelResultado() { return panelResultado; }
    public JButton getBtnCrear() { return btnCrear; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnHoy() { return btnHoy; }
    public JButton getBtnSemana() { return btnSemana; }
    public JButton getBtnTodas() { return btnTodas; }
}

