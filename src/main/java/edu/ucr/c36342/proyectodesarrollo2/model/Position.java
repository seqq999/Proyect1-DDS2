package edu.ucr.c36342.proyectodesarrollo2.model;

import java.util.Objects;

/**
 * Representa una posición (fila, columna) en el tablero de Reverse Dots.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class Position {

    private int row;
    private int col;

    /**
     * crea una nueva posición en el tablero.
     *
     * @param row fila de la posición.
     * @param col columna de la posición.
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * obtiene la fila de la posición.
     *
     * @return fila de la posición.
     */
    public int getRow() {
        return row;
    }

    /**
     * obtiene la columna de la posición.
     *
     * @return columna de la posición.
     */
    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
