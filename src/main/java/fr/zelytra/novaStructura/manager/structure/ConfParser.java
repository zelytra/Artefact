package fr.zelytra.novaStructura.manager.structure;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.logs.LogType;
import fr.zelytra.novaStructura.manager.structure.exception.ConfigParserException;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public abstract class ConfParser {

    public static List<String> unparseMaterial(List<Material> materials) {
        List<String> materialNames = new ArrayList<>();

        for (Material material : materials)
            materialNames.add(material.name());

        return materialNames;
    }

    public static List<Material> parseMaterial(List<String> data) {
        List<Material> materials = new ArrayList<>();

        for (String material : data) {
            try {

                if (Material.getMaterial(material) == null)
                    throw new ConfigParserException("Material name not found for " + material);

                materials.add(Material.getMaterial(material));

            } catch (ConfigParserException e) {
                NovaStructura.log(e.getLocalizedMessage(), LogType.ERROR);
            }
        }
        return materials;

    }
}
