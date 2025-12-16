package Main;


import backend.Agendas.AgendaCitas;
import backend.Agendas.AgendaConsultas;
import backend.Agendas.Pacientes;
import backend.Agendas.Plantilla;
import backend.Enumeradores.Centros;
import backend.Enumeradores.Especialidades;
import backend.Usuarios.Admin;
import backend.Usuarios.AdminCentroSalud;
import backend.Usuarios.Medico;
import backend.Usuarios.Paciente;
import interaciones.Controlador;
import interfaz.MainVentana;

public class Main {

    public static void main(String[] args) {

        Log.WARN("No hay archivo CSV");
        Log.INFO("Cargando usuarios manualmente...");

        //Inicializacion temporal.
        Plantilla plantilla = new Plantilla();
        Pacientes listaPacientes = new Pacientes();
        AgendaConsultas agendaConsultas = new AgendaConsultas();
        AgendaCitas citas = new AgendaCitas();

        Medico m1 = new Medico("11112222A", 101, Especialidades.CARDIOLOGIA, Centros.HOSPITAL_UNIVERSITARIO_DE_FUENLABRADA);
        Medico m2 = new Medico("33334444Z", 102, Especialidades.PEDIATRIA, Centros.HOSPITAL_UNIVERSITARIO_PRINCIPE_DE_ASTURIAS);
        Paciente pa1 = new Paciente("Laura Sánchez", "Calle Río 21", 612334455, "98765432L", 5001);
        Paciente pa2 = new Paciente("Pedro Martín", "Avenida Verde 13", 698765432, "11223344X", 5002);
        AdminCentroSalud administrador = new AdminCentroSalud("44556677M", 4001, Centros.HOSPITAL_UNIVERSITARIO_TORREJON);
        Admin admin = new Admin("84754928O", 5000);

        plantilla.agregarMedico(m1);
        plantilla.agregarMedico(m2);
        plantilla.agregarAdministradorCentro(administrador);
        plantilla.agregarAdministrador(admin);

        listaPacientes.agregarPaciente(pa1);
        listaPacientes.agregarPaciente(pa2);

        Controlador c = new Controlador(plantilla, listaPacientes, agendaConsultas, citas);

        Log.INFO("Usuarios en el sistema: ");
        System.out.println(c.stringGetUsuarios());

        // Iniciar Interfaz gráfica.
        new MainVentana(c).setVisible(true);

    }
}