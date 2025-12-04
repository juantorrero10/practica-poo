package Reestricion;

import Usuarios.Admin;
import Usuarios.AdminCentroSalud;
import Usuarios.Medico;
import Usuarios.Usuario;

import java.rmi.AccessException;

public class Reestricion {

    private static void lanzarExcepcion(String accion, String admitidos) throws AccessException {
        throw new AccessException("Acceso denegado para la accion: "+accion+"(), Solo: "+admitidos+" pueden realizar esta accion.");
    }

    public static void noPaciente(Usuario u, String accion)
            throws AccessException
    {
        if (!(u instanceof Medico) &&
                !(u instanceof Admin) &&
                !(u instanceof AdminCentroSalud)) {
            Reestricion.lanzarExcepcion(accion, " Médicos, administradores de centro y administradores generales");
        }
    }

    public static void adminCentro(Usuario u, String accion)
            throws AccessException
    {
        if (!(u instanceof Admin) &&
                !(u instanceof AdminCentroSalud)) {
            Reestricion.lanzarExcepcion(accion, "Administradores de centro y administradores generales");
        }
    }

    public static void adminSuper(Usuario u, String accion)
            throws AccessException
    {
        if (!(u instanceof Admin)) {
            Reestricion.lanzarExcepcion(accion, "Administradores generales");
        }
    }

    public static void medico(Usuario u, String accion)
            throws AccessException
    {
        if (!(u instanceof Admin) &&
                !(u instanceof Medico)) {
            Reestricion.lanzarExcepcion(accion, " Médicos y administradores generales");
        }
    }
}
