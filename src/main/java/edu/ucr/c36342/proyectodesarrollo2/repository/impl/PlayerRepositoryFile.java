package edu.ucr.c36342.proyectodesarrollo2.repository.impl;

import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.interfaces.IPlayerRepository;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PlayerRepositoryFile implements IPlayerRepository{

    private final String filePath;
    private File xmlFile;
    private Document doc;

    public PlayerRepositoryFile(String filePath) throws IOException {
        this.filePath = filePath;
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<players>\n");
                writer.write("</players>");
            } catch (IOException e) {
                throw e;
            }
        }

        this.xmlFile = file;
        loadDocument();
    }


    @Override
    public void save(Player player) throws IOException {
        if(player == null){
            throw new NullPointerException("Player no puede ser null");
        }

        loadDocument();

        Element root = doc.getRootElement();
        Element playerElement = playerToElement(player);
        root.addContent(playerElement);

        saveXml();
    }

    @Override
    public Player findByName(String name) throws IOException {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("El nombre del jugador no puede ser null o vacío");
        }

        loadDocument();

        Element root = doc.getRootElement();
        List<Element> players = root.getChildren("player");

        for(Element playerElem : players){
            String nameElem = playerElem.getAttributeValue("name");
            if(playerElem.getAttributeValue("name").equals(name)){
                return elementToPlayer(playerElem);//si se encuentra el jugador, se convierte el elemento XML a un objeto Player y se retorna
            }
        }
        return null; //si no se encuentra el jugador, se retorna null
    }

    @Override
    public List<Player> findAll() throws IOException {
        loadDocument();

        List<Player> players = new ArrayList<>();
        Element root = doc.getRootElement();
        List<Element> playerElements = root.getChildren("player");

        for(Element playerElem : playerElements){
            players.add(elementToPlayer(playerElem));
        }

        return players;
    }

    @Override
    public void update(Player player) throws IOException {
        if(player == null) {
            throw new NullPointerException("Player no puede ser null");
        }
        loadDocument();
        Element root = doc.getRootElement();
        List<Element> players = root.getChildren("player");
        for(Element playerElem : players){
            if(playerElem.getAttributeValue("name").equals(player.getName())){
                playerElem.getChild("wins").setText(String.valueOf(player.getWins()));
                playerElem.getChild("losses").setText(String.valueOf(player.getLosses()));
                try {
                    saveXml();
                } catch (IOException e) {
                    throw new RuntimeException("Error al guardar el archivo XML: " + e.getMessage(), e);
                }
                return;
            }
        }

    }

    @Override
    public boolean delete(Player player) throws IOException {
        if (player == null) {
            throw new NullPointerException("Player no puede ser null");
        }

        loadDocument();

        Element root = doc.getRootElement();
        Element playerElement = findPlayerElement(player.getName(), root);

        if(playerElement == null){
            return false;
        }

        root.removeContent(playerElement);
        saveXml();
        return true;
    }

    /**
     * Verifica si un jugador existe en el repositorio.
     *
     * @param playerName Nombre del jugador a buscar
     * @return true si el jugador existe, false en caso contrario
     * @throws IllegalArgumentException Si playerName es null o vacío
     */
    @Override
    public boolean exists(String playerName) throws IOException {
        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name no puede ser null o vacío");
        }
        loadDocument();

            Element root = doc.getRootElement();
            List<Element> players = root.getChildren("player");

            return players.stream()
                    .anyMatch(playerElem ->
                            playerName.equals(playerElem.getAttributeValue("name"))
                    );

    }

    /**
     * Verifica si un jugador existe en el repositorio, usando un objeto player.
     *
     * @param player Jugador a buscar
     * @return true si el jugador existe, false en caso contrario
     * @throws IllegalArgumentException Si player es null
     */
    public boolean exists(Player player) throws IOException {
        if (player == null) {
            throw new IllegalArgumentException("Player no puede ser null");
        }
        return exists(player.getName());
    }

    private Element findPlayerElement(String playerName, Element root) {
        if (playerName == null || playerName.trim().isEmpty() || root == null) {
            return null;
        }
        List<Element> players = root.getChildren("player");

        return players.stream().filter(playerElemn ->
                playerName.equals(playerElemn.getAttributeValue("name")))
                .findFirst().orElse(null);

    }

    private void loadDocument() throws IOException {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            this.doc = saxBuilder.build(this.xmlFile);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar/parsing el archivo XML: " + e.getMessage(), e);
        }
    }

    /**
     * Convierte un objeto Player a un elemento XML.
     *
     * @param player El jugador a serializar
     * @return Elemento XML representando al jugador
     */
    private Element playerToElement(Player player) {
        Element playerElem = new Element("player");
        playerElem.setAttribute("name", player.getName());

        Element winsElem = new Element("wins").setText(String.valueOf(player.getWins()));
        playerElem.addContent(winsElem);

        Element lossesElem = new Element("losses").setText(String.valueOf(player.getLosses()));
        playerElem.addContent(lossesElem);

        return playerElem;
    }

    /**
     * Convierte un elemento XML a un objeto Player.
     *
     * @param playerElement El elemento XML a deserializar
     * @return Objeto Player reconstruido
     */
    private Player elementToPlayer(Element playerElement) {
        String name = playerElement.getAttributeValue("name");
        int wins = Integer.parseInt(playerElement.getChildText("wins"));
        int losses = Integer.parseInt(playerElement.getChildText("losses"));

        return new Player(name, wins, losses);
    }

    private void saveXml() throws IOException{
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        try (FileOutputStream fos = new FileOutputStream(xmlFile)) {
            xmlOutputter.output(doc, fos);
        }
    }
}
