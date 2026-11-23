# Proyecto Lupin — Resumen del juego

Videojuego del **Proyecto Final (alternativa 2: Lupin)**, Unidad 6: Concurrencia.
Hecho en **JavaFX**, con patrón **MVC** y principios **SOLID**.

---

## 1. Qué pediste (en orden)

1. Interpretar la guía y sacar los parámetros del juego.
2. Empezar por lo visual: un tablero con el ladrón, que se mueva.
3. Correcciones de base: **vidas** en vez de puntos, seguir **MVC**, aplicar **SOLID**, interfaces en **JavaFX** con los sprites de `resources/Sprites`.
4. Las **dos bóvedas** en la misma escena (bóveda 1 de 10x10 con paredes café, bóveda 2 de 5x10 con el tesoro, separadas por una puerta).
5. Rediseño: bóvedas **lado a lado**, pared **simple** entre ellas, paredes también en la bóveda 2.
6. Implementar el **tesoro** dentro del espacio jugable, con su sprite.
7. Abrir ciertas casillas que estaban como pared (ajustando el desfase de conteo: tú cuentas desde 1).
8. **3 perros**: se mueven solos, al azar, cada uno con su **propio reloj** (hilo) a distinta velocidad; solo por área jugable; nunca sobre puerta ni llaves.
9. Perros: arrancan pegados a la puerta, **no repiten** el movimiento anterior y **no se pisan** entre ellos.
10. Pasar la escena a **FXML** y mostrar un texto de **vidas** arriba.
11. **3 llaves** con su sprite, en posición **aleatoria** en la bóveda 1 cada arranque.
12. Lógica de la **puerta**: no se cruza sin las 3 llaves; al pisar una llave, desaparece y su casilla queda libre.
13. Al tocar un perro a Lupin: **detener relojes**, alerta *"Lupin ha sido atrapado, quedan X vidas"*, reinicio y lógica de vidas.
14. **Vigilante**: quieto hasta que Lupin entra a la bóveda 2, luego lo **persigue**.
15. Al tocar el **tesoro**, Lupin **gana**; vigilante un poco más rápido.
16. Pedir el **nombre** del jugador y guardar el **resultado** en un archivo de texto.

---

## 2. Cómo está organizado (MVC)

| Capa | Archivos | Trabajo |
|------|----------|---------|
| **Modelo** | `Tablero`, `TipoCasilla`, `ConstructorMapa`, `Personaje`, `Lupin`, `Perro`, `Vigilante`, `Llave`, `Direccion`, `Posicionable`, `JuegoLupin`, `RelojPerro`, `RelojVigilante`, `RepositorioResultados` | Reglas, estado del juego, hilos y guardado |
| **Vista** | `Juego.fxml`, `App`, `VistaTablero`, `Paths` | Dibuja el tablero, los sprites y el texto de vidas |
| **Controlador** | `JuegoFxmlController`, `ControladorJuego` | Une teclado, relojes, modelo y vista |

**SOLID aplicado:**
- *Responsabilidad única*: el mapa lo arma `ConstructorMapa`, el guardado `RepositorioResultados`, cada reloj solo cuenta tiempo.
- *Abierto/cerrado*: se agregaron perros, llaves y vigilante sin reescribir lo anterior.
- *Sustitución de Liskov*: `Lupin`, `Perro` y `Vigilante` son todos `Personaje`.
- *Segregación de interfaces*: `Posicionable` es pequeña (solo fila, columna, símbolo).
- *Inversión de dependencias*: el `ControladorJuego` avisa a la ventana por la interfaz `ObservadorJuego`, no depende de la clase concreta.

---

## 3. El tablero

- Una sola escena, **10 filas x 21 columnas**.
- **Bóveda 1**: columnas 0–9, rodeada de paredes café.
- **Pared simple**: columna 10, con una **puerta** en la fila 5.
- **Bóveda 2**: columnas 11–20, con sus paredes y el **tesoro** dentro del área jugable, al final.
- Colores: pared = café, puerta = dorado, tesoro y llaves = su sprite sobre suelo normal.

---

## 4. Funcionamiento paso a paso

1. **Inicio.** La app pide el **nombre** del jugador en una ventana.
2. Se arma el tablero. Lupin aparece en la bóveda 1; los 3 perros, pegados a la puerta; las 3 llaves en posiciones al azar; el vigilante quieto en la bóveda 2. Arriba se ve **Vidas: 3**.
3. **El jugador mueve a Lupin** con las flechas o W A S D. No atraviesa paredes ni se sale.
4. **Los perros se mueven solos**, cada uno con su reloj (0,5 / 0,8 / 1,2 segundos). Van al azar, sin repetir su último movimiento, sin pisarse y sin pararse en la puerta ni en las llaves.
5. **Llaves.** Cuando Lupin pisa una llave, la recoge (suma 1) y esa casilla queda libre.
6. **Puerta.** Lupin solo puede cruzar la puerta a la bóveda 2 si ya tiene las **3 llaves**.
7. **Vigilante.** En cuanto Lupin entra a la bóveda 2, el vigilante se **activa** y empieza a **perseguirlo** (ahora un poco más rápido).
8. **Si un perro o el vigilante tocan a Lupin:** se detienen todos los relojes, Lupin **pierde una vida**, sale la alerta *"Lupin ha sido atrapado, quedan X vidas"*, y al cerrarla todos vuelven a su posición inicial y el juego sigue.
9. **Fin por derrota.** Si las vidas llegan a **0**, sale el mensaje de fin, se **guarda el resultado** (ATRAPADO) en el archivo y la app se cierra.
10. **Victoria.** Si Lupin llega al **tesoro**, sale *"Lupin ha ganado"*, se **guarda el resultado** (GANO) en el archivo y la app se cierra.

---

## 5. Concurrencia (el tema de la Unidad 6)

- Cada perro y el vigilante corren en **su propio hilo** (`RelojPerro`, `RelojVigilante`).
- Cada uno tiene su **velocidad distinta**, por eso se mueven de forma independiente.
- Un **candado compartido** sincroniza los pasos para que los hilos no se pisen al leer/escribir posiciones, y los redibujos se hacen siempre en el hilo de JavaFX.

---

## 6. Guardado de resultados

- Archivo: `resultados.txt` (en la carpeta del proyecto).
- Cada partida agrega una línea, por ejemplo:

```
2026-11-23 14:57:52 | Jugador: Freddy | Resultado: GANO | Vidas: 3
2026-11-23 14:57:52 | Jugador: Ana | Resultado: ATRAPADO | Vidas: 0
```

---

## 7. Cómo ejecutarlo

```
cd "c:\Users\Freddy\Desktop\proyecto final\ProyectoFinal"
.\mvnw.cmd javafx:run
```

**Controles:** flechas o W A S D para mover a Lupin.
