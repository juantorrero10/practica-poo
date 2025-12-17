package backend.Usuarios;

import backend.Citas.Cita;
import backend.Enumeradores.*;
import backend.Enumeradores.Centros;
import backend.Enumeradores.Especialidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Medico extends Usuario{

    private static final int MAXCITASDIARIAS = 20;
    private ArrayList<Cita> agendaCitas;
    private Especialidades especialidad;
    private Centros centro;



    public Medico(String DNI , long CIPA , Especialidades especialidad , Centros c){

        super(DNI,CIPA);
        this.especialidad = especialidad;
        this.centro = c;
        agendaCitas = new ArrayList<>();
    }

    public Especialidades getEspecialidad() {
        return especialidad;
    }

    public Centros getCentro() {
        return this.centro;
    }

    public List<Cita> getAgenda() {
        return agendaCitas;
    }


    public boolean anadirCita(Cita c) {
        if (c == null) return false;
        if (getCitasParaDia(c.getFechaHora().toLocalDate()) >= MAXCITASDIARIAS ||
            agendaCitas.contains(c)) {
            return false;
        }
        agendaCitas.add(c);
        return true;
    }

    public void visualizarCitas(){
        for(Cita cita: agendaCitas){
            System.out.println(cita.toString());
        }
    }

    public LocalDate encontrarProximoDiaDisponible(LocalDate inicio, int citasAMover) {
        // Empezamos a buscar desde el día siguiente a la fecha indicada.
        LocalDate fechaActual = inicio.plusDays(1);

        // El bucle comprueba si citas existentes + citas a mover exceden el límite de 20 diarias.
        while((getCitasParaDia(fechaActual) + citasAMover) >= MAXCITASDIARIAS) {
            fechaActual = fechaActual.plusDays(1); // Avanza al día siguiente
        }

        return fechaActual;
    }

    public LocalTime encontrarPrimeraHoraDisponible(LocalDate fecha) {
        if (getCitasParaDia(fecha) >= MAXCITASDIARIAS) {
            return null;
        }

        //Creamos un set de las horas ya ocupadas para ese día para controlar repetidos
        HashSet<LocalTime> horasOcupadas = new HashSet<>();
        for (Cita cita : agendaCitas) {
            if (cita.getFechaHora().toLocalDate().isEqual(fecha) && !cita.isAnulada()) {
                // Añadimos solo la hora al set
                horasOcupadas.add(cita.getFechaHora().toLocalTime());
            }
        }

        //Iteramos por los huecos del día desde las 9 de la mañana
        LocalTime horaInicio = LocalTime.of(9, 0);

        for (int i = 0; i < MAXCITASDIARIAS; i++) {
            LocalTime horaPrueba = horaInicio.plusMinutes(i * 30);

            // Si el conjunto de horasOcupadas no contiene esta hora la retornamos
            if (!horasOcupadas.contains(horaPrueba)) {
                return horaPrueba; // primer hueco libre
            }
        }

        return null;
    }


    public int getCitasParaDia(LocalDate fecha) {
        int contador = 0;

        for (Cita cita : agendaCitas) {
            if (!cita.isAnulada() && cita.getFechaHora().toLocalDate().isEqual(fecha)) {
                contador++;
            }
        }

        return contador;
    }

    public int reagendarCitas(LocalDate antigua){
        //Contamos cuántas citas activas hay en el día antiguo que necesitan ser movidas
        int citasAMover = 0;
        for(Cita cita : agendaCitas) {
            if (!cita.isAnulada() && cita.getFechaHora().toLocalDate().isEqual(antigua)) {
                citasAMover++;
            }
        }

        if (citasAMover == 0) {
            return 0;
        }

        //Encontrar el próximo día disponible, pasando el numero de citas a mover
        LocalDate nuevaFecha = encontrarProximoDiaDisponible(antigua, citasAMover);

        int citasMovidas = 0;

        //encontramos y movemos las citas
        for(Cita cita : agendaCitas) {

            if (!cita.isAnulada() && cita.getFechaHora().toLocalDate().isEqual(antigua)) {

                LocalDateTime nuevaFechaHora = cita.getFechaHora().with(nuevaFecha);
                LocalDateTime fechaAntigua = cita.getFechaHora();
                cita.reagendar(nuevaFechaHora);
                cita.notificarReagendar(fechaAntigua);
                citasMovidas++;
            }
        }


        return citasMovidas;
    }

    @Override
    public String toString() {
        return super.toString()  + ", especialidad = " + especialidad + ", Centro = " + centro ;
    }


}
