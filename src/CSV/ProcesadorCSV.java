package CSV;

import Main.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ProcesadorCSV {

    private static Path fichero = null;
    private static final String SEPELEMENTO = ";";

    ProcesadorCSV(String Ruta) {
        if (Ruta.isEmpty()) {
            return;
        }
        try {
            fichero = Paths.get(Ruta);
        } catch (InvalidPathException e) {
            Log.ERR("Error al abrir el archivo: " + Ruta + ", " + e.getMessage());
            fichero = null;
        }

    }

    public void setFichero(String Ruta) { fichero = Paths.get(Ruta);}

    public boolean exportarCSV(ArrayList<String[]> valores, Path fichero) throws IOException {
        if (fichero == null) return false;

        List<String> lineas = new ArrayList<>();

        for (String[] fila : valores) {
            lineas.add(String.join(SEPELEMENTO, fila));
        }

        try {
            Files.write(fichero, lineas);
            Log.INFO("Exportado -> " + fichero.getFileName());
            return true;
        } catch (IOException e) {
            Log.ERR("Error al escribir el fichero: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<String[]> importarCSV(Path fichero) {
        ArrayList<String[]> contenido;

        try {

            contenido = Files.readAllLines(fichero)
                    .stream()
                    .map(l -> l.split(SEPELEMENTO))
                    .collect(Collectors.toCollection(ArrayList::new));

        } catch (IOException e) {
            Log.ERR("Error al cargar archivo CSV: " + e.getMessage());
            return null;
        }

        Log.INFO("Importado -> " + fichero.getFileName());
        if (contenido.isEmpty()) {
            Log.WARN("El archivo: " +  fichero.getFileName() + " esta vacio");
        }

        return contenido;
    }

    public boolean exportarCSV(ArrayList<String[]> val) throws IOException {
        return exportarCSV(val, fichero);
    }


    public ArrayList<String[]> importarCSV() { return importarCSV(fichero); }
}
