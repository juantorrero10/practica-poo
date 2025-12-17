package Controlador;

import backend.Usuarios.*;

public enum TipoUsuario {
    NO_ESPECIFICADO("No especificado"),
    PACIENTE("Paciente"),
    MEDICO("Médico"),
    ADMINCENTRO("Admin Centro de Salud"),
    ADMIN("Admin");

    private final String t;

    TipoUsuario(String t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return this.t;
    }


    public static TipoUsuario getTipoUsuario(Usuario u) {
        if (u instanceof Paciente) {
            return PACIENTE;
        } else if (u instanceof Medico) {
            return MEDICO;
        } else if (u instanceof AdminCentroSalud) {
            return ADMINCENTRO;
        } else if (u instanceof Admin) {
            return ADMIN;
        }
        return NO_ESPECIFICADO;
    }
}


