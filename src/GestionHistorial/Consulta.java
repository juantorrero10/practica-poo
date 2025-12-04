package GestionHistorial;

import Enumeradores.*;
import Medicacion.Medicamento;
import Reestricion.Reestricion;
import Usuarios.Medico;
import Usuarios.Usuario;

import javax.management.InvalidAttributeValueException;
import java.rmi.AccessException;
import java.time.LocalDate;

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
        return "Consulta \n" +
                "Fecha: " + fecha +
                "Motivo: " + motivo + '\'' +
                "Telefonica: " + tipoConsulta +
                "InformeSeguimineto: " + tipoInforme +
                "Medico: " + medico +
                '\n';
    }

    public LocalDate getFecha() { return fecha; }
    public String getMotivo() { return motivo; }
    public TipoConsulta getTipoConsulta() { return tipoConsulta; }
    public TipoInforme getTipoInforme() { return tipoInforme; }
    public Centros getCentro() { return centro; }
    public Medico getMedico() { return medico; }
    public Medicamento getPreescripcion() { return preescripcion; }
}

