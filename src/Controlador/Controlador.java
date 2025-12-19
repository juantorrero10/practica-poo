package Controlador;


import Main.Log;
import backend.Agendas.AgendaCitas;
import backend.Agendas.Pacientes;
import backend.Agendas.Plantilla;
import backend.Citas.Cita;
import backend.Enumeradores.Centros;
import backend.Enumeradores.Especialidades;
import backend.Usuarios.*;
import CSV.*;

import javax.management.InvalidAttributeValueException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Controlador {

    private Plantilla plantilla;
    private Pacientes listaPacientes;
    private AgendaCitas citas;


    // Hasta 4 sesiones abiertas, una por cada tipo.
    private Paciente loginPaciente;
    private Medico loginMedico;
    private AdminCentroSalud loginAdminC;
    private Admin loginAdmin;

    private String rutaPacientes = "";
    private String rutaPlantilla = "";
    private String rutaCitas = "";
    private String carpetaHistoriales = "";

    // Oyentes
    private List<OyenteSesion> oyentesSesion;
    private List<OyenteCitas> oyenteCitas;

    public Controlador(Plantilla p, Pacientes pa) {
        this.plantilla = p;
        this.listaPacientes = pa;

        loginPaciente = null;
        loginMedico = null;
        loginAdmin = null;
        loginAdminC = null;

        oyentesSesion = new ArrayList<>();
        oyenteCitas = new ArrayList<>();
    }

    public Controlador() {
        this(new Plantilla(), new Pacientes());
    }

    //Temporal
    public Controlador(String CSVPlantilla, String CSVPacientes) {
        this(
                new PlantillaCSV(CSVPlantilla).importarPlantilla(),
                new PacientesCSV(CSVPacientes).importarPacientes()
        );
        rutaPacientes = CSVPacientes;
        rutaPlantilla = CSVPlantilla;
    }

    public void cargarCitas(String CSVCItas) {
        citas = new CitasCSV(CSVCItas).importarCitas(listaPacientes, plantilla);
    }

    public void cargarHistoriales(String carpetaHistoriales) throws InvalidAttributeValueException {
        this.carpetaHistoriales = carpetaHistoriales;

        //Crear historiales
        for(Paciente p : listaPacientes.getPacientes()){
            File file = new File(carpetaHistoriales+"/"+p.getCIPA()+".csv");
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.INFO("Ya existe");
            }
        }


        for (int i = 0; i < listaPacientes.getPacientes().size(); i++) {
            Paciente p = listaPacientes.getPacientes().get(i);
            p = new HistorialesCSV(carpetaHistoriales).importarHistorial(p, plantilla);
            listaPacientes.getPacientes().set(i, p);
        }
    }

    public void exportarHistoriales() throws IOException {
        for (Paciente p : listaPacientes.getPacientes()) {
            new HistorialesCSV(carpetaHistoriales).exportarHistorial(p);
        }
    }

    public void exportarHistorialPaciente(Paciente p) throws IOException {
        new HistorialesCSV(carpetaHistoriales).exportarHistorial(p);
    }

    public Controlador(String CSVPacientes, String CSVPlantilla, String CSVCitas, String CSVHistoriales) {
        //TODO: Cargar fichero
    }

    public void notificarCambioSesion(Usuario usuario, TipoUsuario tipoUsuario, boolean cambiarPest) {
        for (OyenteSesion oyente : oyentesSesion) {
            oyente.onSesionUpdate(usuario, tipoUsuario, cambiarPest);
        }
    }

    public void notificarCambioCita(Cita c) {
        for (OyenteCitas oyente : this.oyenteCitas) {
            oyente.onCitaUpdate(c);
        }
    }

    public void addOyenteSesion(OyenteSesion l) {
        oyentesSesion.add(l);
    }

    public void addOyenteCitas(OyenteCitas l) {
        oyenteCitas.add(l);
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

    public int crearUsuario(Usuario u) throws IOException {

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

    public boolean borrarUsuario(long CIPA) throws IOException {
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
                                    Centros centro) throws IOException {

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



    public int crearCitaPaciente(
            Especialidades especialidad,
            LocalDateTime fecha

    ) throws IOException {
        Log.INFO("crearCita -> esp: " + especialidad);

        if (especialidad == Especialidades.NO_ESPECIFICADO) {
            Log.ERR("Especialidad no válido.");
            return 1;
        }

        boolean pedirProximaDisponible = fecha.isEqual(LocalDateTime.now());
        Medico med = plantilla.encontrarEspecilistaAleatorio(especialidad);

        if (med == null) {
            Log.WARN("No hay médicos disponibles para la especialidad: " + especialidad);
            return 4;
        }

        Cita cita = new Cita(fecha, loginPaciente, med);


        // Comprobar disponibilidad global
        if (!pedirProximaDisponible && !med.getAgenda().comprobarDisponibilidad(cita)) {
            Log.WARN("El médico no está disponible en esa fecha/hora");
            return 2;
        }

        // Comprobar disponibilidad usuario
        if (!pedirProximaDisponible) {
            for (Cita c : citas.getCitas()) {
                if (fecha.equals(c.getFechaHora()) && loginPaciente == c.getPaciente()) {
                    Log.WARN(loginPaciente.getNombreCompleto()+" ("+loginPaciente.getCIPA()+") ya tiene una cita a las " + fecha.toString());
                    return 5;
                }
            }
        }


        if (pedirProximaDisponible) {
            LocalDate ld = med.encontrarProximoDiaDisponible(LocalDate.now(), 0);
            LocalDateTime ldt = LocalDateTime.of(
                    ld,
                    med.encontrarPrimeraHoraDisponible(ld));
            cita.setFechaHora(ldt);


        }


        // Añadir a la agenda del médico (comprueba límite diario)
        if (!med.anadirCita(cita)) {
            Log.ERR("El médico ha alcanzado el máximo de citas diarias");
            return 3;
        }

        // Añadir a la agenda del paciente
        if (!citas.agregarCita(cita, 20)) {
            Log.ERR("No se pudo agregar la cita");
            return 4;
        }

        Paciente copia = loginPaciente;
        loginPaciente.getArrayCitas().add(cita);
        listaPacientes.cambiarPaciente(copia, loginPaciente);

        Log.INFO("Cita creada correctamente -> \n\t" + cita);
        new CitasCSV(rutaCitas).exportarCitas(citas);
        return 0;
    }

    public void exportarCitas() throws IOException {
        new CitasCSV(rutaCitas).exportarCitas(citas);

    }

    // Crear Cita
    public boolean crearCita(
            Paciente paciente,
            Medico medico,
            LocalDateTime fechaHora
    ) {
        if (paciente == null || medico == null || fechaHora == null) {
            Log.ERR("Datos incompletos para crear la cita");
            return false;
        }

        // Crear la cita
        Cita nueva = new Cita(fechaHora, paciente, medico);

        // Comprobar disponibilidad global
        if (!citas.comprobarDisponibilidad(nueva)) {
            Log.WARN("El médico no está disponible en esa fecha/hora");
            return false;
        }

        // Añadir a la agenda del médico (comprueba límite diario)
        if (!medico.anadirCita(nueva)) {
            Log.WARN("El médico ha alcanzado el máximo de citas diarias");
            return false;
        }

        // Añadir a la agenda del paciente
        if (!paciente.solicitarCitaMedico(nueva)) {
            Log.WARN("No se pudo asignar la cita al paciente");
            return false;
        }

        // Añadir a la agenda global
        if (!citas.agregarCita(nueva, 20)) {
            Log.WARN("No se pudo añadir la cita a la agenda global");
            return false;
        }

        Log.INFO("Cita creada correctamente -> " + nueva);
        return true;
    }
    public Plantilla getPlantilla() {
        return plantilla;
    }

    public Pacientes getListaPacientes() {
        return listaPacientes;
    }

    // Crear metodo exportar historial paciente
    public void cargarHistorialPaciente(Paciente p) throws InvalidAttributeValueException {
        if (p == null) return;

        new HistorialesCSV(carpetaHistoriales).importarHistorial(p, plantilla);
    }

    public AgendaCitas getCitas() {
        return citas;
    }

    public int anularCita(Cita c, String mot) {
        notificarCambioCita(c);
        return citas.anularCita(c, mot);
    }
}
