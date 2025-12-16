package GestionHistorial;
import Agendas.AgendaCitas;
import Agendas.AgendaConsultas;
import Medicacion.Medicamento;
import Pruebas.Imagen;
import Pruebas.Laboratorio;
import Pruebas.Prueba;

import java.util.ArrayList;
import java.util.List;

public class Historial {
    private ArrayList<Consulta> consultas;
    private ArrayList<Imagen> pruebasImagen;
    private ArrayList<Laboratorio> pruebasLaboratorio;


    public Historial() {
        consultas = new ArrayList<>();
        pruebasImagen = new ArrayList<>();
        pruebasLaboratorio = new ArrayList<>();
    }


    /**
     * @return lista de medicamentos activos.
     */
    public List<Medicamento> getMedicamentosActivos() {
        ArrayList<Medicamento> ret = new ArrayList<>();

        for (Consulta c : consultas) {
            if (c.getPreescripcion().estaActivo()) {
                ret.add(c.getPreescripcion());
            }
        }

        return ret;
    }

    public String consultasToString() {
        return AgendaConsultas.arrayConsultasToString(consultas);
    }

    // Añadir elementos a los ArrayLists
    public void agregarImagen(Imagen pi) {
        pruebasImagen.add(pi);
    }
    public void agregarLabotorio(Laboratorio pl) {
        pruebasLaboratorio.add(pl);
    }
    public void agregarConsulta(Consulta c) {consultas.add(c);}

    public List<Imagen> getImagen() {
        return pruebasImagen;
    }
    public List<Laboratorio> getLaboratorio() {
        return pruebasLaboratorio;
    }
    public List<Consulta> getConsultas() { return consultas; }
}