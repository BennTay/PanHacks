package logic;

import graphics.Maze;
import graphics.Renderer;
import input.GameKeyListener;
import input.InputManager;
import network.NetworkManager;

import javax.swing.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends JFrame {
    private Renderer renderer;
    private GameState gameState;
    private NetworkManager networkManager;
    private InputManager inputManager;
    private boolean isServer;
    private int id;
    private Logger logger;
    private GameKeyListener gameKeyListener;

    public Game(Logger logger) {
        this.logger = logger;
        gameKeyListener = GameKeyListener.getSingleton();
        init();
    }

    public void initUI() {
       add(renderer);
       setTitle("PanHacks Game");
       setSize(1200, 800);
       setLocationRelativeTo(null);
       setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Core gameplay loop, calls the update method of all entities inside GameState
     */
    public void update() throws InterruptedException {
        // gets latest inputs from clients
        // inputManager.updateInputs(networkManager.getInputs());
        Time.deltaTime = (System.currentTimeMillis() - Time.lastTime)/1000f;
        gameState.update(inputManager);
        if (isServer) {
            networkManager.push(gameState);
        }

        renderer.repaint();
        Time.lastTime = System.currentTimeMillis();
        Thread.sleep(15);
        update();
    }

    public void init() {
        id = generateId();
        gameState = new GameState(Maze.generateGrid());
        renderer = new Renderer(gameState);
        renderer.addKeyListener(gameKeyListener);
        renderer.setFocusable(true);
        networkManager = new NetworkManager();
        initUI();
        Time.lastTime = System.currentTimeMillis();
        logger.log(Level.INFO, "Successfully initialized with id " + id);
    }

    public void start() {
        try {
            update();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int generateId() {
        return new Random().nextInt();
    }
}
