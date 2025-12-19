package interfaz.Dialog;

import Controlador.*;
import backend.Citas.Cita;
import backend.Usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DialogAnularCita extends JDialog {

    private final TipoUsuario tp;
    private final Controlador c;
    private final Cita cita;

    private JLabel labelAnular;
    private JLabel labelMotivo;
    private JTextArea textMotivo;

    private JLabel labelError;
    private JButton botonAnular;

    public DialogAnularCita(Window owner, Controlador c, TipoUsuario tp, Cita cita) {
        super(owner, "Anular cita", ModalityType.APPLICATION_MODAL);

        this.cita = cita;
        this.c = c;
        this.tp = tp;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(owner);

        inicializarComponentes();
        layoutComponentes();
        registrarEventos();
    }

    // =========================
    // Inicializar componentes
    // =========================
    private void inicializarComponentes() {
        labelAnular = new JLabel("Anular Cita");
        labelAnular.setFont(labelAnular.getFont().deriveFont(Font.BOLD, 20f));
        labelAnular.setHorizontalAlignment(SwingConstants.CENTER);

        labelMotivo = new JLabel("Motivo:");

        textMotivo = new JTextArea(6, 20);
        textMotivo.setLineWrap(true);
        textMotivo.setWrapStyleWord(true);

        labelError = new JLabel(" ");
        labelError.setForeground(Color.RED);
        labelError.setFont(labelError.getFont().deriveFont(Font.PLAIN, 11f));

        botonAnular = new JButton("Anular");
        botonAnular.setForeground(Color.RED);
        botonAnular.setFocusPainted(false);
        botonAnular.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
    }

    // =========================
    // Layout (una sola columna)
    // =========================
    private void layoutComponentes() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridy = 0;
        add(labelAnular, gbc);

        // Motivo
        gbc.gridy = 1;
        add(labelMotivo, gbc);

        // TextArea grande
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JScrollPane scrollMotivo = new JScrollPane(textMotivo);
        add(scrollMotivo, gbc);

        // Error
        gbc.gridy = 3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(labelError, gbc);

        // Botón Anular
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botonAnular, gbc);
    }

    // =========================
    // Eventos
    // =========================
    private void registrarEventos() {
        botonAnular.addActionListener(e -> {
            try {
                anularCita();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    // =========================
    // Error helper
    // =========================
    private void setLabelError(String text, Color c) {
        labelError.setText(text);
        labelError.setForeground(c);
    }

    // =========================
    // Lógica (la implementarás tú)
    // =========================
    private void anularCita() throws IOException {
        String mot = textMotivo.getText();
        if (mot.isEmpty()) {
            setLabelError("El motivo no puede estar vacio.", Color.RED);
            return;
        }


        switch(c.anularCita(cita, mot)) {
            case 1:
                setLabelError("Ha ocurrido un error al cancelar la cita", Color.RED);
                return;
            case 2:
                setLabelError("La cita ya esta cancelada.", Color.RED);
                return;
            case 0:
                setLabelError("La cita se ha cancelado", Color.GREEN);
                break;
            default:
                setLabelError("Error desconocido", Color.RED);
                break;
        }

        c.exportarCitas();

    }
}

