package backend.Agendas;

import Main.Log;
import backend.Citas.Cita;
import backend.Enumeradores.Centros;
import backend.Usuarios.Medico;
import backend.Usuarios.Paciente;
import backend.Usuarios.Usuario;


import java.rmi.AccessException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AgendaCitas {
    private ArrayList<Cita> citas;

    public AgendaCitas() {
        citas = new ArrayList<>();
    }

    public AgendaCitas(ArrayList<Cita> citas) {
        this.citas = citas;
    }
    private void anadirCitas(Cita cita) {
        citas.add(cita);
    }


    private List<Cita> getCitaUsuario(Object o, List<Cita> lista) {
        if (lista == null) { lista = citas; }
        List<Cita> r = new ArrayList<>();
        for (Cita c : lista) {
            if      (o instanceof Medico && c.getMedico().equals(o)) { r.add(c); }
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
    public boolean agregarCita(Cita cita, int MAX_CITAS_MEDICOS) {
        if (cita == null || citas.contains(cita)) return false;
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

    public void eliminarCitasPasadas(LocalDate fecha) {
        citas.removeIf(c -> c.getFechaHora().toLocalDate().isBefore(fecha));
    }

    /**
     * Reagendar todas las citas de un día al otro.
     * Los pacientes no pueden realizar esta acción.
     */
    public void reagendarCitasDia(LocalDate fecha, LocalDateTime nueva)
    {

        for (Cita c : citas) {
            if (c.getFechaHora().toLocalDate().equals(fecha)) {
                c.reagendar(nueva);}
        }
    }

    /**
     * Actualizar todas las listas.
     * Solo los administradores generales pueden realizar esta accion.
     */
    public void actualizarCitas(Plantilla p) throws AccessException {
        ArrayList<Cita> lista = new ArrayList<>();
        for (Medico m : p.getMedicos()) {
            lista.addAll(m.getAgenda().getCitas());
        }
        citas.clear();
        citas = lista;
    }

    /**
     * Actualizar todas las listas.
     * Solo los administradores generales pueden realizar esta accion.
     */
    public void actualizarCitasCentro(Plantilla p, Centros c){
        for (Medico m : p.getMedicosCentro(c)) {
            for (Cita cita : m.getAgenda().getCitas()) {
                if (!citas.contains(cita)) {
                    citas.add(cita);
                }
            }
        }
    }

    public List<Cita> getCitas(){
        return citas;
    }

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


    public int anularCita(Cita c, String mot) {
        int n = 0;
        if (!citas.contains(c)) {
            Log.WARN("Se ha intentado cancelar una cita que no existe.");
            return 1;
        }
        else if (c.isAnulada()) {
            Log.WARN("La cita ya esta cancelada.");
            return 2;
        }

        n = citas.indexOf(c);
        c.cancelar(mot);
        citas.set(n, c);
        return 0;
    }

    public void eliminarCita(Cita c) {
        citas.remove(c);
    }
}
