package edu.ucr.c36342.proyectodesarrollo2.view;

import edu.ucr.c36342.proyectodesarrollo2.controller.GameController;
import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.services.SlotBasedSaveSystemService;
import edu.ucr.c36342.proyectodesarrollo2.view.dialogs.NewGameDialog;
import edu.ucr.c36342.proyectodesarrollo2.view.dialogs.PlayersDialog;
import edu.ucr.c36342.proyectodesarrollo2.view.panels.BoardPanel;
import edu.ucr.c36342.proyectodesarrollo2.view.panels.StatsPanel;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GameView {
    private Stage stage;
    private GameController gameController;
    private PlayerController playerController;
    private BoardPanel boardPanel;
    private StatsPanel statsPanel;
    private Label statusLabel;
    private SlotBasedSaveSystemService saveSystem;
    private MenuBar menuBar;
    private BorderPane root;
    private String selectedSlot = "slot1"; // Valor por defecto

    public GameView(GameController gameController, PlayerController playerController, BoardPanel boardPanel) {
        this.gameController = gameController;
        this.playerController = playerController;
        this.boardPanel = boardPanel;
        this.statsPanel = new StatsPanel(gameController);
        this.statusLabel = new Label();
        this.menuBar = new MenuBar();
        this.root = new BorderPane();
    }

    private void initComponents() {
        VBox leftPanel = new VBox(10, statsPanel);
        leftPanel.setMinWidth(180);
        root.setLeft(leftPanel);
        root.setCenter(boardPanel);
        root.setBottom(statusLabel);
        root.setTop(menuBar);
    }

    private void setupMenu() throws  IOException {
        Menu gameMenu = new Menu("Juego");
        MenuItem newGame = new MenuItem("Nuevo Juego");
        MenuItem loadGame = new MenuItem("Cargar Juego");
        MenuItem saveGame = new MenuItem("Guardar Juego");
        MenuItem exitGame = new MenuItem("Salir");

        newGame.setOnAction(e -> showNewGameDialog());
        loadGame.setOnAction(e -> {
            try {
                loadGame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        saveGame.setOnAction(e -> saveGame());
        exitGame.setOnAction(e -> exit());

        gameMenu.getItems().addAll(newGame, loadGame, saveGame, new SeparatorMenuItem(), exitGame);

        Menu showMenu = new Menu("Ver");
        MenuItem showPlayers = new MenuItem("Jugadores");
        showPlayers.setOnAction(e -> showPlayersDialog());
        showMenu.getItems().add(showPlayers);

        menuBar.getMenus().clear();
        menuBar.getMenus().addAll(gameMenu, showMenu);
    }

    private void showNewGameDialog() {
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

    private void loadGame() throws IOException {
        try {
            String slot = setSelectedSlot();
            if (slot != null) {
                gameController.loadGame(slot);
            } else {
                showError("No se seleccionó ningún slot.");
            }
        } catch (Exception e) {
            showError("Error al cargar el juego: " + e.getMessage());
        }
    }

    private void saveGame(){
        try{
            String slot = setSelectedSlot();
            if (slot != null) {
                gameController.saveGame(slot);
                updateStatus("Juego guardado en " + selectedSlot);
            } else {
                showError("No se seleccionó ningún slot.");
            }
        } catch (Exception e) {
            showError("Error al guardar el juego: " + e.getMessage());
        }

    }

    private void showPlayersDialog(){
        PlayersDialog dialog = new PlayersDialog(stage, playerController);
        dialog.showDialog();
    }

    private void exit(){
        stage.close();
    }

    public void updateStatus(String message){
        if(statusLabel != null){
            statusLabel.setText(message);
        }
    }

    public void updateStats(){
        if(statsPanel != null){
            statsPanel.updatePane();
        }
    }

    public void refresh(){
        //todo
        boardPanel.repaint();
        updateStats();
    }

    private void showError(String message){
        //todo
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void show() throws IOException {
        setupMenu();
        initComponents();
        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Reverse Dots");
        stage.show();
        refresh();
        boardPanel.repaint();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private String setSelectedSlot() {
        ChoiceDialog<String> slotDialog = new ChoiceDialog<>(selectedSlot, "slot1", "slot2", "slot3", "slot4", "slot5");
        slotDialog.setTitle("Seleccionar un slot para guardar o cargar el juego");
        slotDialog.setHeaderText("Elija el slot para guardar o cargar el juego");
        slotDialog.setContentText("Slot:");
        var result = slotDialog.showAndWait();

        if (result.isPresent()) {
            selectedSlot = result.get();
            return "saved_games/" + selectedSlot + ".xml";
        } else {
            // Usuario canceló el diálogo
            return null;
        }
    }
}
