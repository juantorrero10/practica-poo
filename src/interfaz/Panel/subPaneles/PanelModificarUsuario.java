package interfaz.Panel.subPaneles;

import javax.swing.*;
import java.awt.*;
import Controlador.Controlador;
import Controlador.TipoUsuario;
import backend.Enumeradores.*;
import backend.Usuarios.*;

public class PanelModificarUsuario extends JPanel {

    private JLabel labelCIPA;
    private JTextField campoCipa;

    private JLabel labelElementos;

    private JLabel labelDNI;
    private JTextField campoDni;
    private JLabel labelTipo;
    private JComboBox<TipoUsuario> selectorTipo;

    // Panel dinámico
    private JPanel panelExtra;
    private CardLayout cardLayout;

    // Campos Paciente
    private JTextField campoNombre;
    private JTextField campoDireccion;
    private JTextField campoTelefono;

    // Campos Médico
    private JComboBox<Especialidades> selectorEspecialidad;
    private JComboBox<Centros> selectorCentroMedico;

    // Campos Admin Centro
    private JComboBox<Centros> selectorCentroAdmin;

    // Botón
    private JButton botonModificar;
    private JLabel labelError;

    private Controlador controlador;

    public PanelModificarUsuario(Controlador c) {
        this.controlador = c;

        inicializarComponentes();
        registrarEventos();
        layoutComponentes();
    }

    private void inicializarComponentes() {
        // Primer campo: CIPA
        labelCIPA = new JLabel("CIPA:");
        labelCIPA.setFont(labelCIPA.getFont().deriveFont(Font.BOLD, 14f));
        campoCipa = new JTextField();

        // Label grande para elementos
        labelElementos = new JLabel("Elementos a modificar");
        labelElementos.setFont(labelElementos.getFont().deriveFont(Font.BOLD, 16f));

        // DNI y Tipo
        labelDNI = new JLabel("DNI:");
        campoDni = new JTextField();

        labelTipo = new JLabel("Tipo de Usuario:");
        selectorTipo = new JComboBox<>(new TipoUsuario[]{
                TipoUsuario.PACIENTE,
                TipoUsuario.MEDICO,
                TipoUsuario.ADMINCENTRO,
                TipoUsuario.ADMIN
        });

        // ===== Paciente =====
        campoNombre = new JTextField(15);
        campoDireccion = new JTextField(15);
        campoTelefono = new JTextField(10);

        // ===== Médico =====
        selectorEspecialidad = new JComboBox<>(Especialidades.values());
        selectorCentroMedico = new JComboBox<>(Centros.values());

        // ===== Admin Centro =====
        selectorCentroAdmin = new JComboBox<>(Centros.values());

        // Panel dinámico
        cardLayout = new CardLayout();
        panelExtra = new JPanel(cardLayout);

        panelExtra.add(crearPanelPaciente(), TipoUsuario.PACIENTE.name());
        panelExtra.add(crearPanelMedico(), TipoUsuario.MEDICO.name());
        panelExtra.add(crearPanelAdminCentro(), TipoUsuario.ADMINCENTRO.name());
        panelExtra.add(new JPanel(), TipoUsuario.ADMIN.name()); // vacío

        // Botón
        botonModificar = new JButton("Modificar");

        // Label error
        labelError = new JLabel("");
        labelError.setForeground(Color.RED);
        labelError.setFont(labelError.getFont().deriveFont(Font.PLAIN, 11f));
        labelError.setPreferredSize(new Dimension(300, 15));
    }

    private void registrarEventos() {
        selectorTipo.addActionListener(e -> cambiarPanel());
        cambiarPanel(); // inicial
        botonModificar.addActionListener(e -> modificarUsuario());
    }

    private void layoutComponentes() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = crearGbc();

        // CIPA
        add(labelCIPA, gbc);
        gbc.gridy++;
        add(campoCipa, gbc);

        // Label grande
        gbc.gridy++;
        add(labelElementos, gbc);

        // DNI
        gbc.gridy++;
        add(labelDNI, gbc);
        gbc.gridy++;
        add(campoDni, gbc);

        // Tipo Usuario
        gbc.gridy++;
        add(labelTipo, gbc);
        gbc.gridy++;
        add(selectorTipo, gbc);

        // Panel dinámico
        gbc.gridy++;
        add(panelExtra, gbc);

        // Botón
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botonModificar, gbc);

        // Label error
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        add(labelError, gbc);
    }

    private void cambiarPanel() {
        TipoUsuario tipo = (TipoUsuario) selectorTipo.getSelectedItem();
        cardLayout.show(panelExtra, tipo.name());
    }

    private void modificarUsuario() {
        String cipaStr = campoCipa.getText();
        String dni = campoDni.getText();
        String nombre = "";
        String direccion = "";
        long telefono = -1;
        String telefonoStr = "";
        Especialidades especialidad = Especialidades.NO_ESPECIFICADO;
        Centros centro = Centros.NO_ESPECIFICADO;

        if (cipaStr.isEmpty()) {
            setLabelError("EL CIPA no puede estar vacio.", Color.RED);
            return;
        }

        TipoUsuario tp =  (TipoUsuario) selectorTipo.getSelectedItem();
        if (tp == TipoUsuario.NO_ESPECIFICADO) {
            setLabelError("Tipo de usuario no válido.", Color.RED);
            return;
        }

        long cipa =  Long.parseLong(cipaStr);
        Usuario u = controlador.getUsuario((int)cipa, tp);

        if (u == null) {
            setLabelError("El usuario no existe", Color.RED);
            return;
        }


        if (tp == TipoUsuario.PACIENTE) {
            nombre = campoNombre.getText();
            direccion = campoDireccion.getText();
            telefonoStr = campoTelefono.getText();
            if (!telefonoStr.isEmpty()) {
                telefono = Long.parseLong(campoTelefono.getText());
            }
        } else if (tp == TipoUsuario.MEDICO) {
            especialidad = (Especialidades) selectorEspecialidad.getSelectedItem();
        } if (tp == TipoUsuario.ADMINCENTRO || tp == TipoUsuario.MEDICO) {
            centro = (Centros) selectorCentroMedico.getSelectedItem();
        }

        controlador.modificarUsuario(u, dni, nombre, direccion, telefono, especialidad, centro);
        setLabelError("El Usuario se modificó correctamente.", Color.GREEN);

        //Actualizar barra
        Usuario upt = controlador.getUsuario((int)cipa, tp);
        if (upt.equals(controlador.getLoginUsuario(tp))) {
            controlador.notificarCambioSesion(upt, tp, false);
        }
    }

    private void setLabelError(String msg, Color c) {
        labelError.setForeground(c);
        labelError.setText(msg);
    }

    // =======================
    // Paneles específicos
    // =======================

    private JPanel crearPanelPaciente() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = crearGbc();

        p.add(new JLabel("Nombre:"), gbc);
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

        p.add(new JLabel("Centro:"), gbc);
        gbc.gridy++;
        p.add(selectorCentroAdmin, gbc);

        return p;
    }

    private GridBagConstraints crearGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;
        return gbc;
    }
}
