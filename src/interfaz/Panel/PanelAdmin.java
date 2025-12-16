package interfaz.Panel;

import Controlador.*;

import javax.swing.*;
import java.awt.*;

public class PanelAdmin extends Panel {

    public PanelAdmin(Controlador c) {

        super("Administrador", c);
        crearBarraUsuario(TipoUsuario.ADMIN);
        hasBarraSuperior = true;
    }
}
