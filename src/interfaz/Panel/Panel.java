package interfaz.Panel;

import Controlador.Controlador;
import Controlador.TipoUsuario;
import backend.Usuarios.Paciente;
import backend.Usuarios.Usuario;

import javax.swing.*;
import java.awt.*;

public abstract class Panel extends JPanel {

    protected Controlador controlador;

    // ----  Elementos comunes ---

    //Barra superior
    protected boolean hasBarraSuperior = false;
    private JLabel labelUsuario = null;
    private JButton botonCerrarSesion = null;

    public Panel(String NombrePanel, Controlador controlador) {
        setLayout(new BorderLayout());
        this.controlador = controlador;
    }

    protected void crearBarraUsuario(TipoUsuario tp) {
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

        // ---- DERECHA (cerrar sesión) ----
        botonCerrarSesion = new JButton("Cerrar sesión");
        botonCerrarSesion.setForeground(Color.RED);
        botonCerrarSesion.setFocusPainted(false);
        botonCerrarSesion.setBorder(
                BorderFactory.createLineBorder(Color.RED, 1)
        );

        botonCerrarSesion.addActionListener(e -> controlador.cerrarSesion(tp));

        barra.add(botonCerrarSesion, BorderLayout.EAST);

        add(barra, BorderLayout.NORTH);

    }

    public void actualizarLabelUsuario(Usuario u) {
        Paciente pac;

        if (!hasBarraSuperior) { return; }

        if (u == null) {
            labelUsuario.setText("");
            return;
        }

        StringBuilder texto = new StringBuilder("<html>");
        texto.append("<span style='color:black;'>Usuario → </span>");
        texto.append("<span style='color:#2e7d32;'>");

        if (u instanceof Paciente) {
            pac = (Paciente) u;
            texto.append(pac.getNombreCompleto()).append(" · ");
        }

        texto.append("DNI: ").append(u.getDNI());
        texto.append(" · CIPA: ").append(u.getCIPA());
        texto.append("</span></html>");

        labelUsuario.setText(texto.toString());
    }

}
