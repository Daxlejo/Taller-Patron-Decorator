package decorator;

import model.GameCharacter;

public class TemporaryBuffDecorator extends EquipmentDecorator {
    private String buffName;
    private int attackBonus;
    private int defenseBonus;
    private int healthBonus;

    public TemporaryBuffDecorator(GameCharacter character, String buffName,
            int attackBonus, int defenseBonus, int healthBonus) {
        super(character);
        this.buffName = buffName;
        this.attackBonus = attackBonus;
        this.defenseBonus = defenseBonus;
        this.healthBonus = healthBonus;
    }

    @Override
    public int getAttack() {
        return super.getAttack() + attackBonus;
    }

    @Override
    public int getDefense() {
        return super.getDefense() + defenseBonus;
    }

    @Override
    public int getHealth() {
        return super.getHealth() + healthBonus;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " con " + buffName + "(ATK+" + attackBonus + ", DEF+" + defenseBonus + ", HP+"
                + healthBonus + ")";
    }
}
