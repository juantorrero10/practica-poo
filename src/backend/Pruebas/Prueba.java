package backend.Pruebas;

import backend.Enumeradores.Centros;

import java.time.LocalDateTime;

public abstract class Prueba {
    protected LocalDateTime fecha;
    protected Centros centro;
    protected String rutaInforme;


    public Prueba(LocalDateTime fecha, Centros centro, String rutaInforme) {
        this.fecha = fecha;
        this.centro = centro;
        this.rutaInforme = rutaInforme;
    }

    // Getters
    public LocalDateTime getFecha() { return fecha; }
    public Centros getCentro() { return centro; }
    public String getRutaInforme() { return rutaInforme; }

    @Override
    public String toString() {
        return "Fecha: " + fecha +
                "\nCentro:" + centro +
                "\nPathInforme: " + rutaInforme;
    }
}


