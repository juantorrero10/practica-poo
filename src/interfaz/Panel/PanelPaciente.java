package interfaz.Panel;

import Controlador.Controlador;
import Controlador.TipoUsuario;
import backend.Usuarios.Paciente;
import interfaz.Dialog.DialogCrearCita;
import interfaz.Panel.subPaneles.PanelPacienteHistorial;

import javax.swing.*;
import java.awt.*;

public class PanelPaciente extends Panel {

    private JPanel panelCentral;
    private boolean historialVisible = false;

    public PanelPaciente(Controlador c) {
        super("Paciente", c);
        crearBarraUsuario(TipoUsuario.PACIENTE);
        hasBarraSuperior = true;

        panelCentral = new JPanel(new BorderLayout());
        add(panelCentral, BorderLayout.CENTER);

        crearBotonera();
    }

    private void crearBotonera() {
        JButton botonCrearCita = new JButton("Crear cita");
        JButton botonHistorial = new JButton("Historial");

        botonCrearCita.addActionListener(e -> abrirCrearCita());
        botonHistorial.addActionListener(e -> toggleHistorial());

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.add(botonCrearCita);
        panelSur.add(botonHistorial);

        add(panelSur, BorderLayout.SOUTH);
    }

    private void abrirCrearCita() {
        Paciente p = controlador.getLoginPaciente();
        if (p == null) return;

        DialogCrearCita dialog = new DialogCrearCita(
                SwingUtilities.getWindowAncestor(this),
                p,
                controlador
        );
        dialog.setVisible(true);
    }

    private void toggleHistorial() {
        Paciente p = controlador.getLoginPaciente();
        if (p == null) return;

        try {
            controlador.cargarHistorialPaciente(p);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando historial del paciente",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        panelCentral.removeAll();

        if (!historialVisible) {
            panelCentral.add(new PanelPacienteHistorial(p), BorderLayout.CENTER);
        }

        historialVisible = !historialVisible;

        panelCentral.revalidate();
        panelCentral.repaint();
    }

}