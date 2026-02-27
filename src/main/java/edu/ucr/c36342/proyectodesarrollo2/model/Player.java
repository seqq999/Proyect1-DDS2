package edu.ucr.c36342.proyectodesarrollo2.model;

import java.util.Objects;

/**
 * Representa un jugador de Reverse Dots.
 * Almacena nombre y estadísticas de victorias y derrotas.
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class Player {
    private String name;
    private int wins;
    private int losses;

    /**
     * Constructor para crear un nuevo jugador con un nombre específico.
     * Las victorias y derrotas se inicializan en 0.
     *
     * @param name El nombre del jugador.
     */
    public Player(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
    }

    /**
     * Constructor para deserialización de un jugador existente.
     *
     * @param name   El nombre del jugador.
     * @param wins   El número de victorias del jugador.
     * @param losses El número de derrotas del jugador.
     */
    public Player(String name, int wins, int losses) {
        this.name = name;
        this.wins = wins;
        this.losses = losses;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return El nombre del jugador.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el número de victorias del jugador.
     *
     * @return El número de victorias.
     */
    public int getWins() {
        return wins;
    }

    /**
     * Obtiene el número de derrotas del jugador.
     *
     * @return El número de derrotas.
     */
    public int getLosses() {
        return losses;
    }

    /**
     * Establece el número de victorias del jugador.
     *
     * @param wins El nuevo número de victorias.
     */
    public void setWins(int wins) {this.wins = wins;}

    /**
     * Establece el número de derrotas del jugador.
     *
     * @param losses El nuevo número de derrotas.
     */
    public void setLosses(int losses) {this.losses = losses;}

    /**
     * Incrementa el contador de victorias en uno.
     */
    public void incrementWins() {
        this.wins++;
    }

    /**
     * Incrementa el contador de derrotas en uno.
     */
    public void incrementLosses() {
        this.losses++;
    }

    /**
     * calcula la tasa de victorias del jugador.
     * partidas ganadas / partidas totales (ganadas + perdidas)
     *
     * @return La tasa de victorias como un valor entre 0.0 y 1.0.
     */
    public double getWinRate() {
        int totalGames = wins + losses;
        if (totalGames == 0) {
            return 0.0; //sin partidas jugadas
        }
        return wins / (double) totalGames;//% de victorias
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return wins == player.wins && losses == player.losses && Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, wins, losses);
    }
}
