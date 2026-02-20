package edu.ucr.c36342.proyectodesarrollo2.services;

import edu.ucr.c36342.proyectodesarrollo2.controller.GameController;
import edu.ucr.c36342.proyectodesarrollo2.repository.implementation.GameRepository;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SlotBasedSaveSystemService {

    private static final String SAVE_DIRECTORY = "saved_games/";
    private static final int MAX_SLOTS = 5;
    private GameRepository gameRepository;
    private GameController gameController;

    public SlotBasedSaveSystemService(GameController gameController, GameRepository gameRepository) {
        this.gameController = gameController;
        this.gameRepository = gameRepository;

        // Crear directorio si no existe
        File dir = new File(SAVE_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

    }

    public void saveToSlot(int slotNumber) throws IOException {
        validateSlotNumber(slotNumber);

        if (!gameController.isGameStarted()) {
            throw new IllegalStateException("No hay ninguna partida iniciada para guardar");
        }

        String filePath = getSlotFilePath(slotNumber);
        gameController.saveGame(filePath);

        System.out.println("✓ Partida guardada en Slot " + slotNumber);
    }

    /**
     * Carga una partida desde un slot específico.
     *
     * @param slotNumber Número del slot (1-5)
     * @throws IllegalArgumentException Si el slot no está en el rango válido
     * @throws IOException Si el slot está vacío o hay error al cargar
     */
    public void loadFromSlot(int slotNumber) throws IOException {
        validateSlotNumber(slotNumber);

        String filePath = getSlotFilePath(slotNumber);

        if (!gameRepository.exists(filePath)) {
            throw new IOException("El Slot " + slotNumber + " está vacío");
        }

        gameController.loadGame(filePath);
        System.out.println("✓ Partida cargada desde Slot " + slotNumber);
    }

    /**
     * Verifica si un slot contiene una partida guardada.
     *
     * @param slotNumber Número del slot (1-5)
     * @return true si el slot contiene una partida
     */
    public boolean isSlotOccupied(int slotNumber) {
        validateSlotNumber(slotNumber);
        String filePath = getSlotFilePath(slotNumber);
        return gameRepository.exists(filePath);
    }

    /**
     * Elimina la partida guardada en un slot.
     *
     * @param slotNumber Número del slot (1-5)
     * @return true si se eliminó exitosamente
     */
    public boolean deleteSlot(int slotNumber) {
        validateSlotNumber(slotNumber);

        String filePath = getSlotFilePath(slotNumber);
        File file = new File(filePath);

        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("✓ Slot " + slotNumber + " eliminado");
            }
            return deleted;
        }

        return false;
    }

    /**
     * Obtiene información detallada de un slot.
     *
     * @param slotNumber Número del slot (1-5)
     * @return Información del slot, o null si está vacío
     */
    public SlotInfo getSlotInfo(int slotNumber) {
        validateSlotNumber(slotNumber);

        String filePath = getSlotFilePath(slotNumber);
        File file = new File(filePath);

        if (!file.exists()) {
            return null;
        }

        // Obtener fecha de última modificación
        Date lastModified = new Date(file.lastModified());

        // Obtener tamaño del archivo
        long fileSize = file.length();

        return new SlotInfo(slotNumber, filePath, lastModified, fileSize);
    }

    /**
     * Lista información de todos los slots.
     */
    public void listAllSlots() {
        System.out.println("\n========== SLOTS DE GUARDADO ==========");

        for (int i = 1; i <= MAX_SLOTS; i++) {
            SlotInfo info = getSlotInfo(i);

            if (info == null) {
                System.out.println("Slot " + i + ": [VACÍO]");
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String dateStr = sdf.format(info.getLastModified());
                System.out.println("Slot " + i + ": [OCUPADO] - " + dateStr);
            }
        }

        System.out.println("=======================================\n");
    }

    /**
     * Obtiene el número máximo de slots disponibles.
     */
    public int getMaxSlots() {
        return MAX_SLOTS;
    }

    /**
     * Cuenta cuántos slots están ocupados.
     */
    public int getOccupiedSlotsCount() {
        int count = 0;
        for (int i = 1; i <= MAX_SLOTS; i++) {
            if (isSlotOccupied(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Encuentra el primer slot vacío.
     *
     * @return Número del primer slot vacío, o -1 si todos están ocupados
     */
    public int findFirstEmptySlot() {
        for (int i = 1; i <= MAX_SLOTS; i++) {
            if (!isSlotOccupied(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Guarda automáticamente en el primer slot vacío.
     * Si todos están ocupados, sobrescribe el más antiguo.
     *
     * @return Número del slot donde se guardó
     * @throws IOException Si hay error al guardar
     */
    public int autoSave() throws IOException {
        if (!gameController.isGameStarted()) {
            throw new IllegalStateException("No hay ninguna partida iniciada para guardar");
        }

        // Buscar primer slot vacío
        int emptySlot = findFirstEmptySlot();

        if (emptySlot != -1) {
            saveToSlot(emptySlot);
            return emptySlot;
        }

        // Si todos están ocupados, sobrescribir el más antiguo
        int oldestSlot = findOldestSlot();
        saveToSlot(oldestSlot);
        System.out.println("(Slot " + oldestSlot + " sobrescrito - era el más antiguo)");
        return oldestSlot;
    }

    /**
     * Encuentra el slot con la partida más antigua.
     */
    private int findOldestSlot() {
        int oldestSlot = 1;
        long oldestTime = Long.MAX_VALUE;

        for (int i = 1; i <= MAX_SLOTS; i++) {
            SlotInfo info = getSlotInfo(i);
            if (info != null) {
                long time = info.getLastModified().getTime();
                if (time < oldestTime) {
                    oldestTime = time;
                    oldestSlot = i;
                }
            }
        }

        return oldestSlot;
    }

    /**
     * Genera la ruta completa del archivo para un slot.
     */
    private String getSlotFilePath(int slotNumber) {
        return SAVE_DIRECTORY + "slot" + slotNumber + ".xml";
    }

    /**
     * Valida que el número de slot esté en el rango válido.
     */
    private void validateSlotNumber(int slotNumber) {
        if (slotNumber < 1 || slotNumber > MAX_SLOTS) {
            throw new IllegalArgumentException(
                    "El número de slot debe estar entre 1 y " + MAX_SLOTS + ". Recibido: " + slotNumber
            );
        }
    }
}