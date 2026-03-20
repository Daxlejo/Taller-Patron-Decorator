package combat;

import model.GameCharacter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombatEngine {
    private Random random = new Random();

    public List<String> simulateCombat(GameCharacter p1, GameCharacter p2) {
        List<String> log = new ArrayList<>();
        int healthP1 = p1.getHealth();
        int healthP2 = p2.getHealth();
        int turn = 1;

        log.add("=== COMBATE: " + p1.getName() + " vs " + p2.getName() + " ===");
        log.add(p1.getName() + " → ATK:" + p1.getAttack() + " DEF:" + p1.getDefense() + " HP:" + healthP1);
        log.add(p2.getName() + " → ATK:" + p2.getAttack() + " DEF:" + p2.getDefense() + " HP:" + healthP2);
        log.add("---");

        while (healthP1 > 0 && healthP2 > 0) {
            log.add(">> Turno " + turn);

            int damage1 = calculateDamage(p1.getAttack(), p2.getDefense());
            healthP2 -= damage1;
            log.add(p1.getName() + " ataca → " + damage1 + " daño. " + p2.getName() + " HP: " + Math.max(0, healthP2));

            if (healthP2 <= 0) {
                log.add("🏆 ¡" + p1.getName() + " GANA!");
                break;
            }

            int damage2 = calculateDamage(p2.getAttack(), p1.getDefense());
            healthP1 -= damage2;
            log.add(p2.getName() + " ataca → " + damage2 + " daño. " + p1.getName() + " HP: " + Math.max(0, healthP1));

            if (healthP1 <= 0) {
                log.add("🏆 ¡" + p2.getName() + " GANA!");
                break;
            }

            turn++;
        }

        return log;
    }

    // Daño = ataque - defensa (mínimo 1), con variación aleatoria +/- 20%
    private int calculateDamage(int attack, int defense) {
        int baseDamage = Math.max(1, attack - defense);
        double variation = 0.8 + (random.nextDouble() * 0.4);
        return Math.max(1, (int)(baseDamage * variation));
    }
}
