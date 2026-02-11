package edu.ucr.c36342.proyectodesarrollo2.repository.impl;

import edu.ucr.c36342.proyectodesarrollo2.model.Board;
import edu.ucr.c36342.proyectodesarrollo2.model.Game;
import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.CellState;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.Color;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.GameStatus;
import edu.ucr.c36342.proyectodesarrollo2.repository.interfaces.IGameRepository;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GameRepository implements IGameRepository {
    private final String filePath;
    private File xmlFile;
    private Document doc;

    public GameRepository(String filePath) throws IOException {
        this.filePath = filePath;
        File file = new File(filePath);

        File parentrDir = file.getParentFile();
        if (parentrDir != null && !parentrDir.exists()) {
            parentrDir.mkdirs();
        }

        if(!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<games>\n");
                writer.write("</games>");
            } catch (IOException e) {
                throw e;
            }
        }

            this.xmlFile = file;
            loadDocument();
    }
    @Override
    public void save(Game game, String filePath) throws IOException {
        if (game == null) {
            throw new NullPointerException("Game no puede ser null");
        }

        loadDocument();

        Element root = doc.getRootElement();
        Element gameElement = gameToElement(game);
        root.addContent(gameElement);

        saveXml();
    }

    @Override
    public Game load(String filePath) throws IOException {
        loadDocument();

        if (xmlFile == null) {
            throw new NullPointerException("No hay juegos guardados.");
        }





        return null;
    }

    @Override
    public boolean exists(String filePath) throws IOException {
        return false;
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
     * Convierte un objeto Game a un elemento XML.
     *
     * @param game El juego a serializar
     * @return Elemento XML representando el juego completo
     */
    private Element gameToElement(Game game) {
        Element gameElem = new Element("game");

        // Serializar jugadores (reutilizando el métod0 existente)
        Element player1Elem = playerToElement(game.getPlayer1());
        player1Elem.setName("player1");  // Renombrar para distinguir
        gameElem.addContent(player1Elem);

        Element player2Elem = playerToElement(game.getPlayer2());
        player2Elem.setName("player2");  // Renombrar para distinguir
        gameElem.addContent(player2Elem);

        // Colores asignados
        Element player1ColorElem = new Element("player1Color")
                .setText(game.getPlayer1Color().name());
        gameElem.addContent(player1ColorElem);

        Element player2ColorElem = new Element("player2Color")
                .setText(game.getPlayer2Color().name());
        gameElem.addContent(player2ColorElem);

        // Color del jugador actual
        Element currentPlayerColorElem = new Element("currentPlayerColor")
                .setText(game.getCurrentPlayerColor().name());
        gameElem.addContent(currentPlayerColorElem);

        // Estado del juego
        Element gameStatusElem = new Element("gameStatus")
                .setText(game.getGameStatus().name());
        gameElem.addContent(gameStatusElem);

        // Tablero
        Element boardElem = boardToElement(game.getBoard());
        gameElem.addContent(boardElem);

        return gameElem;
    }

    /**
     * Convierte un elemento XML a un objeto Game.
     *
     * @param gameElement El elemento XML a deserializar
     * @return Objeto Game reconstruido
     */
    private Game elementToGame(Element gameElement) {
        // Deserializar jugadores
        Element player1Elem = gameElement.getChild("player1");
        Player player1 = elementToPlayer(player1Elem);

        Element player2Elem = gameElement.getChild("player2");
        Player player2 = elementToPlayer(player2Elem);

        // Deserializar colores
        Color player1Color = Color.valueOf(gameElement.getChildText("player1Color"));
        Color player2Color = Color.valueOf(gameElement.getChildText("player2Color"));
        Color currentPlayerColor = Color.valueOf(gameElement.getChildText("currentPlayerColor"));

        // Deserializar estado
        GameStatus gameStatus = GameStatus.valueOf(gameElement.getChildText("gameStatus"));

        // Deserializar tablero
        Element boardElem = gameElement.getChild("board");
        Board board = elementToBoard(boardElem);

        // Usar el constructor completo para cargar partidas
        return new Game(player1, player2, board,
                player1Color, player2Color,
                currentPlayerColor, gameStatus);
    }

    /**
     * Convierte un tablero a un elemento XML.
     *
     * @param board El tablero a serializar
     * @return Elemento XML representando el tablero
     */
    private Element boardToElement(Board board) {
        Element boardElem = new Element("board");
        boardElem.setAttribute("size", String.valueOf(board.getSize()));

        int size = board.getSize();

        // Serializar cada fila del tablero
        for (int row = 0; row < size; row++) {
            Element rowElem = new Element("row");
            rowElem.setAttribute("index", String.valueOf(row));

            StringBuilder rowData = new StringBuilder();
            for (int col = 0; col < size; col++) {
                CellState cell = board.getCells(row, col);
                rowData.append(cell.name());

                if (col < size - 1) {
                    rowData.append(",");  // Separador entre celdas
                }
            }

            rowElem.setText(rowData.toString());
            boardElem.addContent(rowElem);
        }

        return boardElem;
    }

    /**
     * Convierte un elemento XML a un objeto Board.
     *
     * @param boardElement El elemento XML a deserializar
     * @return Objeto Board reconstruido
     */
    private Board elementToBoard(Element boardElement) {
        int size = Integer.parseInt(boardElement.getAttributeValue("size"));

        //se crea un tablero vacío (se inicializa con el patrón por defecto)
        Board board = new Board(size);

        List<Element> rowElements = boardElement.getChildren("row");

        //sobrescribir las celdas con los valores guardados
        for (Element rowElem : rowElements) {
            int rowIndex = Integer.parseInt(rowElem.getAttributeValue("index"));
            String rowData = rowElem.getText();

            // Separar las celdas
            String[] cells = rowData.split(",");

            for (int col = 0; col < cells.length; col++) {
                CellState cellState = CellState.valueOf(cells[col].trim());
                board.setCells(rowIndex, col, cellState);
            }
        }

        return board;
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
