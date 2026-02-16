package edu.ucr.c36342.proyectodesarrollo2.view;

import edu.ucr.c36342.proyectodesarrollo2.controller.GameController;
import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.view.panels.BoardPanel;
import edu.ucr.c36342.proyectodesarrollo2.view.panels.StatsPanel;
import javafx.scene.control.*;

import javax.swing.*;

public class GameFrame {
    private GameController gameController;
    private PlayerController playerController;
    private BoardPanel boardPanel;
    private StatsPanel statsPanel;
    private Label statusLabel;

    public GameFrame(GameController gameController, PlayerController playerController, BoardPanel boardPanel) {
        this.gameController = gameController;
        this.playerController = playerController;
        this.boardPanel = boardPanel;
        this.statsPanel = new StatsPanel();
        this.statusLabel = new Label();

    }

    private void initComponents() {

    }

    private void setUpLayout() {

    }

    private void setUpMenu() {

    }

    private void showNewGameDialog(){

    }

    private void loadGame(){

    }

    private void saveGame(){

    }

    private void showPlayersDialog(){

    }

    private void exit(){

    }

    public void updateStatus(String message){

    }

    public void updateStats(){

    }

    public void refresh(){


    }

}
