package CSV;

import backend.Agendas.AgendaCitas;
import backend.Agendas.Pacientes;
import backend.Agendas.Plantilla;
import backend.Citas.Cita;
import backend.Usuarios.Medico;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CitasCSV extends ProcesadorCSV {



    public CitasCSV(String csv) {
        super(csv);
    }


    /**
     * Citas:
     * Fecha            <Fecha>:     "DD-MM-AAAA:HH:MM"
     * Paciente         <CIPA>
     * Medico           <CIPA>
     * Anulada          <boolean>    false, true
     * causa                         NULL -> "--"
     * Fecha can        <Fecha>      NULL -> "--"
     */



    public AgendaCitas importarCitas(Pacientes p, Plantilla pl) {
        ArrayList<String[]> contenido = importarCSV();
        ArrayList<Cita> arrayCitas = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm");

        for (String[] fila : contenido) {
            Cita c = new Cita(
                    LocalDateTime.parse(fila[0], dtf),
                    p.identificarPaciente(Long.parseLong(fila[1])),
                    (Medico) pl.getUsuarioCIPA(Long.parseLong(fila[2]))
            );
            if (fila[3].equals("true")) {
                c.cancelar(fila[4]);
                if (!fila[5].equals("--")) {
                    c.setFechaCancelacion(LocalDateTime.parse(fila[5], dtf));
                }
            }
            arrayCitas.add(c);
        }
        return new  AgendaCitas(arrayCitas);

    }

    public void exportarCitas(AgendaCitas ac) throws IOException {
        ArrayList<String[]> contenido = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm");

        for (Cita c : ac.getCitas()) {
            contenido.add(new String[]{
                    c.getFechaHora().format(dtf),
                    ((Long)c.getPaciente().getCIPA()).toString(),
                    ((Long)c.getMedico().getCIPA()).toString(),
                    (c.isAnulada()) ? "true" : "false",
                    (c.getCausaAnulacion().isEmpty()) ? "--" : c.getCausaAnulacion(),
                    (c.getFechaCancelacion() == null)? "--" : c.getFechaCancelacion().format(dtf)
            });
        }

        exportarCSV(contenido);

    }
}
