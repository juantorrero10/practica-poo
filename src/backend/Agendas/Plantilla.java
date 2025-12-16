package backend.Agendas;

import backend.Enumeradores.Centros;
import backend.Enumeradores.Especialidades;
import backend.Usuarios.Admin;
import backend.Usuarios.AdminCentroSalud;
import backend.Usuarios.Medico;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Plantilla {
    private final ArrayList<Admin> administradores;
    private final ArrayList<Medico> medicos;
    private final ArrayList<AdminCentroSalud> administradoresCentroSalud;

    //Generador de números aleatorios para obtener especialista al azar
    private final Random random;

    public Plantilla() {
        administradores = new ArrayList<>();
        medicos = new ArrayList<>();
        administradoresCentroSalud = new ArrayList<>();
        random = new Random();
    }

    public List<Medico> getMedicosCentro(Centros c) {
        List<Medico> medicosCentro = new ArrayList<>();
        for (Medico m : this.medicos) {
            if (m.getCentro() == c) { medicosCentro.add(m); }
        }
        return medicosCentro;
    }

    public List<Medico> getEspecialistas(Especialidades esp){
        List<Medico> medicos = new ArrayList<>();
        for (Medico m : this.medicos){
            if (m.getEspecialidad().equals( esp)) { medicos.add(m); }
        }
        return medicos;
    }

    public List<AdminCentroSalud> getAdministradoresCentroSalud() {
        return administradoresCentroSalud;
    }

    public List<Admin> getAdministradores() {
        return administradores;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public Medico encontrarEspecilistaAleatorio(Especialidades esp){

        List<Medico> listaEsp = getEspecialistas(esp);
        if (listaEsp.isEmpty()) { return null; }
        int idx = random.nextInt(listaEsp.size());
        return listaEsp.get(idx);
    }

    public void agregarAdministrador(Admin nuevo)  {
        if(!administradores.contains(nuevo)) administradores.add(nuevo);
    }
    public void agregarMedico(Medico nuevo) {
        if(!medicos.contains(nuevo)) medicos.add(nuevo);
    }
    public void agregarAdministradorCentro(AdminCentroSalud nuevo) {
        if(!administradoresCentroSalud.contains(nuevo)) {
            administradoresCentroSalud.add(nuevo);
        }
    }

    @Override
    public String toString() {
        String s= "----ADMINISTRADORES----\n";
        for (Admin a : administradores) {
            s += a.toString();
            s += "\n";
        }

        s += "-----GESTION CENTRO-----\n";
        for (AdminCentroSalud ac : administradoresCentroSalud) {
            s += ac.toString();
            s += "\n";
        }

        s += "----MEDICOS----\n";
        for (Medico m : medicos) {
            s +=  m.toString();
            s += "\n";
        }
        return s;
    }
}
