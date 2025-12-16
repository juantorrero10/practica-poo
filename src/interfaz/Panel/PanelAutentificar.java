package interfaz.Panel;

import Main.Log;
import backend.Usuarios.Usuario;
import Controlador.Controlador;
import Controlador.TipoUsuario;

import javax.swing.*;
import java.awt.*;

public class PanelAutentificar extends Panel {

    private final Controlador controlador;

    private JComboBox<TipoUsuario> selectorRol;
    private JTextField campoCIPA;
    private JLabel labelError;
    private JButton botonAutentificar;

    public PanelAutentificar(Controlador c) {
        super("Autentificar");
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

        labelError = new JLabel(" "); // espacio reservado
        labelError.setForeground(Color.RED);
        labelError.setFont(labelError.getFont().deriveFont(Font.PLAIN, 11f));
        labelError.setPreferredSize(new Dimension(250, 15));
    }

    private void layoutComponentes() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Tipo usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Tipo de usuario:"), gbc);

        gbc.gridx = 1;
        add(selectorRol, gbc);

        // CIPA
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("CIPA:"), gbc);

        gbc.gridx = 1;
        add(campoCIPA, gbc);

        // Botón (centrado debajo)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botonAutentificar, gbc);

        // Error (centrado, espacio fijo)
        gbc.gridy = 3;
        add(labelError, gbc);
    }

    private void registrarEventos() {
        botonAutentificar.addActionListener(e -> autentificar());
    }

    private void autentificar() {
        limpiarError();

        int cipa;
        try {
            cipa = Integer.parseInt(campoCIPA.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarError("El CIPA debe ser un número");
            return;
        }

        TipoUsuario tipoSeleccionado =
                (TipoUsuario) selectorRol.getSelectedItem();

        if (tipoSeleccionado == null) tipoSeleccionado = TipoUsuario.NO_ESPECIFICADO;

        Log.INFO("Autentificación con CIPA=" + cipa + ", TipoUsuario=" + tipoSeleccionado);

        Usuario usuario = controlador.getUsuario(cipa, tipoSeleccionado);

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