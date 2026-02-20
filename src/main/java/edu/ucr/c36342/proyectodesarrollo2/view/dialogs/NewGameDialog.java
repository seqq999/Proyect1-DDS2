package edu.ucr.c36342.proyectodesarrollo2.view.dialogs;

import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

public class NewGameDialog {
    private Stage stage;
    private PlayerController playerController;
    private ComboBox<String> player1Field;
    private ComboBox<String> player2Field;
    private Spinner boardSizeField;
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

        // Aquí se deberían agregar los campos al diálogo (player1Field, player2Field, boardSizeField)
        // y configurar los botones de confirmación y cancelación.

    }

    public DialogResult showDialog(){
        // Aquí se mostraría el diálogo y se manejarían las acciones de los botones.
        // Al confirmar, se deberían validar los campos y llenar el result con la información ingresada.

        return result;
    }



}
