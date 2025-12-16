package backend.Agendas;

import backend.Reestricion.Reestricion;
import backend.Usuarios.Paciente;
import backend.Usuarios.Usuario;

import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.List;

public class Pacientes {
    private static ArrayList<Paciente> pacientes;

    public Pacientes() {
        //todo: cargar archivo
        pacientes = new ArrayList<Paciente>();
    }

    public Paciente identificarPaciente(long CIPA) {
        for (Paciente p : pacientes) {
            if (p.getCIPA() == CIPA) {return p;}
        }
        return null;
    }

    public boolean agregarPaciente(Usuario u, Paciente p) throws AccessException {
        Reestricion.adminCentro(u, "Pacientes.agregarPaciente");
        if (!pacientes.contains(p)) {pacientes.add(p); return true;}
        return false;
    }

    public List<Paciente> getPacientes(Usuario u) throws AccessException {
        Reestricion.noPaciente(u, "Pacientes.getPacientes");
        return pacientes;
    }


}
