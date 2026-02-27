package edu.ucr.c36342.proyectodesarrollo2.view.dialogs;

/**
 * Resultado de un diálogo de configuración de partida en Reverse Dots.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class DialogResult {
    private boolean confirmed;
    private String player1Name;
    private String player2Name;
    private int boardSize;

    public DialogResult(){
        this.confirmed = false;
        this.player1Name = "";
        this.player2Name = "";
        this.boardSize = 0;
    }

    /**
     * Indica si el usuario confirmó la acción.
     * @return true si se confirmó
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Establece si el usuario confirmó la acción.
     * @param confirmed true si se confirmó
     */
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    /**
     * Obtiene el nombre del primer jugador.
     * @return Nombre del jugador 1
     */
    public String getPlayer1Name() {
        return player1Name;
    }

    /**
     * Establece el nombre del primer jugador.
     * @param player1Name Nombre del jugador 1
     */
    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    /**
     * Obtiene el nombre del segundo jugador.
     * @return Nombre del jugador 2
     */
    public String getPlayer2Name() {
        return player2Name;
    }

    /**
     * Establece el nombre del segundo jugador.
     * @param player2Name Nombre del jugador 2
     */
    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    /**
     * Obtiene el tamaño del tablero.
     * @return Tamaño del tablero
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Establece el tamaño del tablero.
     * @param boardSize Tamaño del tablero
     */
    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }
}
