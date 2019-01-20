package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import graphics.Wall;
import input.InputManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameState {
    private ArrayList<Player> players;
    private transient ArrayList<Wall> walls;
    private ArrayList<Bullet> bullets;
    public GameState(ArrayList<Wall> walls) {
        players = new ArrayList<>();
        this.walls = walls;

        //when player joins game must be added to the player arraylist

        //for testing
        // players.add(new Player(new Vector(100, 100), 32, 1));
        // players.add(new Player(new Vector(300, 300), 32, 2));
    }

    public void registerPlayer(int id) {
        players.add(new Player(new Vector(new Random().nextFloat() * 1000, 100), 32, id));
    }


    public void update(InputManager inputManager) {
        for (Player p: players) {
            p.update(inputManager, this);
        }

        for (Wall w: walls) {
            w.update(inputManager, this);
        }
    }

    public String serialize() {
        return new Gson().toJson(players);
    }

    public void apply(String newState) {
        Player[] arr = new Gson().fromJson(newState, Player[].class);
        players = new ArrayList<>(Arrays.asList(arr));
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Iterable<? extends Wall> getWalls() {
        return walls;
    }
}
