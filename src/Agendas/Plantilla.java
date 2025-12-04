package Agendas;

import Usuarios.Admin;
import Usuarios.AdminCentroSalud;
import Usuarios.Medico;

import java.util.ArrayList;

public class Plantilla {
    private ArrayList<Admin> administradores;
    private ArrayList<Medico> medicos;
    private ArrayList<AdminCentroSalud> administradoresCentroSalud;

    public Plantilla() {
        administradores = new ArrayList<>();
        medicos = new ArrayList<>();
        administradoresCentroSalud = new ArrayList<>();
    }

    public void agregarAdministrador(Admin a){
        if(administradores.contains(a)) administradores.add(a);
    }
    public void agregarMedico(Medico m){
        if(medicos.contains(m)) medicos.add(m);
    }
    public void agregarAdminCentroSalud(AdminCentroSalud a){
        if (administradoresCentroSalud.contains(a)) administradoresCentroSalud.add(a);
    }


}
