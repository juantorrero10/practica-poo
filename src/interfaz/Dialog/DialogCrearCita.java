package interfaz.Dialog;

import CSV.CitasCSV;
import Controlador.*;
import backend.Enumeradores.Especialidades;
import backend.Usuarios.Paciente;
import backend.Usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DialogCrearCita extends JDialog {

    private Controlador controlador;
    private TipoUsuario tipoUsuario;
    private Usuario usuario;

    // Campos comunes
    private JLabel labelFecha;
    private JLabel labelHora;
    private JTextField campoFecha;
    private JTextField campoHora;
    private JLabel labelAclaracion;
    private JButton botonCrear;
    private JLabel labelError;

    // Campos específicos
    private JLabel labelEspecialidad;
    private JComboBox<Especialidades> comboEspecialidad;

    private JLabel labelPaciente;
    private JComboBox<Paciente> comboPaciente;

    public DialogCrearCita(Window owner, Usuario u, Controlador c) {
        super(owner, "Crear Cita", ModalityType.APPLICATION_MODAL);

        this.usuario = u;
        this.controlador = c;
        this.tipoUsuario = u.getTipoUsuario();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(owner);

        inicializarComponentes();
        layoutComponentes();
        registrarEventos();
    }

    private void inicializarComponentes() {
        campoFecha = new JTextField("YYYY-MM-DD", 10);
        campoHora = new JTextField("HH:MM", 5);
        botonCrear = new JButton("Crear");
        labelError = new JLabel("", SwingConstants.CENTER);
        labelError.setForeground(Color.RED);
        labelError.setFont(labelError.getFont().deriveFont(Font.PLAIN, 11f));
        labelError.setPreferredSize(new Dimension(300, 15));

        labelFecha = new JLabel("Fecha:");
        labelFecha.setFont(labelError.getFont().deriveFont(Font.BOLD, 14f));

        labelHora = new JLabel("Hora:");
        labelHora.setFont(labelError.getFont().deriveFont(Font.BOLD, 14f));

        labelAclaracion = new JLabel("Si se dejan en blanco se asignará una automática");
        labelAclaracion.setHorizontalAlignment(SwingConstants.CENTER);
        labelAclaracion.setFont(labelError.getFont().deriveFont(Font.PLAIN, 12f));

        if (tipoUsuario == TipoUsuario.PACIENTE) {
            labelEspecialidad = new JLabel("Especialidad:");
            labelEspecialidad.setFont(labelError.getFont().deriveFont(Font.BOLD, 14f));
            comboEspecialidad = new JComboBox<>(Especialidades.values());
        } else if (tipoUsuario == TipoUsuario.MEDICO) {
            labelPaciente = new JLabel("Paciente:");
            labelPaciente.setFont(labelError.getFont().deriveFont(Font.BOLD, 14f));
            comboPaciente = new JComboBox<>(
                    controlador.getListaPacientes().getPacientes().toArray(new Paciente[0])
            );
        }
    }

    private void layoutComponentes() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 8, 7, 8);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;

        if (tipoUsuario == TipoUsuario.PACIENTE) {
            add(labelEspecialidad, c); c.gridy++;
            add(comboEspecialidad, c); c.gridy++;
        } else if (tipoUsuario == TipoUsuario.MEDICO) {
            add(labelPaciente, c); c.gridy++;
            add(comboPaciente, c); c.gridy++;
        }

        add(labelFecha, c); c.gridy++;
        add(campoFecha, c); c.gridy++;
        add(labelHora, c); c.gridy++;
        add(campoHora, c); c.gridy++;
        add(labelAclaracion, c); c.gridy++;
        add(botonCrear, c); c.gridy++;
        add(labelError, c); c.gridy++;
    }

    private void setLabelError(String msg, Color c) {
        labelError.setForeground(c);
        labelError.setText(msg);
    }

    private void registrarEventos() {
        botonCrear.addActionListener(e -> {
            try {
                crearCita();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void crearCita() throws IOException {
        setLabelError("", Color.RED);

        LocalDate fecha;
        LocalTime hora;
        try {
            if (campoFecha.getText().isEmpty() || campoHora.getText().isEmpty() ||
                    (campoFecha.getText()).equals("YYYY-MM-DD") && campoHora.getText().equals("HH:MM")) {
                fecha = LocalDate.now();
                hora = LocalTime.now();
            } else {
                fecha = LocalDate.parse(campoFecha.getText().trim());
                hora = LocalTime.parse(campoHora.getText().trim());
            }
        } catch (Exception ex) {
            setLabelError("Fecha u hora inválidas", Color.RED);
            return;
        }

        LocalDateTime fh = LocalDateTime.of(fecha, hora);

        if (tipoUsuario == TipoUsuario.PACIENTE) {
            Especialidades especialidad = (Especialidades) comboEspecialidad.getSelectedItem();
            int res = controlador.crearCitaPaciente(especialidad, fh);
            mostrarResultado(res);
        } else if (tipoUsuario == TipoUsuario.MEDICO) {
            Especialidades especialidad = controlador.getLoginMedico().getEspecialidad();
            Paciente paciente = (Paciente) comboPaciente.getSelectedItem();
            int res = controlador.crearCitaMedico(paciente, especialidad, fh);
            mostrarResultado(res);
        }

        controlador.exportarCitas();
    }

    private void mostrarResultado(int res) {
        switch (res) {
            case 0: setLabelError("Cita creada", Color.GREEN); break;
            case 1: setLabelError("Especialidad incorrecta", Color.RED); break;
            case 2: setLabelError("La fecha y hora no están disponibles", Color.RED); break;
            case 3: setLabelError("El médico ha alcanzado el máximo de citas", Color.RED); break;
            case 4:
            default: setLabelError("No hay médicos disponibles", Color.RED); break;
        }
    }
}

