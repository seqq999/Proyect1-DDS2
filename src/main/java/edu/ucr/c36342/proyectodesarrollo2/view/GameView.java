package edu.ucr.c36342.proyectodesarrollo2.view;

import edu.ucr.c36342.proyectodesarrollo2.controller.GameController;
import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.services.SlotBasedSaveSystemService;
import edu.ucr.c36342.proyectodesarrollo2.view.panels.BoardPanel;
import edu.ucr.c36342.proyectodesarrollo2.view.panels.StatsPanel;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameView {
    private Stage stage;
    private GameController gameController;
    private PlayerController playerController;
    private BoardPanel boardPanel;
    private StatsPanel statsPanel;
    private Label statusLabel;
    private SlotBasedSaveSystemService saveSystem;
    private MenuBar menuBar;
    private BorderPane root;

    public GameView(GameController gameController, PlayerController playerController, BoardPanel boardPanel) {
        this.gameController = gameController;
        this.playerController = playerController;
        this.boardPanel = boardPanel;
        this.statsPanel = new StatsPanel(gameController);
        this.statusLabel = new Label();
        this.menuBar = new MenuBar();
        this.root = new BorderPane();
    }

    private void initComponents() {
        VBox leftPanel = new VBox(10, statsPanel);
        leftPanel.setMinWidth(180);
        root.setLeft(leftPanel);
        root.setCenter(boardPanel);
        root.setBottom(statusLabel);
        root.setTop(menuBar);
    }

    private void setupMenu() {
        Menu menuJuego = new Menu("Juego");
        MenuItem nuevoJuego = new MenuItem("Nuevo Juego");
        MenuItem cargarJuego = new MenuItem("Cargar Juego");
        MenuItem guardarJuego = new MenuItem("Guardar Juego");
        MenuItem salir = new MenuItem("Salir");

        nuevoJuego.setOnAction(e -> showNewGameDialog());
        cargarJuego.setOnAction(e -> loadGame());
        guardarJuego.setOnAction(e -> saveGame());
        salir.setOnAction(e -> exit());

        menuJuego.getItems().addAll(nuevoJuego, cargarJuego, guardarJuego, new SeparatorMenuItem(), salir);

        Menu menuVer = new Menu("Ver");
        MenuItem verJugadores = new MenuItem("Jugadores");
        verJugadores.setOnAction(e -> showPlayersDialog());
        menuVer.getItems().add(verJugadores);

        menuBar.getMenus().clear();
        menuBar.getMenus().addAll(menuJuego, menuVer);
    }

    private void showNewGameDialog(){

    }

    private void loadGame(){

    }

    private void saveGame(){

    }

    private void showPlayersDialog(){

    }

    private void exit(){

    }

    public void updateStatus(String message){

    }

    public void updateStats(){

    }

    public void refresh(){


    }

    private void showError(String message){

    }

    public void show() {
        setupMenu();
        initComponents();
        javafx.scene.Scene scene = new javafx.scene.Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Reverse Dots");
        stage.show();
        refresh();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
