package Controlador;


import Main.Log;
import backend.Agendas.AgendaCitas;
import backend.Agendas.AgendaConsultas;
import backend.Agendas.Pacientes;
import backend.Agendas.Plantilla;
import backend.Usuarios.*;

import java.util.ArrayList;
import java.util.List;

public class Controlador {

    private Plantilla plantilla;
    private Pacientes listaPacientes;
    private AgendaConsultas agendaConsultas;
    private AgendaCitas citas;


    // Hasta 4 sesiones abiertas, una por cada tipo.
    private Paciente loginPaciente;
    private Medico loginMedico;
    private AdminCentroSalud loginAdminC;
    private Admin loginAdmin;

    // Oyentes
    private List<OyenteSesion> oyentes;

    public Controlador(Plantilla p, Pacientes pa, AgendaConsultas a, AgendaCitas c) {
        this.plantilla = p;
        this.agendaConsultas = a;
        this.citas = c;
        this.listaPacientes = pa;

        loginPaciente = null;
        loginMedico = null;
        loginAdmin = null;
        loginAdminC = null;

        oyentes = new ArrayList<>();
    }

    public Controlador() {
        this(new Plantilla(), new Pacientes(), new AgendaConsultas(), new AgendaCitas());
    }

    public void notificarCambioSesion(Usuario usuario) {
        for (OyenteSesion oyente : oyentes) {
            oyente.onSesionUpdate(usuario);
        }
    }

    public void addOyente(OyenteSesion l) {
        oyentes.add(l);
    }

    public Controlador(String RutaFicheroCSV) {
        //TODO: Cargar fichero
    }

    public String stringGetUsuarios() {
        String usuarios = "";
        usuarios += "----PACIENTES----\n";
        usuarios += listaPacientes.toString();
        usuarios += plantilla.toString();
        return usuarios;
    }

    public void setLoginAdminC(AdminCentroSalud loginAdminC) {
        this.loginAdminC = loginAdminC;
    }

    public void setLoginMedico(Medico loginMedico) {
        this.loginMedico = loginMedico;
    }

    public void setLoginAdmin(Admin loginAdmin) {
        this.loginAdmin = loginAdmin;
    }

    public void setLoginPaciente(Paciente loginPaciente) {
        this.loginPaciente = loginPaciente;
    }

    public Paciente getLoginPaciente() {
        return loginPaciente;
    }

    public Medico getLoginMedico() {
        return loginMedico;
    }

    public AdminCentroSalud getLoginAdminC() {
        return loginAdminC;
    }

    public Admin getLoginAdmin() {
        return loginAdmin;
    }

    public boolean cerrarSession(TipoUsuario tp) {
        switch (tp) {
            case ADMIN:
                if (loginAdmin != null) { notificarCambioSesion(null); loginAdmin = null; return true; }
                break;
            case MEDICO:
                if (loginMedico != null) { notificarCambioSesion(null); loginMedico = null; return true; }
                break;
            case ADMINCENTRO:
                if (loginAdminC != null) { notificarCambioSesion(null); loginAdminC = null; return true; }
                break;
            case PACIENTE:
                if (loginPaciente != null) { notificarCambioSesion(null); loginPaciente = null; return true; }
                break;
        }
        return false;
    }

    public boolean cambiarUsuario(Usuario usuario) {
        if (usuario == null) {
            Log.ERR("El Usuario es nulo.");
            return false;
        }
        if (usuario instanceof Admin) {
            if (loginAdmin == null)  { loginAdmin = (Admin) usuario; notificarCambioSesion(usuario); return true; }
        }
        else if (usuario instanceof Paciente) {
            if (loginPaciente == null)  { loginPaciente = (Paciente) usuario; notificarCambioSesion(usuario); return true; }
        }
        else if (usuario instanceof Medico) {
            if (loginMedico == null)  { loginMedico = (Medico) usuario; notificarCambioSesion(usuario); return true;}
        }
        else if (usuario instanceof AdminCentroSalud) {
            if (loginAdminC == null)  { loginAdminC = (AdminCentroSalud) usuario; notificarCambioSesion(usuario); return true;}
        }
        return false;
    }

    public Usuario getLoginUsuario(TipoUsuario tp) {
        switch (tp) {
            case ADMIN:
                return loginAdmin;
            case PACIENTE:
                return loginPaciente;
            case MEDICO:
                return loginMedico;
            case ADMINCENTRO:
                return loginAdminC;
            default:
                return null;
        }
    }



    public Usuario getUsuario(int CIPA, TipoUsuario tp) {

        if (tp == TipoUsuario.NO_ESPECIFICADO) return null;

        switch (tp) {
            case PACIENTE:
            for (Paciente p : listaPacientes.getPacientes()) {
                if (p.getCIPA() == CIPA) return p;
            }
            break;

            case MEDICO:
            for (Medico m: plantilla.getMedicos()) {
                if (m.getCIPA() == CIPA) return m;
            }
            break;

            case ADMINCENTRO:
            for (AdminCentroSalud ac : plantilla.getAdministradoresCentroSalud()) {
                if (ac.getCIPA() == CIPA) return ac;
            }
            break;

            case ADMIN:
            for (Admin ac : plantilla.getAdministradores()) {
                if (ac.getCIPA() == CIPA) return ac;
            }
            break;
        }

        return null;
    }


}
