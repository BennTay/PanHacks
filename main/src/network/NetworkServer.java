package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import input.InputManager;
import logic.GameState;

/**
 * Server that maintains a synchronized gamestate and periodically updates clients with information about
 * latest gamestate.
 *
 * Also accepts inputs updates and updates internal inputmanager
 */
public class NetworkServer implements Runnable {
    int portNumber = 3000;
    ServerSocket serverSocket;
    Socket clientSocket;
    PrintWriter out;
    BufferedReader in;
    private ArrayList<NetworkServerThread> threads;
    private GameState gameState;

    public NetworkServer(GameState gameState) {
        threads = new ArrayList<>();
        this.gameState = gameState;
    }

    @Override
    public void run() {
        Logger logger = Logger.getGlobal();
        try {
            serverSocket = new ServerSocket(portNumber);
            while (true) {
                clientSocket = serverSocket.accept();
                logger.log(Level.INFO, "Accepted a client");
                NetworkServerThread thread = new NetworkServerThread(clientSocket, gameState);
                threads.add(thread);
                thread.start();
                logger.log(Level.INFO, "Waiting for next client");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void push() {
        for (NetworkServerThread t: threads) {
            t.push();
        }
    }
}

class NetworkServerThread extends Thread {
    Socket clientSocket;
    PrintWriter out;
    BufferedReader in;
    public int id;
    private GameState gameState;
    private boolean connected = false;

    NetworkServerThread(Socket clientSocket, GameState gameState) {
        this.clientSocket = clientSocket;
        this.gameState = gameState;
    }

    public void run() {
        String fromClient;
        Logger logger = Logger.getGlobal();
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            logger.log(Level.INFO, "Waiting for client id...");
            // first thing client does when connecting is send id
            fromClient = in.readLine();
            id = Integer.parseInt(fromClient);
            logger.log(Level.INFO, "Received id from client: " + id);
            gameState.registerPlayer(id);

            connected = true;
            // send gamestate
            out.println(gameState.serialize());

            while (true) {
                // accepts inputs in the format key:value
                fromClient = in.readLine();
                Logger.getGlobal().log(Level.INFO,
                        String.format("Received packet from client %d: %s \n", id, fromClient));
                String[] split = fromClient.split(":");
                int id = Integer.parseInt(split[0]);
                int key = Integer.parseInt(split[1]);
                boolean status = Boolean.parseBoolean(split[2]);
                InputManager.getSingleton().update(id, key, status);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void push() {
        if (!connected) {
            return;
        }
        out.println(gameState.serialize());
    }
}
