package edu.ucr.c36342.proyectodesarrollo2.model.enums;

public enum Color {
    WHITE, BLACK;

    public Color opposite() {
        return this == BLACK ? WHITE : BLACK;
    }
}