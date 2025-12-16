package backend.Pruebas;


import backend.Enumeradores.Centros;

import java.time.LocalDateTime;

public class Laboratorio extends Prueba {
    private String informe;
    public Laboratorio(LocalDateTime fecha, Centros centro, String rutaInforme, String informe) {
        super(fecha, centro, rutaInforme);
        this.informe = informe;
    }

    @Override
    public String toString() {
        return "Laboratorio " +
                "informe: " + informe + '\'' +
                super.toString();
    }
}