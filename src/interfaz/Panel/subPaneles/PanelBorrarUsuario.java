package interfaz.Panel.subPaneles;

import Controlador.Controlador;
import Controlador.TipoUsuario;
import backend.Enumeradores.Centros;
import backend.Enumeradores.Especialidades;

import javax.swing.*;
import java.awt.*;

public class PanelBorrarUsuario extends JPanel {

    private JLabel labelCIPA;
    private JTextField campoCipa;
    private JButton botonBorrar;
    private JLabel labelError;

    private Controlador controlador;

    public  PanelBorrarUsuario(Controlador c) {
        this.controlador = c;

        inicializarComponentes();
        registrarEventos();
        layoutComponentes();
    }

    private void inicializarComponentes() {
        labelCIPA = new JLabel("CIPA:");
        labelCIPA.setFont(labelCIPA.getFont().deriveFont( Font.BOLD, 14f));
        campoCipa = new JTextField();
        botonBorrar = new JButton("Borrar");
        labelError = new JLabel("");
        labelError.setForeground(Color.RED);
        labelError.setFont(labelError.getFont().deriveFont(Font.PLAIN, 11f));
        labelError.setPreferredSize(new Dimension(300, 15));
    }

    private void registrarEventos() {
        botonBorrar.addActionListener(e -> borrarUsuario());
    }

    private void layoutComponentes() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 1, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        add(labelCIPA, gbc); gbc.gridy++;
        add(campoCipa, gbc); gbc.gridy++;
        add(botonBorrar, gbc); gbc.gridy++;
        add(labelError, gbc); gbc.gridy++;

    }

    private void borrarUsuario() {
        setLabelError("", Color.RED);
        long CIPA = Long.parseLong(campoCipa.getText());
        if (controlador.borrarUsuario(CIPA)) {
            setLabelError("Usuario borrado correctamente", Color.GREEN);
            return;
        }
        setLabelError("El usuario no existe.", Color.RED);
    }

    private void setLabelError (String msg, Color c) {
        labelError.setForeground(c);
        labelError.setText(msg);
    }
}
