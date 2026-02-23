package edu.ucr.c36342.proyectodesarrollo2.repository.implementation;

import edu.ucr.c36342.proyectodesarrollo2.model.Board;
import edu.ucr.c36342.proyectodesarrollo2.model.Game;
import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.CellState;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.Color;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.GameStatus;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.GameLoadException;
import edu.ucr.c36342.proyectodesarrollo2.repository.interfaces.IGameRepository;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Implementación del repositorio de partidas usando archivos XML.
 * Cada archivo contiene una sola partida guardada.
 *
 * Estructura: Un archivo XML = Una partida
 * Ejemplo:
 * - saved_games/monday.xml    → Partida del lunes
 * - saved_games/tuesday.xml   → Partida del martes
 *
 * @author Sebastian Quiros Solano --- C36342
 * @version 1.0
 */
public class GameRepository implements IGameRepository {

    /**
     * Constructor del repositorio de partidas.
     * No requiere configuración inicial.
     */
    public GameRepository() {
        //no necesita estado interno
    }

    /**
     * Guarda una partida en un archivo XML específico.
     * Si el archivo ya existe, lo sobrescribe.
     * Crea los directorios necesarios si no existen.
     *
     * @param game El juego a guardar
     * @param filePath Ruta completa del archivo donde guardar (ej: "saved_games/partida1.xml")
     * @throws IllegalArgumentException Si game o filePath son null/vacíos
     * @throws IOException Si hay error al guardar el archivo
     */

    @Override
    public void save(Game game, String filePath) throws IOException {
        // Validaciones
        if (game == null) {
            throw new IllegalArgumentException("Game no puede ser null");
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("FilePath no puede ser null o vacío");
        }

        // Crear directorio padre si no existe
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                throw new IOException("No se pudo crear el directorio: " + parentDir.getAbsolutePath());
            }
        }

        try {
            // Crear elemento del juego
            Element gameElement = gameToElement(game);

            // Crear documento con el juego como elemento raíz
            Document document = new Document(gameElement);

            // Guardar en el archivo especificado
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            try (FileOutputStream fos = new FileOutputStream(file)) {
                outputter.output(document, fos);
            }
        } catch (Exception e) {
            throw new IOException("Error al guardar la partida: " + e.getMessage(), e);
        }
    }

    /**
     * Carga una partida desde un archivo XML específico.
     *
     * @param filePath Ruta completa del archivo a cargar
     * @return El juego reconstruido desde el archivo
     * @throws IllegalArgumentException Si filePath es null/vacío
     * @throws FileNotFoundException Si el archivo no existe
     * @throws IOException Si hay error al leer o parsear el archivo
     */
    @Override
    public Game load(String filePath) throws GameLoadException, IOException {
        // Validaciones
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo no puede ser nula o vacía");
        }

        File file = new File(filePath);
        if (!file.exists()) {
            throw new GameLoadException("Archivo de partida no encontrado: " + filePath);
        }

        try {
            // Cargar documento XML
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(file);

            // Obtener elemento raíz (que ES el <game>)
            Element gameElement = document.getRootElement();

            // Verificar que sea un elemento <game>
            if (!"game".equals(gameElement.getName())) {
                throw new GameLoadException(
                        "El archivo no contiene un elemento <game> válido. Encontrado: <" +
                                gameElement.getName() + ">"
                );
            }

            // Convertir a objeto Game
            return elementToGame(gameElement);

        } catch (JDOMException e) {
            throw new GameLoadException("Error al parsear el archivo XML: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new GameLoadException("Error de IO al cargar la partida: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new GameLoadException("Error inesperado al cargar la partida: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica si un archivo de partida existe.
     *
     * @param filePath Ruta del archivo a verificar
     * @return true si el archivo existe, false en caso contrario
     */
    @Override
    public boolean exists(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    // ==================== MÉTODOS PRIVADOS DE SERIALIZACIÓN ====================

    /**
     * Convierte un objeto Game a un elemento XML.
     *
     * @param game El juego a serializar
     * @return Elemento XML representando el juego completo
     */
    private Element gameToElement(Game game) {
        Element gameElem = new Element("game");

        // Serializar jugadores
        Element player1Elem = playerToElement(game.getPlayer1());
        player1Elem.setName("player1");
        gameElem.addContent(player1Elem);

        Element player2Elem = playerToElement(game.getPlayer2());
        player2Elem.setName("player2");
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
                    rowData.append(",");
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

        // Crear un tablero vacío
        Board board = new Board(size, false);

        List<Element> rowElements = boardElement.getChildren("row");

        // Sobrescribir las celdas con los valores guardados
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

        Element winsElem = new Element("wins")
                .setText(String.valueOf(player.getWins()));
        playerElem.addContent(winsElem);

        Element lossesElem = new Element("losses")
                .setText(String.valueOf(player.getLosses()));
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
}