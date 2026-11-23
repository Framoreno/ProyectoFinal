package Ud.Modelo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Guarda el resultado de cada partida en un archivo de texto plano
 * (resultados.txt), como pide la guia.
 *
 * Unica responsabilidad: escribir resultados en disco (SOLID). El juego y la
 * interfaz no saben de archivos; solo le piden "guarda este resultado".
 */
public class RepositorioResultados {

    private static final String ARCHIVO = "resultados.txt";
    private static final DateTimeFormatter FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Agrega una linea con el resultado de la partida. Crea el archivo si no
     * existe. Si falla la escritura, lanza IOException para que la capa que
     * llama decida que hacer.
     *
     * @param nombre    nombre del jugador.
     * @param resultado "GANO" o "ATRAPADO".
     * @param vidas     vidas restantes al terminar.
     */
    public void guardar(String nombre, String resultado, int vidas) throws IOException {
        String linea = String.format("%s | Jugador: %s | Resultado: %s | Vidas: %d%n",
                LocalDateTime.now().format(FECHA),
                nombre == null || nombre.isBlank() ? "Anonimo" : nombre.trim(),
                resultado,
                vidas);
        Path ruta = Path.of(ARCHIVO);
        Files.writeString(ruta, linea, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /** Ruta absoluta del archivo (para informar al jugador donde quedo). */
    public String rutaArchivo() {
        return Path.of(ARCHIVO).toAbsolutePath().toString();
    }
}
