package interaciones;

public enum TipoUsuario {
    NO_ESPECIFICADO("No especificado"),
    PACIENTE("Paciente"),
    MEDICO("Médico"),
    ADMINCENTRO("Admin Centro de Salud"),
    ADMIN("Super usuario");

    private final String t;

    TipoUsuario(String t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return this.t;
    }
}


