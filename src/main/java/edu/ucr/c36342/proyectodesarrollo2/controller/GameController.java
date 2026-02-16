package edu.ucr.c36342.proyectodesarrollo2.controller;

import edu.ucr.c36342.proyectodesarrollo2.controller.enums.GameResult;
import edu.ucr.c36342.proyectodesarrollo2.model.Board;
import edu.ucr.c36342.proyectodesarrollo2.model.Game;
import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.model.Position;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.CellState;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.Color;
import edu.ucr.c36342.proyectodesarrollo2.model.enums.GameStatus;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.GameRepository;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.PlayerRepositoryFile;

import java.io.IOException;
import java.util.List;

public class GameController {
    private Game game;
    private PlayerRepositoryFile playerRepository;
    private GameRepository gameRepository;

    public GameController(PlayerRepositoryFile playerRepository, GameRepository gameRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    public void startNewGame(String player1Name, String player2Name, int boardSize) throws IOException {
        var player1 = playerRepository.findByName(player1Name);
        var player2 = playerRepository.findByName(player2Name);
        this.game = new Game(player1, player2, boardSize);
    }

    public GameResult makeMove(int row, int col){
        // Lógica para realizar un movimiento en el juego
        // Validar el movimiento, actualizar el estado del juego y determinar el resultado
        // Retornar el resultado del movimiento (ej. JUGADOR1_GANA, JUGADOR2_GANA, EMPATE, CONTINUA)

        //todo ver si se hace el movimiento de manera correcta.
        int size = game.getBoard().getSize();
        game.getBoard().isValidMove(row, col,game.getCurrentPlayerColor());
        if(game.getBoard().isValidMove(row, col,game.getCurrentPlayerColor()) == true){
            game.getBoard().makeMove(row,col,game.getCurrentPlayerColor());
            return GameResult.SUCCCES;
        } else {
            return GameResult.INVALID_MOVE;
        }

    }

    public GameResult skipTurn(){
        // Lógica para saltar el turno del jugador actual
        // Validar si el jugador puede saltar, actualizar el estado del juego y determinar el resultado

        //todo hacer este metodo en la casa :p

        //game.getBoard()
        return GameResult.TURN_SKIPPED; // Placeholder
    }

    public List<Position> getValidMoves(){
        // Lógica para obtener la lista de movimientos válidos para el jugador actual
        //todo revisar si esto le muestra al jugador los movimientos disponibles
        return game.getBoard().getValidMoves(game.getCurrentPlayerColor()); // Placeholder
    }

    public boolean isGameOver(){
        game.getGameStatus();

        if(game.getGameStatus() == GameStatus.IN_PROGRESS || game.getGameStatus() == GameStatus.NOT_STARTED){
            game.isInProgress();
            return false;
            //todo ver q tiene q retornar aquí
        } else {
            game.isFinished();
            return true;

        }
    }

    public Player getWinner() throws IOException {

        if(game.getGameStatus() == GameStatus.FINISHED){
            game.getBoard();
            //todo revisar la condicion del if
            //todo revisar la secuencia que pasa dentro del if
            if(game.getPlayer1Color().compareTo(game.getPlayer2Color()) == 0){
                game.getPlayer1().incrementWins();
                game.getPlayer2().incrementLosses();
                playerRepository.save(game.getPlayer1());
                playerRepository.save(game.getPlayer2());
            } else {
                 game.getPlayer2().incrementWins();
                 game.getPlayer1().incrementLosses();
                 playerRepository.save(game.getPlayer1());
                 playerRepository.save(game.getPlayer2());
            }
        }

        return null;
    }

    public Player getCurrentPlayer(){return game.getCurrentPlayer();}


    public Color getCurrentColor(){return game.getCurrentPlayerColor();}

    public int getBlackPieceCount() {
        int blackPieces = 0;
        if (game.getPlayer1Color() == Color.BLACK || game.getPlayer2Color() == Color.BLACK) {
            blackPieces = game.getBoard().countTokens(Color.BLACK);
        }
        return blackPieces;
    }

    public int getWhitePieceCount(){
        int whitePieces = 0;
        if (game.getPlayer1Color() == Color.WHITE || game.getPlayer2Color() == Color.WHITE) {
            whitePieces = game.getBoard().countTokens(Color.WHITE);
        }
        return whitePieces;
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




}
