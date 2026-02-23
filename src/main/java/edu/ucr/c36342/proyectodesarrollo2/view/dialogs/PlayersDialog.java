package edu.ucr.c36342.proyectodesarrollo2.view.dialogs;

import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.PlayerNotFoundException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

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
    private Button updateButton;
    private TextField createPlayerField;

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

    public void showDialog() {
        dialog.showAndWait();
    }

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
        deleteButton.setOnAction(event -> handleDeletePlayer());

        updateButton = new Button("Actualizar jugador");
        updateButton.setDisable(true);
        updateButton.setOnAction(event -> handleUpdatePlayer());

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> deleteButton.setDisable(newValue == null));

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(tableView, createPlayerField, createButton, deleteButton, updateButton);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefSize(1000, 700);
    }

    private void createTableModel() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        winsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getWins()).asObject());
        lossesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getLosses()).asObject());
        winRateColumn.setCellValueFactory(cellData -> {
            double rate = cellData.getValue().getWinRate() * 100;
            return new SimpleStringProperty(String.format("%.1f%%", rate));
        });
    }

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

    private void handleCreatePlayer(){
        String newName = createPlayerField.getText().trim();
        if(newName.isEmpty()){
            showError("El nombre del jugador no puede estar vacío.");
            return;
        }

        try{
            boolean created = playerController.registerPlayer(newName);
            if(created){
                loadPlayers();
                createPlayerField.clear();
            } else {
                showError("No se pudo crear el jugador: " + newName);
            }
        } catch (IOException e){
            showError("Error al crear el jugador: " + e.getMessage());
        }
    }

    private void handleDeletePlayer() {
        Player selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecciona un jugador para eliminar.");
            return;
        }
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
            throw new RuntimeException(e);
        }
    }

    private void handleUpdatePlayer(){
        Player selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecciona un jugador para eliminar.");
            return;
        }

        try{
            boolean updated = playerController.updatePlayerStats(selected);
            if(updated){
                loadPlayers();
            } else {
                showError("No se pudo actualizar el jugador: " + selected.getName());
            }

        } catch (IOException e){
                showError("Error al actualizar el jugador: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validación");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(stage);
        alert.showAndWait();
    }
}
