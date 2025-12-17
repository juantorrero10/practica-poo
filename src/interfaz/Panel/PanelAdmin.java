package interfaz.Panel;

import Controlador.*;
import interfaz.Panel.subPaneles.PanelUsuarios;

import javax.swing.*;
import java.awt.*;

public class PanelAdmin extends Panel {

    public PanelAdmin(Controlador c) {

        super("Administrador", c);
        crearBarraUsuario(TipoUsuario.ADMIN);
        hasBarraSuperior = true;

        JPanel contenido = new JPanel(new BorderLayout());

        // ========================
        // Panel izquierdo
        // ========================
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        PanelUsuarios panelUsuarios = new PanelUsuarios(c);

        // Evitar estirado vertical
        JPanel contenedor = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        contenedor.add(panelUsuarios);

        panelIzquierdo.add(contenedor, BorderLayout.NORTH);

        // Ancho fijo (1/3)
        panelIzquierdo.setPreferredSize(new Dimension(350, 200));

        // ========================
        // Panel derecho
        // ========================
        JPanel panelDerecho = new JPanel();
        panelDerecho.setBackground(Color.LIGHT_GRAY);

        // ========================
        // Añadir al contenido
        // ========================
        contenido.add(panelIzquierdo, BorderLayout.WEST);
        contenido.add(panelDerecho, BorderLayout.CENTER);

        // ❗ SOLO esto va al CENTER del Panel base
        add(contenido, BorderLayout.CENTER);



    }
}
