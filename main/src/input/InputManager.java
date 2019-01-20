package input;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;
import events.EventsCenter;
import events.KeyPressedEvent;
import events.KeyReleasedEvent;

/**
 * Stores the movement states (true or false) of each movement direction (up, down, left, right)
 * for each of the four players.
 */
public class InputManager {

    HashMap<Integer, String> mappings = new HashMap<>();

    private static InputManager singleton;
    private HashMap<Integer, HashMap<String, Boolean>> playerInputs;

    private InputManager() {
        EventsCenter.getSingleton().registerHandler(this);
        playerInputs = new HashMap<>();
        mappings.put(87, "up");
        mappings.put(65, "left");
        mappings.put(83, "down");
        mappings.put(68, "right");
        /*for (int i = 0; i < 4; i++) {
            HashMap<String, Boolean> hmap = new HashMap<>();
            hmap.put("up", false);
            hmap.put("down", false);
            hmap.put("left", false);
            hmap.put("right", false);
            // playerInputs.add(hmap);
        }*/
    }

    public static InputManager getSingleton() {
        if (singleton == null) {
            singleton = new InputManager();
        }
        return singleton;
    }

    public static boolean getKey(String key, int id) {
        if (getSingleton().playerInputs.containsKey(id) && getSingleton().playerInputs.get(id).containsKey(key)) {
            return getSingleton().playerInputs.get(id).get(key);
        }
        return false;
    }

    public static void initialize(int id) {
        HashMap<String, Boolean> hmap = new HashMap<>();
        hmap.put("up", false);
        hmap.put("down", false);
        hmap.put("left", false);
        hmap.put("right", false);
        hmap.put("space", false);
        getSingleton().playerInputs.put(id, hmap);
    }

    /*public ArrayList<HashMap<String, Boolean>> getPlayerInputs() {
        return playerInputs;
    }*/

    public void setPlayerMovement(int playerNum, String movementDirection, boolean movementState) {
        playerInputs.get(playerNum).replace(movementDirection, movementState);
    }

    public GameKeyListener getInput(String key) {
        return GameKeyListener.getSingleton();
    }

    @Subscribe
    public void updateKeyPress(KeyPressedEvent e) {
        int code = e.getKeyEvent().getKeyCode();

        switch (code) {
            case KeyEvent.VK_UP:
                playerInputs.get(1).put("up", true);
                break;
            case KeyEvent.VK_LEFT:
                playerInputs.get(1).put("left", true);
                break;
            case KeyEvent.VK_DOWN:
                playerInputs.get(1).put("down", true);
                break;
            case KeyEvent.VK_RIGHT:
                playerInputs.get(1).put("right", true);
                break;
            case KeyEvent.VK_SPACE:
                playerInputs.get(1).put("space", true);
                break;
        }
    }

    @Subscribe
    public void updateKeyRelease(KeyReleasedEvent e) {
        int code = e.getKeyEvent().getKeyCode();

        switch (code) {
            case KeyEvent.VK_UP:
                playerInputs.get(1).put("up", false);
                break;
            case KeyEvent.VK_LEFT:
                playerInputs.get(1).put("left", false);
                break;
            case KeyEvent.VK_DOWN:
                playerInputs.get(1).put("down", false);
                break;
            case KeyEvent.VK_RIGHT:
                playerInputs.get(1).put("right", false);
                break;
            case KeyEvent.VK_SPACE:
                playerInputs.get(1).put("space", false);
                break;
        }
    }

    public void update(int id, int key, boolean status) {
        Logger logger = Logger.getGlobal();
        if(!playerInputs.containsKey(id)) {
            logger.log(Level.SEVERE, "invalid id: " + id);
            return;
        }
        playerInputs.get(id).put(mappings.get(key), status);
        logger.log(Level.INFO, String.format("Updated key %s to %s (id: %s", mappings.get(key), status, id));
    }
}
