package CSV;

import Main.Log;
import backend.Agendas.Pacientes;
import backend.Agendas.Plantilla;
import backend.Enumeradores.Centros;
import backend.Enumeradores.TipoConsulta;
import backend.Enumeradores.TipoInforme;
import backend.Enumeradores.TipoPreescripcion;
import backend.GestionHistorial.Consulta;
import backend.GestionHistorial.Historial;
import backend.Medicacion.Medicamento;
import backend.Medicacion.Vacuna;
import backend.Pruebas.Imagen;
import backend.Pruebas.Laboratorio;
import backend.Pruebas.Prueba;
import backend.Usuarios.Medico;
import backend.Usuarios.Paciente;

import javax.management.InvalidAttributeValueException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HistorialesCSV extends ProcesadorCSV {


    private String carpeta;

    public HistorialesCSV(String carpeta) {
        super("");
        this.carpeta = carpeta;
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
     *      Fecha       <Fecha>              "DD-MM-YYYY:HH:mm"
     *      Centro      <Centros>
     *      RutaInf     <String>             NULL -> "--"
     *      RutaImg     <String>             NULL -> "--" (Solo imagen)
     *      Descrip     <String>             NULL -> "--" (Solo lab.)
     */

    public void exportarHistorial(Paciente p) throws IOException {
        ArrayList<String[]> contenido = new  ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm");


        Historial historial = p.getHistorial();
        for (Consulta c : historial.getConsultas()) {
            boolean noP = c.getPreescripcion() == null;
            contenido.add(new String[] {
                    ElementoHistorial.CONSULTA.toString(),
                    c.getFecha().format(dtf),
                    c.getMotivo(),
                    c.getTipoConsulta().toString(),
                    c.getTipoConsulta().toString(),
                    c.getRutaInforme(),
                    c.getCentro().toString(),
                    ((Long)c.getMedico().getCIPA()).toString(),
                    (noP)? "false" : "true",
                    (noP)? "false" : (c.getPreescripcion() instanceof Vacuna)? "true" : "false",
                    (noP)? "--" : (c.getPreescripcion().getNombre()),
                    (noP)? "-1" : ((Integer)(c.getPreescripcion().getDosis())).toString(),
                    (noP)? "-1" : ((Integer)(c.getPreescripcion().getFrecuencia())).toString(),
                    (noP)? "NO_ESPECIFICADO" : (c.getPreescripcion().getTipoPreescripcion().name()),
                    (noP)? "--" :  (c.getPreescripcion().getFechaInicio().format(dtf)),
                    (noP)? "--" :  (c.getPreescripcion().getFechaFin().format(dtf)),
                    (noP)? "--" : (c.getPreescripcion() instanceof Vacuna)?
                            ((Vacuna)(c.getPreescripcion())).getFechaSgteDosis().format(dtf) : "--"
            });
        }
        for (Imagen i : historial.getImagen()) {
            contenido.add(new String[] {
                    ElementoHistorial.PRUEBAIMAGEN.toString(),
                    i.getFecha().format(dtf2),
                    i.getCentro().toString(),
                    i.getRutaInforme(),
                    i.getRutaImagen()
            });
        }

        for (Laboratorio l : historial.getLaboratorio()) {
            contenido.add(new String[] {
                    ElementoHistorial.PRUEBALABORATORIO.toString(),
                    l.getFecha().format(dtf2),
                    l.getCentro().toString(),
                    l.getRutaInforme(),
                    l.getInforme()
            });
        }

        exportarCSV(contenido, Paths.get(carpeta+"/"+p.getCIPA()+".csv"));
    }

    public Paciente importarHistorial(Paciente p, Plantilla pl) throws InvalidAttributeValueException {
        Path hist = Paths.get(carpeta+"/"+p.getCIPA()+".csv");
        ArrayList<String[]> contenido = importarCSV(hist);
        Historial historial = new Historial();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm");
        if (contenido == null) {
            p.setHistorial(historial);
            return p;
        }

        for (String[] fila : contenido) {
            switch (ElementoHistorial.valueOf(fila[0])) {
                case CONSULTA:
                    Consulta c = new Consulta(
                            LocalDate.parse(fila[1], dtf),
                            fila[2],
                            TipoConsulta.valueOf(fila[3]),
                            TipoInforme.valueOf(fila[4]),
                            Centros.valueOf(fila[6]),
                            (Medico)pl.getUsuarioCIPA(Long.parseLong(fila[7])));
                    c.setRutaInforme(fila[5]);
                    if (fila[8].equals("true")) {
                        Medicamento m = new Medicamento(
                                fila[10],
                                Integer.parseInt(fila[11]),
                                Integer.parseInt(fila[12]),
                                TipoPreescripcion.valueOf(fila[13]),
                                (fila[14].equals("--"))? null : LocalDate.parse(fila[14], dtf),
                                (fila[15].equals("--"))? null : LocalDate.parse(fila[15], dtf));
                        if (fila[9].equals("true")) {
                            c.recetarMedicamento(new Vacuna
                                    (m,
                                    (fila[16].equals("--"))? null : LocalDate.parse(fila[16], dtf)));
                        }
                        else {
                            c.recetarMedicamento(m);
                        }
                    }
                    historial.agregarConsulta(c);
                    break;
                case PRUEBAIMAGEN:
                case PRUEBALABORATORIO:

                    LocalDateTime ld =  LocalDateTime.parse(fila[1], dtf2);
                    Centros centro = Centros.valueOf(fila[2]);
                    String rutaInforme =  fila[3];
                    if (ElementoHistorial.valueOf(fila[0]).equals(ElementoHistorial.PRUEBAIMAGEN)) {
                        Imagen img = new Imagen(ld, centro, rutaInforme, fila[4]);
                        historial.agregarImagen(img);
                    } else {
                        Laboratorio lb = new Laboratorio(ld, centro, rutaInforme, fila[5]);
                        historial.agregarLabotorio(lb);
                    }
                    break;
                default:
                    Log.ERR("Tipo de elemento no válido");
                    break;
            }
        }
        p.setHistorial(historial);
        return p;
    }




}
