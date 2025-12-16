package interfaz;

import interaciones.Controlador;
import interfaz.Panel.*;

import javax.swing.*;

public class MainVentana extends JFrame {
    public MainVentana(Controlador controlador) {
        setTitle("Sistema de Citas Médicas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Autentificar", new PanelAutentificar(controlador));
        /*
        tabs.addTab("Paciente", new PanelPaciente());
        tabs.addTab("Médico", new PanelMedico());
        tabs.addTab("Gestión Centro", new PanelCentro());
        tabs.addTab("Admin", new PanelAdmin());
        */

        add(tabs);
    }
}
