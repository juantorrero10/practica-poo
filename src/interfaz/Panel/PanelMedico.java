package interfaz.Panel;

import Controlador.*;

public class PanelMedico extends Panel{

    public PanelMedico(Controlador c) {

        super("Médico", c);
        crearBarraUsuario(TipoUsuario.MEDICO);
        hasBarraSuperior = true;
    }
}
