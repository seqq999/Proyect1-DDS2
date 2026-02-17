package edu.ucr.c36342.proyectodesarrollo2;

import edu.ucr.c36342.proyectodesarrollo2.model.Game;
import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.GameRepository;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.PlayerRepositoryFile;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //CAMBIAR LA RUTA ANTES DE USAR
        String gamePath = "saved_games/slot1.xml"; // Ruta fija para todos los jugadores
        String playerFilePath = "data/players.xml";
        PlayerRepositoryFile prf = new PlayerRepositoryFile(playerFilePath);
        Player p1 = new Player("Santi");
        Player p2 = new Player("Sebas");

        //hi
        prf.save(p1);
        prf.save(p2);
        Game gameTry1 = new Game(p1, p2, 8);
        GameRepository gR = new GameRepository();
        gR.save(gameTry1, gamePath);

//        Player player1 = new Player("Santi");
//        Player player2 = new Player("Sebas");
//
//        Game gameToSave = new Game(player1, player2, 4);
//        gR.save(gameToSave, filePath);

        //System.out.println(gR.load(filePath));
    }
}