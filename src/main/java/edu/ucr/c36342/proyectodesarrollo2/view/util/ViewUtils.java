package edu.ucr.c36342.proyectodesarrollo2.view.util;

import edu.ucr.c36342.proyectodesarrollo2.controller.GameController;
import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.view.GameView;
import edu.ucr.c36342.proyectodesarrollo2.view.panels.BoardPanel;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * Utilidades para mostrar diálogos y cargar partidas en la interfaz de Reverse Dots.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class ViewUtils {
    /**
     * Muestra el diálogo de estadísticas de jugadores.
     * @param stage Ventana principal
     * @param playerController Controlador de jugadores
     */
    public static void showStatsDialog(Stage stage, PlayerController playerController) {
        edu.ucr.c36342.proyectodesarrollo2.view.dialogs.PlayersDialog playersDialog =
                new edu.ucr.c36342.proyectodesarrollo2.view.dialogs.PlayersDialog(stage, playerController);
        playersDialog.showDialog();
    }

    /**
     * Carga una partida desde archivo y muestra la vista del juego.
     * @param stage Ventana principal
     * @param gameController Controlador de partidas
     * @param playerController Controlador de jugadores
     */
    public static void loadGameAndShow(Stage stage, GameController gameController, PlayerController playerController) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar partida guardada");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos XML", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                gameController.loadGame(selectedFile.getAbsolutePath());
                BoardPanel boardPanel = new BoardPanel(gameController, null);
                GameView gameView = new GameView(gameController, playerController, boardPanel);
                gameView.setStage(stage);
                boardPanel.setParent(gameView);
                gameView.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
