package model;

public class BaseHero implements GameCharacter {
    private String name;
    private int health;
    private int attack;
    private int defense;

    public BaseHero(String name, int health, int attack, int defense) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public String getDescription() {
        return name + " [Base]";
    }
}
