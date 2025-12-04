package Agendas;

import GestionHistorial.Consulta;
import Usuarios.Paciente;

import java.util.ArrayList;

public class AgendaConsultas {
    ArrayList<Consulta> consultas; // consultas de todos los usuarios.

    public AgendaConsultas(){
        consultas = new ArrayList<>();
    }

    public void añadirConsulta(Consulta consulta){
        consultas.add(consulta);
    }



}
