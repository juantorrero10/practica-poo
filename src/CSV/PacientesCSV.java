package CSV;

import backend.Agendas.Pacientes;
import backend.Usuarios.Paciente;

import java.io.IOException;
import java.util.ArrayList;

public class PacientesCSV extends ProcesadorCSV{

    public PacientesCSV(String ruta){ super(ruta); }

    public Pacientes importarPacientes(){
        ArrayList<String[]> contenido = importarCSV();
        ArrayList<Paciente> arrayPacientes = new ArrayList<>();

        for (String[] fila : contenido) {
            arrayPacientes.add(new Paciente(
                    fila[0],
                    fila[1],
                    Integer.parseInt(fila[2]),
                    fila[3],
                    Integer.parseInt(fila[4])
            ));
        }

        return new Pacientes( arrayPacientes );
    }

    public boolean exportarPacientes(Pacientes pacientes) throws IOException {
        ArrayList<String[]> contenido = new ArrayList<>();

        for( Paciente p : pacientes.getPacientes()){
            contenido.add(new String[]{
                    p.getNombreCompleto(),
                    p.getDireccion(),
                    ((Long)p.getTelefono()).toString(),
                    p.getDNI(),
                    ((Long)p.getCIPA()).toString()
                    });
        }

        return exportarCSV(contenido);
    }
}
