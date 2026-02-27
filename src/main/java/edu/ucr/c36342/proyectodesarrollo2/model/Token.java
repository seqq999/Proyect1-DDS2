package edu.ucr.c36342.proyectodesarrollo2.model;

import edu.ucr.c36342.proyectodesarrollo2.model.enums.Color;

/**
 * Representa una ficha en el tablero de Reverse Dots.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class Token {
    private Color color;

    public Token(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void flipColor() {
        this.color = this.color.opposite();
    }
}
