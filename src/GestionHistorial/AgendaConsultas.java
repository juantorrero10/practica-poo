package GestionHistorial;

import Usuarios.Medico;

public class AgendaConsultas {
    private String motivo;
    private boolean telefonica;
    private boolean informeSeguimiento;
    private Medico medico;

    public AgendaConsultas(String motivo, boolean telefonica, boolean informeSeguimiento, Medico medico) {
        this.motivo = motivo;
        this.telefonica = telefonica;
        this.informeSeguimiento = informeSeguimiento;
        this.medico = medico;
    }

    // Getters y setters
    public String getMotivo() { return motivo; }
    public boolean isTelefonica() { return telefonica; }
    public boolean isInformeSeguimiento() { return informeSeguimiento; }
    public Medico getMedico() { return medico; }
}