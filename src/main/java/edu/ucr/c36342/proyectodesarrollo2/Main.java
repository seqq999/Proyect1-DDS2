package edu.ucr.c36342.proyectodesarrollo2;

import edu.ucr.c36342.proyectodesarrollo2.controller.GameController;
import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.GameRepository;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.PlayerRepositoryFile;
import edu.ucr.c36342.proyectodesarrollo2.view.GameView;
import edu.ucr.c36342.proyectodesarrollo2.view.MainMenuView;
import edu.ucr.c36342.proyectodesarrollo2.view.panels.BoardPanel;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private PlayerController playerController;
    private GameController gameController;
    private BoardPanel boardPanel;
    private GameView gameView;
    private MainMenuView mainMenuView;

    @Override
    public void start(Stage primaryStage) {
        try {
            //inicializa los repositorios
            PlayerRepositoryFile playerRepo = new PlayerRepositoryFile("data/players.xml");
            GameRepository gameRepo = new GameRepository();

            //inicializa los controladores
            playerController = new PlayerController(playerRepo);
            gameController = new GameController(playerRepo, gameRepo, playerController);

            //inicializae el panel del tablero y vista principal del juego
            boardPanel = new BoardPanel(gameController, null);
            gameView = new GameView(gameController, playerController, boardPanel);
            gameView.setStage(primaryStage);
            boardPanel.setParent(gameView);

            //incializa el menú principal y hace el wiring para cambiar a GameView
            mainMenuView = new MainMenuView(primaryStage, playerController, gameController, this::showGameView);
            mainMenuView.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showGameView() {
        try {
            gameView.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}