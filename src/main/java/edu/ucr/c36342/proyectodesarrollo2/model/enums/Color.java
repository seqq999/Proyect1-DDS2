package edu.ucr.c36342.proyectodesarrollo2.model.enums;

/**
 * Enumeración de los colores posibles para las fichas y jugadores en Reverse Dots.
 */
public enum Color {
    WHITE, BLACK;

    public Color opposite() {
        return this == BLACK ? WHITE : BLACK;
    }
}