package Ud.Vista;

/**
 * Rutas a los recursos (escenas e imagenes) dentro de resources.
 * Tenerlas en un solo sitio evita repetir cadenas magicas por el codigo.
 */
public class Paths {
    public static final String ESCENA_PRINCIPAL = "/Escenas/EscenaPrincipal.fxml";
    public static final String ESCENA_JUEGO = "/Escenas/Juego.fxml";

    // Sprites. Los archivos .jfif son imagenes JPEG y JavaFX los carga igual.
public static final String SPRITE_LUPIN = "/Sprites/Lupinfinal.jpg";
public static final String SPRITE_TESORO = "/Sprites/Tesorofinal.jpg";
public static final String SPRITE_LLAVE = "/Sprites/Llavefinal.jpg";
public static final String SPRITE_VIGILANTE = "/Sprites/Policiafinal.jpg";
public static final String[] SPRITE_PERROS = {
    "/Sprites/Perro1final.jpg",
    "/Sprites/Perro2final.jpg",
    "/Sprites/Perro3final.jpg"
};
}
