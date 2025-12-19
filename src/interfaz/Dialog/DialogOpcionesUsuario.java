package interfaz.Dialog;

import Controlador.*;
import Main.Log;
import backend.Enumeradores.Centros;
import backend.Enumeradores.Especialidades;
import backend.Usuarios.AdminCentroSalud;
import backend.Usuarios.Medico;
import backend.Usuarios.Paciente;
import backend.Usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DialogOpcionesUsuario extends JDialog {


    private final Usuario usuario;
    private final Controlador controlador;

    private JTextField campoDni;
    private JTextField campoTelefono;
    private JTextField campoNombre;
    private JTextField campoDireccion;

    private JComboBox<Especialidades> comboEspecialidad;
    private JComboBox<Centros> comboCentro;

    private JLabel labelError;

    public DialogOpcionesUsuario(Window owner, Usuario u, Controlador c) {
        super(owner, "Opciones de usuario", ModalityType.APPLICATION_MODAL);

        this.usuario = u;
        this.controlador = c;

        int width = 400;
        if (u.getTipoUsuario() == TipoUsuario.MEDICO ||  u.getTipoUsuario() == TipoUsuario.ADMINCENTRO) {
            width = 500;
        }


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(width, 350);
        setLocationRelativeTo(owner);

        setLayout(new BorderLayout());

        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBoton(), BorderLayout.SOUTH);
    }

    private JPanel crearFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // DNI
        campoDni = new JTextField(usuario.getDNI(), 15);
        campoDni.setEnabled(usuario.isAdmin());
        nuevoCampo(p, gbc, "DNI:", campoDni);

        switch (usuario.getTipoUsuario()) {
        // ===== PACIENTE =====
        case PACIENTE:
            Paciente pac = (Paciente) usuario;
            campoNombre = new JTextField(pac.getNombreCompleto(), 15);
            campoDireccion = new JTextField(pac.getDireccion(), 15);
            campoTelefono = new JTextField(String.valueOf(pac.getTelefono()), 15);

            campoNombre.setEnabled(false);
            campoDireccion.setEnabled(false);

            nuevoCampo(p, gbc, "Nombre:", campoNombre);
            nuevoCampo(p, gbc, "Dirección:", campoDireccion);
            nuevoCampo(p, gbc, "Teléfono:", campoTelefono);

            break;

        // ===== MEDICO =====
        case MEDICO:
            Medico med = (Medico) usuario;
            comboEspecialidad = new JComboBox<>(Especialidades.values());
            comboEspecialidad.setSelectedItem(med.getEspecialidad());
            comboEspecialidad.setEnabled(false);

            comboCentro = new JComboBox<>(Centros.values());
            comboCentro.setSelectedItem(med.getCentro());
            comboCentro.setEnabled(false);

            nuevoCampo(p, gbc, "Especialidad:", comboEspecialidad);
            nuevoCampo(p, gbc, "Centro:", comboCentro);
            break;

        // ===== ADMIN CENTRO =====
        case ADMINCENTRO:
            AdminCentroSalud ac = (AdminCentroSalud) usuario;
            comboCentro = new JComboBox<>(Centros.values());
            comboCentro.setSelectedItem(ac.getCentro());
            comboCentro.setEnabled(true);

            nuevoCampo(p, gbc, "Centro:", comboCentro);

            break;
        }

        return p;
    }


    private void nuevoCampo(JPanel p, GridBagConstraints gbc, String texto, JComponent campo) {
        gbc.gridx = 0;
        p.add(new JLabel(texto), gbc);

        gbc.gridx = 1;
        p.add(campo, gbc);

        gbc.gridy++;
    }

    private JPanel crearBoton() {
        JPanel p = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 1, 2, 0);
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton botonAplicar = new JButton("Aplicar");
        botonAplicar.addActionListener(e -> {
            try {
                aplicarCambios();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        labelError = new JLabel("");
        labelError.setForeground(Color.RED);
        labelError.setFont(labelError.getFont().deriveFont(Font.PLAIN, 11f));
        labelError.setPreferredSize(new Dimension(300, 15));

        p.add(botonAplicar, gbc); gbc.gridy++;
        p.add(labelError, gbc);
        return p;
    }

    private void setLabelError(String texto, Color c) {
        labelError.setText(texto);
        labelError.setForeground(c);
    }

    private void campoVacioError() {
        Log.ERR("Alguno de los campos está vacio");
        setLabelError("No puede haber un campo vacio.", Color.RED);
    }

    private void aplicarCambios() throws IOException {
        setLabelError("", Color.GREEN);
        long telefono = -1;
        String nombre = "";
        String direccion = "";
        String telefonoStr = "";
        Especialidades especialidad = Especialidades.NO_ESPECIFICADO;
        Centros centros = Centros.NO_ESPECIFICADO;
        String DNI = campoDni.getText();

        switch(usuario.getTipoUsuario()) {
            case PACIENTE:
                nombre = campoNombre.getText();
                direccion = campoDireccion.getText();
                telefonoStr = campoTelefono.getText();
                if (nombre.isEmpty() ||  direccion.isEmpty() || telefonoStr.isEmpty()) {
                    campoVacioError(); return;
                }
                telefono = Long.parseLong(telefonoStr);
                break;
            case MEDICO:
                especialidad = (Especialidades) comboEspecialidad.getSelectedItem();
            case ADMINCENTRO:
                centros = (Centros) comboCentro.getSelectedItem();
                break;
        }

        if (DNI.isEmpty()) {
            campoVacioError(); return;
        }

        controlador.modificarUsuario(usuario, DNI, nombre, direccion, telefono, especialidad, centros);
        setLabelError("Información modificada correctamente", Color.GREEN);

        controlador.notificarCambioSesion(usuario, usuario.getTipoUsuario(), false);
    }
}