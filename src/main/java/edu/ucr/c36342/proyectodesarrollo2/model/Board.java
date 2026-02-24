package edu.ucr.c36342.proyectodesarrollo2.model;

import edu.ucr.c36342.proyectodesarrollo2.model.enums.CellState;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa el tablero del juego Reverse Dots.
 * Gestiona el estado de las celdas y las operaciones sobre el tablero.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class Board {
    private int size;
    private CellState[][] cells;

    /**
     * Constructor que crea un tablero con el tamaño especificado.
     * Inicializa con el patrón estándar de 4 fichas en el centro.
     *
     * @param size Tamaño del tablero (debe ser par y >= 4)
     */
    public Board(int size) {
        this(size, true);
    }

    /**
     * Constructor que permite controlar la inicialización.
     *
     * @param size Tamaño del tablero
     * @param initialize Si es true, inicializa con el patrón por defecto
     */
    public Board(int size, boolean initialize) {
        if (size < 4 || size % 2 != 0) {
            throw new IllegalArgumentException("El tamaño debe ser par y >= 4");
        }

        this.size = size;
        cells = new CellState[size][size];

        // Llena t0do de EMPTY
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = CellState.EMPTY;
            }
        }

        // Inicializar con patrón si se solicita
        if (initialize) {
            initializeBoard();
        }
    }

    /**
     * Inicializa el tablero con las 4 fichas centrales.
     */
    private void initializeBoard() {
        int mid = size / 2;
        cells[mid - 1][mid - 1] = CellState.WHITE;
        cells[mid - 1][mid] = CellState.BLACK;
        cells[mid][mid - 1] = CellState.BLACK;
        cells[mid][mid] = CellState.WHITE;
    }

    /**
     * Verifica si un movimiento es válido.
     *
     * @param row Fila del tablero
     * @param col Columna del tablero
     * @param color Color de la ficha a colocar
     * @return true si el movimiento es válido
     */
    public boolean isValidMove(int row, int col, Color color) {
        // Verificar que la celda esté dentro del tablero
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return false;
        }

        // La celda debe estar vacía
        if (cells[row][col] != CellState.EMPTY) {
            return false;
        }

        // Verificar en las 8 direcciones
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},  // arriba-izq, arriba, arriba-der
                {0, -1},           {0, 1},    // izquierda, derecha
                {1, -1},  {1, 0},  {1, 1}     // abajo-izq, abajo, abajo-der
        };

        for (int[] dir : directions) {
            if (wouldFlipInDirection(row, col, dir[0], dir[1], color)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Realiza un movimiento en el tablero.
     *
     * @param row Fila donde colocar la ficha
     * @param col Columna donde colocar la ficha
     * @param color Color de la ficha
     * @throws IllegalArgumentException Si el movimiento no es válido
     */
    public void makeMove(int row, int col, Color color) {
        if (!isValidMove(row, col, color)) {
            throw new IllegalArgumentException("Movimiento inválido");
        }

        // Colocar la ficha
        cells[row][col] = colorToCellState(color);

        // Voltear fichas en todas las direcciones
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] dir : directions) {
            flipPiecesInDirection(row, col, dir[0], dir[1], color);
        }
    }

    /**
     * Obtiene todas las jugadas válidas para un color.
     *
     * @param color Color del jugador
     * @return Lista de posiciones válidas
     */
    public List<Position> getValidMoves(Color color) {
        List<Position> validMoves = new ArrayList<>();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (isValidMove(row, col, color)) {
                    validMoves.add(new Position(row, col));
                }
            }
        }

        return validMoves;
    }

    /**
     * Cuenta las fichas de un color en el tablero.
     *
     * @param color Color a contar
     * @return Cantidad de fichas de ese color
     */
    public int countTokens(Color color) {
        int count = 0;
        CellState targetState = colorToCellState(color);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (cells[row][col] == targetState) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Verifica si el tablero está completamente lleno.
     *
     * @return true si no hay celdas vacías
     */
    public boolean isFull() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (cells[row][col] == CellState.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Obtiene el estado de una celda.
     *
     * @param row Fila
     * @param col Columna
     * @return Estado de la celda
     */
    public CellState getCells(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IndexOutOfBoundsException("Posición fuera del tablero");
        }
        return cells[row][col];
    }

    /**
     * Establece el estado de una celda.
     *
     * @param row Fila
     * @param col Columna
     * @param cellState Nuevo estado
     */
    public void setCells(int row, int col, CellState cellState) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IndexOutOfBoundsException("Posición fuera del tablero");
        }
        if (cellState == null) {
            throw new IllegalArgumentException("CellState no puede ser null");
        }
        cells[row][col] = cellState;
    }

    /**
     * Obtiene el tamaño del tablero.
     *
     * @return Tamaño del tablero
     */
    public int getSize() {
        return size;
    }

    /**
     * Verifica si voltear fichas en una dirección específica es posible.
     *
     * @param row Fila de inicio
     * @param col Columna de inicio
     * @param dRow Delta de fila (-1, 0, 1)
     * @param dCol Delta de columna (-1, 0, 1)
     * @param color Color de la ficha a colocar
     * @return true si se pueden voltear fichas en esa dirección
     */
    private boolean wouldFlipInDirection(int row, int col, int dRow, int dCol, Color color) {
        CellState myState = colorToCellState(color);
        CellState opponentState = colorToCellState(color.opposite());

        int currentRow = row + dRow;
        int currentCol = col + dCol;
        boolean foundOpponent = false;

        // Avanzar en la dirección
        while (currentRow >= 0 && currentRow < size &&
                currentCol >= 0 && currentCol < size) {

            CellState currentCell = cells[currentRow][currentCol];

            if (currentCell == CellState.EMPTY) {
                return false;  // Encontramos espacio vacío
            }

            if (currentCell == opponentState) {
                foundOpponent = true;  // Encontramos al menos una ficha del oponente
            } else if (currentCell == myState) {
                // Encontramos nuestra ficha
                return foundOpponent;  // Solo es válido si había fichas del oponente en medio
            }

            currentRow += dRow;
            currentCol += dCol;
        }

        return false;  // Salimos del tablero sin encontrar nuestra ficha
    }

    /**
     * Voltea las fichas en una dirección específica.
     *
     * @param row Fila de inicio
     * @param col Columna de inicio
     * @param dRow Delta de fila
     * @param dCol Delta de columna
     * @param color Color de la ficha colocada
     * @return Cantidad de fichas volteadas
     */
    private int flipPiecesInDirection(int row, int col, int dRow, int dCol, Color color) {
        if (!wouldFlipInDirection(row, col, dRow, dCol, color)) {
            return 0;
        }

        CellState myState = colorToCellState(color);
        int flipped = 0;

        int currentRow = row + dRow;
        int currentCol = col + dCol;

        // Voltear fichas hasta encontrar nuestra ficha
        while (currentRow >= 0 && currentRow < size &&
                currentCol >= 0 && currentCol < size) {

            if (cells[currentRow][currentCol] == myState) {
                break;  // Encontramos nuestra ficha, detenerse
            }

            // Voltear la ficha del oponente
            cells[currentRow][currentCol] = myState;
            flipped++;

            currentRow += dRow;
            currentCol += dCol;
        }

        return flipped;
    }

    /**
     * Convierte un Color a CellState.
     *
     * @param color Color a convertir
     * @return CellState correspondiente
     */
    private CellState colorToCellState(Color color) {
        return color == Color.BLACK ? CellState.BLACK : CellState.WHITE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Encabezado de columnas
        sb.append("  ");
        for (int col = 0; col < size; col++) {
            sb.append(String.format(" %2d", col));
        }
        sb.append("\n");

        // Filas
        for (int row = 0; row < size; row++) {
            sb.append(String.format("%2d ", row));

            for (int col = 0; col < size; col++) {
                CellState cell = cells[row][col];
                char symbol;

                switch (cell) {
                    case BLACK:
                        symbol = 'B';
                        break;
                    case WHITE:
                        symbol = 'W';
                        break;
                    default:
                        symbol = '·';
                }

                sb.append(String.format(" %2c", symbol));
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}