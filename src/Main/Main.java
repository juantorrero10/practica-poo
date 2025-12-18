package Main;


import CSV.HistorialesCSV;
import backend.Agendas.AgendaCitas;
import backend.Agendas.AgendaConsultas;
import Controlador.Controlador;
import backend.Usuarios.Paciente;
import interfaz.MainVentana;

import javax.management.InvalidAttributeValueException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws InvalidAttributeValueException, IOException {

        inicializarSistemaArchivos();

        //Inicializacion temporal.

        Controlador c = new Controlador("datos/Plantilla.csv", "datos/Pacientes.csv");
        c.cargarCitas("datos/Citas.csv");
        c.cargarHistoriales("datos/Historiales");
        

        Log.INFO("Usuarios en el sistema: ");
        System.out.println(c.stringGetUsuarios());

        // Iniciar Interfaz gráfica.
        new MainVentana(c).setVisible(true);

    }

    private static void inicializarSistemaArchivos() {

        Path dataDir = Paths.get("datos");
        Path historialesDir = dataDir.resolve("Historiales");

        Path pacientes = dataDir.resolve("Pacientes.csv");
        Path plantilla = dataDir.resolve("Plantilla.csv");
        Path citas = dataDir.resolve("Citas.csv");

        Log.INFO("Iniciando sistema de ficheros");

        try {
            // Crear carpeta datos/
            Files.createDirectories(dataDir);

            // Crear carpeta Historiales/
            Files.createDirectories(historialesDir);

            // Crear CSV si no existen
            crearCSVSiNoExiste(pacientes);
            crearCSVSiNoExiste(plantilla);
            crearCSVSiNoExiste(citas);

        } catch (IOException e) {
            Log.ERR("Error inicializando el sistema de ficheros: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void crearCSVSiNoExiste(Path fichero) throws IOException {
        if (Files.notExists(fichero)) {
            Files.createFile(fichero);
        }
    }
}