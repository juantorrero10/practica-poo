package backend.GestionHistorial;

import backend.Citas.Cita;
import Enumeradores.*;
import backend.Enumeradores.Centros;
import backend.Enumeradores.Especialidades;
import backend.Enumeradores.TipoConsulta;
import backend.Enumeradores.TipoInforme;
import backend.Medicacion.Medicamento;
import backend.Reestricion.Reestricion;
import backend.Usuarios.Medico;
import backend.Usuarios.Usuario;

import javax.management.InvalidAttributeValueException;
import java.rmi.AccessException;
import java.time.LocalDate;
import java.util.Scanner;

public class Consulta {

    private final LocalDate fecha;
    private final String motivo;
    private final TipoConsulta tipoConsulta;
    private final TipoInforme tipoInforme;
    private final Centros centro;
    private final Medico medico;
    //Puede ser nulo
    private Medicamento preescripcion;

    public Consulta(LocalDate fecha,
                    String motivo,
                    TipoConsulta tipoConsulta,
                    TipoInforme tipoInforme,
                    Centros centro,
                    Medico medico)
            throws InvalidAttributeValueException
    {
        this.fecha = fecha;
        this.motivo = motivo;
        this.tipoInforme = tipoInforme;
        this.medico = medico;
        this.centro = centro;

        //Restriciones
        if ((medico.getEspecialidad() != Especialidades.ENFERMERO &&
                medico.getEspecialidad() != Especialidades.GENERAL)
            && tipoConsulta == TipoConsulta.TELEFONICA
        ) {
            throw new InvalidAttributeValueException("Las consultas con especialista deben ser presenciales.");
        }else{
            this.tipoConsulta = tipoConsulta;
        }
    }

    public void recetarMedicamento(Usuario u, Medicamento med) throws AccessException {
        Reestricion.medico(u, "Consulta.recetarMedicamento");
        this.preescripcion = med;
    }


    @Override
    public String toString() {
        String s = "Fecha: " + fecha +
                "\nMotivo: " + motivo + '\'' +
                "\nTelefonica: " + tipoConsulta +
                "\nInformeSeguimineto: " + tipoInforme +
                "\nCentro: " + centro +
                "\nMedico: " + medico.getCIPA();
        if (this.preescripcion != null) {
            s += "\nPreescripcion: " + this.preescripcion.toString();
        }
            s += "\n";
        return s;
    }

    @Override
    // Comparar por fecha y médico.
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Consulta consulta = (Consulta) o;

        return (consulta.getFecha().equals(this.getFecha())
                && consulta.getMedico().equals(this.getMedico())
                && consulta.motivo.equals(this.getMotivo()));
    }

    public LocalDate getFecha() { return fecha; }
    public String getMotivo() { return motivo; }
    public TipoConsulta getTipoConsulta() { return tipoConsulta; }
    public TipoInforme getTipoInforme() { return tipoInforme; }
    public Centros getCentro() { return centro; }
    public Medico getMedico() { return medico; }
    public Medicamento getPreescripcion() { return preescripcion; }


    /**
     * @return String con un menu con las opciones posibles de los valores de un enumerador.
     * Salta NO_ESPECIFICADO
     * FORMATO: [1]: Opcion 1
     *          [2]: Opcion 2
     *          ...
     */
    private static <T extends Enum<T>> String dialogoMenuEnumerador(Class<T> enumClass) {
        String menu = "";
        T[] valores = enumClass.getEnumConstants();

        for (int i = 1; i < valores.length; i++) {
            menu += "["+i+"]: ";
            menu += valores[i].name() + "\n";
        }

        return menu;
    }

    private static void lanzarValorInvalidoEnum(int i, int max) throws Exception {
        if (i < 1 || i > max) {
            throw new Exception("Respuesta invalida a dialogo.");
        }
    }

    /**
     * @param cita debe contener la fecha y hora a la que sucedio y el medico que atendió
     */
    public static Consulta completarConsulta(Usuario u, Cita cita, String motivo, TipoInforme ti, TipoConsulta tc, Centros cen)
            throws AccessException, InvalidAttributeValueException
    {
        Reestricion.noPaciente(u, "Consulta.completarConsulta");
        return new Consulta(cita.getFechaHora().toLocalDate(),
                motivo,
                tc,
                ti,
                cen,
                cita.getMedico());
    }

    /**
     * @param cita debe contener la fecha y hora a la que sucedio y el medico que atendió
     * Completar consulta por dialogo.
     */
    public static Consulta completarConsultaDialogo(Usuario u, Cita cita, Scanner sc)
            throws Exception {
        if (cita.getFechaHora() == null || cita.getMedico() == null) {
            return null;
        }
        Reestricion.noPaciente(u, "Consulta.completarConsulta");
        String respuesta;
        String motivo;
        TipoInforme ti;
        TipoConsulta tc;
        Centros cen;
        System.out.println("====DIALOGO CONSULTAS====");
        System.out.println("Completar informacion de consulta con:\n\tPaciente: "
                +cita.getPaciente().getNombreCompleto()+"\n\tMedico: "
                +cita.getMedico().getCIPA()+ "\n\tFecha: "
                +cita.getFechaHora());
        System.out.print("Motivo: ");
        motivo = sc.nextLine();

        System.out.println("Tipo de consultas:");
        System.out.println(dialogoMenuEnumerador(TipoConsulta.class));
        System.out.print(">>> ");
        respuesta = sc.nextLine();
        tc = TipoConsulta.values()[Integer.parseInt(respuesta)];
        lanzarValorInvalidoEnum(tc.ordinal(), TipoConsulta.TELEFONICA.ordinal());

        System.out.println("\nTipo de informe:");
        System.out.println(dialogoMenuEnumerador(TipoInforme.class));
        System.out.print(">>> ");
        respuesta = sc.nextLine();
        ti = TipoInforme.values()[Integer.parseInt(respuesta)];
        lanzarValorInvalidoEnum(ti.ordinal(), TipoInforme.ALTA.ordinal());

        System.out.println("\nCentros:");
        System.out.println(dialogoMenuEnumerador(Centros.class));
        System.out.print(">>> ");
        respuesta = sc.nextLine();
        cen = Centros.values()[Integer.parseInt(respuesta)];
        lanzarValorInvalidoEnum(cen.ordinal(), Centros.HOSPITAL_UNIVERSITARIO_TORREJON.ordinal());

        return completarConsulta(u, cita, motivo, ti, tc, cen);
    }


}

