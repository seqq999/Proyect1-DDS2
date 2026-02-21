package edu.ucr.c36342.proyectodesarrollo2.view.dialogs;

import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class NewGameDialog {
    private Stage stage;
    private PlayerController playerController;
    private ComboBox<String> player1Field;
    private ComboBox<String> player2Field;
    private Spinner<Integer> boardSizeField;
    private DialogResult result;
    private Dialog<ButtonType> dialog;

    public NewGameDialog(Stage parent, PlayerController playerController) {
        this.stage = parent;
        this.playerController = playerController;
        this.result = new DialogResult();
        this.dialog = new Dialog<>();
        this.dialog.initOwner(stage);
        this.dialog.setTitle("Nuevo Juego");
        this.dialog.setHeaderText("Configura tu nuevo juego");

        initComponents();
        loadPlayerName();

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    }

    public DialogResult showDialog() {
        Optional<ButtonType> buttonResult = dialog.showAndWait();
        if (buttonResult.isPresent() && buttonResult.get() == ButtonType.OK) {
            handleOk();
        } else {
            handleCancel();
        }
        return result;
    }

    private void initComponents() {
        player1Field = new ComboBox<>();
        player2Field = new ComboBox<>();
        boardSizeField = new Spinner<>(3, 10, 3);
        boardSizeField.setEditable(true);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Jugador 1:"), 0, 0);
        grid.add(player1Field, 1, 0);
        grid.add(new Label("Jugador 2:"), 0, 1);
        grid.add(player2Field, 1, 1);
        grid.add(new Label("Tamaño del tablero:"), 0, 2);
        grid.add(boardSizeField, 1, 2);

        dialog.getDialogPane().setContent(grid);
    }

    private void loadPlayerName() {
        try {
            List<Player> players = playerController.getAllPlayers();
            for (Player player : players) {
                player1Field.getItems().add(player.getName());
                player2Field.getItems().add(player.getName());
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudieron cargar los jugadores");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void handleOk() {
        if (!validateInputs()) {
            return;
        }
        result.setPlayer1Name(player1Field.getValue());
        result.setPlayer2Name(player2Field.getValue());
        result.setBoardSize(boardSizeField.getValue());
        result.setConfirmed(true);
    }

    private void handleCancel() {
        result.setConfirmed(false);
    }

    private boolean validateInputs() {
        String p1 = player1Field.getValue();
        String p2 = player2Field.getValue();

        if (p1 == null || p1.isEmpty()) {
            showError("Debe seleccionar un Jugador 1.");
            return false;
        }
        if (p2 == null || p2.isEmpty()) {
            showError("Debe seleccionar un Jugador 2.");
            return false;
        }
        if (p1.equals(p2)) {
            showError("Los jugadores deben ser diferentes.");
            return false;
        }
        return true;
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
