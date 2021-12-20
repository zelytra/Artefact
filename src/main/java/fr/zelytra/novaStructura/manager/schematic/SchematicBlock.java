package fr.zelytra.novaStructura.manager.schematic;

import java.io.Serializable;

public record SchematicBlock(int materialId, String data) implements Serializable {
}
