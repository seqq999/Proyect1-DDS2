package edu.ucr.c36342.proyectodesarrollo2.view.panels;

import javafx.stage.Stage;

import java.awt.*;

public class MainMenuPanel {
    private Stage stage;
    private Button viewStatsBtn;
    private Button newGameBtn;
    private Button loadGame;
    private Button exitBtn;

    public MainMenuPanel(Stage stage) {
        this.stage = stage;

    }

    private void intiComponents(){
        viewStatsBtn = new Button("Ver Estadisticas");
        newGameBtn = new Button("Nueva partida");
        loadGame = new Button("Cargar partida");
        exitBtn =  new Button("Salir");

    }

}
