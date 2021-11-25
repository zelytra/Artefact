package fr.zelytra.novaStructura.manager.structure;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.logs.LogType;
import fr.zelytra.novaStructura.utils.timer.Timer;

import java.io.File;

public class StructureManager {
    public static final String PATH = NovaStructura.getInstance().getDataFolder() + File.separator;

    public StructureManager() {
        folderInit();
        loadStructures();
    }

    private void loadStructures() {
        File folder = new File(PATH + StructureFolder.SCHEMATIC);

        int count = 0;
        Timer timer = new Timer();

        NovaStructura.log("Loading structures...", LogType.INFO);

        for (File schematic : folder.listFiles()) {
            new Structure(schematic);
            count++;
        }

        NovaStructura.log(count + " structures loaded §8[" + timer.stop() + "]", LogType.INFO);
    }

    private void folderInit() {
        for (StructureFolder folder : StructureFolder.values()) {
            File f = new File(PATH + folder);
            if (!f.exists()) f.mkdirs();
        }
    }

}
