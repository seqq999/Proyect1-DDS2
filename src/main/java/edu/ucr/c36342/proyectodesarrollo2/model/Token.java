package edu.ucr.c36342.proyectodesarrollo2.model;

import edu.ucr.c36342.proyectodesarrollo2.model.enums.Color;

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
