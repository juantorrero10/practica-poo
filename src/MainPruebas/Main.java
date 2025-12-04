import Usuarios.*;
import Enumerados.*;
import Citas.Cita;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

void main() {

    System.out.println(">>> INICIANDO SISTEMA DE PRUEBAS <<<");

    // Inicialización obligatoria del sistema
    new Plantilla();
    new ListaPaciente();

    // ===== creación de usuarios =====
    System.out.println("\n[1] Preparando datos iniciales...");

    Medico m1 = new Medico("11112222A", 101, Especialidades.CARDIOLOGIA, Centros.HOSPITAL_UNIVERSITARIO_DE_FUENLABRADA);
    Medico m2 = new Medico("33334444Z", 102, Especialidades.PEDIATRIA, Centros.HOSPITAL_UNIVERSITARIO_PRINCIPE_DE_ASTURIAS);

    Paciente pa1 = new Paciente("Laura Sánchez", "Calle Río 21", 612334455, "98765432L", 5001);
    Paciente pa2 = new Paciente("Pedro Martín", "Avenida Verde 13", 698765432, "11223344X", 5002);

    AdminCentroSalud administrador = new AdminCentroSalud("44556677M", 4001, Centros.HOSPITAL_UNIVERSITARIO_TORREJON);

    System.out.println("Usuarios registrados correctamente.\n");

    // ===== pruebas administrador =====
    System.out.println("[2] Comprobando funciones del administrador...");
    System.out.println("Paciente original: " + pa1);

    administrador.setNombre(pa1, "Laura Beatriz Sánchez");
    administrador.setDireccion(pa1, "Camino Alto 77");
    administrador.setDNI(pa1, "00998877Q");
    administrador.setCIPA(pa1, 5999);

    System.out.println("Paciente tras modificaciones administrativas: " + pa1 + "\n");

    // ===== pruebas médico =====
    System.out.println("[3] Verificación de agenda del médico...");

    LocalDateTime d1 = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(8, 30));
    LocalDateTime d2 = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(9, 15));

    Cita ct1 = new Cita(d1, pa1, m1);
    Cita ct2 = new Cita(d2, pa2, m1);

    System.out.println("Citas programadas para el médico seleccionado:");
    m1.visualizarCitas();

    LocalDate diaReagenda = LocalDate.now().plusDays(3);
    Cita ct3 = new Cita(LocalDateTime.of(diaReagenda, LocalTime.of(10, 45)), pa1, m1);

    System.out.println("Intentando reagendar citas al día: " + diaReagenda);
    System.out.println("Resultado del proceso: " + m1.reagendarCitas(diaReagenda) + "\n");

    // ===== pruebas paciente =====
    System.out.println("[4] Probando acciones del paciente...");

    System.out.println("Solicitud de cita por parte del paciente: " + pa1.solicitarCitaMedico(ct2));

    Cita alternativa = new Cita(LocalDateTime.now().plusDays(4), pa1, m2);
    System.out.println("Intento de modificar la cita actual: " + pa1.modificarCita(ct1, alternativa));

    System.out.println("\nAccediendo a información personal...");
    System.out.println("Nombre: " + pa1.getNombre());
    System.out.println("Dirección: " + pa1.getDireccion());
    System.out.println("Teléfono actual: " + pa1.getTelefono());

    pa1.setTlf(611220033);
    System.out.println("Nuevo número telefónico asignado: " + pa1.getTelefono() + "\n");

    // ===== pruebas cita =====
    System.out.println("[5] Validando que las citas funcionan correctamente...");

    System.out.println("¿La cita ct1 está anulada? " + ct1.isAnulada());
    ct1.cancelar("Imposibilidad de asistencia");
    System.out.println("¿Y ahora? " + ct1.isAnulada());
    System.out.println("Motivo registrado: " + ct1.getCausaAnulacion());

    LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(5);
    ct2.modificarFechaHora(nuevaFecha);
    System.out.println("ct2 actualizada a: " + ct2.imprimirFechaHora());

    System.out.println("Notificación de reagendación: " + ct2.notificarReagendar(d2));

    String aviso = ct2.notificar2Dias();
    System.out.println("Recordatorio (2 días antes): " + (aviso != null ? aviso : "No corresponde enviar recordatorio"));

    System.out.println("¿ct1 y ct2 son iguales? " + ct1.equals(ct2));
    System.out.println("ct1 → " + ct1);
    System.out.println("ct2 → " + ct2 + "\n");

    // ===== pruebas usuario =====
    System.out.println("[6] Acciones generales del usuario...");

    LocalDateTime otraFecha = LocalDateTime.now().plusDays(7);
    System.out.println("Modificando fecha de ct2: " + pa1.modificarFechaHora(ct2, otraFecha));

    System.out.println("Cancelación de cita ct2 por el paciente: " + pa1.cancelarCita(ct2, "Cambio personal"));

    System.out.println("\nComparaciones:");
    System.out.println("m1 = m2 ? → " + m1.equals(m2));
    System.out.println("pa1 = pa2 ? → " + pa1.equals(pa2));

    System.out.println("\n>>> PRUEBAS FINALIZADAS <<<");
}
