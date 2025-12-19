package interfaz.Dialog;

import Controlador.*;
import backend.Enumeradores.Especialidades;
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

    private JLabel labelEspecialidad;
    private JComboBox<Especialidades> comboEspecialidad;
    private JLabel labelFecha;
    private JLabel labelHora;
    private JTextField campoFecha;
    private JTextField campoHora;
    private JLabel labelAclaracion;
    private JButton botonCrear;
    private JLabel labelError;

    public DialogCrearCita(Window owner, Usuario u, Controlador c) {
        super(owner, "Opciones de usuario", ModalityType.APPLICATION_MODAL);

        this.usuario = u;
        this.controlador = c;
        this.tipoUsuario = u.getTipoUsuario();

        int width = 500;


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(width, 350);
        setLocationRelativeTo(owner);


        inicizializarComponentes();
        layoutComponentes();
        registrarEventos();
    }

    public void inicizializarComponentes() {
        comboEspecialidad = new JComboBox<>(Especialidades.values());
        campoFecha = new JTextField("YYYY-MM-DD", 10);
        campoHora = new JTextField("HH:MM", 5);
        botonCrear = new JButton("Crear");
        labelError = new JLabel();
        labelError.setHorizontalAlignment(SwingConstants.CENTER);
        labelError.setForeground(Color.RED);
        labelError.setFont(labelError.getFont().deriveFont(Font.PLAIN, 11f));
        labelError.setPreferredSize(new Dimension(300, 15));
        labelFecha = new JLabel("Fecha:");
        labelFecha.setFont(labelError.getFont().deriveFont(Font.BOLD, 14f));

        labelHora = new JLabel("Hora:");   // CORRECTO
        labelHora.setFont(labelError.getFont().deriveFont(Font.BOLD, 14f));

        labelEspecialidad = new JLabel("Especialidad:");
        labelEspecialidad.setFont(labelError.getFont().deriveFont(Font.BOLD, 14f));
        labelAclaracion = new JLabel("Si se dejan en blanco se asignará una automática");
        labelAclaracion.setHorizontalAlignment(SwingConstants.CENTER);
        labelAclaracion.setFont(labelError.getFont().deriveFont(Font.PLAIN, 12f));
    }

    public void layoutComponentes() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.insets = new Insets(5, 8, 7, 8);
        c.gridy = 0;
        c.gridx = 0;
        c.anchor = GridBagConstraints.NORTH;

        add(labelEspecialidad, c); c.gridy++;
        add(comboEspecialidad, c); c.gridy++;
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
        Especialidades especialidad = (Especialidades) comboEspecialidad.getSelectedItem();

        LocalDate fecha;
        LocalTime hora;

        try {
            if (campoFecha.getText().isEmpty() || campoHora.getText().isEmpty() ||
                    (campoFecha.getText().equals("YYYY-MM-DD") && campoHora.getText().equals("HH:MM"))) {
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

        switch (controlador.crearCitaPaciente(especialidad, fh)) {
            case 0:
                setLabelError("Cita creada", Color.GREEN);
                break;
            case 1:
                setLabelError("Especialidad incorrecta", Color.RED);
                break;
            case 2:
                setLabelError("La fecha y hora no estan disponibles", Color.RED);
                break;
            case 3:
                setLabelError("El médico ha alcanzado el máximo de citas", Color.RED);
                break;
            case 4:
            default:
                setLabelError("No hay medicos disponibles", Color.RED);
                break;
        }


    }


}
