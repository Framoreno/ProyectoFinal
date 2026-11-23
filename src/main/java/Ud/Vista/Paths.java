package Ud.Vista;

/**
 * Rutas a los recursos (escenas e imagenes) dentro de resources.
 * Tenerlas en un solo sitio evita repetir cadenas magicas por el codigo.
 */
public class Paths {
    public static final String ESCENA_PRINCIPAL = "/Escenas/EscenaPrincipal.fxml";
    public static final String ESCENA_JUEGO = "/Escenas/Juego.fxml";

    // Sprites. Los archivos .jfif son imagenes JPEG y JavaFX los carga igual.
    public static final String SPRITE_LUPIN = "/Sprites/Lupin.jfif";
    public static final String SPRITE_TESORO = "/Sprites/Tesoro.jfif";
    public static final String SPRITE_LLAVE = "/Sprites/llave.jfif";
    public static final String SPRITE_VIGILANTE = "/Sprites/Policia.jfif";
    public static final String[] SPRITE_PERROS = {
            "/Sprites/perro1.jfif",
            "/Sprites/perro2.jfif",
            "/Sprites/perro3.jfif"
    };
}
