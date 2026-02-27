package edu.ucr.c36342.proyectodesarrollo2.view.dialogs;

import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.PlayerNotFoundException;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Diálogo para configurar y crear una nueva partida en Reverse Dots.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class NewGameDialog {
    private Stage stage;
    private PlayerController playerController;
    private ComboBox<String> player1Field;
    private ComboBox<String> player2Field;
    private Spinner<Integer> boardSizeField;
    private DialogResult result;
    private Dialog<ButtonType> dialog;
    private TextField newPlayer1Field;
    private Button addPlayer1Button;
    private TextField newPlayer2Field;
    private Button addPlayer2Button;

    /**
     * Crea el diálogo de nueva partida.
     * @param parent Ventana principal
     * @param playerController Controlador de jugadores
     */
    public NewGameDialog(Stage parent, PlayerController playerController) {
        this.stage = parent;
        this.playerController = playerController;
        this.result = new DialogResult();
        this.dialog = new Dialog<>();
        this.dialog.initOwner(stage);
        this.dialog.setTitle("Nueva Partida");
        this.dialog.setHeaderText("Configura tu nueva partida");

        initComponents();
        loadPlayerName();

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    }

    /**
     * Muestra el diálogo y devuelve el resultado de la configuración.
     * @return Resultado de la configuración (jugadores, tamaño, confirmación)
     */
    public DialogResult showDialog() {
        Optional<ButtonType> buttonResult = dialog.showAndWait();
        if (buttonResult.isPresent() && buttonResult.get() == ButtonType.OK) {
            handleOk();
        } else {
            handleCancel();
        }
        return result;
    }

    /**
     * Inicializa los componentes visuales del diálogo.
     */
    private void initComponents() {
        player1Field = new ComboBox<>();
        player2Field = new ComboBox<>();
        boardSizeField = new Spinner<>(4, 10, 4);
        boardSizeField.setEditable(true);

        newPlayer1Field = new TextField();
        newPlayer1Field.setPromptText("Nuevo jugador 1");
        addPlayer1Button = new Button("Agregar");
        addPlayer1Button.setOnAction(e -> addNewPlayer(newPlayer1Field, player1Field, player2Field));

        newPlayer2Field = new TextField();
        newPlayer2Field.setPromptText("Nuevo jugador 2");
        addPlayer2Button = new Button("Agregar");
        addPlayer2Button.setOnAction(e -> addNewPlayer(newPlayer2Field, player2Field, player1Field));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Jugador 1:"), 0, 0);
        grid.add(player1Field, 1, 0);
        grid.add(newPlayer1Field, 2, 0);
        grid.add(addPlayer1Button, 3, 0);

        grid.add(new Label("Jugador 2:"), 0, 1);
        grid.add(player2Field, 1, 1);
        grid.add(newPlayer2Field, 2, 1);
        grid.add(addPlayer2Button, 3, 1);

        grid.add(new Label("Tamaño del tablero:"), 0, 2);
        grid.add(boardSizeField, 1, 2);

        dialog.getDialogPane().setContent(grid);
    }

    /**
     * Carga los nombres de jugadores existentes en los ComboBox.
     */
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

    /**
     * Maneja la acción al confirmar la configuración.
     */
    private void handleOk() {
        if (!validateInputs()) {
            return;
        }
        result.setPlayer1Name(player1Field.getValue());
        result.setPlayer2Name(player2Field.getValue());
        result.setBoardSize(boardSizeField.getValue());
        result.setConfirmed(true);
    }

    /**
     * Maneja la acción al cancelar la configuración.
     */
    private void handleCancel() {
        result.setConfirmed(false);
    }

    /**
     * Valida los datos ingresados por el usuario.
     * @return true si los datos son válidos
     */
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
     * Agrega un nuevo jugador a la lista y ComboBox.
     * @param inputField Campo de texto de entrada
     * @param targetCombo ComboBox a actualizar
     * @param otherCombo Otro ComboBox a actualizar
     */
    private void addNewPlayer(TextField inputField, ComboBox<String> targetCombo, ComboBox<String> otherCombo) {
        String name = inputField.getText().trim();
        if (name.isEmpty()) {
            showError("El nombre del jugador no puede estar vacío.");
            return;
        }
        try {
            Player player = playerController.getOrCreatePlayer(name);
            if (!targetCombo.getItems().contains(player.getName())) {
                targetCombo.getItems().add(player.getName());
            }
            if (!otherCombo.getItems().contains(player.getName())) {
                otherCombo.getItems().add(player.getName());
            }
            targetCombo.setValue(player.getName()); //se selecciona automáticamente el nuevo jugador
            inputField.clear();
        } catch (IOException | IllegalArgumentException | PlayerNotFoundException e) {
            showError("Error al agregar jugador: " + e.getMessage());
        }
    }
}
