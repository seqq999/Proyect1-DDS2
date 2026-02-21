package edu.ucr.c36342.proyectodesarrollo2.view.dialogs;

import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.model.Player;
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

        nameColumn    = new TableColumn<>("Nombre");
        winsColumn    = new TableColumn<>("Victorias");
        lossesColumn  = new TableColumn<>("Derrotas");
        winRateColumn = new TableColumn<>("Win Rate");

        tableView.getColumns().addAll(nameColumn, winsColumn, lossesColumn, winRateColumn);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().add(tableView);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefSize(450, 300);
    }

    private void createTableModel() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));
        lossesColumn.setCellValueFactory(new PropertyValueFactory<>("losses"));

        // getWinRate() devuelve double, lo formateamos como porcentaje para mostrarlo
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
}
