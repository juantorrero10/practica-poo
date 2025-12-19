package interfaz.Panel;

import Controlador.Controlador;
import Controlador.TipoUsuario;
import backend.Usuarios.Medico;
import backend.Usuarios.Paciente;
import backend.Usuarios.Usuario;
import backend.Enumeradores.Especialidades;
import backend.Citas.Cita;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PanelCrearCita extends JPanel {
    private Controlador controlador;
    private TipoUsuario tipoUsuario;

    private JComboBox<Especialidades> comboEspecialidad; // Solo para paciente
    private JComboBox<Paciente> comboPaciente;           // Solo para médico
    private JTextField campoFecha;
    private JTextField campoHora;
    private JTextArea campoMotivo;
    private JButton botonCrear;
    private JLabel labelResultado;

    public PanelCrearCita(Controlador c, TipoUsuario tipoUsuario) {
        this.controlador = c;
        this.tipoUsuario = tipoUsuario;

        setLayout(new BorderLayout());
        inicializarComponentes();
        layoutComponentes();
        registrarEventos();
    }

    private void inicializarComponentes() {
        campoFecha = new JTextField("YYYY-MM-DD", 10);
        campoHora = new JTextField("HH:MM", 5);

        campoMotivo = new JTextArea(3, 20);
        campoMotivo.setLineWrap(true);
        campoMotivo.setWrapStyleWord(true);

        botonCrear = new JButton("Crear cita");
        labelResultado = new JLabel(" ");
        labelResultado.setForeground(Color.RED);

        // Paciente: muestra combo de especialidad
        if (tipoUsuario == TipoUsuario.PACIENTE) {
            comboEspecialidad = new JComboBox<>(Especialidades.values());
            comboEspecialidad.removeItem(Especialidades.NO_ESPECIFICADO);
        }

        // Médico: combo de paciente
        if (tipoUsuario == TipoUsuario.MEDICO) {
            comboPaciente = new JComboBox<>();
            for (Paciente p : controlador.getListaPacientes().getPacientes()) {
                comboPaciente.addItem(p);
            }
        }
    }

    private void layoutComponentes() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        // Especialidad solo para pacientes
        if (tipoUsuario == TipoUsuario.PACIENTE) {
            gbc.gridx = 0; gbc.gridy = y;
            form.add(new JLabel("Especialidad:"), gbc);
            gbc.gridx = 1;
            form.add(comboEspecialidad, gbc);
            y++;
        }

        // Paciente solo para médico
        if (tipoUsuario == TipoUsuario.MEDICO) {
            gbc.gridx = 0; gbc.gridy = y;
            form.add(new JLabel("Paciente:"), gbc);
            gbc.gridx = 1;
            form.add(comboPaciente, gbc);
            y++;
        }

        // Fecha
        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        form.add(campoFecha, gbc);
        y++;

        // Hora
        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Hora:"), gbc);
        gbc.gridx = 1;
        form.add(campoHora, gbc);
        y++;

        // Motivo
        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 1;
        form.add(new JScrollPane(campoMotivo), gbc);
        y++;

        // Botón
        gbc.gridx = 0; gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(botonCrear, gbc);
        y++;

        // Label resultado
        form.add(labelResultado, gbc);

        add(form, BorderLayout.CENTER);
    }

    private void registrarEventos() {
        botonCrear.addActionListener(e -> crearCita());
    }

    private void crearCita() {
        labelResultado.setText(" ");
        labelResultado.setForeground(Color.RED);

        Usuario u = controlador.getLoginUsuario(tipoUsuario);
        if (u == null) {
            labelResultado.setText("No hay sesión activa");
            return;
        }

        LocalDate fecha;
        LocalTime hora;

        try {
            fecha = LocalDate.parse(campoFecha.getText().trim());
            hora = LocalTime.parse(campoHora.getText().trim());
        } catch (Exception ex) {
            labelResultado.setText("Fecha u hora inválidas");
            return;
        }

        if (hora.getMinute() % 30 != 0) {
            labelResultado.setText("La hora debe ser en tramos de 30 minutos");
            return;
        }

        Paciente paciente;
        Medico medico;

        if (tipoUsuario == TipoUsuario.PACIENTE) {
            paciente = controlador.getLoginPaciente();
            Especialidades esp = (Especialidades) comboEspecialidad.getSelectedItem();
            medico = controlador.getPlantilla().encontrarEspecilistaAleatorio(esp);
            if (medico == null) {
                labelResultado.setText("No hay médicos disponibles para esta especialidad");
                return;
            }
        } else if (tipoUsuario == TipoUsuario.MEDICO) {
            medico = (Medico) u;
            paciente = (Paciente) comboPaciente.getSelectedItem();
            if (paciente == null) {
                labelResultado.setText("Debe seleccionar un paciente");
                return;
            }
        } else {
            labelResultado.setText("Usuario no autorizado");
            return;
        }

        boolean ok = controlador.crearCita(
                paciente,
                medico,
                LocalDateTime.of(fecha, hora)
        );

        if (!ok) {
            labelResultado.setText("No se pudo crear la cita");
            return;
        }

        labelResultado.setForeground(new Color(0,128,0));
        labelResultado.setText("Cita creada correctamente");
    }
}
