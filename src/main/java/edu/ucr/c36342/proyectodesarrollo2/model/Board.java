package edu.ucr.c36342.proyectodesarrollo2.model;

import edu.ucr.c36342.proyectodesarrollo2.model.enums.CellState;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.Color;

import java.util.List;

public class Board {
    private int size;
    private CellState[][] cells;

    public Board(int size) {
        this.size = size;
        cells = new CellState[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = CellState.EMPTY;
            }
        }

        int mid = size / 2;
        cells[mid - 1][mid - 1] = CellState.WHITE;
        cells[mid - 1][mid] = CellState.BLACK;
        cells[mid][mid - 1] = CellState.BLACK;
        cells[mid][mid] = CellState.WHITE;
    }

    public boolean isValidMove(int row, int col, Color color) {

        return cells[row][col] == CellState.WHITE;
    }

    public void makeMove(int row, int col, Color color) {

    }

    public List<Position> getValidMoves(Color color){
        return null;
    }

    public int countTokens(Color color){
        return 0;
    }

    public boolean isFull(){
        return false;
    }

    public CellState getCells(int row, int col) {
        return cells[row][col];
    }

    public void setCells(int row, int col, CellState cellState) {

    }

    public int getSize() {
        return size;
    }

    private boolean wouldFlipInDirection(int row, int col, int dRow, int dCol, Color color) {
        return false;
    }

    private int flipPiecesInDirection(int row, int col, int dRow, int dCol, Color color) {
        return 0;
    }

    private void initializeBoard() {

    }

}
