package edu.ucr.c36342.proyectodesarrollo2.view.dialogs;

import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.PlayerNotFoundException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Diálogo para mostrar y gestionar jugadores registrados en Reverse Dots.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class PlayersDialog {
    private Stage stage;
    private PlayerController playerController;
    private TableView<Player> tableView;
    private TableColumn<Player, String> nameColumn;
    private TableColumn<Player, Integer> winsColumn;
    private TableColumn<Player, Integer> lossesColumn;
    private TableColumn<Player, String> winRateColumn;
    private Dialog<ButtonType> dialog;
    private Button createButton;
    private Button deleteButton;
    private TextField createPlayerField;

    /**
     * Crea el diálogo de jugadores.
     * @param parent Ventana principal
     * @param playerController Controlador de jugadores
     */
    public PlayersDialog(Stage parent, PlayerController playerController) {
        this.stage = parent;
        this.playerController = playerController;
        this.dialog = new Dialog<>();
        this.dialog.initOwner(stage);
        this.dialog.setTitle("Jugadores");
        this.dialog.setHeaderText("Lista de jugadores registrados");

        initComponents();
        createTableModel();
        loadPlayers();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    }

    /**
     * Muestra el diálogo de jugadores.
     */
    public void showDialog() {
        dialog.showAndWait();
    }

    /**
     * Inicializa los componentes visuales del diálogo.
     */
    private void initComponents() {
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        nameColumn = new TableColumn<>("Nombre");
        winsColumn = new TableColumn<>("Victorias");
        lossesColumn = new TableColumn<>("Derrotas");
        winRateColumn = new TableColumn<>("Win Rate");

        tableView.getColumns().addAll(nameColumn, winsColumn, lossesColumn, winRateColumn);

        createPlayerField = new TextField();
        createPlayerField.setPromptText("Nombre del nuevo jugador");

        createButton = new Button("Crear jugador");
        createButton.setDisable(false);
        createButton.setOnAction(e -> handleCreatePlayer());

        deleteButton = new Button("Eliminar jugador");
        deleteButton.setDisable(true);
        deleteButton.setOnAction(e -> handleDeletePlayer());

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean selected = newValue != null;
            deleteButton.setDisable(!selected);
        });

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(tableView, createPlayerField, createButton, deleteButton);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefSize(1000, 700);
    }

    /**
     * Configura el modelo de la tabla de jugadores.
     */
    private void createTableModel() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        winsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getWins()).asObject());
        lossesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getLosses()).asObject());
        winRateColumn.setCellValueFactory(cellData -> {
            double rate = cellData.getValue().getWinRate() * 100;
            return new SimpleStringProperty(String.format("%.1f%%", rate));
        });
    }

    /**
     * Carga la lista de jugadores en la tabla.
     */
    private void loadPlayers() {
        try {
            List<Player> players = playerController.getAllPlayers();
            tableView.setItems(FXCollections.observableArrayList(players));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudieron cargar los jugadores");
            alert.setContentText(e.getMessage());
            alert.initOwner(stage);
            alert.showAndWait();
        }
    }

    /**
     * Maneja la creación de un nuevo jugador.
     */
    private void handleCreatePlayer() {
        String newName = createPlayerField.getText().trim();
        if (newName.isEmpty()) {
            showError("El nombre del jugador no puede estar vacío.");
            return;
        }

        try {
            boolean created = playerController.registerPlayer(newName);
            if (created) {
                loadPlayers();
                createPlayerField.clear();
            } else {
                showError("No se pudo crear el jugador: " + newName);
            }
        } catch (IOException e) {
            showError("Error al crear el jugador: " + e.getMessage());
        }
    }

    /**
     * Maneja la eliminación de un jugador seleccionado.
     */
    private void handleDeletePlayer() {
        Player selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecciona un jugador para eliminar.");
            return;
        }
        showConfirmation("¿Estás seguro de que deseas eliminar al jugador: " + selected.getName() + "? Esta acción no se puede deshacer.");
        try {
            boolean deleted = playerController.deletePlayer(selected.getName());
            if (deleted) {
                loadPlayers();
            } else {
                showError("No se pudo eliminar el jugador: " + selected.getName());
            }
        } catch (IOException e) {
            showError("Error al eliminar el jugador: " + e.getMessage());
        } catch (PlayerNotFoundException e) {
            showError("Jugador no encontrado: " + selected.getName());
        }
    }

    /**
     * Muestra un mensaje de error.
     * @param message Mensaje de error
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validación");
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


}
