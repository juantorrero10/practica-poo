package Usuarios;

import Enumerados.Especialidades;
import java.util.ArrayList;




public class Plantilla {

    private static ArrayList <Usuario> plantilla;


    public Plantilla(){
        plantilla= new ArrayList <>();
    }

    public static boolean añadirPlantilla(Usuario usuario){
        if (usuario==null || plantilla.contains(usuario)) return false;
        plantilla.add(usuario);
        return true;
    }

    public static Medico encontrarEspecialidadMedico(Especialidades esp){

        for(Usuario u: plantilla){

            if(u instanceof Medico) {
                Medico medico= (Medico) u;
                if(medico.getEspecialidad().equals(esp)) return medico;
            }
        }
        return null;
    }


}
