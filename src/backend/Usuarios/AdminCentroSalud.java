package backend.Usuarios;

import backend.Enumeradores.Centros;

public class AdminCentroSalud extends Usuario{

    private Centros centro;

    public AdminCentroSalud(String DNI , long CIPA , Centros centro){
        super(DNI,CIPA);
        this.centro = centro;
    }

    public Centros getCentro() {
        return centro;
    }


    @Override
    public String toString() {
        return super.toString() + ", Centro = " + centro.toString();
    }

}
