package server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import model.GameCharacter;
import model.BaseHero;
import decorator.*;
import combat.CombatEngine;
import adventure.GameMap;
import adventure.MapNode;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GameServer {
    private GameCharacter currentHero;
    private GameCharacter baseHeroBackup;
    private CombatEngine combatEngine = new CombatEngine();
    private GameMap gameMap = new GameMap();

    public void start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Sirve el HTML
        server.createContext("/", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                String html = new String(Files.readAllBytes(Paths.get("src/web/index.html")), "UTF-8");
                sendResponse(exchange, html, "text/html; charset=UTF-8");
            }
        });

        // Crea y equipa al héroe
        server.createContext("/equipar", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseParams(readBody(exchange));
                buildHero(params);
                baseHeroBackup = currentHero;

                String json = String.format(
                        "{\"nombre\":\"%s\",\"vida\":%d,\"ataque\":%d,\"defensa\":%d,\"descripcion\":\"%s\"}",
                        currentHero.getName(), currentHero.getHealth(),
                        currentHero.getAttack(), currentHero.getDefense(),
                        currentHero.getDescription());
                sendResponse(exchange, json, "application/json; charset=UTF-8");
            }
        });

        // Retorna info del mapa
        server.createContext("/mapa", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseParams(readBody(exchange));
                int currentNode = Integer.parseInt(params.getOrDefault("nodo", "0"));
                String json = gameMap.getMapInfoJson(currentNode);
                sendResponse(exchange, json, "application/json; charset=UTF-8");
            }
        });

        // Retorna info de un nodo específico
        server.createContext("/nodo", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseParams(readBody(exchange));
                int routeIndex = Integer.parseInt(params.getOrDefault("ruta", "0"));
                int nodeIndex = Integer.parseInt(params.getOrDefault("nodo", "0"));

                MapNode node = gameMap.getNode(routeIndex, nodeIndex);
                if (node != null) {
                    sendResponse(exchange, node.toJson(), "application/json; charset=UTF-8");
                } else {
                    sendResponse(exchange, "{\"error\":\"Nodo no encontrado\"}", "application/json; charset=UTF-8");
                }
            }
        });

        // Ejecuta combate contra el enemigo del nodo
        server.createContext("/combatir", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseParams(readBody(exchange));
                int routeIndex = Integer.parseInt(params.getOrDefault("ruta", "0"));
                int nodeIndex = Integer.parseInt(params.getOrDefault("nodo", "0"));

                if (currentHero == null) {
                    currentHero = new BaseHero("Guerrero", 100, 10, 5);
                }

                MapNode node = gameMap.getNode(routeIndex, nodeIndex);
                GameCharacter enemy;
                if (node != null) {
                    enemy = new BaseHero(node.getEnemyName(),
                            node.getEnemyHealth(), node.getEnemyAttack(), node.getEnemyDefense());
                } else {
                    enemy = new BaseHero("Goblin", 60, 8, 3);
                }

                List<String> log = combatEngine.simulateCombat(currentHero, enemy);

                // Restaurar héroe sin buff temporal después del combate
                currentHero = baseHeroBackup;

                String json = "{\"log\":" + toJsonArray(log) + "}";
                sendResponse(exchange, json, "application/json; charset=UTF-8");
            }
        });

        // Abre caja misteriosa y aplica buff temporal
        server.createContext("/caja", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                String buffJson = gameMap.getRandomBuff();

                Map<String, String> buffData = parseJsonSimple(buffJson);
                int atkBonus = Integer.parseInt(buffData.getOrDefault("attackBonus", "0"));
                int defBonus = Integer.parseInt(buffData.getOrDefault("defenseBonus", "0"));
                int hpBonus = Integer.parseInt(buffData.getOrDefault("healthBonus", "0"));
                String buffName = buffData.getOrDefault("name", "Buff");

                baseHeroBackup = currentHero;
                currentHero = new TemporaryBuffDecorator(currentHero, buffName, atkBonus, defBonus, hpBonus);

                String response = String.format(
                        "{\"buff\":%s,\"heroStats\":{\"vida\":%d,\"ataque\":%d,\"defensa\":%d}}",
                        buffJson, currentHero.getHealth(), currentHero.getAttack(), currentHero.getDefense());
                sendResponse(exchange, response, "application/json; charset=UTF-8");
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("=================================");
        System.out.println("  CombatEnvolved Server");
        System.out.println("  http://localhost:" + port);
        System.out.println("=================================");
    }

    private void buildHero(Map<String, String> params) {
        String heroClass = params.getOrDefault("clase", "Guerrero");
        String heroName = params.getOrDefault("nombre", heroClass);

        switch (heroClass) {
            case "Guerrero":
                currentHero = new BaseHero(heroName, 120, 12, 8);
                break;
            case "Mago":
                currentHero = new BaseHero(heroName, 80, 18, 4);
                break;
            case "Arquero":
                currentHero = new BaseHero(heroName, 90, 15, 6);
                break;
            default:
                currentHero = new BaseHero(heroName, 100, 10, 5);
        }

        applyWeapon(params.getOrDefault("arma", ""));
        applyArmor(params.getOrDefault("armadura", ""));
        applyPower(params.getOrDefault("poder", ""));
    }

    private void applyWeapon(String weapon) {
        if (weapon.isEmpty())
            return;
        switch (weapon) {
            case "Espada de Fuego":
                currentHero = new WeaponDecorator(currentHero, "Espada de Fuego", 15);
                break;
            case "Arco Elfico":
                currentHero = new WeaponDecorator(currentHero, "Arco Elfico", 12);
                break;
            case "Baston Arcano":
                currentHero = new WeaponDecorator(currentHero, "Baston Arcano", 18);
                break;
        }
    }

    private void applyArmor(String armor) {
        if (armor.isEmpty())
            return;
        switch (armor) {
            case "Armadura de Hierro":
                currentHero = new ArmorDecorator(currentHero, "Armadura de Hierro", 10);
                break;
            case "Tunica Magica":
                currentHero = new ArmorDecorator(currentHero, "Tunica Magica", 6);
                break;
            case "Cota de Malla":
                currentHero = new ArmorDecorator(currentHero, "Cota de Malla", 8);
                break;
        }
    }

    private void applyPower(String power) {
        if (power.isEmpty())
            return;
        switch (power) {
            case "Bola de Fuego":
                currentHero = new PowerDecorator(currentHero, "Bola de Fuego", 10, 0);
                break;
            case "Escudo Divino":
                currentHero = new PowerDecorator(currentHero, "Escudo Divino", 0, 30);
                break;
            case "Furia Berserker":
                currentHero = new PowerDecorator(currentHero, "Furia Berserker", 20, -10);
                break;
        }
    }

    private void sendResponse(HttpExchange exchange, String body, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        byte[] bytes = body.getBytes("UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private String readBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes());
    }

    private Map<String, String> parseParams(String body) {
        Map<String, String> params = new HashMap<>();
        if (body == null || body.isEmpty())
            return params;
        for (String param : body.split("&")) {
            String[] kv = param.split("=", 2);
            if (kv.length == 2) {
                params.put(
                        java.net.URLDecoder.decode(kv[0], java.nio.charset.StandardCharsets.UTF_8),
                        java.net.URLDecoder.decode(kv[1], java.nio.charset.StandardCharsets.UTF_8));
            }
        }
        return params;
    }

    private Map<String, String> parseJsonSimple(String json) {
        Map<String, String> map = new HashMap<>();
        json = json.replace("{", "").replace("}", "");
        for (String pair : json.split(",")) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) {
                String key = kv[0].trim().replace("\"", "");
                String value = kv[1].trim().replace("\"", "");
                map.put(key, value);
            }
        }
        return map;
    }

    private String toJsonArray(List<String> items) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            sb.append("\"").append(items.get(i).replace("\"", "\\\"")).append("\"");
            if (i < items.size() - 1)
                sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
