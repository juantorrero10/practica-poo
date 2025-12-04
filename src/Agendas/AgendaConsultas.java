package Agendas;

import GestionHistorial.Consulta;
import Reestricion.Reestricion;
import Usuarios.Paciente;
import Usuarios.Usuario;

import java.rmi.AccessException;
import java.util.ArrayList;

public class AgendaConsultas {
    ArrayList<Consulta> consultas; // consultas de todos los usuarios.

    public AgendaConsultas(){
        consultas = new ArrayList<>();
    }

    public void agregarConsulta(Usuario u, Consulta consulta) throws AccessException {
        Reestricion.noPaciente(u, "AgendaConsultas.agregarConsulta");
        consultas.add(consulta);
    }



}
