package Usuarios;


import Citas.Cita;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


public abstract class Usuario {

    private String dni;
    private long cipa;

    public Usuario(String dni, long cipa){

        this.dni=dni;
        this.cipa=cipa;
        Plantilla.añadirPlantilla(this);

    }


    public boolean modificarFechaHora(Cita cita, LocalDateTime fechaHora) {
        if (cita != null && !cita.isAnulada()) {
            cita.modificarFechaHora(fechaHora);
            return true;
        }
        return false;
    }


    public boolean cancelarCita(Cita cita , String causa){
        if(cita==null || causa==null || cita.isAnulada()) return false;
        cita.cancelar(causa);
        cita.getPaciente().eliminarCita(cita);
        return true;
    }



    public String getDNI(){
        return this.dni;
    }

    public void setDNI(String dni) {
        this.dni = dni;
    }

    public long getCIPA(){
        return this.cipa;
    }

    public void setCIPA(long cipa){
        this.cipa = cipa;
    }

    @Override
    public String toString() {
        return  "DNI=" + dni + ", cipa=" + cipa ;

    }


    @Override
    public boolean equals(Object o){
        if(this==o) return true;

        if(o==null || this.getClass()!=o.getClass()) return false;

        Usuario u = (Usuario) o;

        return (this.dni.equals(u.dni));
    }
}