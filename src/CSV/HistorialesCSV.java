package CSV;

import backend.Agendas.Pacientes;
import backend.Enumeradores.TipoConsulta;
import backend.Enumeradores.TipoInforme;
import backend.GestionHistorial.Consulta;
import backend.GestionHistorial.Historial;
import backend.Usuarios.Paciente;

import javax.management.InvalidAttributeValueException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HistorialesCSV extends ProcesadorCSV {

    private Pacientes pacientes;
    private String carpeta;

    public HistorialesCSV(Pacientes pacientes, String carpeta) {
        super("");
        this.pacientes = pacientes;
        this.carpeta = carpeta;


        for (Paciente p : pacientes.getPacientes()) {
            long CIPA = p.getCIPA();
            String historialFile = CIPA + ".csv";
            Paths.get(carpeta+"/"+historialFile);
        }
    }

    /**
     * Formato Historial:
     * Tipo         <ElementoHistorial>
     * Consultas:
     *      Fecha       <Fecha>               "DD-MM-YYYY"
     *      Motivo      <String>
     *      Tipo        <TipoConsulta>
     *      TipoInforme <TipoInforme>        NULL -> "NO_ESPECIFICADO"
     *      RutaINf     <String>             NULL -> "--"
     *      Centro      <Centros>
     *      CIPAMED     <CIPA>
     *      Prescr      <boolean>            "true", "false"
     *      esVacuna    <boolean>                  !!
     *      NombreMed   <String>             NULL -> "--"
     *      Dosis       <int>                NULL -> "-1"
     *      Frec        <int>                NULL -> "-1"
     *      TP          <TipoPreescr.>
     *      FechaIn     <Fecha>              NULL -> "--"
     *      FechaFin    <Fecha>              NULL -> "--"
     *      FechaSig    <Fecha>              NULL -> "--" (Solo vacunas)
     * Pruebas:
     *      Fecha       <Fecha>
     *      Centro      <Centros>
     *      RutaInf     <String>             NULL -> "--"
     *      RutaImg     <String>             NULL -> "--" (Solo imagen)
     *      Descrip     <String>             NULL -> "--" (Solo lab.)
     */

    public Paciente importarHistorial(Paciente p) throws InvalidAttributeValueException {
        Path hist = Paths.get(carpeta+"/"+p.getCIPA()+".csv");
        ArrayList<String[]> contenido = importarCSV(hist);
        Historial historial = new Historial();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (String[] fila : contenido) {
            switch (ElementoHistorial.valueOf(fila[0])) {
                case CONSULTA:
                    Consulta c = new Consulta(
                            LocalDate.parse(fila[1], dtf),
                            fila[2],
                            TipoConsulta.valueOf(fila[3]),
                            TipoInforme.valueOf(fila[4]),
                            Centros.)
                    historial.agregarConsulta(new Consulta(
                    ));
            }

        }
    }


}
