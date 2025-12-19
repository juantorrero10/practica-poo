package interfaz.Panel;

import Controlador.*;
import interfaz.Panel.subPaneles.PanelUsuarios;

import javax.swing.*;
import java.awt.*;

public class PanelCentro extends Panel {

    public PanelCentro(Controlador c) {
        super("Gestión Centro", c);

        add(crearBarraUsuario(TipoUsuario.ADMINCENTRO), BorderLayout.NORTH);
        hasBarraSuperior = true;


        JPanel contenido = new JPanel(new BorderLayout());

        // ========================
        // Panel izquierdo
        // ========================);

        PanelUsuarios panelUsuarios = new PanelUsuarios(c);

        // Evitar estirado vertical
        JPanel contenedor = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        contenedor.add(panelUsuarios);

        // ========================
        // Añadir al contenido
        // ========================
        contenido.add(contenedor, BorderLayout.CENTER);

        // ❗ SOLO esto va al CENTER del Panel base
        add(contenido, BorderLayout.CENTER);



    }
}
