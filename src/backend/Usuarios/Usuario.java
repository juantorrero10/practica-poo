package backend.Usuarios;


import backend.Citas.Cita;

import java.time.LocalDateTime;


public abstract class Usuario {

    protected String dni;
    protected long cipa;

    public Usuario(String dni, long cipa){

        this.dni=dni;
        this.cipa=cipa;

    }


    public boolean reagendarCita(Cita cita, LocalDateTime fechaHora) {
        if (cita != null && !cita.isAnulada()) {
            cita.reagendar(fechaHora);
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

        if(o==null || this.getClass()!=o.getClass()) return false;

        Usuario u = (Usuario) o;

        return (this.cipa == u.cipa);
    }
}