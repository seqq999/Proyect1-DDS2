package edu.ucr.c36342.proyectodesarrollo2.services;

import java.util.Date;

/**
 * Información sobre una partida guardada en un slot.
 */
public class SlotInfo {
    private int slotNumber;
    private String filePath;
    private Date lastModified;
    private long fileSize;

    public SlotInfo(int slotNumber, String filePath, Date lastModified, long fileSize) {
        this.slotNumber = slotNumber;
        this.filePath = filePath;
        this.lastModified = lastModified;
        this.fileSize = fileSize;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public String getFilePath() {
        return filePath;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public long getFileSize() {
        return fileSize;
    }

    /**
     * Obtiene el tamaño del archivo en formato legible (KB, MB).
     */
    public String getFileSizeFormatted() {
        if (fileSize < 1024) {
            return fileSize + " B";
        } else if (fileSize < 1024 * 1024) {
            return String.format("%.2f KB", fileSize / 1024.0);
        } else {
            return String.format("%.2f MB", fileSize / (1024.0 * 1024.0));
        }
    }

    @Override
    public String toString() {
        return String.format("Slot %d [%s] - %s",
                slotNumber,
                getFileSizeFormatted(),
                lastModified);
    }
}
