package adventure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMap {
    private List<List<MapNode>> routes;
    private Random random = new Random();

    private static final String[] BUFF_NAMES = {
        "Pocion de Fuerza", "Elixir de Vida", "Escudo Magico",
        "Furia del Dragon", "Bendicion Divina"
    };
    // {attackBonus, defenseBonus, healthBonus}
    private static final int[][] BUFF_STATS = {
        {8, 0, 0}, {0, 0, 40}, {0, 8, 0}, {12, 0, -15}, {4, 4, 20}
    };

    public GameMap() {
        this.routes = new ArrayList<>();
        buildMap();
    }

    private void buildMap() {
        // Ruta 1: Bosque Oscuro (TRAMPA)
        List<MapNode> route1 = new ArrayList<>();
        route1.add(new MapNode("Entrada del Bosque", "Goblin Explorador", "👺",
            60, 8, 3, "El bosque susurra mentiras... el oro no crece entre arboles.", true, false, false));
        route1.add(new MapNode("Claro del Bosque", "Esqueleto Arquero", "💀",
            80, 12, 5, "Las flechas oseas apuntan al norte, pero el tesoro esta al sur.", false, false, false));
        route1.add(new MapNode("Cueva del Bosque", "Dragon Verde", "🐉",
            130, 18, 10, "Este dragon no custodia nada valioso... solo huesos.", true, false, false));
        route1.add(new MapNode("Corazon del Bosque", "Demonio del Bosque", "😈",
            160, 22, 12, "La oscuridad total no esconde tesoros, solo peligro.", false, false, false));
        route1.add(new MapNode("Final del Bosque", "Gran Treant", "🌳",
            180, 20, 15, "", false, false, true));

        // Ruta 2: Montanas Heladas (TRAMPA)
        List<MapNode> route2 = new ArrayList<>();
        route2.add(new MapNode("Pie de la Montana", "Lobo Helado", "🐺",
            70, 10, 4, "El frio congela hasta las esperanzas... busca calor.", true, false, false));
        route2.add(new MapNode("Paso de Montana", "Goblin de Hielo", "👺",
            65, 9, 6, "Las montanas son hermosas pero vacias de fortuna.", false, false, false));
        route2.add(new MapNode("Pico Nevado", "Yeti", "⛄",
            140, 20, 8, "El Yeti ruge, pero no protege nada mas que nieve.", true, false, false));
        route2.add(new MapNode("Glaciar Eterno", "Esqueleto de Hielo", "💀",
            90, 14, 7, "Todo esta congelado aqui... incluyendo tus posibilidades.", false, false, false));
        route2.add(new MapNode("Cumbre Helada", "Dragon de Hielo", "🐉",
            200, 24, 14, "", false, false, true));

        // Ruta 3: Templo Antiguo (TESORO)
        List<MapNode> route3 = new ArrayList<>();
        route3.add(new MapNode("Puerta del Templo", "Guardian de Piedra", "🗿",
            75, 9, 8, "Los antiguos dejaron sus riquezas en templos sagrados...", true, false, false));
        route3.add(new MapNode("Sala de Pilares", "Dragon de Fuego", "🐉",
            120, 16, 9, "El fuego purifica el camino hacia algo grandioso.", false, false, false));
        route3.add(new MapNode("Biblioteca Secreta", "Esqueleto Mago", "💀",
            85, 13, 5, "Los libros antiguos hablan de un tesoro al final del tercer camino.", true, false, false));
        route3.add(new MapNode("Camara de Pruebas", "Goblin Anciano", "👺",
            55, 7, 4, "El guardian mas debil custodia la entrada al premio mayor.", false, false, false));
        route3.add(new MapNode("Camara del Tesoro", "Guardian Final", "👑",
            100, 15, 10, "", false, true, false));

        // Ruta 4: Pantano Maldito (TRAMPA)
        List<MapNode> route4 = new ArrayList<>();
        route4.add(new MapNode("Orilla del Pantano", "Dragon del Pantano", "🐉",
            110, 15, 7, "Los pantanos esconden podredumbre, no riquezas.", false, false, false));
        route4.add(new MapNode("Cienaga Profunda", "Esqueleto Pantanoso", "💀",
            80, 11, 5, "Hundirse en el pantano no te acerca al tesoro.", true, false, false));
        route4.add(new MapNode("Isla del Pantano", "Goblin Venenoso", "👺",
            60, 10, 3, "El veneno nubla la mente... este no es el camino correcto.", false, false, false));
        route4.add(new MapNode("Templo Hundido", "Demonio del Agua", "😈",
            170, 23, 13, "Las aguas turbias solo traen desgracia.", true, false, false));
        route4.add(new MapNode("Corazon del Pantano", "Hidra", "🐍",
            220, 26, 16, "", false, false, true));

        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        routes.add(route4);
    }

    public MapNode getNode(int routeIndex, int nodeIndex) {
        if (routeIndex < 0 || routeIndex >= routes.size()) return null;
        List<MapNode> route = routes.get(routeIndex);
        if (nodeIndex < 0 || nodeIndex >= route.size()) return null;
        return route.get(nodeIndex);
    }

    public String getMapInfoJson(int currentNodeIndex) {
        StringBuilder json = new StringBuilder("{\"currentNode\":" + currentNodeIndex + ",\"routes\":[");
        for (int r = 0; r < routes.size(); r++) {
            json.append("[");
            for (int n = 0; n < routes.get(r).size(); n++) {
                MapNode node = routes.get(r).get(n);
                json.append(String.format(
                    "{\"scenarioName\":\"%s\",\"enemyIcon\":\"%s\",\"nodeIndex\":%d}",
                    node.getScenarioName(), node.getEnemyIcon(), n
                ));
                if (n < routes.get(r).size() - 1) json.append(",");
            }
            json.append("]");
            if (r < routes.size() - 1) json.append(",");
        }
        json.append("],\"routeNames\":[\"Bosque Oscuro\",\"Montanas Heladas\",\"Templo Antiguo\",\"Pantano Maldito\"]}");
        return json.toString();
    }

    public String getRandomBuff() {
        int index = random.nextInt(BUFF_NAMES.length);
        return String.format(
            "{\"name\":\"%s\",\"attackBonus\":%d,\"defenseBonus\":%d,\"healthBonus\":%d}",
            BUFF_NAMES[index], BUFF_STATS[index][0], BUFF_STATS[index][1], BUFF_STATS[index][2]
        );
    }

    public int getRouteCount() { return routes.size(); }
    public int getNodeCount() { return routes.get(0).size(); }
}
