package edu.ucr.c36342.proyectodesarrollo2.view;

import edu.ucr.c36342.proyectodesarrollo2.controller.GameController;
import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.view.util.ViewUtils;
import edu.ucr.c36342.proyectodesarrollo2.view.dialogs.NewGameDialog;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Vista del menú principal de Reverse Dots.
 * Permite navegar entre las opciones principales del juego.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class MainMenuView {
    private Stage stage;
    private Button viewStatsBtn;
    private Button newGameBtn;
    private Button loadGameBtn;
    private Button exitBtn;
    private NewGameDialog newGameDialog;
    private PlayerController playerController;
    private GameController gameController;
    private Runnable onStartGame;
    private VBox vbox;

    /**
     * Crea la vista del menú principal.
     *
     * @param stage Ventana principal
     * @param playerController Controlador de jugadores
     * @param gameController Controlador de partidas
     * @param onStartGame Acción a ejecutar al iniciar una partida
     */
    public MainMenuView(Stage stage, PlayerController playerController, GameController gameController, Runnable onStartGame) {
        this.stage = stage;
        this.playerController = playerController;
        this.gameController = gameController;
        this.onStartGame = onStartGame;
        initComponents();
    }

    /**
     * Inicializa los componentes visuales del menú.
     */
    private void initComponents() {
        viewStatsBtn = new Button("Ver estadísticas");
        viewStatsBtn.setOnAction(e -> ViewUtils.showStatsDialog(stage, playerController));

        newGameBtn = new Button("Nueva partida");
        newGameBtn.setOnAction(e -> startNewGame());

        loadGameBtn = new Button("Cargar partida");
        loadGameBtn.setOnAction(e -> ViewUtils.loadGameAndShow(stage, gameController, playerController));

        exitBtn =  new Button("Salir");
        exitBtn.setOnAction(e -> stage.close());

        vbox = new VBox(15, newGameBtn, loadGameBtn, viewStatsBtn, exitBtn);
        vbox.setStyle("-fx-padding: 40; -fx-alignment: center;");
        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
    }

    /**
     * Inicia el flujo para crear una nueva partida.
     */
    private void startNewGame(){
        NewGameDialog newGameDialog = new NewGameDialog(stage, playerController);
        var result = newGameDialog.showDialog();
        if (result.isConfirmed()) {
            try {
                gameController.startNewGame(result.getPlayer1Name(), result.getPlayer2Name(), result.getBoardSize());
                onStartGame.run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Muestra la ventana del menú principal.
     */
    public void show() {
        stage.show();
    }
}
