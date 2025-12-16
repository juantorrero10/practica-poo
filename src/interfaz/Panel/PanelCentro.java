package interfaz.Panel;

import Controlador.*;

import javax.swing.*;
import java.awt.*;

public class PanelCentro extends Panel {

    public PanelCentro(Controlador c) {
        super("Gestión Centro", c);
        crearBarraUsuario(TipoUsuario.ADMINCENTRO);
        hasBarraSuperior = true;
    }
}
