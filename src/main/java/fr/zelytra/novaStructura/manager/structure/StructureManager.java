package fr.zelytra.novaStructura.manager.structure;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.logs.LogType;
import fr.zelytra.novaStructura.utils.timer.Timer;

import java.io.File;

public class StructureManager {
    public static final String PATH = NovaStructura.getInstance().getDataFolder() + File.separator;
    public static int structureCount = 0;

    public StructureManager() {
        folderInit();
        loadStructures();
    }

    private void loadStructures() {
        File folder = new File(PATH + StructureFolder.SCHEMATIC);

       structureCount =0;
        Timer timer = new Timer();

        NovaStructura.log("Loading structures...", LogType.INFO);

        for (File schematic : folder.listFiles()) {
            String name = schematic.getName().replace(StructureFolder.SCHEMATIC.extension, "");
            File config = new File(PATH + StructureFolder.CONFIG + name + StructureFolder.CONFIG.extension);

            if (!config.exists()) {
                NovaStructura.log("No config detected for " + name + " : generating default conf", LogType.WARN);
                Structure structure = new Structure(schematic);
                structure.save(config);
            }else {
                new Structure(schematic,config);
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
