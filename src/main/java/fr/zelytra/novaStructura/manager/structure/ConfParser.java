package fr.zelytra.novaStructura.manager.structure;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.logs.LogType;
import fr.zelytra.novaStructura.manager.structure.exception.ConfigParserException;
import org.bukkit.Material;
import org.bukkit.block.Biome;

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

                if (Material.getMaterial(material.toUpperCase()) == null)
                    throw new ConfigParserException("Material name not found for " + material);

                materials.add(Material.getMaterial(material.toUpperCase()));

            } catch (ConfigParserException e) {
                NovaStructura.log(e.getLocalizedMessage(), LogType.ERROR);
            }
        }
        return materials;

    }

    public static List<Biome> parseBiome(List<String> list) {
        List<Biome> biomes = new ArrayList<>();
        for (String biome : list)
            if (Biome.valueOf(biome) != null)
                biomes.add(Biome.valueOf(biome));
        return biomes;
    }
}
