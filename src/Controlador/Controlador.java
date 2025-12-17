package Controlador;


import Main.Log;
import backend.Agendas.AgendaCitas;
import backend.Agendas.AgendaConsultas;
import backend.Agendas.Pacientes;
import backend.Agendas.Plantilla;
import backend.Enumeradores.Centros;
import backend.Enumeradores.Especialidades;
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

    private String rutaPacientes = "";
    private String rutaPlantilla = "";
    private String rutaCitas = "";
    private String rutaConsultas = "";

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
    public Controlador(String CSVPlantilla, String CSVPacientes, AgendaConsultas a, AgendaCitas c) {
        this(
                new PlantillaCSV(CSVPlantilla).importarPlantilla(),
                new PacientesCSV(CSVPacientes).importarPacientes(),
                a,
                c
        );
        rutaPacientes = CSVPacientes;
        rutaPlantilla = CSVPlantilla;
    }

    public Controlador(String CSVPacientes, String CSVPlantilla, String CSVCitas, String CSVHistoriales) {
        //TODO: Cargar fichero
    }

    public void notificarCambioSesion(Usuario usuario, TipoUsuario tipoUsuario, boolean cambiarPest) {
        for (OyenteSesion oyente : oyentes) {
            oyente.onSesionUpdate(usuario, tipoUsuario, cambiarPest);
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
                if (loginAdmin != null) { notificarCambioSesion(null, tp, true); loginAdmin = null; return true; }
                break;
            case MEDICO:
                if (loginMedico != null) { notificarCambioSesion(null, tp, true); loginMedico = null; return true; }
                break;
            case ADMINCENTRO:
                if (loginAdminC != null) { notificarCambioSesion(null, tp, true); loginAdminC = null; return true; }
                break;
            case PACIENTE:
                if (loginPaciente != null) { notificarCambioSesion(null, tp, true); loginPaciente = null; return true; }
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
                if (loginAdmin == null)  { loginAdmin = (Admin) usuario; notificarCambioSesion(usuario, tp, true); return true; }
                break;
            case PACIENTE:
                if (loginPaciente == null)  { loginPaciente = (Paciente) usuario; notificarCambioSesion(usuario, tp, true); return true; }
                break;
            case MEDICO:
                if (loginMedico == null)  { loginMedico = (Medico) usuario; notificarCambioSesion(usuario, tp, true); return true;}
                break;
            case ADMINCENTRO:
                if (loginAdminC == null)  { loginAdminC = (AdminCentroSalud) usuario; notificarCambioSesion(usuario, tp, true); return true;}
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

    public int crearUsuario(Usuario u) {

        Log.INFO("Nuevo usuario");


        if ( !(listaPacientes.verificarCIPA(u.getCIPA())
                && plantilla.verificarCIPA(u.getCIPA()))  ) {
            Log.WARN("El CIPA esta ocupado");
            return 1;
        }


        if (u instanceof Paciente) {
            listaPacientes.agregarPaciente((Paciente) u);
            Log.INFO("Nuevo Paciente -> " + u.toString());
            new PacientesCSV(rutaPacientes).exportarPacientes(listaPacientes);
            return 0;

        } else {
            switch (u.getTipoUsuario()) {
                case ADMIN:
                    plantilla.agregarAdministrador((Admin) u);
                    break;
                case ADMINCENTRO:
                    plantilla.agregarAdministradorCentro((AdminCentroSalud) u);
                    break;
                case MEDICO:
                    plantilla.agregarMedico((Medico) u);
                    break;
                default:
                    Log.ERR("Tipo invalido de: " + u.toString());
                    return 2;
            }
        }

        new PlantillaCSV(rutaPlantilla).exportarPlantilla(plantilla);
        Log.INFO("Nuevo usuario -> (" + u.getTipoUsuario() + ") " + u.toString());
        return 0;
    }

    public boolean borrarUsuario(long CIPA) {
        Usuario u = null;
        if (!listaPacientes.verificarCIPA(CIPA)) {
            u = listaPacientes.identificarPaciente(CIPA);
        } else {
            u = plantilla.getUsuarioCIPA(CIPA);
        }

        if (u == null) {
            Log.WARN("EL usuario con CIPA: " + CIPA + " no existe");
            return false;
        }

        if (u.getTipoUsuario() == TipoUsuario.PACIENTE) {
            listaPacientes.borrarPaciente((Paciente) u);
            new PacientesCSV(rutaPacientes).exportarPacientes(listaPacientes);
        } else {
            plantilla.borrarUsuario(u);
            new PlantillaCSV(rutaPlantilla).exportarPlantilla(plantilla);
        }

        return true;
    }

    public boolean modificarUsuario(Usuario u,
                                    String DNI,
                                    String nombre,
                                    String direccion,
                                    long telefono,
                                    Especialidades especialidad,
                                    Centros centro) {

        if (u == null) return false;

        if (DNI.isEmpty()) {
            DNI = u.getDNI();
        }

        switch (u.getTipoUsuario()) {
            case ADMIN:
                plantilla.cambiarAdministrador((Admin) u, new Admin(DNI, u.getCIPA()));
                break;
            case ADMINCENTRO:
                if (centro == Centros.NO_ESPECIFICADO) centro = ((AdminCentroSalud)u).getCentro();
                plantilla.cambiarAdCentro((AdminCentroSalud) u, new AdminCentroSalud(DNI, u.getCIPA(), centro));
                break;
            case MEDICO:
                if (centro == Centros.NO_ESPECIFICADO) centro = ((Medico)u).getCentro();
                if (especialidad == Especialidades.NO_ESPECIFICADO) especialidad = ((Medico)u).getEspecialidad();
                plantilla.cambiarMedico((Medico) u, new Medico(DNI, u.getCIPA(), especialidad, centro));
                break;
            case PACIENTE:
                if (nombre.isEmpty()) nombre = ((Paciente)u).getNombreCompleto();
                if (direccion.isEmpty()) direccion = ((Paciente)u).getDireccion();
                if (telefono == -1) telefono = ((Paciente)u).getTelefono();
                listaPacientes.cambiarPaciente((Paciente) u, new Paciente(nombre, direccion, telefono, DNI, u.getCIPA()));
                break;
            default:
                Log.ERR("Tipo invalido de: " + u.toString());
        }

        new PlantillaCSV(rutaPlantilla).exportarPlantilla(plantilla);
        new PacientesCSV(rutaPacientes).exportarPacientes(listaPacientes);
        return true;

    }

}
