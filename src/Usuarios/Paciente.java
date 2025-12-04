package Usuarios;

import Citas.Cita;
import Enumeradores.Especialidades;
import GestionHistorial.Historial;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;


public class Paciente extends Usuario{

    private final String nombreCompleto;
    private final String direccion;
    private long telefono;
    private ArrayList<Cita> arrayCitas; // historial de citas del paciente
    private Historial historial;


    public Paciente (String nombreCompleto, String direccion, long telefono , String DNI , long CIPA){

        super(DNI,CIPA);
        this.nombreCompleto=nombreCompleto;
        this.direccion=direccion;
        this.telefono=telefono;
        arrayCitas = new ArrayList<>();
        ListaPaciente.añadirListaPaciente(this);
    }

    public boolean asignarCitaAutomatica(Especialidades esp){
        Medico m = Plantilla.encontrarEspecialidadMedico(esp);
        LocalDate[] array = new LocalDate[5];
        Scanner scanner = new Scanner(System.in);

        if(m!=null){
            LocalDate diaBuscado = LocalDate.now();
            for(int i=0; i < 5; i++){						//Usamos dia buscado porque se va aumentando en cada iteración
                diaBuscado = m.encontrarProximoDiaDisponible(diaBuscado, 1);
                array[i] = diaBuscado;
            }
        } else {
            System.out.println("No se encontró médico para esa especialidad.");
            return false;
        }

        System.out.println("--- Fechas disponibles ---");
        for (int i = 0; i < 5; i++) {
            System.out.println("Opcion numero "+ i +": "+ array[i]);
        }

        System.out.println("Escoja la opcion deseada (0-4):");
        int j = scanner.nextInt();

        if (j < 0 || j >= 5) {
            System.out.println("Opción no válida.");
            return false;
        }

        LocalDate fechaElegida = array[j];

        LocalTime horaAsignada = m.encontrarPrimeraHoraDisponible(fechaElegida);


        if (horaAsignada == null) {
            System.out.println("Error: El día seleccionado (" + fechaElegida + ") se ha llenado. Inténtelo de nuevo.");
            return false;
        }

        //Combinamos la fecha elegida y la hora encontrada
        LocalDateTime fechaHoraCita = LocalDateTime.of(fechaElegida, horaAsignada);

        Cita asignada = new Cita(fechaHoraCita, this, m);

        this.arrayCitas.add(asignada);
        boolean anadidaMedico = !m.anadirCita(asignada);

        if (anadidaMedico) {
            System.out.println("Cita asignada con éxito: " + asignada.toString());
            return true;
        } else {
            System.out.println("Error: El médico no pudo aceptar la cita (agenda llena).");
            this.arrayCitas.remove(asignada);
            return false;
        }
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

    public boolean eliminarCita(Cita c){
        if(c==null) return false;

        if(!arrayCitas.contains(c)) return false;

        arrayCitas.remove(c);
        return true;
    }

    public ArrayList<Cita> getArrayCitas() {
        return arrayCitas;
    }

    public String getNombre(){
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


    @Override
    public boolean equals(Object o) {
        if(this==o) return true;

        if(o==null || this.getClass()!=o.getClass()) return false;

        Paciente p = (Paciente) o;

        return (this.getDNI().equals(p.getDNI()));
    }

    @Override
    public String toString() {
        return  "NombreCompleto = " + nombreCompleto + ", direccion = " + direccion + ", telefono = " + telefono  ;
    }


}
