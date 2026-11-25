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
 * vista del juego
 * dibujar el estado del modelo en pantalla
 * pinta la grilla del tablero y coloca el sprite de Lupin en su casilla
 */
public class VistaTablero extends GridPane {

    public static final int TAMANO_CASILLA = 48;

    private final Tablero tablero;
    private final ImageView spriteLupin;
    private final List<ImageView> spritesPerros = new ArrayList<>();
    private final List<ImageView> spritesVigilantes = new ArrayList<>();
    private final Map<Llave, ImageView> spritesLlaves = new HashMap<>();

    public VistaTablero(Tablero tablero) {
        this.tablero = tablero;
        this.spriteLupin = crearSprite(Paths.SPRITE_LUPIN);
        dibujarGrilla();
        dibujarTesoro();
    }


    private ImageView crearSprite(String ruta) {
    System.out.println("Cargando: [" + ruta + "] -> " + getClass().getResource(ruta));        
        Image imagen = new Image(getClass().getResourceAsStream(ruta));
        ImageView vista = new ImageView(imagen);
        vista.setFitWidth(TAMANO_CASILLA);
        vista.setFitHeight(TAMANO_CASILLA);
        vista.setPreserveRatio(true);
        return vista;
    }

    private static final Color COLOR_PARED = Color.web("#6b4226");  // cafe
    private static final Color COLOR_PUERTA = Color.web("#c9a227"); // dorado
    private static final Color COLOR_LIBRE_CLARA = Color.web("#3b3b55");
    private static final Color COLOR_LIBRE_OSCURA = Color.web("#2a2a3d");


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

    private Color colorDe(int fila, int columna) {
        TipoCasilla tipo = tablero.getTipo(fila, columna);
        return switch (tipo) {
            case PARED -> COLOR_PARED;
            case PUERTA -> COLOR_PUERTA;
            case LIBRE, TESORO, LLAVE -> (fila + columna) % 2 == 0
                    ? COLOR_LIBRE_CLARA : COLOR_LIBRE_OSCURA;
        };
    }

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

    public void dibujarLupin(Lupin lupin) {
        getChildren().remove(spriteLupin);
        add(spriteLupin, lupin.getColumna(), lupin.getFila());
    }

    public void prepararPerros(List<Perro> perros) {
        spritesPerros.clear();
        for (int i = 0; i < perros.size(); i++) {
            String ruta = Paths.SPRITE_PERROS[i % Paths.SPRITE_PERROS.length];
            spritesPerros.add(crearSprite(ruta));
        }
    }

    public void dibujarPerros(List<Perro> perros) {
        for (int i = 0; i < perros.size(); i++) {
            ImageView sprite = spritesPerros.get(i);
            Perro perro = perros.get(i);
            getChildren().remove(sprite);
            add(sprite, perro.getColumna(), perro.getFila());
        }
    }

    public void dibujarLlaves(List<Llave> llaves) {
        for (Llave llave : llaves) {
            ImageView sprite = crearSprite(Paths.SPRITE_LLAVE);
            spritesLlaves.put(llave, sprite);
            add(sprite, llave.getColumna(), llave.getFila());
        }
    }


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


    public void prepararVigilantes(List<Vigilante> vigilantes) {
        spritesVigilantes.clear();
        for (int i = 0; i < vigilantes.size(); i++) {
            spritesVigilantes.add(crearSprite(Paths.SPRITE_VIGILANTE));
        }
    }

    public void dibujarVigilantes(List<Vigilante> vigilantes) {
        for (int i = 0; i < vigilantes.size(); i++) {
            ImageView sprite = spritesVigilantes.get(i);
            Vigilante v = vigilantes.get(i);
            getChildren().remove(sprite);
            add(sprite, v.getColumna(), v.getFila());
        }
    }


    public double anchoPreferido() {
        return (double) tablero.getColumnas() * TAMANO_CASILLA;
    }

    public double altoPreferido() {
        return (double) tablero.getFilas() * TAMANO_CASILLA;
    }

    public void ajustarTamano() {
        setMinSize(anchoPreferido(), altoPreferido());
        setPrefSize(anchoPreferido(), altoPreferido());
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    }
}
