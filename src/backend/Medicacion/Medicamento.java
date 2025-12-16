package Medicacion;


import Enumeradores.TipoPreescripcion;

import javax.management.InvalidAttributeValueException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Medicamento {

    private String nombre;
    private int dosis;
    private int frecuencia;
    private TipoPreescripcion tipoPreescripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;


    public Medicamento(
            String nombre,int dosis, int frecuencia,
            TipoPreescripcion tipoPreescripcion,
            LocalDate fechaInicio, LocalDate fechaFin)
            throws InvalidAttributeValueException
    {
        this.nombre = nombre;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.tipoPreescripcion = tipoPreescripcion;
        this.fechaInicio = fechaInicio;

        if (tipoPreescripcion == TipoPreescripcion.CRONICO && fechaFin.isBefore(LocalDate.now().plusYears(10))) {
            throw new InvalidAttributeValueException("Las preescipciones crónicas tienen un mínimo de 10 años.");
        } else {
            this.fechaFin = fechaFin;
        }
    }

    public boolean estaActivo() {
        LocalDate hoy = LocalDate.now();
        return tipoPreescripcion == TipoPreescripcion.CRONICO && fechaFin.isBefore(hoy);
    }

    // Genera próximas fechas de retirada mensuales hasta fechaFin
    public List<LocalDate> calcularFechasRetirada() {
        List<LocalDate> fechas = new ArrayList<>();
        if(fechaFin == null) return fechas; // tratamiento crónico sin fin definido

        LocalDate fecha = fechaInicio;
        while (!fecha.isAfter(fechaFin)) {
            fechas.add(fecha);
            fecha = fecha.plusMonths(1);
        }
        return fechas;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Medicamento that = (Medicamento) o;
        return Objects.equals(nombre, that.nombre);
    }

    @Override
    public String toString() {
        String s = "medicamento: " + nombre;
        s += "\ndosis: " + dosis + " mg" +
                "\nfrecuencia: " + frecuencia + "veces/dia";
        s += "\ntipo de preescripcion: " + tipoPreescripcion;
        if (tipoPreescripcion == TipoPreescripcion.CRONICO) {
            s += "\nfecha inicio: " + fechaInicio;
            s += "\nfecha fin: " + fechaFin;
        }
        return s;
    }

    // Getters
    public String getNombre() { return nombre; }
    public int getDosis() { return dosis; }
    public int getFrecuencia() { return frecuencia; }
    public boolean isCronico() { return tipoPreescripcion == TipoPreescripcion.CRONICO; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
}
