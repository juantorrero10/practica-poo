package MainPruebas;

import Agendas.AgendaCitas;
import Agendas.AgendaConsultas;
import Agendas.Pacientes;
import Agendas.Plantilla;
import GestionHistorial.Consulta;
import Medicacion.Medicamento;
import Reestricion.Reestricion;
import Usuarios.*;
import Enumeradores.*;
import Citas.Cita;

import javax.management.InvalidAttributeValueException;
import java.rmi.AccessException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {

    private static void imprimirSeparador() {
        System.out.println("--------------------------------------------------------\n");
    }

    private static void SOP(Object o) {
        System.out.println(o);
    }

    private static void SEP(Object o) {
        System.err.println(o);
    }

    public static void main(String[] args) throws Exception {

        SOP(">>> PROGRAMA DE PRUEBA <<<");

        // Inicialización obligatoria del sistema
        Plantilla plantilla = new Plantilla();
        Pacientes listaPacientes = new Pacientes();
        AgendaConsultas agendaConsultas = new AgendaConsultas();
        AgendaCitas citas = new AgendaCitas();

        //Usuarios para probar la "autentificacion"
        Admin su = new Admin("00000000T", 0);
        Paciente p = new Paciente("Paciente prueba", null, 0, "1111111B", 1);
        Medico m = new Medico("2222222C", 2, Especialidades.GENERAL, Centros.HOSPITAL_UNIVERSITARIO_12_DE_OCTUBRE);
        AdminCentroSalud ac = new AdminCentroSalud("3333333D", 3, Centros.HOSPITAL_UNIVERSITARIO_DE_GETAFE);

        //"Autentificar": -> Comentar y descomentar codigo
        Admin u = su;
        //Paciente u = p;
        //Medico u = m;
        //AdminCentroSalud u = ac;

        SOP("\n[1] Preparando datos iniciales...");

        Medico m1 = new Medico("11112222A", 101, Especialidades.CARDIOLOGIA, Centros.HOSPITAL_UNIVERSITARIO_DE_FUENLABRADA);
        Medico m2 = new Medico("33334444Z", 102, Especialidades.PEDIATRIA, Centros.HOSPITAL_UNIVERSITARIO_PRINCIPE_DE_ASTURIAS);
        //Medico m3 = new Medico("77778888Z", 103, Especialidades.CARDIOLOGIA, Centros.HOSPITAL_UNIVERSITARIO_HENARES);
        plantilla.agregarMedico(u, m1);
        plantilla.agregarMedico(u, m2);
        //plantilla.agregarMedico(u, m3);


        Paciente pa1 = new Paciente("Laura Sánchez", "Calle Río 21", 612334455, "98765432L", 5001);
        Paciente pa2 = new Paciente("Pedro Martín", "Avenida Verde 13", 698765432, "11223344X", 5002);
        listaPacientes.agregarPaciente(u, pa1);
        listaPacientes.agregarPaciente(u, pa2);

        AdminCentroSalud administrador = new AdminCentroSalud("44556677M", 4001, Centros.HOSPITAL_UNIVERSITARIO_TORREJON);
        plantilla.agregarAdministradorCentro(u, administrador);

        SOP("Usuarios registrados correctamente.\n");

        imprimirSeparador();
        SOP("[2] Prueba asignacion citas\n");
        SOP(">>  Intentar asignar más de 20 citas\n");
        for (int i = 0; i < 21; i++) {
            Cita c = pa1.asignarCitaAutomatica(u, LocalDate.now(), Especialidades.CARDIOLOGIA, plantilla);
            if (c == null) {
                SEP("No se pudo asignar la cita " + i);
            } else {
                citas.agregarCita(u, c, 20);
            }
        }

        //Imprimir Citas
        SOP(citas);

        //Crear consultas y su historial.
        imprimirSeparador();
        SOP("[3]: Creacion consultas\n");
        // "Avanzar" dos días.
        SOP("\"Pasan\" 2 dias: " + LocalDate.now() + " -> " + LocalDate.now().plusDays(2));
        LocalDate hoy =  LocalDate.now().plusDays(2);


        // El propio medico debe rellenar informacion extra para completar el objeto consulta.
        Cita c1 = citas.getCitas(u).get(0);
        Cita c2 = citas.getCitas(u).get(1);
        Cita c3 = citas.getCitas(u).get(2);
        Scanner sc = new Scanner(System.in);
        Medicamento ibuprofenoLaura = new Medicamento("Ibuprofeno",
                10, 2,
                TipoPreescripcion.PUNTUAL
                , null, null);

        //Completar con parametros
        pa1.agregarAlHistorial(u,
                Consulta.completarConsulta(
                        u, c1, "Revision hipertension", TipoInforme.SEGUIMIENTO,
                        TipoConsulta.PRESENCIAL,
                        Centros.HOSPITAL_UNIVERSITARIO_12_DE_OCTUBRE));

        //Añadir al segundo aunque se haya creado para el primero
        //para probrar algo mas tarde.
        pa2.agregarAlHistorial(u,
                Consulta.completarConsulta(
                        u, c1, "Ataque", TipoInforme.ALTA,
                        TipoConsulta.PRESENCIAL,
                        Centros.HOSPITAL_UNIVERSITARIO_12_DE_OCTUBRE));

        //Completar con dialogo.
        pa1.agregarAlHistorial(u,
                Consulta.completarConsultaDialogo(u, c2, sc));

        //Recetar un medicamenteo
        pa1.getHistorial().getConsultas().get(1).recetarMedicamento(u, ibuprofenoLaura);

        //Imprimir consultas
        agendaConsultas.sincronizar(u, listaPacientes);
        SOP("==== AGENDA GENERAL ====");
        SOP(agendaConsultas);

        //Borrar Citas
        citas.eliminarCitasPasadas(u, hoy);
        SOP("Citas restantes: ");
        SOP(citas);

        imprimirSeparador();
        SOP("[4]: Consultas de cada paciente\n");
        SOP("Consultas de " + pa1.getNombreCompleto() + "\n");
        SOP(pa1.getHistorial().consultasToString());
        SOP("Consultas de " + pa2.getNombreCompleto() + "\n");
        SOP(pa2.getHistorial().consultasToString());

    }
}