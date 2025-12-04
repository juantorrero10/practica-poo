package Agendas;

import Citas.Cita;
import Usuarios.Medico;
import Usuarios.Paciente;
import Usuarios.Usuario;


import java.time.LocalDate;
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

    public void reagendarCitasDia(Usuario usuario, LocalDate fecha, LocalDate nueva) {
        
    }

}
