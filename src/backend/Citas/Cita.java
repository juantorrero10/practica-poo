package backend.Citas;

import backend.Usuarios.Paciente;
import backend.Usuarios.Medico;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cita {
    private LocalDateTime fechaHora;
    private Paciente paciente;
    private Medico medico;

    private boolean anulada;
    private String causaAnulacion;
    private LocalDateTime fechaCancelacion;


    public Cita(LocalDateTime fechaHora , Paciente paciente, Medico medico) {
        this.paciente = paciente;
        this.medico = medico;
        this.fechaHora = fechaHora;
        this.anulada = false;
    }


    public void cancelar(String causa) {
        this.anulada = true;
        this.causaAnulacion = causa;
        this.fechaCancelacion = LocalDateTime.now();
        this.paciente.eliminarCita(this);
    }

    public void reagendar(LocalDateTime nueva){
        this.fechaHora = nueva;
    }


    public String notificarReagendar(LocalDateTime fechaAntigua){
        DateTimeFormatter f =DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm ");

        return "Su cita del "+fechaAntigua.format(f)+" ha sido pospuesta al dia "+ this.fechaHora.format(f);
    }

    public String notificar2Dias(){
        LocalDateTime dia= LocalDateTime.now().plusDays(2);

        for(Cita c: this.paciente.getArrayCitas()){
            if(c.fechaHora.isBefore(dia)){
                return "Recuerde su cita "+c.toString();
            }
        }
        return null;
    }

    public String imprimirFechaHora(){
        DateTimeFormatter f =DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm ");
        return  this.fechaHora.format(f);
    }

    public boolean isAnulada() {
        return this.anulada;
    }

    public LocalDateTime getFechaHora() {
        return this.fechaHora;
    }

    public String getCausaAnulacion() {
        return this.causaAnulacion;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public Medico getMedico() {
        return this.medico;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;

        if(o==null || this.getClass()!=o.getClass()) return false;

        Cita c = (Cita) o;

        return (this.fechaHora.equals(c.fechaHora) && this.paciente.equals(c.paciente)
                && this.medico.equals(c.medico));
    }

    @Override
    public String toString() {
        DateTimeFormatter f =DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm ");
        String s = "Cita:" + fechaHora.format(f) + "\nPaciente: \n" + paciente.toString() + "\nMedico: \n" + medico.toString();
        s += "\nEstado = "; s += (anulada)? "Anulada\n" : "Vigente\n";
        if (anulada) s += "Causa Anulacion: " +  causaAnulacion + "Fecha anulacion: \n" + fechaCancelacion.format(f);
        return s;
    }
}