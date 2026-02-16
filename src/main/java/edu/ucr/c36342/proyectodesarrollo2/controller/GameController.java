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
    private PlayerController playerController;

    public GameController(PlayerRepositoryFile playerRepository, GameRepository gameRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    public void startNewGame(String player1Name, String player2Name, int boardSize) throws IOException {
        if (player1Name == null || player2Name == null || player1Name.isEmpty() || player2Name.isEmpty()) {
            throw new IllegalArgumentException("Los nombres de los jugadores no pueden ser nulos o vacíos");
        }
        Player player1 = playerController.getPlayerByName(player1Name);
        Player player2 = playerController.getPlayerByName(player2Name);

        this.game = new Game(player1, player2, boardSize);
    }

    public GameResult makeMove(int row, int col) throws IOException {
        if(game == null){
            throw new IllegalStateException("No se ha iniciado una partida.");
        }

        //todo ver si se hace el movimiento de manera correcta.
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
    public GameResult skipTurn() throws IOException {
        // Lógica para saltar el turno del jugador actual
        // Validar si el jugador puede saltar, actualizar el estado del juego y determinar el resultado

        //todo hacer este metodo en la casa :p

        if(game == null){
            throw new IllegalStateException("No se ha iniciado una partida.");
        }

        Board board = game.getBoard();
        //verifica si el jugador actual tiene movimientos válidos, si no los tiene, cambia el turno. Si el siguiente jugador tampoco tiene movimientos válidos, termina el juego.
        if(board.getValidMoves(game.getCurrentPlayerColor()).isEmpty()){
            game.switchTurn();
            //verifica si el siguiente jugador tiene movimientos válidos, si no los tiene, termina el juego.
            if(board.getValidMoves(game.getCurrentPlayerColor()).isEmpty()){
               game.setGameStatus(GameStatus.FINISHED);
               endGame();
               return GameResult.GAME_OVER;
            }
            //el otro jugador tiene movimientos validos
            return GameResult.TURN_SKIPPED;
        } else {
            return GameResult.NO_VALID_MOVES;
        }

    }

    public List<Position> getValidMoves(){
        // Lógica para obtener la lista de movimientos válidos para el jugador actual
        //todo revisar si esto le muestra al jugador los movimientos disponibles
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

    public int getBlackPieceCount() {
        return game.getBoard().countTokens(Color.BLACK);
    }

    public int getWhitePieceCount(){
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

    public GameResult loadGame(String filePath) throws IOException {
        if(filePath == null || filePath.isEmpty()){
            throw new IllegalArgumentException("Invalid file path");
        }

        Game loadedGame = (Game) gameRepository.load(filePath);

        if(loadedGame != null){
            this.game = loadedGame;
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
    }




}
