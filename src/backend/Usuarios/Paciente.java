package backend.Usuarios;

import backend.Agendas.Plantilla;
import backend.Citas.Cita;
import backend.Enumeradores.Especialidades;
import backend.GestionHistorial.Consulta;
import backend.GestionHistorial.Historial;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;


public class Paciente extends Usuario{

    private String nombreCompleto;
    private String direccion;
    private long telefono;
    private ArrayList<Cita> arrayCitas; // historial de citas del paciente
    private Historial historial;


    public Paciente (String nombreCompleto, String direccion, long telefono , String DNI , long CIPA){

        super(DNI,CIPA);
        this.nombreCompleto=nombreCompleto;
        this.direccion=direccion;
        this.telefono=telefono;
        arrayCitas = new ArrayList<>();
        this.historial = new Historial();

    }

    public Cita asignarCitaAutomatica(LocalDate inicio, Especialidades esp, Plantilla p){

        Medico m = p.encontrarEspecilistaAleatorio(esp);
        if (m == null) {
            System.err.println("No existen medicos especialistas disponibles.");
            return null;
        }

        LocalDate dia = m.encontrarProximoDiaDisponible(LocalDate.now(), 0);
        if (dia == null) {
            return null;
        }
        LocalTime hora = m.encontrarPrimeraHoraDisponible(dia);
        if (hora == null) {
            return null;
        }
        LocalDateTime horario = LocalDateTime.of(dia, hora);
        Cita c = new Cita(horario, this, m);
        if (c.getFechaHora() == null) {
            System.err.println("No se pudo encontrar un dia y una hora para asignar la cita.");
            return null;
        }

        if (!m.anadirCita(c)) {
            System.err.println("No se pudo asignar la cita.");
            return null;
        }
        if (!arrayCitas.contains(c))arrayCitas.add(c);
        return c;
    }


    public boolean solicitarCitaMedico(Cita c){
        if(c==null || c.isAnulada()) return false;

        this.arrayCitas.add(c);
        return true;
    }

    public boolean modificarCita(Cita antigua , Cita nueva){
        if(antigua==null || nueva==null) return false;
        if(antigua.equals(nueva)) return false;

        if(!arrayCitas.contains(antigua)) return false;

        int posicion = arrayCitas.indexOf(antigua);

        arrayCitas.set(posicion, nueva);
        return true;
    }

    public void agregarAlHistorial(Consulta c) {
        if (!historial.getConsultas().contains(c))historial.agregarConsulta(c);
    }

    public boolean eliminarCita(Cita c){
        if(c==null) return false;

        if(!arrayCitas.contains(c)) return false;

        arrayCitas.remove(c);
        return true;
    }

    // Geters y el setTelefono publicos para el paciente

    public ArrayList<Cita> getArrayCitas() {
        return arrayCitas;
    }

    public String getNombreCompleto(){
        return this.nombreCompleto;
    }

    public String getDireccion(){
        return this.direccion;
    }

    public long getTelefono(){
        return this.telefono;
    }
    public Historial getHistorial(){return historial;}

    public void setTelefono(long telefono){
        this.telefono = telefono;
    }

    // Setters que solo pueden ejecutar administradores de centro o administardores generales

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }


    public void setHistorial(Historial historial) {
        this.historial = historial;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;

        if(o==null || this.getClass()!=o.getClass()) return false;

        Paciente p = (Paciente) o;

        return (this.getDNI().equals(p.getDNI()));
    }

    public String shortString(){
        return this.nombreCompleto + " (" + this.getCIPA() + ")";
    }

    @Override
    public String toString() {
        return  super.toString() + ", Nombre Completo = " + nombreCompleto + ", direccion = " + direccion + ", telefono = " + telefono  ;
    }


}
