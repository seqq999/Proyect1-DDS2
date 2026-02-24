package edu.ucr.c36342.proyectodesarrollo2.view.panels;

import edu.ucr.c36342.proyectodesarrollo2.controller.GameController;
import edu.ucr.c36342.proyectodesarrollo2.controller.enums.GameResult;
import edu.ucr.c36342.proyectodesarrollo2.model.Position;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.CellState;
import edu.ucr.c36342.proyectodesarrollo2.view.GameView;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.List;

public class BoardPanel extends Pane {

    private GameController gameController;
    private GameView parent;
    private Canvas canvas;
    private GraphicsContext gc;
    private int cellSize = 60;

    // Colores del tablero (JavaFX Paint)
    private static final Paint EMPTY_COLOR = javafx.scene.paint.Color.web("#2E7D32");  // verde oscuro
    private static final Paint BLACK_COLOR = javafx.scene.paint.Color.BLACK;
    private static final Paint WHITE_COLOR = javafx.scene.paint.Color.WHITE;
    private static final Paint VALID_MOVE_COLOR = javafx.scene.paint.Color.web("#A5D6A7AA"); // verde claro semitransparente
    private static final Paint GRID_COLOR = javafx.scene.paint.Color.web("#1B5E20");  // verde más oscuro para líneas

    public BoardPanel(GameController gameController, GameView parent) {
        this.gameController = gameController;
        this.parent = parent;
        initCanvas();
    }

    /** Redibuja el tablero. Llamar cada vez que cambie el estado del juego. */
    public void repaint() {
        if (!gameController.isGameStarted()) return;
        drawBoard();
        drawPieces();
        drawValidMoves();
    }

    private void initCanvas() {
        // Calcular tamaño del canvas según el tamaño del tablero (se inicializa con 8x8 por defecto)
        // Se recalcula en repaint() si el tablero ya está iniciado
        int defaultSize = 8;
        int canvasSize  = defaultSize * cellSize;
        canvas = new Canvas(canvasSize, canvasSize);
        gc     = canvas.getGraphicsContext2D();

        // Registrar el handler de clics sobre el canvas
        canvas.setOnMouseClicked(this::handleClick);

        this.getChildren().add(canvas);
    }

    private void drawBoard() {
        int boardSize  = gameController.getBoard().getSize();
        int canvasSize = boardSize * cellSize;

        // Redimensionar canvas si el tamaño cambió
        if (canvas.getWidth() != canvasSize) {
            canvas.setWidth(canvasSize);
            canvas.setHeight(canvasSize);
        }

        // Fondo verde
        gc.setFill(EMPTY_COLOR);
        gc.fillRect(0, 0, canvasSize, canvasSize);

        // Dibujar la cuadrícula
        gc.setStroke(GRID_COLOR);
        gc.setLineWidth(1.5);
        for (int i = 0; i <= boardSize; i++) {
            gc.strokeLine(i * cellSize, 0, i * cellSize, canvasSize);         // líneas verticales
            gc.strokeLine(0, i * cellSize, canvasSize, i * cellSize);         // líneas horizontales
        }
    }

    private void drawPieces() {
        int boardSize = gameController.getBoard().getSize();
        int margin    = (int) (cellSize * 0.1);
        int diameter  = cellSize - 2 * margin;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                CellState cell = gameController.getBoard().getCells(row, col);

                if (cell == CellState.BLACK) {
                    gc.setFill(BLACK_COLOR);
                } else if (cell == CellState.WHITE) {
                    gc.setFill(WHITE_COLOR);
                } else {
                    continue; // celda vacía, no dibujar
                }

                Point2D center = getCellCenter(row, col);
                gc.fillOval(center.getX() - diameter / 2.0,
                            center.getY() - diameter / 2.0,
                            diameter, diameter);

                // Borde de la ficha
                gc.setStroke(javafx.scene.paint.Color.GRAY);
                gc.setLineWidth(1);
                gc.strokeOval(center.getX() - diameter / 2.0,
                              center.getY() - diameter / 2.0,
                              diameter, diameter);
            }
        }
    }

    private void drawValidMoves() {
        if (!gameController.isGameStarted() || gameController.isGameOver()) return;

        List<Position> validMoves = gameController.getValidMoves();
        int margin   = (int) (cellSize * 0.3);
        int diameter = cellSize - 2 * margin;

        gc.setFill(VALID_MOVE_COLOR);
        for (Position pos : validMoves) {
            Point2D center = getCellCenter(pos.getRow(), pos.getCol());
            gc.fillOval(center.getX() - diameter / 2.0,
                        center.getY() - diameter / 2.0,
                        diameter, diameter);
        }
    }

    private void handleClick(MouseEvent event) {
        if (!gameController.isGameStarted() || gameController.isGameOver()) return;

        Position cell = pixelToCell(event.getX(), event.getY());
        if (cell == null) return;

        try {
            GameResult result = gameController.makeMove(cell.getRow(), cell.getCol());

            switch (result) {
                case SUCCESS:
                    repaint();
                    if (parent != null) parent.updateStats();
                    break;
                case TURN_SKIPPED:
                    repaint();
                    if (parent != null) {
                        parent.updateStats();
                        parent.updateStatus("El oponente no tiene movimientos. ¡Sigue jugando!");
                    }
                    break;
                case GAME_OVER:
                    repaint();
                    showGameOver();
                    break;
                case INVALID_MOVE:
                    if (parent != null) parent.updateStatus("Movimiento inválido. Elige otra celda.");
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            if (parent != null) parent.updateStatus("Error al realizar el movimiento: " + e.getMessage());
        }
    }

    private Position pixelToCell(double x, double y) {
        if (!gameController.isGameStarted()) return null;
        int boardSize = gameController.getBoard().getSize();
        int col = (int) (x / cellSize);
        int row = (int) (y / cellSize);
        if (row < 0 || row >= boardSize || col < 0 || col >= boardSize) return null;
        return new Position(row, col);
    }

    private Point2D getCellCenter(int row, int col) {
        double x = col * cellSize + cellSize / 2.0;
        double y = row * cellSize + cellSize / 2.0;
        return new Point2D(x, y);
    }

    private void showGameOver() {
        try {
            String message;
            var winner = gameController.getWinner();
            if (winner != null) {
                int black = gameController.getBlackTokensCount();
                int white = gameController.getWhiteTokensCount();
                message = "¡Juego terminado!\nGanador: " + winner.getName()
                        + "\n⚫ Negro: " + black + "  |  ⚪ Blanco: " + white;
            } else {
                message = "¡Juego terminado!\nResultado: Empate";
            }

            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Fin del juego");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();

            if (parent != null) parent.updateStatus("Juego terminado.");

        } catch (IOException e) {
            if (parent != null) parent.updateStatus("Error al determinar el ganador: " + e.getMessage());
        }
    }

    public void setParent(GameView parent) {
        this.parent = parent;
    }
}
