package backend.Agendas;

import backend.Usuarios.Paciente;
import backend.Usuarios.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Pacientes {
    private final ArrayList<Paciente> pacientes;

    public Pacientes() {
        //todo: cargar archivo
        pacientes = new ArrayList<Paciente>();
    }

    public Pacientes(ArrayList<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public Paciente identificarPaciente(long CIPA) {
        for (Paciente p : pacientes) {
            if (p.getCIPA() == CIPA) {return p;}
        }
        return null;
    }

    public boolean agregarPaciente(Paciente p)  {
        if (!pacientes.contains(p)) {pacientes.add(p); return true;}
        return false;
    }

    public List<Paciente> getPacientes(){
        return pacientes;
    }
    @Override
    public String toString() {
        String s = "";
        for (Paciente paciente : pacientes) {
            s += paciente.toString();
            s += "\n";
        }
        return s;
    }
}
