package interfaz;

import Controlador.Controlador;
import Controlador.OyenteSesion;
import Controlador.TipoUsuario;
import backend.Usuarios.Usuario;
import interfaz.Panel.*;

import javax.swing.*;

public class MainVentana extends JFrame implements OyenteSesion {

    private JTabbedPane tabs;

    private int tabAutentificar;
    private int tabPaciente;
    private int tabMedico;
    private int tabAdminCentro;
    private int tabAdmin;

    public MainVentana(Controlador controlador) {
        setTitle("Sistema de Citas Médicas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Añadir oyente para las pestañas
        controlador.addOyente(this);

        tabs = new JTabbedPane();

        tabAutentificar = tabs.getTabCount();
        tabs.add("Autentificar", new PanelAutentificar(controlador));

        tabPaciente = tabs.getTabCount();
        tabs.addTab("Paciente", new PanelPaciente());

        tabMedico = tabs.getTabCount();
        tabs.addTab("Médico", new PanelMedico());

        tabAdminCentro = tabs.getTabCount();
        tabs.addTab("Gestión Centro", new PanelCentro());

        tabAdmin = tabs.getTabCount();
        tabs.addTab("Admin", new PanelAdmin());

        add(tabs);

        //Desabilitar todas
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.setEnabledAt( i, false);
        }
        tabs.setEnabledAt(tabAutentificar,true);
        tabs.setSelectedIndex(tabAutentificar);

    }

    @Override
    public void onSesionUpdate(Usuario usuario) {
        switch(TipoUsuario.getTipoUsuario(usuario)) {
            case TipoUsuario.PACIENTE:
                tabs.setEnabledAt(tabPaciente,true);    break;
            case TipoUsuario.ADMIN:
                tabs.setEnabledAt(tabAdmin,true);       break;
            case TipoUsuario.ADMINCENTRO:
                tabs.setEnabledAt(tabAdminCentro,true); break;
            case TipoUsuario.MEDICO:
                tabs.setEnabledAt(tabMedico,true);      break;

        }
    }
}
