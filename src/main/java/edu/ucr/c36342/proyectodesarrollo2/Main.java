package edu.ucr.c36342.proyectodesarrollo2;

import edu.ucr.c36342.proyectodesarrollo2.controller.GameController;
import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.GameRepository;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.PlayerRepositoryFile;
import edu.ucr.c36342.proyectodesarrollo2.services.SlotBasedSaveSystemService;
import edu.ucr.c36342.proyectodesarrollo2.view.GameView;
import edu.ucr.c36342.proyectodesarrollo2.view.panels.BoardPanel;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Inicializar repositorios
            PlayerRepositoryFile playerRepo = new PlayerRepositoryFile("data/players.xml");
            GameRepository gameRepo = new GameRepository();

            // Inicializar controladores
            PlayerController playerController = new PlayerController(playerRepo);
            GameController gameController = new GameController(playerRepo, gameRepo, playerController);

            // Inicializar servicio de slots
            SlotBasedSaveSystemService saveSystem = new SlotBasedSaveSystemService(gameController, gameRepo);

            // Inicializar panel del tablero y vista principal
            BoardPanel boardPanel = new BoardPanel(gameController, null);
            GameView gameView = new GameView(gameController, playerController, boardPanel);
            gameView.setStage(primaryStage);
            boardPanel.setParent(gameView);

            // Mostrar la interfaz
            gameView.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}