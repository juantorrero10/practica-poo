package Agendas;

import Enumeradores.Centros;
import Enumeradores.Especialidades;
import Reestricion.Reestricion;
import Usuarios.Admin;
import Usuarios.AdminCentroSalud;
import Usuarios.Medico;
import Usuarios.Usuario;

import java.rmi.AccessException;
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

    public List<Medico> getMedicos() {
        return medicos;
    }

    public Medico encontrarEspecilistaAleatorio(Especialidades esp){

        List<Medico> listaEsp = getEspecialistas(esp);
        if (listaEsp.isEmpty()) { return null; }
        int idx = random.nextInt(listaEsp.size());
        return listaEsp.get(idx);
    }

    public void agregarAdministrador(Usuario u, Admin nuevo) throws AccessException {
        Reestricion.adminSuper(u, "Plantilla.agregarAdministrador");
        if(!administradores.contains(nuevo)) administradores.add(nuevo);
    }
    public void agregarMedico(Usuario u, Medico nuevo) throws AccessException {
        Reestricion.adminCentro(u, "Plantilla.agregarMedico");
        if(!medicos.contains(nuevo)) medicos.add(nuevo);
    }
    public void agregarAdministradorCentro(Usuario u, AdminCentroSalud nuevo) throws AccessException {
        Reestricion.adminSuper(u, "Plantilla.agregarAdministradorCentro");
        if(administradoresCentroSalud.contains(nuevo)) administradoresCentroSalud.add(nuevo);
    }


}
