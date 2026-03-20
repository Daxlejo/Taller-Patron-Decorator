package decorator;

import model.GameCharacter;

public abstract class EquipmentDecorator implements GameCharacter {
    protected GameCharacter decoratedCharacter;

    public EquipmentDecorator(GameCharacter decoratedCharacter) {
        this.decoratedCharacter = decoratedCharacter;
    }

    @Override
    public String getName() {
        return decoratedCharacter.getName();
    }

    @Override
    public int getHealth() {
        return decoratedCharacter.getHealth();
    }

    @Override
    public int getAttack() {
        return decoratedCharacter.getAttack();
    }

    @Override
    public int getDefense() {
        return decoratedCharacter.getDefense();
    }

    @Override
    public String getDescription() {
        return decoratedCharacter.getDescription();
    }
}
