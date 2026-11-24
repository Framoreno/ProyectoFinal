package Ud.Vista;

import Ud.Modelo.Llave;
import Ud.Modelo.Lupin;
import Ud.Modelo.Perro;
import Ud.Modelo.Tablero;
import Ud.Modelo.TipoCasilla;
import Ud.Modelo.Vigilante;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vista del juego (capa Vista del patron MVC).
 *
 * Unica responsabilidad: DIBUJAR el estado del modelo en pantalla. Pinta la
 * grilla del tablero y coloca el sprite de Lupin en su casilla. No lee el
 * teclado ni cambia posiciones; eso es tarea del Controlador. Asi la vista se
 * puede cambiar (otros colores, otro tamano) sin tocar el modelo.
 */
public class VistaTablero extends GridPane {

    /** Lado de cada casilla en pixeles. */
    public static final int TAMANO_CASILLA = 48;

    private final Tablero tablero;
    private final ImageView spriteLupin;
    private final List<ImageView> spritesPerros = new ArrayList<>();
    private ImageView spriteVigilante;
    private final Map<Llave, ImageView> spritesLlaves = new HashMap<>();

    public VistaTablero(Tablero tablero) {
        this.tablero = tablero;
        this.spriteLupin = crearSprite(Paths.SPRITE_LUPIN);
        dibujarGrilla();
        dibujarTesoro();
    }

    /**
     * Carga una imagen de resources/Sprites y la ajusta al tamano de una
     * casilla. Sirve para cualquier sprite (Lupin, tesoro, etc.).
     */
    private ImageView crearSprite(String ruta) {
    System.out.println("Cargando: [" + ruta + "] -> " + getClass().getResource(ruta));        
        Image imagen = new Image(getClass().getResourceAsStream(ruta));
        ImageView vista = new ImageView(imagen);
        vista.setFitWidth(TAMANO_CASILLA);
        vista.setFitHeight(TAMANO_CASILLA);
        vista.setPreserveRatio(true);
        return vista;
    }

    // Colores de cada tipo de casilla.
    private static final Color COLOR_PARED = Color.web("#6b4226");  // cafe
    private static final Color COLOR_PUERTA = Color.web("#c9a227"); // dorado
    private static final Color COLOR_LIBRE_CLARA = Color.web("#3b3b55");
    private static final Color COLOR_LIBRE_OSCURA = Color.web("#2a2a3d");

    /**
     * Pinta el fondo de la grilla: una casilla por cada fila y columna,
     * con un color segun su tipo. Las paredes van en cafe, la puerta en
     * dorado, el tesoro en verde, y las casillas libres en dos tonos
     * alternados tipo tablero de ajedrez.
     */
    private void dibujarGrilla() {
        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                Rectangle casilla = new Rectangle(TAMANO_CASILLA, TAMANO_CASILLA);
                casilla.setFill(colorDe(fila, columna));
                casilla.setStroke(Color.web("#1a1a28"));
                add(casilla, columna, fila);
            }
        }
    }

    /**
     * Devuelve el color de fondo de la casilla segun su tipo. La casilla del
     * tesoro se pinta como suelo libre, porque es transitable y encima lleva
     * su sprite.
     */
    private Color colorDe(int fila, int columna) {
        TipoCasilla tipo = tablero.getTipo(fila, columna);
        return switch (tipo) {
            case PARED -> COLOR_PARED;
            case PUERTA -> COLOR_PUERTA;
            case LIBRE, TESORO, LLAVE -> (fila + columna) % 2 == 0
                    ? COLOR_LIBRE_CLARA : COLOR_LIBRE_OSCURA;
        };
    }

    /**
     * Busca la casilla del tesoro en el tablero y coloca ahi su sprite.
     * El tesoro es parte del mapa (fijo), asi que se dibuja una sola vez.
     */
    private void dibujarTesoro() {
        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                if (tablero.getTipo(fila, columna) == TipoCasilla.TESORO) {
                    ImageView sprite = crearSprite(Paths.SPRITE_TESORO);
                    add(sprite, columna, fila);
                    return;
                }
            }
        }
    }

    /**
     * Coloca (o recoloca) el sprite de Lupin en la casilla donde esta ahora
     * segun el modelo. El controlador llama a este metodo cada vez que Lupin
     * se mueve.
     */
    public void dibujarLupin(Lupin lupin) {
        getChildren().remove(spriteLupin);
        add(spriteLupin, lupin.getColumna(), lupin.getFila());
    }

    /**
     * Crea un sprite para cada perro (perro1, perro2, perro3). Se llama una
     * sola vez al preparar la vista.
     */
    public void prepararPerros(List<Perro> perros) {
        spritesPerros.clear();
        for (int i = 0; i < perros.size(); i++) {
            String ruta = Paths.SPRITE_PERROS[i % Paths.SPRITE_PERROS.length];
            spritesPerros.add(crearSprite(ruta));
        }
    }

    /**
     * Coloca (o recoloca) cada perro en su casilla actual. El controlador la
     * llama cada vez que un perro se mueve. Es segura de llamar seguido.
     */
    public void dibujarPerros(List<Perro> perros) {
        for (int i = 0; i < perros.size(); i++) {
            ImageView sprite = spritesPerros.get(i);
            Perro perro = perros.get(i);
            getChildren().remove(sprite);
            add(sprite, perro.getColumna(), perro.getFila());
        }
    }

    /**
     * Dibuja las 3 llaves en sus casillas, cada una con el sprite de llave.
     * Guarda cada sprite para poder quitarlo cuando la llave se recoja.
     */
    public void dibujarLlaves(List<Llave> llaves) {
        for (Llave llave : llaves) {
            ImageView sprite = crearSprite(Paths.SPRITE_LLAVE);
            spritesLlaves.put(llave, sprite);
            add(sprite, llave.getColumna(), llave.getFila());
        }
    }

    /**
     * Quita de la pantalla los sprites de las llaves que Lupin ya recogio.
     * Una llave esta recogida cuando su casilla dejo de ser LLAVE en el
     * tablero (paso a LIBRE). El controlador llama a esto cuando Lupin se mueve.
     */
    public void actualizarLlaves() {
        var it = spritesLlaves.entrySet().iterator();
        while (it.hasNext()) {
            var entrada = it.next();
            Llave llave = entrada.getKey();
            if (tablero.getTipo(llave.getFila(), llave.getColumna())
                    != TipoCasilla.LLAVE) {
                getChildren().remove(entrada.getValue());
                it.remove();
            }
        }
    }

    /**
     * Crea el sprite del vigilante (policia). Se llama una sola vez.
     */
    public void prepararVigilante() {
        spriteVigilante = crearSprite(Paths.SPRITE_VIGILANTE);
    }

    /**
     * Coloca (o recoloca) al vigilante en su casilla actual. El controlador la
     * llama cuando el vigilante se mueve.
     */
    public void dibujarVigilante(Vigilante vigilante) {
        getChildren().remove(spriteVigilante);
        add(spriteVigilante, vigilante.getColumna(), vigilante.getFila());
    }

    /** Ancho total de la vista en pixeles, util para dimensionar la ventana. */
    public double anchoPreferido() {
        return (double) tablero.getColumnas() * TAMANO_CASILLA;
    }

    /** Alto total de la vista en pixeles. */
    public double altoPreferido() {
        return (double) tablero.getFilas() * TAMANO_CASILLA;
    }

    /** Marca esta region con un tamano fijo acorde al tablero. */
    public void ajustarTamano() {
        setMinSize(anchoPreferido(), altoPreferido());
        setPrefSize(anchoPreferido(), altoPreferido());
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    }
}
