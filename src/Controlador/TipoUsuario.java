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

    public static TipoUsuario fromString(String texto) {
        if (texto == null) {
            return NO_ESPECIFICADO;
        }

        for (TipoUsuario t : values()) {
            if (t.t.equalsIgnoreCase(texto.trim())) {
                return t;
            }
        }

        return NO_ESPECIFICADO;
    }
}


