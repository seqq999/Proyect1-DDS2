package edu.ucr.c36342.proyectodesarrollo2.model;

import java.util.Objects;

public class Player {
    private String name;
    private int wins;
    private int losses;

    public Player(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void incrementWins() {
        this.wins++;
    }

    public void incrementLosses() {
        this.losses++;
    }

    public double getWinRate() {
        return wins/(double)losses;
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
