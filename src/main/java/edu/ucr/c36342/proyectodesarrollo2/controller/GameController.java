package edu.ucr.c36342.proyectodesarrollo2.controller;

import edu.ucr.c36342.proyectodesarrollo2.controller.enums.GameResult;
import edu.ucr.c36342.proyectodesarrollo2.model.Board;
import edu.ucr.c36342.proyectodesarrollo2.model.Game;
import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.model.Position;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.Color;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.GameStatus;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.GameLoadException;
import edu.ucr.c36342.proyectodesarrollo2.repository.exceptions.PlayerNotFoundException;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.GameRepository;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.PlayerRepositoryFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameController {
    private Game game;
    private PlayerRepositoryFile playerRepository;
    private GameRepository gameRepository;
    private PlayerController playerController;
    // Ruta del archivo de la partida cargada (si aplica)
    private String currentGameFilePath;

    public GameController(PlayerRepositoryFile playerRepository,
                          GameRepository gameRepository,
                          PlayerController playerController) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.playerController = playerController;
    }


    public void startNewGame(String player1Name, String player2Name, int boardSize) throws IOException, PlayerNotFoundException {
        if (player1Name == null || player2Name == null || player1Name.isEmpty() || player2Name.isEmpty()) {
            throw new IllegalArgumentException("Los nombres de los jugadores no pueden ser nulos o vacíos");
        }

        if (!playerRepository.exists(player1Name)) {
            playerRepository.save(new Player(player1Name));
        }

        if (!playerRepository.exists(player2Name)) {
            playerRepository.save(new Player(player2Name));
        }

        Player player1 = playerRepository.findByName(player1Name);
        Player player2 = playerRepository.findByName(player2Name);

        this.game = new Game(player1, player2, boardSize);
    }



    public GameResult makeMove(int row, int col) throws IOException {
        if(game == null){
            throw new IllegalStateException("No se ha iniciado una partida.");
        }

        //valida si es valido y hace el movimiento
        if(game.getBoard().isValidMove(row, col,game.getCurrentPlayerColor())){
            game.getBoard().makeMove(row,col,game.getCurrentPlayerColor());
            game.switchTurn();
            //verifica si el siguiente jugador tiene movimientos válidos
            if (game.getBoard().getValidMoves(game.getCurrentPlayerColor()).isEmpty()) {
                //si no tiene movimientos, volver a cambiar turno
                game.switchTurn();

                //verifica si el jugador original tampoco tiene movimientos
                if (game.getBoard().getValidMoves(game.getCurrentPlayerColor()).isEmpty()) {
                    //nadie puede jugar = se termina la partida
                    game.setGameStatus(GameStatus.FINISHED);
                    endGame();
                    return GameResult.GAME_OVER;
                }

                //el jugador original sí puede seguir jugando
                return GameResult.TURN_SKIPPED;
            }

            //verifica si el tablero está lleno
            if (game.getBoard().isFull()) {
                game.setGameStatus(GameStatus.FINISHED);
                endGame();
                return GameResult.GAME_OVER;
            }

            return GameResult.SUCCESS;
        } else {
            return GameResult.INVALID_MOVE;
        }
    }

    public List<Position> getValidMoves(){
        return game.getBoard().getValidMoves(game.getCurrentPlayerColor()); // Placeholder
    }

    public boolean isGameOver(){
        return game.isFinished();
    }

    public Player getWinner() throws IOException {
        if (game == null) {
            throw new IllegalStateException("No se ha iniciado una partida.");
        }

        int blackCount = game.getBoard().countTokens(Color.BLACK);
        int whiteCount = game.getBoard().countTokens(Color.WHITE);

        Color winnerColor;
        if (blackCount > whiteCount) {
            winnerColor = Color.BLACK;
        } else if (whiteCount > blackCount) {
            winnerColor = Color.WHITE;
        } else {
            return null; // Empate
        }

        if(game.getPlayer1Color() == winnerColor){
            Player winner = game.getPlayer1();
            playerRepository.update(winner);
            return winner;
        } else {
            Player winner = game.getPlayer2();
            playerRepository.update(winner);
            return winner;
        }
    }

    public Player getCurrentPlayer(){return game.getCurrentPlayer();}


    public Color getCurrentColor(){return game.getCurrentPlayerColor();}

    public int getBlackTokensCount() {
        return game.getBoard().countTokens(Color.BLACK);
    }

    public int getWhiteTokensCount(){
        return game.getBoard().countTokens(Color.WHITE);
    }

    public GameResult saveGame(String filePath) throws IOException {

        if(filePath == null || filePath.isEmpty()){
            throw new IllegalArgumentException("Invalid file path");
        }

        if(game != null){
            gameRepository.save(game, filePath);
            return GameResult.SAVE_SUCCESS;

        } else {
            throw new IllegalArgumentException("No se puede guardar una partida vacía.");
        }
    }

    public Board getBoard(){return game.getBoard();}

    public Game getGame(){return game;}

    public boolean isGameStarted(){return game != null;}

    public void resetGame(){
        if(game != null){
            game.reset();
        }
    }

    public GameResult loadGame(String filePath) throws IOException, GameLoadException {
        if(filePath == null || filePath.isEmpty()){
            throw new IllegalArgumentException("Invalid file path");
        }

        Game loadedGame = (Game) gameRepository.load(filePath);

        if(loadedGame != null){
            this.game = loadedGame;
            this.currentGameFilePath = filePath; // Guardar la ruta del archivo cargado
            return GameResult.LOAD_SUCCESS;
        } else {
            return GameResult.LOAD_ERROR;
        }
    }

    public void endGame() throws IOException {
        if (game == null) {
            throw new IllegalStateException("No se ha iniciado una partida.");
        }
        game.setGameStatus(GameStatus.FINISHED);

        Player winner = getWinner();

        if (winner != null) {
            //actualizar estadísticas
            Player loser = (winner == game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1();

            winner.incrementWins();
            loser.incrementLosses();

            //guardar en repositorio
            playerRepository.update(winner);
            playerRepository.update(loser);
        }
        //si winner es null, es empate (no actualizar stats)

        //si la partida fue cargada desde archivo, eliminar el archivo al finalizar
        if (currentGameFilePath != null) {
            File file = new File(currentGameFilePath);
            if (file.exists()) {
                file.delete();
            }
            currentGameFilePath = null;
        }
    }

    /**
     * Setter para el objeto Game, solo para pruebas unitarias.
     * @param game instancia de Game a usar en el controlador
     */
    public void setGame(Game game) {
        this.game = game;
    }




}
