package interfaz;

import Controlador.Controlador;
import Controlador.OyenteSesion;
import Controlador.TipoUsuario;
import Main.Log;
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

    private final Controlador controlador;

    public MainVentana(Controlador controlador) {
        this.controlador = controlador;

        setTitle("Sistema de Citas Médicas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        controlador.addOyenteSesion(this);

        tabs = new JTabbedPane();

        pAutentificar = new PanelAutentificar(controlador);
        tabAutentificar = tabs.getTabCount();
        tabs.add("Autentificar", pAutentificar);

        tabPaciente = tabs.getTabCount();
        tabs.add("Paciente", new JPanel());

        tabMedico = tabs.getTabCount();
        tabs.add("Médico", new JPanel());

        tabAdminCentro = tabs.getTabCount();
        tabs.add("Gestión Centro", new JPanel());

        tabAdmin = tabs.getTabCount();
        tabs.add("Admin", new JPanel());

        add(tabs);

        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.setEnabledAt(i, false);
        }
        tabs.setEnabledAt(tabAutentificar, true);
        tabs.setSelectedIndex(tabAutentificar);
    }


    // ==================================================
    // CREACIÓN DE PANELES
    // ==================================================
    private void crearPanelAutentificar() {
        pAutentificar = new PanelAutentificar(controlador);
        tabs.setComponentAt(tabAutentificar, pAutentificar);
    }

    private void crearPanelPaciente() {
        pPaciente = new PanelPaciente(controlador);
        tabs.setComponentAt(tabPaciente, pPaciente);
    }

    private void crearPanelMedico() {
        pMedico = new PanelMedico(controlador);
        tabs.setComponentAt(tabMedico, pMedico);
    }

    private void crearPanelAdminCentro() {
        pCentro = new PanelCentro(controlador);
        tabs.setComponentAt(tabAdminCentro, pCentro);
    }

    private void crearPanelAdmin() {
        pAdmin = new PanelAdmin(controlador);
        tabs.setComponentAt(tabAdmin, pAdmin);
    }

    // ==================================================
    // OYENTE DE SESIÓN
    // ==================================================
    @Override
    public void onSesionUpdate(Usuario usuario, TipoUsuario tipoUsuario, boolean cambiarPest) {
        boolean activo = usuario != null;
        Log.INFO("SessionUpdate -> activo=" + activo + ", tipo=" + tipoUsuario);

        switch (tipoUsuario) {

            case PACIENTE -> {
                if (activo) crearPanelPaciente();
                tabs.setEnabledAt(tabPaciente, activo);
                if (activo) pPaciente.actualizarLabelUsuario(usuario);
                if (cambiarPest) tabs.setSelectedIndex(activo ? tabPaciente : tabAutentificar);
            }

            case MEDICO -> {
                if (activo) crearPanelMedico();
                tabs.setEnabledAt(tabMedico, activo);
                if (activo) pMedico.actualizarLabelUsuario(usuario);
                if (cambiarPest) tabs.setSelectedIndex(activo ? tabMedico : tabAutentificar);
            }

            case ADMINCENTRO -> {
                if (activo) crearPanelAdminCentro();
                tabs.setEnabledAt(tabAdminCentro, activo);
                if (activo) pCentro.actualizarLabelUsuario(usuario);
                if (cambiarPest) tabs.setSelectedIndex(activo ? tabAdminCentro : tabAutentificar);
            }

            case ADMIN -> {
                if (activo) crearPanelAdmin();
                tabs.setEnabledAt(tabAdmin, activo);
                if (activo) pAdmin.actualizarLabelUsuario(usuario);
                if (cambiarPest) tabs.setSelectedIndex(activo ? tabAdmin : tabAutentificar);
            }
        }
    }
}
