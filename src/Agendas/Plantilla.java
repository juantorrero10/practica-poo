package Agendas;

import Reestricion.Reestricion;
import Usuarios.Admin;
import Usuarios.AdminCentroSalud;
import Usuarios.Medico;
import Usuarios.Usuario;

import java.rmi.AccessException;
import java.util.ArrayList;

public class Plantilla {
    private final ArrayList<Admin> administradores;
    private final ArrayList<Medico> medicos;
    private final ArrayList<AdminCentroSalud> administradoresCentroSalud;

    public Plantilla() {
        administradores = new ArrayList<>();
        medicos = new ArrayList<>();
        administradoresCentroSalud = new ArrayList<>();
    }



    private boolean reestricionModificacion(Usuario u, String accion)
            throws AccessException
    {
        if (!(u instanceof Admin)) {
            throw new AccessException("Acceso denegado para la accion: "+accion+"(), tipo de usuario: "+u.getClass().getName());
        }
        return true;
    }

    public void agregarAdministrador(Usuario u, Admin nuevo) throws AccessException {
        Reestricion.adminSuper(u, "Plantilla.agregarAdministrador");
        if(administradores.contains(nuevo)) administradores.add(nuevo);
    }
    public void agregarMedico(Usuario u, Medico nuevo) throws AccessException {
        Reestricion.adminCentro(u, "Plantilla.agregarMedico");
        if(medicos.contains(nuevo)) medicos.add(nuevo);
    }
    public void agregarAdministradorCentro(Usuario u, AdminCentroSalud nuevo) throws AccessException {
        Reestricion.adminSuper(u, "Plantilla.agregarAdministradorCentro");
        if(administradoresCentroSalud.contains(nuevo)) administradoresCentroSalud.add(nuevo);
    }


}
