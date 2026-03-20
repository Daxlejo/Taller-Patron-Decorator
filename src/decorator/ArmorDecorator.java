package decorator;

import model.GameCharacter;

public class ArmorDecorator extends EquipmentDecorator {
    private String armorName;
    private int defenseBonus;

    public ArmorDecorator(GameCharacter character, String armorName, int defenseBonus) {
        super(character);
        this.armorName = armorName;
        this.defenseBonus = defenseBonus;
    }

    @Override
    public int getDefense() {
        return super.getDefense() + defenseBonus;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " con " + armorName + "(DEF+" + defenseBonus + ")";
    }
}
