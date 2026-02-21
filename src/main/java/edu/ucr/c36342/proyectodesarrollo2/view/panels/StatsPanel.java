package edu.ucr.c36342.proyectodesarrollo2.view.panels;

import edu.ucr.c36342.proyectodesarrollo2.controller.GameController;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.Color;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StatsPanel extends VBox {

    private GameController gameController;
    private Label player1Label;
    private Label player2Label;
    private Label player1ColorLabel;
    private Label player2ColorLabel;
    private Label countLabel;
    private Label turnLabel;

    // Constructor sin argumentos requerido por GameView
    public StatsPanel() {
        initComponents();
    }

    public StatsPanel(GameController gameController) {
        this.gameController = gameController;
        initComponents();
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private void initComponents() {
        this.setSpacing(8);
        this.setPadding(new Insets(10));

        Label title = new Label("Estadísticas");
        title.setFont(Font.font("System", FontWeight.BOLD, 14));

        player1Label     = new Label("Jugador 1: -");
        player2Label     = new Label("Jugador 2: -");
        player1ColorLabel = new Label("Color J1: -");
        player2ColorLabel = new Label("Color J2: -");
        countLabel       = new Label("Piezas: ⚫ 0  |  ⚪ 0");
        turnLabel        = new Label("Turno: -");
        turnLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        this.getChildren().addAll(
                title,
                player1Label,
                player1ColorLabel,
                player2Label,
                player2ColorLabel,
                countLabel,
                turnLabel
        );
    }

    public void updatePane() {
        if (gameController == null || !gameController.isGameStarted()) {
            return;
        }

        String p1Name  = gameController.getGame().getPlayer1().getName();
        String p2Name  = gameController.getGame().getPlayer2().getName();
        Color p1Color  = gameController.getGame().getPlayer1Color();
        Color p2Color  = gameController.getGame().getPlayer2Color();
        int blackCount = gameController.getBlackPieceCount();
        int whiteCount = gameController.getWhitePieceCount();
        String currentName = gameController.getCurrentPlayer().getName();

        player1Label.setText("Jugador 1: " + p1Name);
        player2Label.setText("Jugador 2: " + p2Name);
        player1ColorLabel.setText("Color J1: " + colorLabel(p1Color));
        player2ColorLabel.setText("Color J2: " + colorLabel(p2Color));
        countLabel.setText("Piezas: ⚫ " + blackCount + "  |  ⚪ " + whiteCount);
        turnLabel.setText("Turno: " + currentName + " (" + colorLabel(gameController.getCurrentColor()) + ")");

        highlightCurrentPlayer();
    }

    private void highlightCurrentPlayer() {
        // Resaltar en negrita el jugador con turno actual, el otro normal
        Color currentColor = gameController.getCurrentColor();
        Color p1Color = gameController.getGame().getPlayer1Color();

        if (currentColor == p1Color) {
            player1Label.setFont(Font.font("System", FontWeight.BOLD, 12));
            player2Label.setFont(Font.font("System", FontWeight.NORMAL, 12));
        } else {
            player1Label.setFont(Font.font("System", FontWeight.NORMAL, 12));
            player2Label.setFont(Font.font("System", FontWeight.BOLD, 12));
        }
    }

    private String colorLabel(Color color) {
        return color == Color.BLACK ? "⚫ Negro" : "⚪ Blanco";
    }
}
