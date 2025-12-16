package Agendas;

import Citas.Cita;
import Enumeradores.Centros;
import Enumeradores.Especialidades;
import Enumeradores.TipoConsulta;
import Enumeradores.TipoInforme;
import GestionHistorial.Consulta;
import Reestricion.Reestricion;
import Usuarios.*;


import javax.management.InvalidAttributeValueException;
import java.rmi.AccessException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AgendaCitas {
    private ArrayList<Cita> citas;

    public AgendaCitas() {
        citas = new ArrayList<>();
    }
    private void anadirCitas(Cita cita) {
        citas.add(cita);
    }


    private List<Cita> getCitaUsuario(Object o, List<Cita> lista) {
        if (lista == null) { lista = citas; }
        List<Cita> r = new ArrayList<>();
        for (Cita c : lista) {
            if      (o instanceof Medico   && c.getMedico().equals(o)) { r.add(c); }
            else if (o instanceof Paciente && c.getPaciente().equals(o)) { r.add(c); }
        }
        return r;
    }

    private List<Cita> getCitaUsuario(Object o) {
        return getCitaUsuario(o, citas);
    }

    public List<Cita> obtenerCitasPaciente(Pacientes paciente) {
        return  getCitaUsuario(paciente);
    }
    public List<Cita> obtenerCitasPaciente(Pacientes paciente, List<Cita> lista) {
        return  getCitaUsuario(paciente, lista);
    }

    public List<Cita> obtenerCitasMedico(Medico medico) {return getCitaUsuario(medico);}
    public List<Cita> obtenerCitasMedico(Medico medico, List<Cita> lista) {
        return getCitaUsuario(medico, lista);
    }

    public List<Cita> obtenerCitasDia(LocalDate dia) {
        return getCitaUsuario(dia, citas);
    }
    public List<Cita> obtenerCitasDia(LocalDate dia, List<Cita> lista) {
        List <Cita> r = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getFechaHora().toLocalDate().equals(dia)) { r.add(c); }
        }
        return r;
    }


    /**
     * Agregar una cita solo si el medico tiene menos del maximo permitidas en un día.
     * Los pacientes no pueden realizar esta operacion.
     */
    public boolean agregarCita(Usuario u, Cita cita, int MAX_CITAS_MEDICOS) throws AccessException {
        if (cita == null || citas.contains(cita)) return false;
        Reestricion.noPaciente(u, "AgendaCitas.agregarCitas");
        List<Cita> lista = obtenerCitasMedico(cita.getMedico());
        List<Cita> lista2 = obtenerCitasDia(cita.getFechaHora().toLocalDate(), lista);
        if (lista2.size() > MAX_CITAS_MEDICOS) { return false; }
        citas.add(cita);
        return true;
    }

    public List<Cita> getCitasPreviasA(LocalDate fecha) {
        List<Cita> r = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getFechaHora().toLocalDate().equals(fecha)) { r.add(c); }
        }
        return r;
    }

    public void eliminarCitasPasadas(Usuario u, LocalDate fecha) throws AccessException {
        Reestricion.noPaciente(u, "AgendaCitas.eliminarCitasPasadas");
        citas.removeIf(c -> c.getFechaHora().toLocalDate().isBefore(fecha));
    }

    /**
     * Reagendar todas las citas de un día al otro.
     * Los pacientes no pueden realizar esta acción.
     */
    public void reagendarCitasDia(Usuario usuario, LocalDate fecha, LocalDateTime nueva)
            throws AccessException
    {
        Reestricion.noPaciente(usuario, "AgendaCitas.reagendarCitasDia");

        for (Cita c : citas) {
            if (c.getFechaHora().toLocalDate().equals(fecha)) {
                c.reagendar(nueva);}
        }
    }

    /**
     * Actualizar todas las listas.
     * Solo los administradores generales pueden realizar esta accion.
     */
    public void actualizarCitas(Usuario u, Plantilla p) throws AccessException {
        Reestricion.adminSuper(u, "agendaCitas.actualizarCitas");
        ArrayList<Cita> lista = new ArrayList<>();
        for (Medico m : p.getMedicos()) {
            lista.addAll(m.getAgenda());
        }
        citas.clear();
        citas = lista;
    }

    /**
     * Actualizar todas las listas.
     * Solo los administradores generales pueden realizar esta accion.
     */
    public void actualizarCitasCentro(Usuario u, Plantilla p, Centros c) throws AccessException {
        Reestricion.adminCentro(u, "agendaCitas.actualizarCitasCentro");
        for (Medico m : p.getMedicosCentro(c)) {
            for (Cita cita : m.getAgenda()) {
                if (!citas.contains(cita)) {
                    citas.add(cita);
                }
            }
        }
    }

    public List<Cita> getCitas(Usuario u) throws AccessException {
        Reestricion.adminCentro(u, "AgendaCitas.getCitas");
        return citas;
    }

    // Citas automaticas
    // yo creo que mejor hacer un Paciente.asignarCitaAutomatica y luego
    // en el main agendaCitas.anadirCita(cita); eso es lo que estaba pensando. Estoy haciendo esa funcion
    // voy a hacer un main ahora para que entiendas mi vision Okay yo hago esta funcion y correcion de errores.
    // Te voy a comentar el codigo un momento pa ver si compila
    //okay
    /*
    public Cita agregarCitaAutomatica(Especialidades especialidad, Paciente paciente, Plantilla plantilla){ // Pasamos la plantilla completa
        ArrayList<Medico> disponibles = new ArrayList<>();

        for(Medico m: plantilla.getMedicos()){
            if(m.getEspecialidad().equals(especialidad))disponibles.add(m);
        }
        if(disponibles.isEmpty()) return null;

        LocalDate dia = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(8, 0);

        while(true){
            for(Medico m: disponibles){
                LocalDateTime fechaHora = LocalDateTime.of(dia, hora);
                Cita posible = new Cita(fechaHora,paciente,m);

                if(comprobarDisponibilidad(posible) && !m.getAgenda().contains(posible)){
                    citas.add(posible);
                    m.anadirCita(paciente,posible);
                    paciente.getArrayCitas().add(posible);
                    return  posible;
                }

            }

            //Aumentamos la hora
            hora = hora.plusHours(1);
            if(hora.isAfter(LocalTime.of(13,0))){
                dia = dia.plusDays(1);
                hora = hora.plusHours(1);

            }

        }


    }*/

    public boolean comprobarDisponibilidad(Cita c){
        for(Cita cita: citas){
            if(cita.getFechaHora().equals(c.getFechaHora())&&(cita.getMedico().equals(c.getMedico()))) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        int n = 0;
        int sz = citas.size();
        String s = "=========Listado de citas========\n";
        for (Cita c : citas) {
            s += "Cita "+n+" de "+ citas.size()+": =========\n";
            s += c.toString() + "\n";
            n++;
        }
        return s;
    }
}
