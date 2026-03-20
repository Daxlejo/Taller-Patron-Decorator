package decorator;

import model.GameCharacter;

public class PowerDecorator extends EquipmentDecorator {
    private String powerName;
    private int attackBonus;
    private int healthBonus;

    public PowerDecorator(GameCharacter character, String powerName, int attackBonus, int healthBonus) {
        super(character);
        this.powerName = powerName;
        this.attackBonus = attackBonus;
        this.healthBonus = healthBonus;
    }

    @Override
    public int getAttack() {
        return super.getAttack() + attackBonus;
    }

    @Override
    public int getHealth() {
        return super.getHealth() + healthBonus;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " con " + powerName + "(ATK+" + attackBonus + ", HP+" + healthBonus + ")";
    }

}
