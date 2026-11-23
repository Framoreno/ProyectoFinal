package Ud.Modelo;

/**
 * Algo que ocupa una casilla del tablero y se puede dibujar.
 * Interfaz pequena (principio de segregacion de interfaces, SOLID):
 * solo expone lo necesario para ubicar y dibujar un elemento.
 */
public interface Posicionable {
    int getFila();

    int getColumna();

    char getSimbolo();
}
