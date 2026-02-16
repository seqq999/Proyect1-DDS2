package edu.ucr.c36342.proyectodesarrollo2;

import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.GameRepository;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //CAMBIAR LA RUTA ANTES DE USAR 
        String filePath = "C:\\Users\\sebas\\OneDrive\\Escritorio\\Desarollo de Software 2\\Proyect1-DDS2\\src\\main\\java\\edu\\ucr\\c36342\\XmlFiles\\XmlGames.xml";
        //PlayerRepositoryFile prf = new PlayerRepositoryFile(filePath);
        GameRepository gR = new GameRepository(filePath);

//        Player player1 = new Player("Santi");
//        Player player2 = new Player("Sebas");
//
//        Game gameToSave = new Game(player1, player2, 4);
//        gR.save(gameToSave, filePath);

        System.out.println(gR.load(filePath));
    }
}