package interfaz.Panel;

import Controlador.Controlador;
import Controlador.OyenteSesion;
import Controlador.TipoUsuario;

import javax.swing.*;
import java.awt.*;

public class PanelPaciente extends Panel {

    public PanelPaciente(Controlador c) {

        super("Paciente", c);
        crearBarraUsuario(TipoUsuario.PACIENTE);
        hasBarraSuperior = true;
    }


}
