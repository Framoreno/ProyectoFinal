package Ud.Modelo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RepositorioResultados {

    private static final String ARCHIVO = "resultados.txt";
    private static final DateTimeFormatter FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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


    public String rutaArchivo() {
        return Path.of(ARCHIVO).toAbsolutePath().toString();
    }
}
