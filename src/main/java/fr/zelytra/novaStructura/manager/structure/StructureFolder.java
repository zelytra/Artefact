package fr.zelytra.novaStructura.manager.structure;

import java.io.File;

public enum StructureFolder {

    SCHEMATIC("schematics",".struct"),
    CONFIG("configs",".conf"),
    LOOTS("loots",".loot");

    public final String folderName;
    public final String extension;

    StructureFolder(String folderName,String extension) {
        this.folderName = folderName;
        this.extension = extension;
    }

    @Override
    public String toString() {
        return folderName + File.separator;
    }
}
