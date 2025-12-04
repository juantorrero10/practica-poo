package Agendas;

import Reestricion.Reestricion;
import Usuarios.Paciente;
import Usuarios.Usuario;

import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.Objects;

public class Pacientes {
    private static ArrayList<Paciente> pacientes;

    public Pacientes() {
        //todo: cagar archivo
        new ArrayList<Paciente>();
    }

    public Paciente identificarPaciente(long CIPA) {
        for (Paciente p : pacientes) {
            if (p.getCIPA() == CIPA) {return p;}
        }
        return null;
    }

    public boolean agregarPaciente(Usuario u, Paciente p) throws AccessException {
        Reestricion.adminCentro(u, "Pacientes.agregarPaciente");
        if (this.pacientes.contains(p)) {this.pacientes.add(p); return true;}
        return false;
    }


}
