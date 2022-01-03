package fr.zelytra.novaStructura.manager.loottable.content;

public class StringPotion {

    private final String effect;

    private int amplifier, duration;
    private DynamicRange amplifierRange;

    public StringPotion(String effect, int amplifier, int duration) {
        this.effect = effect;
        this.amplifier = amplifier;
        this.duration = duration;
    }

    public StringPotion(String effect, DynamicRange amplifierRange, int duration) {
        this.effect = effect;
        this.amplifierRange = amplifierRange;
        this.duration = duration;
    }

    public String getEffect() {
        return effect;
    }

    public int getAmplifier() {
        if (amplifierRange != null)
            return amplifierRange.drawValue();
        else
            return amplifier;
    }

    public int getDuration() {
        return duration * 20;
    }

    @Override
    public String toString() {
        return "Potion Type: " + effect + " Duration: " + duration + " Amplifier: " + getAmplifier();
    }
}
