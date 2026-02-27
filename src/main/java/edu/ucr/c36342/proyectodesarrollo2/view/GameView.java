package edu.ucr.c36342.proyectodesarrollo2.view;

import edu.ucr.c36342.proyectodesarrollo2.controller.GameController;
import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.view.dialogs.NewGameDialog;
import edu.ucr.c36342.proyectodesarrollo2.view.dialogs.PlayersDialog;
import edu.ucr.c36342.proyectodesarrollo2.view.panels.BoardPanel;
import edu.ucr.c36342.proyectodesarrollo2.view.panels.StatsPanel;
import edu.ucr.c36342.proyectodesarrollo2.view.util.ViewUtils;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Vista principal del juego Reverse Dots.
 * Gestiona la interfaz y las acciones del usuario durante la partida.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class GameView {
    private Stage stage;
    private GameController gameController;
    private PlayerController playerController;
    private BoardPanel boardPanel;
    private StatsPanel statsPanel;
    private Label statusLabel;
    private MenuBar menuBar;
    private BorderPane root;

    /**
     * Crea la vista del juego con los controladores y paneles necesarios.
     *
     * @param gameController Controlador de la partida
     * @param playerController Controlador de jugadores
     * @param boardPanel Panel del tablero
     */
    public GameView(GameController gameController, PlayerController playerController, BoardPanel boardPanel) {
        this.gameController = gameController;
        this.playerController = playerController;
        this.boardPanel = boardPanel;
        this.statsPanel = new StatsPanel(gameController);
        this.statusLabel = new Label();
        this.menuBar = new MenuBar();
        this.root = new BorderPane();
    }

    /**
     * Inicializa los componentes visuales de la vista.
     */
    private void initComponents() {
        VBox leftPanel = new VBox(10, statsPanel);
        leftPanel.setMinWidth(180);
        root.setLeft(leftPanel);
        // Centrar y expandir el BoardPanel
        BorderPane.setAlignment(boardPanel, javafx.geometry.Pos.CENTER);
        boardPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        root.setCenter(boardPanel);
        root.setBottom(statusLabel);
        root.setTop(menuBar);
    }

    /**
     * Configura el menú superior de la ventana.
     * @throws IOException si ocurre un error al cargar recursos
     */
    private void setupMenu() throws IOException {
        Menu gameMenu = new Menu("Juego");
        MenuItem newGame = new MenuItem("Nuevo Juego");
        MenuItem loadGame = new MenuItem("Cargar Juego");
        MenuItem saveGame = new MenuItem("Guardar Juego");
        MenuItem exitGame = new MenuItem("Salir");

        newGame.setOnAction(e -> showNewGameDialog());
        loadGame.setOnAction(e -> {
            loadGame();
        });
        saveGame.setOnAction(e -> saveGame());
        exitGame.setOnAction(e -> exit());

        gameMenu.getItems().addAll(newGame, loadGame, saveGame, new SeparatorMenuItem(), exitGame);

        Menu showMenu = new Menu("Ver");
        MenuItem showPlayers = new MenuItem("Jugadores");

        showPlayers.setOnAction(e -> showPlayersDialog());

        showMenu.getItems().add(showPlayers);
        showMenu.getItems();

        menuBar.getMenus().clear();
        menuBar.getMenus().addAll(gameMenu, showMenu);
    }

    /**
     * Muestra el diálogo para iniciar un nuevo juego.
     */
    public void showNewGameDialog() {
        NewGameDialog dialog = new NewGameDialog(stage, playerController);
        var result = dialog.showDialog();
        if (result.isConfirmed()) {
            try {
                gameController.startNewGame(result.getPlayer1Name(), result.getPlayer2Name(), result.getBoardSize());
                updateStatus("Nuevo juego iniciado: " + result.getPlayer1Name() + " vs " + result.getPlayer2Name());
                refresh();
                boardPanel.repaint();
            } catch (Exception ex) {
                showError("Error al iniciar el juego: " + ex.getMessage());
            }
        }
    }

    /**
     * Carga una partida desde archivo.
     */
    private void loadGame() {
        try {
            ViewUtils.loadGameAndShow(stage, gameController, playerController);
            updateStatus("Partida cargada (ver nueva ventana)");
        } catch (Exception e) {
            showError("Error al cargar la partida: " + e.getMessage());
        }
    }

    /**
     * Guarda la partida actual en archivo.
     */
    private void saveGame() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar partida");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de partida (*.xml)", "*.xml"));

            // Por comodidad, abre en la carpeta de guardados
            File defaultDir = new File(System.getProperty("user.dir"), "saved_games");
            if (defaultDir.exists()) {
                fileChooser.setInitialDirectory(defaultDir);
            }

            File file = fileChooser.showSaveDialog(stage); //stage = ventana principal
            if (file != null) {
                if (file.exists()) {
                    // Confirmación antes de sobrescribir
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Confirmar sobreescritura");
                    confirm.setHeaderText(null);
                    confirm.setContentText("El archivo ya existe, ¿deseas sobrescribirlo?\n" + file.getName());
                    var res = confirm.showAndWait();
                    if (res.isEmpty() || res.get() != ButtonType.OK) {
                        updateStatus("Guardado cancelado por el usuario.");
                        return;
                    }
                }
                // Guardar el juego usando el controlador
                gameController.saveGame(file.getAbsolutePath());
                updateStatus("Partida guardada en: " + file.getName());
                showMessage("Partida guardada exitosamente en: " + file.getName());
            } else {
                updateStatus("Guardado cancelado.");
            }
        } catch (Exception e) {
            showError("Error al guardar la partida: " + e.getMessage());
        }
    }

    /**
     * Muestra el diálogo de jugadores/estadísticas.
     */
    private void showPlayersDialog(){
        ViewUtils.showStatsDialog(stage, playerController);
        updateStats(); //se actualiza el panel de estadísticas
        updateStatus("Lista de jugadores actualizada."); //se refleja el cambio en el status
    }

    /**
     * Sale del juego (cierra la ventana).
     */
    public void exit(){
        showConfirmation("¿Estás seguro de que deseas salir? Se perderán los cambios no guardados.");
        stage.close();
    }

    /**
     * Actualiza el mensaje de estado en la interfaz.
     * @param message Mensaje a mostrar
     */
    public void updateStatus(String message){
        if(statusLabel != null){
            statusLabel.setText(message);
        }
    }

    /**
     * Actualiza el panel de estadísticas.
     */
    public void updateStats(){
        if(statsPanel != null){
            statsPanel.updatePane();
        }
    }

    /**
     * Refresca el tablero y las estadísticas.
     */
    public void refresh(){
        boardPanel.repaint();
        updateStats();
    }

    /**
     * Muestra un mensaje de error en pantalla.
     * @param message Mensaje de error
     */
    private void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra la ventana principal del juego.
     * @throws IOException si ocurre un error al cargar recursos
     */
    public void show() throws IOException {
        setupMenu();
        initComponents();
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Reverse Dots");
        stage.show();
        refresh();
        boardPanel.repaint();
        // Listener para adaptar el tamaño del tablero
        scene.widthProperty().addListener((obs, oldVal, newVal) -> resizeBoardPanel());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> resizeBoardPanel());
    }

    /**
     * Ajusta el tamaño del tablero al tamaño de la ventana.
     */
    private void resizeBoardPanel() {
        double width = root.getWidth();
        double height = root.getHeight();
        double leftPanelWidth = root.getLeft() != null ? root.getLeft().getBoundsInParent().getWidth() : 0;
        double availableWidth = width - leftPanelWidth;
        double availableHeight = height - (menuBar.getHeight() + statusLabel.getHeight());
        double size = Math.min(availableWidth, availableHeight);
        boardPanel.setPrefSize(size, size);
        BorderPane.setAlignment(boardPanel, javafx.geometry.Pos.CENTER);
        boardPanel.repaint();
    }

    /**
     * Asigna el Stage principal a la vista.
     * @param stage Ventana principal
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Muestra un mensaje informativo.
     * @param message Mensaje a mostrar
     */
    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito en el proceso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(stage);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo de confirmación.
     * @param message Mensaje a mostrar
     */
    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(stage);
        alert.showAndWait();
    }

    /**
     * Muestra opciones al finalizar la partida.
     */
    public void onGameFinished() {
        Alert optionsAlert = new Alert(Alert.AlertType.CONFIRMATION);
        optionsAlert.setTitle("¿Qué deseas hacer?");
        optionsAlert.setHeaderText("La partida ha terminado. Elige una opción:");
        optionsAlert.setContentText(null);

        ButtonType playAgainBtn = new ButtonType("Jugar otra partida");
        ButtonType statsBtn = new ButtonType("Ver estadísticas");
        ButtonType exitBtn = new ButtonType("Salir");

        optionsAlert.getButtonTypes().setAll(playAgainBtn, statsBtn, exitBtn);
        var result = optionsAlert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == playAgainBtn) {
                showNewGameDialog();
            } else if (result.get() == statsBtn) {
                //muestra el diálogo de jugadores/estadísticas
                updateStats();
                updateStatus("Mostrando estadísticas.");
                new PlayersDialog(stage, playerController).showDialog();
            } else if (result.get() == exitBtn) {
                exit();
            }
        }
    }

}
