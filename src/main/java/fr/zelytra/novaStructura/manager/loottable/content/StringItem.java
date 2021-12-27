package fr.zelytra.novaStructura.manager.loottable.content;

public class StringItem {

    private final String material;

    private int amount;
    private DynamicRange amountRange;

    public StringItem(String material,int amount){
        this.material = material;
        this.amount = amount;
    }

    public StringItem(String material, DynamicRange amountRange){
        this.material = material;
        this.amountRange = amountRange;
    }

    public String getMaterial() {
        return material;
    }

    public int getAmount() {
        if (amountRange != null)
            return amountRange.drawValue();
        else
            return amount;
    }
}
