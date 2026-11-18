# ProyectoFinal

Proyecto final de Programación Orientada a Objetos (POO). Aplicación de escritorio
hecha con **JavaFX**, siguiendo el patrón **MVC** (Modelo–Vista–Controlador).

## Requisitos

- **JDK 21** instalado (Temurin/Adoptium u otro).
- No necesitas instalar Maven: el proyecto incluye el **Maven Wrapper** (`mvnw` / `mvnw.cmd`).

## Cómo ejecutarlo (sin instalar Maven)

El wrapper descarga Maven la primera vez y luego ejecuta la app.

**Windows (PowerShell o CMD):**

```
mvnw.cmd clean javafx:run
```

**Linux / macOS:**

```
./mvnw clean javafx:run
```

## Abrirlo en un IDE

Funciona tanto en **IntelliJ IDEA** como en **Eclipse**. En ambos basta con
abrir/importar el archivo `pom.xml` como un proyecto Maven; el IDE resolverá las
dependencias (JavaFX) automáticamente.

## Estructura

- `src/main/java/Ud/` — código fuente (modelo, vista, controlador).
- `src/main/resources/` — recursos (FXML en `Escenas/`, imágenes en `Imagenes/`).
- `pom.xml` — configuración Maven (Java 21, JavaFX 21.0.4).
- `mvnw`, `mvnw.cmd`, `.mvn/` — Maven Wrapper.
