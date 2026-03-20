import server.GameServer;

/**
 * Punto de entrada de la aplicación.
 * Inicia el servidor HTTP en el puerto 8080.
 * 
 * Para ejecutar: Abrir http://localhost:8080 en el navegador.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        GameServer server = new GameServer();
        server.start(8080);
    }
}
