package backend.Medicacion;


import backend.Enumeradores.TipoPreescripcion;

import javax.management.InvalidAttributeValueException;
import java.time.LocalDate;

public class Vacuna extends Medicamento {
    private final LocalDate fechaSgteDosis;

    public Vacuna(Medicamento m, LocalDate fechaSgteDosis) {
        super(m);
        this.fechaSgteDosis = fechaSgteDosis;
    }

    public Vacuna(String nombre, int dosis, int frecuencia, TipoPreescripcion prescripcion,
                  LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaSgteDosis)
            throws InvalidAttributeValueException {

        super(nombre, dosis, frecuencia, prescripcion , fechaInicio, fechaFin);
        this.fechaSgteDosis = fechaSgteDosis;
    }

    public LocalDate getFechaSgteDosis() {
        return fechaSgteDosis;
    }

    public boolean necesitaOtraDosis() {
        return fechaSgteDosis != null && LocalDate.now().isBefore(fechaSgteDosis);
    }

    @Override
    public String toString() {
        return super.toString() +" fecha siguiente dosis " + this.fechaSgteDosis +"\n";
    }
}