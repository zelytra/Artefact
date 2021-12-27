package fr.zelytra.novaStructura.manager.loottable.content;

import java.util.Random;

public record DynamicRange(int min, int max) {

    public int drawValue(){
        return new Random().nextInt(min, max);
    }
}
