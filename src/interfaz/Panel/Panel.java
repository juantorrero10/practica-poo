package interfaz.Panel;

import Controlador.Controlador;
import Controlador.TipoUsuario;
import backend.Usuarios.Paciente;
import backend.Usuarios.Usuario;
import interfaz.Dialog.DialogOpcionesUsuario;

import javax.swing.*;
import java.awt.*;

public abstract class Panel extends JPanel {

    protected Controlador controlador;

    // ---- Elementos comunes ----
    protected boolean hasBarraSuperior = false;

    protected JLabel labelUsuario = null;
    protected JButton botonCerrarSesion = null;

    public Panel(String NombrePanel, Controlador controlador) {
        setLayout(new BorderLayout());
        this.controlador = controlador;
    }

    protected JPanel crearBarraUsuario(TipoUsuario tp) {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1),
                        BorderFactory.createEmptyBorder(4, 15, 4, 15)
                )
        );

        barra.setPreferredSize(new Dimension(0, 50));

        // ---- IZQUIERDA (info usuario) ----
        labelUsuario = new JLabel();
        labelUsuario.setFont(labelUsuario.getFont().deriveFont(Font.PLAIN, 13f));
        barra.add(labelUsuario, BorderLayout.WEST);

        // ---- DERECHA (opciones + cerrar sesión) ----
        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JButton botonOpciones = new JButton("Opciones");
        botonOpciones.setFocusPainted(false);
        botonOpciones.addActionListener(e -> abrirDialogoOpciones(tp));

        botonCerrarSesion = new JButton("Cerrar sesión");
        botonCerrarSesion.setForeground(Color.RED);
        botonCerrarSesion.setFocusPainted(false);
        botonCerrarSesion.setBorder(
                BorderFactory.createLineBorder(Color.RED, 1)
        );

        botonCerrarSesion.addActionListener(e -> {
            limpiarPanel();
            controlador.cerrarSesion(tp);
        });

        derecha.add(botonOpciones);
        derecha.add(botonCerrarSesion);

        barra.add(derecha, BorderLayout.EAST);
        return barra;
    }

    public void actualizarLabelUsuario(Usuario u) {
        if (!hasBarraSuperior || labelUsuario == null) return;

        if (u == null) {
            labelUsuario.setText("");
            return;
        }

        StringBuilder texto = new StringBuilder("<html>");
        texto.append("<span style='color:black;'>Usuario → </span>");
        texto.append("<span style='color:#2e7d32;'>");

        if (u instanceof Paciente pac) {
            texto.append(pac.getNombreCompleto()).append(" · ");
        }

        texto.append("DNI: ").append(u.getDNI());
        texto.append(" · CIPA: ").append(u.getCIPA());
        texto.append("</span></html>");

        labelUsuario.setText(texto.toString());
    }

    private void abrirDialogoOpciones(TipoUsuario tp) {
        Usuario u = controlador.getLoginUsuario(tp);
        if (u == null) return;

        DialogOpcionesUsuario dialogo =
                new DialogOpcionesUsuario(
                        SwingUtilities.getWindowAncestor(this),
                        u,
                        controlador
                );

        dialogo.setVisible(true);
    }

    protected void limpiarPanel() {
        removeAll();                // borra TODOS los componentes
        labelUsuario = null;
        botonCerrarSesion = null;
        hasBarraSuperior = false;

        revalidate();               // recalcula layout
        repaint();                  // repinta
    }
}

