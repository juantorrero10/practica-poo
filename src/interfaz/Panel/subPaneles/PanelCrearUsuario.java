package interfaz.Panel.subPaneles;

import Controlador.Controlador;
import Controlador.TipoUsuario;
import Main.Log;
import backend.Enumeradores.*;
import backend.Usuarios.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PanelCrearUsuario extends JPanel {

    private JComboBox<TipoUsuario> selectorTipo;

    private JTextField campoDni;
    private JTextField campoCipa;

    // Panel dinámico
    private JPanel panelExtra;
    private CardLayout cardLayout;

    // Campos Paciente
    private JTextField campoNombre;
    private JTextField campoDireccion;
    private JTextField campoTelefono;

    // Campos Medico (modificados a JComboBox)
    private JComboBox<Especialidades> selectorEspecialidad;
    private JComboBox<Centros> selectorCentroMedico;

    // Campo Admin Centro
    private JComboBox<Centros> selectorCentroAdmin;

    // Botón Crear
    private JButton botonCrear;

    private JLabel labelError;

    private Controlador controlador;

    public PanelCrearUsuario(Controlador controlador) {
        inicializarComponentes();
        layoutComponentes();
        registrarEventos();

        this.controlador = controlador;
    }

    private void inicializarComponentes() {
        selectorTipo = new JComboBox<>(new TipoUsuario[]{
                TipoUsuario.PACIENTE,
                TipoUsuario.MEDICO,
                TipoUsuario.ADMINCENTRO,
                TipoUsuario.ADMIN
        });

        campoDni = new JTextField(10);
        campoCipa = new JTextField(10);

        // ===== Paciente =====
        campoNombre = new JTextField(15);
        campoDireccion = new JTextField(15);
        campoTelefono = new JTextField(10);

        // ===== Medico ===== (usamos enumeradores)
        selectorEspecialidad = new JComboBox<>(Especialidades.values());
        selectorCentroMedico = new JComboBox<>(Centros.values());

        // ===== Admin Centro ===== (también enumerador)
        selectorCentroAdmin = new JComboBox<>(Centros.values());

        // Panel dinámico
        cardLayout = new CardLayout();
        panelExtra = new JPanel(cardLayout);

        panelExtra.add(crearPanelPaciente(), TipoUsuario.PACIENTE.name());
        panelExtra.add(crearPanelMedico(), TipoUsuario.MEDICO.name());
        panelExtra.add(crearPanelAdminCentro(), TipoUsuario.ADMINCENTRO.name());
        panelExtra.add(new JPanel(), TipoUsuario.ADMIN.name()); // vacío

        campoDni.setPreferredSize(new Dimension(150, 25));
        campoCipa.setPreferredSize(new Dimension(150, 25));
        selectorEspecialidad.setPreferredSize(new Dimension(150, 25));
        selectorCentroMedico.setPreferredSize(new Dimension(150, 25));
        selectorCentroAdmin.setPreferredSize(new Dimension(150, 25));

        // Botón Crear
        botonCrear = new JButton("Crear");

        labelError = new JLabel("");
        labelError.setForeground(Color.RED);
        labelError.setFont(labelError.getFont().deriveFont(Font.PLAIN, 11f));
        labelError.setPreferredSize(new Dimension(300, 15));
    }

    private void layoutComponentes() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = crearGbc();

        gbc.gridy = 0;
        add(new JLabel("Tipo de usuario:"), gbc);
        gbc.gridy++;
        add(selectorTipo, gbc);

        // DNI
        gbc.gridy++;
        add(new JLabel("DNI:"), gbc);
        gbc.gridy++;
        add(campoDni, gbc);

        // CIPA
        gbc.gridy++;
        add(new JLabel("CIPA:"), gbc);
        gbc.gridy++;
        add(campoCipa, gbc);

        // Panel dinámico
        gbc.gridy++;
        add(panelExtra, gbc);

        // Botón Crear
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botonCrear, gbc);

        // Label error
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        add(labelError, gbc);
    }

    private void registrarEventos() {
        selectorTipo.addActionListener(e -> cambiarPanel());
        cambiarPanel(); // inicial
        botonCrear.addActionListener(e -> {
            try {
                crearUsuario();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void crearUsuario() throws IOException {
        setErrorLabel("", Color.GRAY);
        TipoUsuario tipoUsuario = (TipoUsuario) selectorTipo.getSelectedItem();
        long cipa = 0;
        try {
            cipa = Long.parseLong(campoCipa.getText());
        } catch (NumberFormatException e) {
            Log.WARN("CIPA Vacio");
            setErrorLabel("El CIPA no puede estar vacio", Color.RED);
            return;
        }
        String dni = campoDni.getText();
        String nombre = "";
        String direccion = "";
        Especialidades especialidad = Especialidades.NO_ESPECIFICADO;
        Centros centro = Centros.NO_ESPECIFICADO;
        long telefono = 0;

        if (tipoUsuario.equals(TipoUsuario.PACIENTE)) {
            nombre = campoNombre.getText();
            direccion = campoDireccion.getText();
            telefono = Long.parseLong(campoTelefono.getText());
        }
        if (tipoUsuario.equals(TipoUsuario.MEDICO)) {
            especialidad = (Especialidades) selectorEspecialidad.getSelectedItem();
        }

        if (tipoUsuario.equals(TipoUsuario.ADMINCENTRO) || tipoUsuario.equals(TipoUsuario.MEDICO)) {
            centro = (Centros) selectorCentroAdmin.getSelectedItem();
        }

        Usuario u;

        switch (tipoUsuario) {
            case PACIENTE:
                u = new Paciente(nombre, direccion, telefono, dni, cipa);
                break;
            case MEDICO:
                u = new Medico(dni, cipa, especialidad, centro);
                break;
            case ADMINCENTRO:
                u = new AdminCentroSalud(dni, cipa, centro);
                break;
            case ADMIN:
                u = new Admin(dni, cipa);
                break;
            case null:
            default:
                setErrorLabel("El tipo de usuario no es válido.", Color.RED);
                Log.ERR("Tipo invalido de usuario");
                return;
        }
        int ret = controlador.crearUsuario(u);

        switch (ret) {
            case 0:
                setErrorLabel("Usuario creado correctamente", Color.GREEN);
                break;
            case 1:
                setErrorLabel("Este CIPA ya esta registrado.", Color.RED);
                break;
            default:
                setErrorLabel("Error desconocido.", Color.RED);
                break;
        }
    }

    private void setErrorLabel(String txt, Color c) {
        labelError.setForeground(c);
        labelError.setText(txt);
    }

    private void cambiarPanel() {
        TipoUsuario tipo = (TipoUsuario) selectorTipo.getSelectedItem();
        cardLayout.show(panelExtra, tipo.name());
    }

    // =======================
    // Paneles específicos
    // =======================

    private JPanel crearPanelPaciente() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = crearGbc();

        gbc.gridy = 0;
        p.add(new JLabel("Nombre completo:"), gbc);
        gbc.gridy++;
        p.add(campoNombre, gbc);

        gbc.gridy++;
        p.add(new JLabel("Dirección:"), gbc);
        gbc.gridy++;
        p.add(campoDireccion, gbc);

        gbc.gridy++;
        p.add(new JLabel("Teléfono:"), gbc);
        gbc.gridy++;
        p.add(campoTelefono, gbc);

        return p;
    }

    private JPanel crearPanelMedico() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = crearGbc();

        gbc.gridy = 0;
        p.add(new JLabel("Especialidad:"), gbc);
        gbc.gridy++;
        p.add(selectorEspecialidad, gbc);

        gbc.gridy++;
        p.add(new JLabel("Centro:"), gbc);
        gbc.gridy++;
        p.add(selectorCentroMedico, gbc);

        return p;
    }

    private JPanel crearPanelAdminCentro() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = crearGbc();

        gbc.gridy = 0;
        p.add(new JLabel("Centro:"), gbc);
        gbc.gridy++;
        p.add(selectorCentroAdmin, gbc);

        return p;
    }

    private GridBagConstraints crearGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        return gbc;
    }
}

