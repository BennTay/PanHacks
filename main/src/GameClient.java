import javax.swing.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import graphics.Maze;
import graphics.Renderer;
import input.GameKeyListener;
import logic.GameState;
import logic.Time;
import network.NetworkClient;

public class GameClient extends JFrame {
    private Renderer renderer;
    private GameState gameState;
    private NetworkClient networkManager;
    private int id;
    private Logger logger;
    private GameKeyListener gameKeyListener;

    public GameClient() {
        logger = Logger.getGlobal();
        gameKeyListener = GameKeyListener.getSingleton();
        init();
    }

    public static void main(String[] args) {
        GameClient gameClient = new GameClient();
        gameClient.init();
    }

    public void initUI() {
        Logger.getGlobal().log(Level.INFO,
                "Initializing UI");
        add(renderer);
        setTitle("PanHacks Game");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void init() {
        id = generateId();
        gameState = new GameState(Maze.generateGrid());
        networkManager = new NetworkClient(id, "localhost", 3000, gameState);

        renderer = new Renderer(gameState);
        renderer.addKeyListener(gameKeyListener);
        renderer.setFocusable(true);
        setVisible(true);

        initUI();
        Time.lastTime = System.currentTimeMillis();
        logger.log(Level.INFO, "Successfully initialized with id " + id);

        start();
    }

    public void start() {
        Runnable update = () -> {
            while (true) {
                renderer.repaint();
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        update.run();
    }

    private int generateId() {
        return new Random().nextInt();
    }

}
