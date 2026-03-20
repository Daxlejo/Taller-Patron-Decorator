package adventure;

public class MapNode {
    private String enemyName;
    private int enemyHealth;
    private int enemyAttack;
    private int enemyDefense;
    private String enemyIcon;
    private String hint;
    private boolean hasMysteryBox;
    private boolean hasTreasure;
    private boolean isTrap;
    private String scenarioName;

    public MapNode(String scenarioName, String enemyName, String enemyIcon,
                   int enemyHealth, int enemyAttack, int enemyDefense,
                   String hint, boolean hasMysteryBox, boolean hasTreasure, boolean isTrap) {
        this.scenarioName = scenarioName;
        this.enemyName = enemyName;
        this.enemyIcon = enemyIcon;
        this.enemyHealth = enemyHealth;
        this.enemyAttack = enemyAttack;
        this.enemyDefense = enemyDefense;
        this.hint = hint;
        this.hasMysteryBox = hasMysteryBox;
        this.hasTreasure = hasTreasure;
        this.isTrap = isTrap;
    }

    public String getEnemyName() { return enemyName; }
    public int getEnemyHealth() { return enemyHealth; }
    public int getEnemyAttack() { return enemyAttack; }
    public int getEnemyDefense() { return enemyDefense; }
    public String getEnemyIcon() { return enemyIcon; }
    public String getHint() { return hint; }
    public boolean hasMysteryBox() { return hasMysteryBox; }
    public boolean hasTreasure() { return hasTreasure; }
    public boolean isTrap() { return isTrap; }
    public String getScenarioName() { return scenarioName; }

    public String toJson() {
        return String.format(
            "{\"scenarioName\":\"%s\",\"enemyName\":\"%s\",\"enemyIcon\":\"%s\"," +
            "\"enemyHealth\":%d,\"enemyAttack\":%d,\"enemyDefense\":%d," +
            "\"hint\":\"%s\",\"hasMysteryBox\":%b,\"hasTreasure\":%b,\"isTrap\":%b}",
            scenarioName, enemyName, enemyIcon,
            enemyHealth, enemyAttack, enemyDefense,
            hint, hasMysteryBox, hasTreasure, isTrap
        );
    }
}
