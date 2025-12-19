package interfaz.Dialog;

import Controlador.Controlador;
import backend.Citas.Cita;
import backend.Enumeradores.*;
import backend.GestionHistorial.Consulta;
import backend.Medicacion.Medicamento;

import javax.management.InvalidAttributeValueException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DialogFinalizarConsulta extends JDialog {

    private final Controlador controlador;
    private final Cita cita;

    private JLabel labelTitulo;
    private JTextArea campoMotivo;
    private JComboBox<TipoConsulta> comboTipoConsulta;
    private JComboBox<TipoInforme> comboTipoInforme;
    private JTextField campoRutaInforme;

    // Panel contenedor para prescripción
    private JPanel panelPrescripcion;
    private JCheckBox checkRecetar;
    private JRadioButton radioMedicamento;
    private JRadioButton radioVacuna;

    private JTextField campoNombre;
    private JTextField campoDosis;
    private JTextField campoFrecuencia;
    private JComboBox<TipoPreescripcion> comboTipoPresc;
    private JTextField campoFechaInicio;
    private JTextField campoFechaFin;
    private JTextField campoFechaSgteDosis;

    private JButton botonFinalizar;
    private JButton botonTogglePrescripcion;
    private JLabel labelError;

    public DialogFinalizarConsulta(Window owner, Controlador controlador, Cita cita) throws InvalidAttributeValueException {
        super(owner, "Finalizar consulta", ModalityType.APPLICATION_MODAL);
        this.controlador = controlador;
        this.cita = cita;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(520, 720);
        setLocationRelativeTo(owner);

        inicializarComponentes();
        layoutComponentes();
        registrarEventos();
    }

    private void inicializarComponentes() {
        labelTitulo = new JLabel("Finalizar y añadir al historial");
        labelTitulo.setFont(labelTitulo.getFont().deriveFont(Font.BOLD, 18f));
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        campoMotivo = new JTextArea(4, 20);
        campoMotivo.setLineWrap(true);
        campoMotivo.setWrapStyleWord(true);

        comboTipoConsulta = new JComboBox<>(TipoConsulta.values());
        comboTipoInforme = new JComboBox<>(TipoInforme.values());

        campoRutaInforme = new JTextField(20);

        // Panel prescripción
        panelPrescripcion = new JPanel(new GridBagLayout());
        panelPrescripcion.setVisible(false);

        checkRecetar = new JCheckBox("Recetar medicamento / vacuna");

        radioMedicamento = new JRadioButton("Medicamento", true);
        radioVacuna = new JRadioButton("Vacuna");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(radioMedicamento);
        grupo.add(radioVacuna);

        campoNombre = new JTextField(20);
        campoDosis = new JTextField(5);
        campoFrecuencia = new JTextField(5);
        comboTipoPresc = new JComboBox<>(TipoPreescripcion.values());
        campoFechaInicio = new JTextField("YYYY-MM-DD", 10);
        campoFechaFin = new JTextField("YYYY-MM-DD", 10);
        campoFechaSgteDosis = new JTextField("YYYY-MM-DD", 10);

        botonTogglePrescripcion = new JButton("Mostrar/Ocultar prescripción");
        botonTogglePrescripcion.setFocusPainted(false);

        botonFinalizar = new JButton("Finalizar consulta");
        botonFinalizar.setFocusPainted(false);
        botonFinalizar.setBackground(new Color(66, 133, 244));
        botonFinalizar.setForeground(Color.WHITE);

        labelError = new JLabel(" ");
        labelError.setForeground(Color.RED);
        labelError.setFont(labelError.getFont().deriveFont(Font.PLAIN, 11f));
    }

    private void layoutComponentes() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; add(labelTitulo, gbc);
        gbc.gridy++; add(new JLabel("Motivo de la consulta:"), gbc);
        gbc.gridy++; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.3;
        add(new JScrollPane(campoMotivo), gbc);

        gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy++; add(new JLabel("Tipo de consulta:"), gbc);
        gbc.gridy++; add(comboTipoConsulta, gbc);
        gbc.gridy++; add(new JLabel("Tipo de informe:"), gbc);
        gbc.gridy++; add(comboTipoInforme, gbc);
        gbc.gridy++; add(new JLabel("Ruta del informe (opcional):"), gbc);
        gbc.gridy++; add(campoRutaInforme, gbc);

        // Botón toggle
        gbc.gridy++; add(botonTogglePrescripcion, gbc);

        // Layout panel prescripción
        GridBagConstraints pgbc = new GridBagConstraints();
        pgbc.insets = new Insets(4, 4, 4, 4);
        pgbc.gridx = 0; pgbc.gridy = 0; pgbc.fill = GridBagConstraints.HORIZONTAL;

        panelPrescripcion.add(checkRecetar, pgbc); pgbc.gridy++;
        panelPrescripcion.add(radioMedicamento, pgbc); pgbc.gridy++;
        panelPrescripcion.add(radioVacuna, pgbc); pgbc.gridy++;
        panelPrescripcion.add(new JLabel("Nombre:"), pgbc); pgbc.gridy++;
        panelPrescripcion.add(campoNombre, pgbc); pgbc.gridy++;
        panelPrescripcion.add(new JLabel("Dosis:"), pgbc); pgbc.gridy++;
        panelPrescripcion.add(campoDosis, pgbc); pgbc.gridy++;
        panelPrescripcion.add(new JLabel("Frecuencia (veces/día):"), pgbc); pgbc.gridy++;
        panelPrescripcion.add(campoFrecuencia, pgbc); pgbc.gridy++;
        panelPrescripcion.add(new JLabel("Tipo prescripción:"), pgbc); pgbc.gridy++;
        panelPrescripcion.add(comboTipoPresc, pgbc); pgbc.gridy++;
        panelPrescripcion.add(new JLabel("Fecha inicio:"), pgbc); pgbc.gridy++;
        panelPrescripcion.add(campoFechaInicio, pgbc); pgbc.gridy++;
        panelPrescripcion.add(new JLabel("Fecha fin:"), pgbc); pgbc.gridy++;
        panelPrescripcion.add(campoFechaFin, pgbc); pgbc.gridy++;
        panelPrescripcion.add(new JLabel("Fecha siguiente dosis (solo vacuna):"), pgbc); pgbc.gridy++;
        panelPrescripcion.add(campoFechaSgteDosis, pgbc); pgbc.gridy++;

        gbc.gridy++; add(panelPrescripcion, gbc);

        gbc.gridy++; gbc.anchor = GridBagConstraints.CENTER; add(botonFinalizar, gbc);
        gbc.gridy++; add(labelError, gbc);
    }

    private void registrarEventos() throws InvalidAttributeValueException {
        botonFinalizar.addActionListener(e -> {
            try {
                finalizarConsulta();
            } catch (InvalidAttributeValueException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        botonTogglePrescripcion.addActionListener(e -> {
            panelPrescripcion.setVisible(!panelPrescripcion.isVisible());
            this.revalidate();
            this.repaint();
        });
    }

    private void setLabelError(String msg, Color c) {
        labelError.setText(msg);
        labelError.setForeground(c);
    }

    private void finalizarConsulta() throws InvalidAttributeValueException, IOException {
        setLabelError("", Color.RED);
        String motivo = campoMotivo.getText().trim();
        if (motivo.isEmpty()) { setLabelError("El motivo no puede estar vacío", Color.RED); return; }

        Consulta consulta = Consulta.completarConsulta(
                cita,
                motivo,
                (TipoInforme) comboTipoInforme.getSelectedItem(),
                (TipoConsulta) comboTipoConsulta.getSelectedItem(),
                controlador.getLoginMedico().getCentro()
        );

        String ruta = campoRutaInforme.getText().trim();
        if (!ruta.isEmpty()) consulta.setRutaInforme(ruta);

        // Crear medicamento solo si se marca la casilla
        if (checkRecetar.isSelected()) {

            Medicamento presc = controlador.crearPrescripcion(
                    radioVacuna.isSelected(),
                    campoNombre.getText(),
                    campoDosis.getText(),
                    campoFrecuencia.getText(),
                    (TipoPreescripcion) comboTipoPresc.getSelectedItem(),
                    campoFechaInicio.getText(),
                    campoFechaFin.getText(),
                    campoFechaSgteDosis.getText()
            );
            consulta.recetarMedicamento(presc);
        }

        controlador.finalizarCitaYAgregarConsulta(cita, consulta);
        dispose();
    }
}
