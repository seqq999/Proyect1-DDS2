package edu.ucr.c36342.proyectodesarrollo2;

import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.impl.PlayerRepositoryFile;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        String name = "Sssss";
        int wins = 15;
        int losses = 10;
        String filePath = "C:\\Users\\sebas\\OneDrive\\Escritorio\\Proyecto DDS2\\src\\main\\java\\edu\\ucr\\c36342\\Xml Files\\XmlPlayer.xml";
        PlayerRepositoryFile prf = new PlayerRepositoryFile(filePath);
        Player p2 = new Player(name, wins, losses);

        prf.save(p2);
    }
}
