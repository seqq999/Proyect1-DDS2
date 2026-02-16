package edu.ucr.c36342.proyectodesarrollo2.view.panels;

import edu.ucr.c36342.proyectodesarrollo2.controller.GameController;
import edu.ucr.c36342.proyectodesarrollo2.view.GameFrame;

import java.awt.*;

public class BoardPanel  {
    private GameController gameController;
    private GameFrame parent;
    private int cellSize;

    public BoardPanel(GameController gameController, GameFrame parent) {
        this.gameController = gameController;
        this.parent = parent;
    }

    public void paintComponent(Graphics g) {

    }

    private void drawGrid(Graphics2D g) {

    }

    private void drawPieces(Graphics2D g) {

    }

    private void drawValidMoves(Graphics2D g) {

    }

    private void handleClick(int x, int y){

    }


}
