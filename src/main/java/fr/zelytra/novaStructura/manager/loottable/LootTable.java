package fr.zelytra.novaStructura.manager.loottable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LootTable implements Serializable {

    private final String name;
    private final List<Loot> loots;

    public LootTable(String name) {
        this.name = name;
        this.loots = new ArrayList<>();
    }

    public void add(Loot loot){
        loots.add(loot);
    }

    public String getName() {
        return name;
    }

    public List<Loot> getLoots() {
        return loots;
    }

    private List<Loot> loadFromFile(String name){
        //TODO Load from file items
        return new ArrayList<>();
    }
}
