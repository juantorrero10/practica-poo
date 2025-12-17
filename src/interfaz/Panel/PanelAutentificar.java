package interfaz.Panel;

import Main.Log;
import backend.Usuarios.Admin;
import backend.Usuarios.Usuario;
import Controlador.Controlador;
import Controlador.TipoUsuario;

import javax.swing.*;
import java.awt.*;

public class PanelAutentificar extends Panel {

    //Elementos
    private JComboBox<TipoUsuario> selectorRol;
    private JLabel labelHeader;
    private JTextField campoCIPA;
    private JLabel labelError;
    private JButton botonAutentificar;
    private JPanel panelMarco;

    public PanelAutentificar(Controlador c) {
        super("Autentificar", c);
        controlador = c;

        inicializarComponentes();
        layoutComponentes();
        registrarEventos();
    }

    private void inicializarComponentes() {
        selectorRol = new JComboBox<>(new TipoUsuario[]{
                TipoUsuario.PACIENTE,
                TipoUsuario.MEDICO,
                TipoUsuario.ADMINCENTRO,
                TipoUsuario.ADMIN
        });

        campoCIPA = new JTextField(10);

        botonAutentificar = new JButton("Autentificar");

        panelMarco = new JPanel(new GridBagLayout());
        labelError = new JLabel(" "); // espacio reservado
        labelError.setForeground(Color.RED);
        labelError.setFont(labelError.getFont().deriveFont(Font.PLAIN, 11f));
        labelError.setPreferredSize(new Dimension(300, 15));

        labelHeader = new JLabel("Autentificación");
        labelHeader.setForeground(Color.DARK_GRAY);
        labelHeader.setFont(labelHeader.getFont().deriveFont(Font.BOLD, 30f));
        labelHeader.setPreferredSize(new Dimension(300, 60));
        labelHeader.setHorizontalAlignment(SwingConstants.CENTER);

    }

    private void layoutComponentes() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        // -------- ESPACIO SUPERIOR --------
        gbc.gridy = 0;
        gbc.weighty = 1;
        add(Box.createVerticalGlue(), gbc);

        // -------- HEADER --------
        gbc.gridy = 1;
        gbc.weighty = 0;
        add(labelHeader, gbc);

        // -------- MARCO --------
        panelMarco.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1),
                        BorderFactory.createEmptyBorder(15, 20, 15, 20)
                )
        );

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        add(panelMarco, gbc);

        // -------- ESPACIO INFERIOR --------
        gbc.gridy = 3;
        gbc.weighty = 1;
        add(Box.createVerticalGlue(), gbc);

        // ===== CONTENIDO DEL MARCO =====
        GridBagConstraints gbcMarco = new GridBagConstraints();
        gbcMarco.insets = new Insets(5, 5, 5, 5);
        gbcMarco.anchor = GridBagConstraints.WEST;

        // Tipo usuario
        gbcMarco.gridx = 0;
        gbcMarco.gridy = 0;
        panelMarco.add(new JLabel("Tipo de usuario:"), gbcMarco);

        gbcMarco.gridx = 1;
        panelMarco.add(selectorRol, gbcMarco);

        // CIPA
        gbcMarco.gridx = 0;
        gbcMarco.gridy = 1;
        panelMarco.add(new JLabel("CIPA:"), gbcMarco);

        gbcMarco.gridx = 1;
        panelMarco.add(campoCIPA, gbcMarco);

        // Botón centrado
        gbcMarco.gridx = 0;
        gbcMarco.gridy = 2;
        gbcMarco.gridwidth = 2;
        gbcMarco.anchor = GridBagConstraints.CENTER;
        panelMarco.add(botonAutentificar, gbcMarco);

        // Error
        gbcMarco.gridy = 3;
        panelMarco.add(labelError, gbcMarco);
    }


    private void registrarEventos() {
        botonAutentificar.addActionListener(e -> autentificar());
    }

    private void autentificar() {
        limpiarError();

        boolean adminDebug = false;
        Usuario usuario = null;

        int cipa;
        try {
            cipa = Integer.parseInt(campoCIPA.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarError("El CIPA debe ser un número");
            return;
        }

        //Super Usuario para depurar
        if (cipa == -1) {
            Log.WARN("Autenticado como Admin de depuración.");
            usuario = new Admin("ADMIN", -1);
            adminDebug = true;
        }

        TipoUsuario tipoSeleccionado =
                (TipoUsuario) selectorRol.getSelectedItem();

        if (tipoSeleccionado == null) tipoSeleccionado = TipoUsuario.NO_ESPECIFICADO;

        Log.INFO("Autentificación con CIPA=" + cipa + ", TipoUsuario=" + tipoSeleccionado);

        if (!adminDebug) usuario = controlador.getUsuario(cipa, tipoSeleccionado);

        if (usuario == null) {
            mostrarError("No se ha encontrado el usuario");
            Log.WARN("No se ha encontrado el usuario");
            return;
        }

        if (!controlador.cambiarUsuario(usuario)) {
            mostrarError("Se debe cerrar la sesión del usuario con el mismo tipo.");
            Log.ERR("Ya se ha autentificado con el tipo: " + usuario.getClass().getSimpleName());
            Log.WARN("Se debe cerrar la sesion de " + controlador.getLoginUsuario(tipoSeleccionado));
            return;
        }

        Log.INFO("(" + usuario.getClass().getSimpleName() + ") Usuario autenticado correctamente: " + usuario);
    }

    private void limpiarError() {
        labelError.setText(" ");
    }

    private void mostrarError(String mensaje) {
        labelError.setText(mensaje);
    }
}