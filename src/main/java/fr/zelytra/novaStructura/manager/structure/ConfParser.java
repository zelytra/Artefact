package fr.zelytra.novaStructura.manager.structure;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.biome.NovaBiome;
import fr.zelytra.novaStructura.manager.logs.LogType;
import fr.zelytra.novaStructura.manager.structure.exception.ConfigParserException;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

    public static List<NovaBiome> parseBiome(List<String> data) {
        List<NovaBiome> biomes = new ArrayList<>();

        for (String biome : data) {
            try {

                if (biome.contains(":")) {
                    String key = biome.split(":")[0], value = biome.split(":")[1];

                    if (key.equalsIgnoreCase("minecraft")) {
                        if (Biome.valueOf(value.toUpperCase()) != null) {
                            biomes.add(new NovaBiome(Biome.valueOf(value.toUpperCase())));
                        }
                    } else {
                        biomes.add(new NovaBiome(new NamespacedKey(key, value)));
                    }

                } else {

                    if (Biome.valueOf(biome.toUpperCase()) == null)
                        throw new ConfigParserException("Biome name not found for " + biome);

                    biomes.add(new NovaBiome(Biome.valueOf(biome.toUpperCase()).getKey()));
                }

            } catch (ConfigParserException | IllegalArgumentException e) {
                NovaStructura.log("Failed to parse " + biome + " biome name, please check config", LogType.ERROR);
            }
        }
        return biomes;
    }


}
