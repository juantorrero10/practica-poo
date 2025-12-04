package Agendas;

import Usuarios.Paciente;

import java.util.ArrayList;
import java.util.Objects;

public class Pacientes {
    private ArrayList<Paciente> pacientes;

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

    public boolean agregarPaciente(Paciente p) {
        if (this.pacientes.contains(p)) {this.pacientes.add(p); return true;}
        return false;
    }


}
