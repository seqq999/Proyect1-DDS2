package edu.ucr.c36342.proyectodesarrollo2;

import edu.ucr.c36342.proyectodesarrollo2.controller.GameController;
import edu.ucr.c36342.proyectodesarrollo2.controller.PlayerController;
import edu.ucr.c36342.proyectodesarrollo2.model.Game;
import edu.ucr.c36342.proyectodesarrollo2.model.Player;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.GameRepository;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.PlayerRepositoryFile;
import edu.ucr.c36342.proyectodesarrollo2.services.SlotBasedSaveSystemService;
import edu.ucr.c36342.proyectodesarrollo2.services.SlotInfo;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // ==================== SETUP ====================
            PlayerRepositoryFile playerRepo = new PlayerRepositoryFile("data/players.xml");
            GameRepository gameRepo = new GameRepository();

            PlayerController playerController = new PlayerController(playerRepo);
            GameController gameController = new GameController(playerRepo, gameRepo,playerController);


            // Crear sistema de slots
            SlotBasedSaveSystemService saveSystem = new SlotBasedSaveSystemService(gameController, gameRepo);

            System.out.println("=== DEMO: Sistema de Slots de Guardado ===\n");

            // ==================== EJEMPLO 1: Guardar en slots ====================

            System.out.println("1. Iniciando partida Alice vs Bob...");
            gameController.startNewGame("Alice", "Bob", 8);

            System.out.println("2. Guardando en Slot 1...");
            saveSystem.saveToSlot(1);

            // Hacer algunos movimientos
            System.out.println("3. Haciendo movimientos...");
            gameController.makeMove(2, 3);
            gameController.makeMove(2, 2);

            System.out.println("4. Guardando en Slot 2...");
            saveSystem.saveToSlot(2);

            // ==================== EJEMPLO 2: Iniciar otra partida ====================

            System.out.println("\n5. Iniciando otra partida: Charlie vs Bob...");
            gameController.startNewGame("Charlie", "Bob", 4);

            System.out.println("6. Guardando en Slot 3...");
            saveSystem.saveToSlot(3);

            // ==================== EJEMPLO 3: Listar slots ====================

            System.out.println("\n7. Listando todos los slots:");
            saveSystem.listAllSlots();

            // ==================== EJEMPLO 4: Ver info detallada ====================

            System.out.println("8. Información detallada de Slot 1:");
            SlotInfo info = saveSystem.getSlotInfo(1);
            if (info != null) {
                System.out.println("   " + info);
            }

            // ==================== EJEMPLO 5: Cargar partida ====================

            System.out.println("\n9. Cargando partida desde Slot 1...");
            saveSystem.loadFromSlot(1);
            System.out.println("   Jugador actual: " + gameController.getCurrentPlayer().getName());

            // ==================== EJEMPLO 6: AutoSave ====================

            System.out.println("\n10. Probando AutoSave...");
            int autoSavedSlot = saveSystem.autoSave();
            System.out.println("    Guardado automáticamente en Slot " + autoSavedSlot);

            // ==================== EJEMPLO 7: Estadísticas ====================

            System.out.println("\n11. Estadísticas:");
            System.out.println("    Slots ocupados: " + saveSystem.getOccupiedSlotsCount() + "/" + saveSystem.getMaxSlots());
            int emptySlot = saveSystem.findFirstEmptySlot();
            if (emptySlot != -1) {
                System.out.println("    Primer slot vacío: " + emptySlot);
            } else {
                System.out.println("    Todos los slots están ocupados");
            }

            // ==================== EJEMPLO 8: Eliminar slot ====================

            System.out.println("\n12. Eliminando Slot 3...");
            boolean deleted = saveSystem.deleteSlot(3);
            System.out.println("    Eliminado: " + deleted);

            System.out.println("\n13. Listando slots después de eliminar:");
            saveSystem.listAllSlots();

            System.out.println("\n=== FIN DEL DEMO ===");

        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}