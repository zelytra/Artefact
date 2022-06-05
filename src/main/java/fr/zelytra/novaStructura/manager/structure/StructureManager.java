package fr.zelytra.novaStructura.manager.structure;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.logs.LogType;
import fr.zelytra.novaStructura.utils.timer.Timer;

import java.io.File;
import java.util.ArrayList;

public class StructureManager {

    public static final String PATH = NovaStructura.getInstance().getDataFolder() + File.separator;
    public static int structureCount = 0;

    public StructureManager() {
        folderInit();
        NovaStructura.log("Loading structures...", LogType.INFO);
        loadStructures();
    }

    public void reload() {
        NovaStructura.log("Reloading structures...", LogType.INFO);
        loadStructures();
    }

    private void loadStructures() {
        File folder = new File(PATH + StructureFolder.SCHEMATIC);
        Structure.structureList = new ArrayList<>();
        structureCount = 0;
        Timer timer = new Timer();

        for (File schematic : folder.listFiles()) {
            String name = schematic.getName().replace(StructureFolder.SCHEMATIC.extension, "");
            NovaStructura.log("Loading Structure " + name , LogType.VERBOSE);
            File config = new File(PATH + StructureFolder.CONFIG + name + StructureFolder.CONFIG.extension);

            if (!config.exists()) {
                NovaStructura.log("No config detected for " + name + " : generating default conf", LogType.WARN);
                Structure structure = new Structure(schematic);
                structure.save();
            } else {
                new Structure(schematic, config);
            }

            structureCount++;
        }

        NovaStructura.log(structureCount + " structures loaded §8[" + timer.stop() + "]", LogType.INFO);
    }

    private void folderInit() {
        for (StructureFolder folder : StructureFolder.values()) {
            File f = new File(PATH + folder);
            if (!f.exists()) f.mkdirs();
        }
    }

}
