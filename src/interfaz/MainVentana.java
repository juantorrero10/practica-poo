package interfaz;

import Controlador.Controlador;
import Controlador.OyenteSesion;
import Controlador.TipoUsuario;
import Main.Log;
import backend.Usuarios.Paciente;
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

    private PanelAutentificar pAutentificar;
    private PanelAdmin pAdmin;
    private PanelMedico pMedico;
    private PanelCentro pCentro;
    private PanelPaciente pPaciente;

    public MainVentana(Controlador controlador) {
        setTitle("Sistema de Citas Médicas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        pAutentificar = new PanelAutentificar(controlador);
        pAdmin = new PanelAdmin(controlador);
        pMedico = new PanelMedico(controlador);
        pCentro = new PanelCentro(controlador);
        pPaciente = new PanelPaciente(controlador);


        //Añadir oyente para las pestañas
        controlador.addOyente(this);

        tabs = new JTabbedPane();

        tabAutentificar = tabs.getTabCount();
        tabs.add("Autentificar", pAutentificar);

        tabPaciente = tabs.getTabCount();
        tabs.addTab("Paciente", pPaciente);

        tabMedico = tabs.getTabCount();
        tabs.addTab("Médico", pMedico);

        tabAdminCentro = tabs.getTabCount();
        tabs.addTab("Gestión Centro", pCentro);

        tabAdmin = tabs.getTabCount();
        tabs.addTab("Admin", pAdmin);

        add(tabs);

        //Desabilitar todas
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.setEnabledAt( i, false);
        }
        tabs.setEnabledAt(tabAutentificar,true);
        tabs.setSelectedIndex(tabAutentificar);

    }


    @Override
    public void onSesionUpdate(Usuario usuario, TipoUsuario tipoUsuario) {
        boolean flag = usuario != null;
        Log.INFO("SessionUpdate -> flag: " + flag + ", type: " + tipoUsuario);

        switch(tipoUsuario) {
            case TipoUsuario.PACIENTE:
                tabs.setEnabledAt(tabPaciente, flag);
                tabs.setSelectedIndex(!flag ? tabAutentificar : tabPaciente);
                pPaciente.actualizarLabelUsuario(usuario);
                break;
            case TipoUsuario.ADMIN:
                tabs.setEnabledAt(tabAdmin, flag);
                tabs.setSelectedIndex(!flag ? tabAutentificar : tabAdmin);
                pAdmin.actualizarLabelUsuario(usuario);
                break;
            case TipoUsuario.ADMINCENTRO:
                tabs.setEnabledAt(tabAdminCentro, flag);
                tabs.setSelectedIndex(!flag ? tabAutentificar : tabAdminCentro);
                pCentro.actualizarLabelUsuario(usuario);
                break;
            case TipoUsuario.MEDICO:
                tabs.setEnabledAt(tabMedico, flag);
                tabs.setSelectedIndex(!flag ? tabAutentificar : tabMedico);
                pMedico.actualizarLabelUsuario(usuario);
                break;

        }
    }
}
