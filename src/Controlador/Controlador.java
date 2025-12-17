package Controlador;


import Main.Log;
import backend.Agendas.AgendaCitas;
import backend.Agendas.AgendaConsultas;
import backend.Agendas.Pacientes;
import backend.Agendas.Plantilla;
import backend.Usuarios.*;
import CSV.*;

import java.lang.management.PlatformManagedObject;
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

    //Temporal
    public Controlador(Plantilla p, String CSVPacientes, AgendaConsultas a, AgendaCitas c) {
        this(
                p,
                new PacientesCSV(CSVPacientes).importarPacientes(),
                a,
                c
        );
    }

    public Controlador(String CSVPacientes, String CSVPlantilla, String CSVCitas, String CSVHistoriales) {
        //TODO: Cargar fichero
    }

    public void notificarCambioSesion(Usuario usuario, TipoUsuario tipoUsuario) {
        for (OyenteSesion oyente : oyentes) {
            oyente.onSesionUpdate(usuario, tipoUsuario);
        }
    }

    public void addOyente(OyenteSesion l) {
        oyentes.add(l);
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

    public boolean cerrarSesion(TipoUsuario tp) {
        Log.INFO("Cierre de Sesion del tipo: "+tp.toString());
        switch (tp) {
            case ADMIN:
                if (loginAdmin != null) { notificarCambioSesion(null, tp); loginAdmin = null; return true; }
                break;
            case MEDICO:
                if (loginMedico != null) { notificarCambioSesion(null, tp); loginMedico = null; return true; }
                break;
            case ADMINCENTRO:
                if (loginAdminC != null) { notificarCambioSesion(null, tp); loginAdminC = null; return true; }
                break;
            case PACIENTE:
                if (loginPaciente != null) { notificarCambioSesion(null, tp); loginPaciente = null; return true; }
                break;
        }
        return false;
    }

    public boolean cambiarUsuario(Usuario usuario) {
        TipoUsuario tp = usuario.getTipoUsuario();

        if (usuario == null) {
            Log.ERR("El Usuario es nulo.");
            return false;
        }
        switch (tp) {
            case ADMIN:
                if (loginAdmin == null)  { loginAdmin = (Admin) usuario; notificarCambioSesion(usuario, tp); return true; }
                break;
            case PACIENTE:
                if (loginPaciente == null)  { loginPaciente = (Paciente) usuario; notificarCambioSesion(usuario, tp); return true; }
                break;
            case MEDICO:
                if (loginMedico == null)  { loginMedico = (Medico) usuario; notificarCambioSesion(usuario, tp); return true;}
                break;
            case ADMINCENTRO:
                if (loginAdminC == null)  { loginAdminC = (AdminCentroSalud) usuario; notificarCambioSesion(usuario, tp); return true;}
                break;
            default:
                Log.WARN("el tipo de usuario no es válido: " + tp.ordinal());
                return false;
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
