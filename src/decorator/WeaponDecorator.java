package decorator;

import model.GameCharacter;

public class WeaponDecorator extends EquipmentDecorator {
    private String weaponName;
    private int attackBonus;

    public WeaponDecorator(GameCharacter character, String weaponName, int attackBonus) {
        super(character);
        this.weaponName = weaponName;
        this.attackBonus = attackBonus;
    }

    @Override
    public int getAttack() {
        return super.getAttack() + attackBonus;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + " + weaponName + "(ATK+" + attackBonus + ")";
    }
}
