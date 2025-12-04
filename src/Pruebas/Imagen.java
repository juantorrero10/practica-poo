package Pruebas;

import Enumeradores.Centros;

import java.time.LocalDateTime;

// Imagen
public class Imagen extends Prueba {
    private String rutaImagen;

    public Imagen(LocalDateTime fecha, Centros centro, String rutaInforme, String rutaImagen) {
        super(fecha, centro, rutaInforme);
        this.rutaImagen = rutaImagen;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    @Override
    public String toString() {
        return "Imagen " +
                "Ruta de Imagen: " + rutaImagen  +
                super.toString();
    }
}