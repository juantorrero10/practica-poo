package interfaz.Panel;

import Controlador.*;
import interfaz.Panel.subPaneles.PanelContenidoMedico;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelMedico extends Panel {

    public PanelMedico(Controlador c) {
        super("Médico", c);

        setLayout(new BorderLayout());

        // Barra de usuario explícita en NORTH
        JPanel barraUsuario = crearBarraUsuario(TipoUsuario.MEDICO);
        hasBarraSuperior = true;
        add(barraUsuario, BorderLayout.NORTH);

        // Contenido principal en CENTER
        PanelContenidoMedico contenido = new PanelContenidoMedico(controlador);
        add(contenido, BorderLayout.CENTER);
    }
}

