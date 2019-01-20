package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;
import events.EventsCenter;
import events.KeyPressedEvent;
import events.KeyReleasedEvent;
import logic.GameState;

/**
 * Client will establish connection with server, then periodically receive gamestate updates,
 * and will send input updates to server
 */
public class NetworkClient {

    PrintWriter out;
    BufferedReader in;
    GameState gameState;
    private int id;

    public NetworkClient(int id, String hostName, int portNumber, GameState gameState) {

        Logger logger = Logger.getGlobal();
        this.gameState = gameState;
        EventsCenter.getSingleton().registerHandler(this);
        this.id = id;

        try {
            // connect to host
            Socket socket = new Socket(hostName, portNumber);
            logger.log(Level.INFO, "Connected to server");

            // get I/O stuff
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // say hi (send id)
            out.println(id);
            logger.log(Level.INFO, "Sent id");

            // wait for gameState
            String fromServer;
            fromServer = in.readLine();
            System.out.println("Server: " + fromServer);
            apply(fromServer);
            logger.log(Level.INFO, "Received gameState");

            // setup runnable to keep receive stuff from server
            Thread clientThread = new NetworkClientThread(out, in, id, this);
            clientThread.start();
            // also setup runnable to send updates to server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void apply(String newState) {
        gameState.apply(newState);
    }

    @Subscribe
    public void keyPressHandler(KeyPressedEvent e) {
        Logger.getGlobal().log(Level.INFO, "key pressed " + e.getKeyEvent().getKeyCode());
        out.println(id+":"+e.toString());
    }

    @Subscribe
    public void keyReleasedHandler(KeyReleasedEvent e) {
        Logger.getGlobal().log(Level.INFO, "key released "+ e.getKeyEvent().getKeyCode());
        out.println(id+":"+e.toString());
    }

    class NetworkClientThread extends Thread {
        Socket clientSocket;
        PrintWriter out;
        BufferedReader in;
        public int id;
        private GameState gameState;
        private boolean connected = false;
        NetworkClient client;

        NetworkClientThread(PrintWriter out, BufferedReader in, int id, NetworkClient client) {
            this.out = out;
            this.in = in;
            this.id = id;
            this.client = client;
        }

        @Override
        public void run() {
            String gameStateUpdate;
            Logger logger = Logger.getGlobal();
            try {
                logger.log(Level.INFO, "Waiting for gamestate update");

                while (true) {
                    // accepts inputs in the format key:value
                    gameStateUpdate = in.readLine();
                    Logger.getGlobal().log(Level.INFO,
                            String.format("Received packet from server\n"));
                    client.apply(gameStateUpdate);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}