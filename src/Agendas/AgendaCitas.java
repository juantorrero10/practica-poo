package Agendas;

import Citas.Cita;
import Reestricion.Reestricion;
import Usuarios.*;


import java.nio.file.AccessDeniedException;
import java.rmi.AccessException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AgendaCitas {
    private ArrayList<Cita> citas;

    public AgendaCitas() {
        citas = new ArrayList<>();
    }
    private void anadirCitas(Cita cita) {
        citas.add(cita);
    }

    private List<Cita> getCitaUsuario(Object o) {
        List<Cita> lista = new ArrayList<>();
        for (Cita c : citas) {
            if      (o instanceof Medico   && c.getMedico().equals(o)) { lista.add(c); }
            else if (o instanceof Paciente && c.getPaciente().equals(o)) { lista.add(c); }
        }
        return lista;
    }

    public List<Cita> obtenerCitasPaciente(Pacientes paciente) {
        return  getCitaUsuario(paciente);
    }

    public List<Cita> obtenerCitasMedico(Medico medico) {
        return getCitaUsuario(medico);
    }

    public void reagendarCitasDia(Usuario usuario, LocalDate fecha, LocalDateTime nueva)
            throws AccessException
    {
        Reestricion.noPaciente(usuario, "AgendaCitas.reagendarCitasDia");

        for (Cita c : citas) {
            if (c.getFechaHora().toLocalDate().equals(fecha)) {
                c.reagendar(nueva);}
        }
    }

}
