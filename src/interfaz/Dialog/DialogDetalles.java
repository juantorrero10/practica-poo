package interfaz.Dialog;



import javax.swing.*;
import java.awt.*;

public class DialogDetalles extends JDialog {

    private final String detalles;
    private final String titulo;

    private JLabel labelTitulo;
    private JTextArea areaDetalles;
    private JButton botonCerrar;

    public DialogDetalles(Window owner, String detalles, String titulo) {
        super(owner, titulo, ModalityType.APPLICATION_MODAL);

        this.detalles = detalles;
        this.titulo = titulo;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(owner);

        inicializarComponentes();
        layoutComponentes();
        registrarEventos();
    }

    private void inicializarComponentes() {
        labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(labelTitulo.getFont().deriveFont(Font.BOLD, 18f));
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        areaDetalles = new JTextArea(detalles);
        areaDetalles.setEditable(false);
        areaDetalles.setLineWrap(true);
        areaDetalles.setWrapStyleWord(true);
        areaDetalles.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaDetalles.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );

        botonCerrar = new JButton("Cerrar");
        botonCerrar.setFocusPainted(false);
    }

    private void layoutComponentes() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        add(labelTitulo, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        add(new JScrollPane(areaDetalles), gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;

        add(botonCerrar, gbc);
    }

    private void registrarEventos() {
        botonCerrar.addActionListener(e -> dispose());
    }
}
