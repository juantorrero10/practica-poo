package interfaz.Panel.subPaneles;

import Controlador.Controlador;

import javax.swing.*;
import java.awt.*;

public class PanelUsuarios extends JPanel {

    private Controlador controlador;

    private final String[] acciones = {"Crear", "Modificar", "Borrar"};

    private JLabel labelHeader;

    private JComboBox<String> selectorAccion;
    private CardLayout cardLayout;
    private JPanel panelAcciones;

    //SubPaneles
    private PanelCrearUsuario panelCrearUsuario;
    private PanelModificarUsuario panelModificarUsuario;
    private PanelBorrarUsuario panelBorrarUsuario;

    public PanelUsuarios(Controlador c) {
        this.controlador = c;

        inicializarComponentes();
        registarEventos();
        layoutComponentes();

    }

    private void inicializarComponentes() {
        labelHeader = new JLabel("Gestion de usuarios");
        labelHeader.setHorizontalAlignment(JLabel.CENTER);
        labelHeader.setFont(new Font(labelHeader.getFont().getFontName(), Font.BOLD, 15));
        selectorAccion = new JComboBox<>(acciones);
        cardLayout = new CardLayout();
        panelAcciones = new JPanel(cardLayout);
        panelCrearUsuario = new PanelCrearUsuario(controlador);
        panelModificarUsuario = new PanelModificarUsuario(controlador);
        panelBorrarUsuario = new PanelBorrarUsuario(controlador);
        panelAcciones.add(panelCrearUsuario, "Crear");
        panelAcciones.add(panelModificarUsuario, "Modificar");
        panelAcciones.add(panelBorrarUsuario, "Borrar");
    }

    private void registarEventos() {
        selectorAccion.addActionListener(e -> {
            String seleccion = (String) selectorAccion.getSelectedItem();
            cardLayout.show(panelAcciones, seleccion); }
        );
    }

    private void layoutComponentes() {
        // Contenedor vertical: selector arriba, panel dinámico debajo
        JPanel contenedor = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 1, 3, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        contenedor.add(labelHeader, gbc); gbc.gridy++;
        contenedor.add(selectorAccion, gbc); gbc.gridy++;
        contenedor.add(panelAcciones, gbc); gbc.gridy++;
        add(contenedor, BorderLayout.WEST);
    }
}
